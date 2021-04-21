package com.codingame.game;

import java.util.*;

import com.codingame.game.action.*;
import com.codingame.game.entities.*;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.common.base.Function;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    private final Set<Entity> entitySet = new HashSet<>();
    private final Set<Robot> robotSet = new HashSet<>();
    private final Set<Bullet> bullets = new HashSet<>();
    private final Map<Integer, Set<Robot>> playersTeam = new HashMap<>();
    public Set<forcefield> forceFields = new HashSet<>();
    public Set<healthPack> healthPacks = new HashSet<>();
    @Inject
    private MultiplayerGameManager<Player> gameManager;
    @Inject
    private GraphicEntityModule graphicEntityModule;
    private int botCount;
    private ViewManager viewManager;

    @Override
    public void init() {
        long seed = gameManager.getSeed();
        for (Player player : gameManager.getPlayers()) {
            player.initialize(seed);
            Set<Robot> team = new HashSet<>();
            botCount = getBotCount();
            for (Point spawn : getSpawns(seed, player.getIndex(), botCount)) {
                Robot robot = new Robot(spawn, RobotType.ASSAULT, player);
                robotSet.add(robot);
                entitySet.add(robot);
                team.add(robot);
            }
            playersTeam.put(player.getIndex(), team);
        }
        viewManager = new ViewManager(graphicEntityModule);
        viewManager.init(robotSet);
        gameManager.setFrameDuration((int)(Constants.DELTA_TIME*1000));
    }

    @Override
    public void gameTurn(int turn) {
        boolean isFinish = false;
        for (int key : playersTeam.keySet()) {
            if (playersTeam.get(key).size() == 0) {
                isFinish = true;
            }
        }

        if (gameManager.getActivePlayers().size() < 2 || isFinish) {
            gameManager.endGame();
            return;
        }
        List<Action> actionList = new ArrayList<>();
        for (Player player : gameManager.getActivePlayers()) {
            sendPlayerInput(player, turn, gameManager.getLeagueLevel());
            player.execute();
        }

        for (Player player : gameManager.getActivePlayers()) {
            robotSet.removeIf(robot -> !robot.checkActive());
            entitySet.removeIf(entity -> !entity.checkActive());
            try {
                List<String> outputs = player.getOutputs();
                String output = outputs.get(0);
                String[] orders = output.split(";");
                for (String order : orders) {
                    String[] splitedOrder = order.split(" ");
                    if (splitedOrder.length < 2) {
                        player.deactivate(String.format("%s is not a proper action !", order));
                        gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                        player.setScore(-1);
                    }
                    boolean move = false;
                    switch (splitedOrder[1]) {
                        case "IDLE":
                            try {
                                Robot controlled = getController(player, splitedOrder[0]);
                                actionList.add(new Idle(controlled));
                            } catch (IllegalArgumentException ignored) {}
                            break;
                        case "ATTACK":
                            if (splitedOrder.length < 3) {
                                player.deactivate(String.format("%s is not a proper action !", order));
                                gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                player.setScore(-1);
                                break;
                            }
                            Robot controlled;
                            try {
                                controlled = getController(player, splitedOrder[0]);
                            }  catch (IllegalArgumentException e) {
                                break;
                            }
                            Set<Entity> targets;
                            try {
                                targets = getTargets(player, splitedOrder[2]);
                                if (targets.size() != 1) {
                                    player.deactivate(String.format("%s is not a proper action, " +
                                            "you can't attack more than 1 bot or 0 bot !", order));
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                Entity target = targets.stream().findFirst().orElse(null);
                                if (target.getType() != EntityType.ROBOT) {
                                    player.deactivate(String.format("you tried to attack id : %d," +
                                                    " you can't attack a %s !",target.getId(),target.getType()));
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                Robot targetRobot = (Robot) target;
                                if (targetRobot.getTeam() == player.getIndex()) {
                                    player.deactivate(String.format("you tried to attack : %d, " +
                                                    "you can't attack your allies !",target.getId()));
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                actionList.add(new Attack(controlled, targetRobot));
                            } catch (IllegalArgumentException ignored) {}
                            break;
                        case "MOVE":
                            move = true;
                        case "FLEE":
                            if (splitedOrder.length < 3) {
                                player.deactivate(String.format("%s is not a proper action !", order));
                                gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                player.setScore(-1);
                                break;
                            }
                            Robot executor;
                            try {
                                executor = getController(player, splitedOrder[0]);
                            }  catch (IllegalArgumentException e) {
                                break;
                            }
                            Set<Entity> moveTargets;
                            try {
                                moveTargets = getTargets(player, splitedOrder[2]);
                                if (moveTargets.size() == 0) {
                                    player.deactivate(String.format("%s is not a proper action, " +
                                            "you need to chose a target !", order));
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                if (move) {
                                    actionList.add(new Move(executor, moveTargets));
                                } else {
                                    actionList.add(new Flee(executor, moveTargets));
                                }
                            } catch (IllegalArgumentException ignored) {}
                            break;
                        default:
                            player.deactivate(String.format("%s is not a proper action", splitedOrder[1]));
                            gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                            player.setScore(-1);
                            break;
                    }
                }

            } catch (TimeoutException e) {
                gameManager.addToGameSummary(String.format("$%d timeout!", player.getIndex()));
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
                player.setScore(-1);
            }
        }
        actionList.sort(Comparator.comparingInt(Action::getPriority));
        Collections.reverse(actionList);
        Set<Robot> updatedBots = new HashSet<>();
        for (Action action : actionList) {
            action.performAction();
            updatedBots.add(action.getExecutor());
        }
        for (Robot robot : robotSet){
            if (!updatedBots.contains(robot)) {
                robot.IDLE();
            }
        }
        viewManager.update();
    }

    public Robot getRobot(int id) {
        for (Robot robot : robotSet) {
            if (robot.getId() == id) {
                return robot;
            }
        }
        throw new NoSuchElementException("Couldn't find robot from ID");
    }

    /**
     * @param id, the entity id you're looking for
     * @return the entity object with this id
     */
    public Entity getEntity(int id) {
        for (Entity entity : entitySet) {
            if (entity.getId() == id) {
                return entity;
            }
        }

        throw new NoSuchElementException("Couldn't find robot from ID");
    }

    private int getBotCount() {
        return Constants.BOT_PER_PLAYER;
    }

    private Point[] getSpawns(long seed, int playerId, int botCount) {
        Point[] spawns = new Point[botCount];
        double y;
        if (playerId == 0) {
            y = 2 * Constants.MIN_SPAWN_DIST;
        } else {
            y = Constants.MAP_SIZE.getY() - 2 * Constants.MIN_SPAWN_DIST;
        }
        for (int i = 0; i < botCount; i++) {
            int shift = botCount % 2 == 0 && i > botCount / 2 ? 1 + i : i;
            spawns[i] = new Point(Constants.MAP_SIZE.getX() / 2 +
                    (shift - (botCount / 2)) * 2 * Constants.MIN_SPAWN_DIST, y);
        }
        return spawns;
    }

//    public Set<Robot> getPlayerBots(int playerId) {  useless for now
//        Set<Robot> result = new HashSet<>();
//        for (Robot robot : robotSet) {
//            if (robot.getTeam() == playerId) {
//                result.add(robot);
//            }
//        }
//        return result;
//    }

    private void sendPlayerInput(Player player, int turn, int league) {
        if (turn == 1) {
            player.sendInputLine(botCount + "");
        }
        Set<Robot> myBots = playersTeam.get(player.getIndex());
        Set<Robot> enemyBots = new HashSet<>();
        for (int id : playersTeam.keySet()) {
            if (id == player.getIndex()) {
                continue;
            }
            enemyBots.addAll(playersTeam.get(id));
        }

        int allyBotAlive = myBots.size();
        player.sendInputLine(allyBotAlive + "");
        int entityCount = entitySet.size();
        player.sendInputLine(entityCount + "");
        int robotCount = robotSet.size();

        List<Robot> healthSorted = new ArrayList<>(robotSet);
        healthSorted.sort(Comparator.comparingDouble(Robot::getHealth));
        Map<Integer, Integer> healthRankings = getRanksR(healthSorted, Robot::getHealth);

        List<Robot> shieldSorted = new ArrayList<>(robotSet);
        shieldSorted.sort(Comparator.comparingDouble(Robot::getShield));
        Map<Integer, Integer> shieldRankings = getRanksR(shieldSorted, Robot::getShield);


        List<Robot> totalSorted = new ArrayList<>(robotSet);
        totalSorted.sort(Comparator.comparingDouble(r -> r.getShield() + r.getHealth()));
        Function<Robot, Double> f = r -> r.getShield() + r.getHealth();
        Map<Integer, Integer> totalRankings = getRanksR(totalSorted, f);

        List<Robot> borderDistSorted = new ArrayList<>(robotSet);
        borderDistSorted.sort(Comparator.comparingDouble(Robot::getBoarderDist));
        Map<Integer, Integer> borderRankings = getRanksR(borderDistSorted, Robot::getBoarderDist);


        List<Robot> distEnSorted = new ArrayList<>(robotSet);
        distEnSorted.sort(Comparator.comparingDouble(r -> r.getDist(r.getClosestEntity(new HashSet<>(enemyBots)))));
        Map<Integer, Integer> distEnRankings = getRanksR(distEnSorted,
                r -> r.getDist(r.getClosestEntity(new HashSet<>(enemyBots))));


        for (Entity entity : entitySet) {
            String input = entity.getSelfInfo(league, enemyBots, player.getIndex())
                    .replaceAll("\\s+$", "");
            if (league > 2 && entity.getType() == EntityType.ROBOT) {
                int healthRank, shieldRank, totalRank, borderDistRank, distEnRank;
                healthRank = healthRankings.get(entity.getId());
                shieldRank = shieldRankings.get(entity.getId());
                totalRank = totalRankings.get(entity.getId());
                borderDistRank = borderRankings.get(entity.getId());
                distEnRank = distEnRankings.get(entity.getId());
                input = String.format(input, healthRank, shieldRank, totalRank, borderDistRank, distEnRank);
            }
            player.sendInputLine(input);
        }

        for (Robot self : myBots) {
            player.sendInputLine(self.giveInfo(league, self, 0, enemyBots));
            List<Entity> rangeSortedEntities = new ArrayList<>(entitySet);
            rangeSortedEntities.sort(Comparator.comparingDouble(r -> r.getDist(self)));
            Map<Integer, Integer> selfRangeRankings = getRanksE(rangeSortedEntities, r -> r.getDist(self));
            for (Robot ally : myBots) {
                if (ally.getId() == self.getId()) {
                    continue;
                }
                String input = ally.giveInfo(league, self, rangeSortedEntities.indexOf(ally), enemyBots)
                        .replaceAll("\\s+$", "");

                player.sendInputLine(input);
            }
            for (Robot enemy : enemyBots) {
                String input = enemy.giveInfo(league, self, rangeSortedEntities.indexOf(enemy), enemyBots)
                        .replaceAll("\\s+$", "");
                player.sendInputLine(input);
            }
            for (Entity entity : entitySet) {
                if (entity.getType() == EntityType.ROBOT) {
                    continue;
                }
                String input = entity.giveInfo(league, self, rangeSortedEntities.indexOf(entity), enemyBots)
                        .replaceAll("\\s+$", "");
                player.sendInputLine(input);
            }
        }
    }

    private Map<Integer, Integer> getRanksR(List<Robot> sortedList, Function<Robot, Double> evaluation) {
        Map<Integer, Integer> rankings = new HashMap<>();
        if (sortedList.size() == 0) {
            return rankings;
        }
        double accD = evaluation.apply(sortedList.get(0));
        int accRank = 1;
        rankings.put(sortedList.get(0).getId(), 1);
        for (int i = 1; i < sortedList.size(); i++) {
            Robot robot = sortedList.get(i);
            if (evaluation.apply(robot) == accD) {
                rankings.put(robot.getId(), accRank);
            } else {
                rankings.put(robot.getId(), ++accRank);
                accD = evaluation.apply(robot);
            }
        }
        return rankings;
    }

    private Map<Integer, Integer> getRanksE(List<Entity> sortedList, Function<Entity, Double> evaluation) {
        Map<Integer, Integer> rankings = new HashMap<>();
        if (sortedList.size() == 0) {
            return rankings;
        }
        double accD = evaluation.apply(sortedList.get(0));
        int accRank = 1;
        rankings.put(sortedList.get(0).getId(), 1);
        for (int i = 1; i < sortedList.size(); i++) {
            Entity entity = sortedList.get(i);
            if (evaluation.apply(entity) == accD) {
                rankings.put(entity.getId(), accRank);
            } else {
                rankings.put(entity.getId(), ++accRank);
                accD = evaluation.apply(entity);
            }
        }
        return rankings;
    }

    private Robot getController(Player player, String ctrlString) {
        int robotId;
        try {
            robotId = Integer.parseInt(ctrlString);
        } catch (NumberFormatException e) {
            player.deactivate(String.format("%s is not a proper id !", ctrlString));
            gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
            player.setScore(-1);
            throw new IllegalArgumentException();
        }
        try {
            Entity controller = getEntity(robotId);
            if (controller.getType() != EntityType.ROBOT) {
                player.deactivate(String.format("%d is not one of your robot id !", robotId));
                gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                player.setScore(-1);
                throw new IllegalArgumentException();
            } else {
                Robot controllerBot = (Robot) controller;
                if (controllerBot.getTeam() != player.getIndex()) {
                    player.deactivate(String.format("%d is not one of your robot id !", robotId));
                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                    player.setScore(-1);
                    throw new IllegalArgumentException();
                }
                return controllerBot;
            }
        } catch (NoSuchElementException e) {
            player.deactivate(String.format("%d is not one of your alive robot id !", robotId));
            gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
            player.setScore(-1);
            throw new IllegalArgumentException();
        }
    }
    private Set<Entity> getTargets(Player player, String input) {
        Set<Entity> result = new HashSet<>();
        String[] targetStringArray = input.split(",");
        for (String targetString : targetStringArray) {
            int entityId;
            try {
                entityId = Integer.parseInt(targetString);
            } catch (NumberFormatException e) {
                player.deactivate(String.format("%s is not a proper id !", targetString));
                gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                player.setScore(-1);
                throw new IllegalArgumentException();
            }
            Entity target;
            try {
                target = getEntity(entityId);
            } catch (NoSuchElementException e) {
                player.deactivate(String.format("%d is not the id of an existing entity!", entityId));
                gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                player.setScore(-1);
                throw new IllegalArgumentException();
            }
            result.add(target);
        }
        return result;
    }

}
