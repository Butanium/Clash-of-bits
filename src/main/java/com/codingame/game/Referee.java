package com.codingame.game;

import com.codingame.game.action.*;
import com.codingame.game.gameEntities.*;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.common.base.Function;
import com.google.inject.Inject;
import view.ViewManager;
import view.modules.CameraModule;

import java.util.*;

public class Referee extends AbstractReferee {
    private final Set<InGameEntity> gameEntitySet = new HashSet<>();
    private final Set<Robot> robotSet = new HashSet<>();
    private final Set<Bullet> bullets = new HashSet<>();
    private final Map<Integer, Set<Robot>> playersTeam = new HashMap<>();

    public Set<forcefield> forceFields = new HashSet<>();
    public Set<healthPack> healthPacks = new HashSet<>();
//    @Inject
//    ViewportModule viewportModule;
    @Inject
    private MultiplayerGameManager<Player> gameManager;
    @Inject
    private GraphicEntityModule graphicEntityModule;
    @Inject
    TooltipModule tooltips;
    @Inject
    EndScreenModule endScreenModule;
    @Inject
    CameraModule cameraModule;

    private int botCount;
    private ViewManager viewManager;

    public static void debug(String message) {
        System.out.println(message);
    }

    @Override
    public void onEnd() {
        endScreenModule.setScores(gameManager.getPlayers().stream().mapToInt(AbstractMultiplayerPlayer::getScore).toArray());
    }

    // public void addToGameSummary(String message) {gameManager.addToGameSummary(message);}
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
                gameEntitySet.add(robot);
                team.add(robot);
            }
            playersTeam.put(player.getIndex(), team);
        }

        viewManager = new ViewManager(graphicEntityModule, tooltips, cameraModule);
        viewManager.init(robotSet);

//        viewportModule.createViewport(viewportGroup);
        gameManager.setFrameDuration((int) (Constants.DELTA_TIME * 1000 / 2));
        gameManager.setTurnMaxTime(50);
        gameManager.setMaxTurns(30000 / gameManager.getTurnMaxTime() / 2);
        gameManager.setFirstTurnMaxTime(1000);
    }

    @Override
    public void gameTurn(int turn) {
        System.out.printf("turn : %d, p1 : %d, p2 : %d\n", turn,
                gameManager.getPlayers().get(0).getScore(),
                gameManager.getPlayers().get(1).getScore());
        destroyBots();
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
            gameEntitySet.removeIf(entity -> !entity.checkActive());
            try {
                List<String> outputs = player.getOutputs();
                String output = outputs.get(0);
                String[] orders = output.split(";");
                if (output.equals("")) {
                    continue;
                }
                Set<Integer> executingRobots = new HashSet<>();
                for (String order : orders) {

                    String[] splitedOrder = order.split(" ");
                    if (splitedOrder.length < 2) {
                        player.deactivate(String.format("%s is not a proper action !", order));
                        gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                        player.setScore(-1);
                        break;
                    }
                    boolean move = false;
                    switch (splitedOrder[1]) {
                        case "IDLE":
                            try {
                                Robot controlled = getController(player, splitedOrder[0]);
                                actionList.add(new Idle(controlled));
                            } catch (IllegalArgumentException ignored) {
                            }
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
                            } catch (IllegalArgumentException e) {
                                break;
                            }
                            Set<InGameEntity> targets;
                            try {
                                targets = getTargets(player, splitedOrder[2]);
                                if (targets.size() != 1) {
                                    player.deactivate(String.format("%s is not a proper action, " +
                                            "you can't attack more than 1 bot or 0 bot !", order));
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                InGameEntity target = targets.stream().findFirst().orElse(null);
                                if (target.getType() != EntityType.ROBOT) {
                                    player.deactivate(String.format("you tried to attack id : %d," +
                                            " you can't attack a %s !", target.getId(), target.getType()));
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                Robot targetRobot = (Robot) target;
                                if (targetRobot.getTeam() == player.getIndex()) {
                                    player.deactivate(String.format("you tried to attack : %d, " +
                                            "you can't attack your allies !", target.getId()));
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                actionList.add(new Attack(controlled, targetRobot));
                            } catch (IllegalArgumentException ignored) {
                            }
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
                            } catch (IllegalArgumentException e) {
                                break;
                            }
                            Set<InGameEntity> moveTargets;
                            try {
                                moveTargets = getTargets(player, splitedOrder[2]);
                                if (moveTargets.size() == 0) {
                                    player.deactivate(String.format("%s is not a proper action, " +
                                            "you need to chose a target !", order));
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input", player.getIndex()));
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                if (moveTargets.size() == 1 && moveTargets.stream().findAny().orElse(null)
                                        .getId() == executor.getId()) {
                                    String err = String.format("%s is not a proper action, " +
                                            "you can't move/flee to yourself !", order);
                                    player.deactivate(err);
                                    gameManager.addToGameSummary(String.format("$%d sent invalid input : ", player.getIndex()));
                                    gameManager.addToGameSummary(err);
                                    player.setScore(-1);
                                    throw new IllegalArgumentException();
                                }
                                if (move) {
                                    actionList.add(new Move(executor, moveTargets));
                                } else {
                                    actionList.add(new Flee(executor, moveTargets));
                                }
                            } catch (IllegalArgumentException ignored) {
                            }
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
        actionList.sort(Comparator.comparingInt(Action::getPriority).thenComparingInt(a -> a.getExecutor().getId()));
        Collections.reverse(actionList);
        Set<Robot> updatedBots = new HashSet<>();
        for (Action action : actionList) {
            if (updatedBots.add(action.getExecutor())) {
                action.performAction();
            } else {
                Player player = action.getExecutor().getPlayer();
                player.deactivate(String.format("$%d tried to perform 2 action in the same turn for robot %d",
                                player.getIndex(), action.getExecutor().getId()));
            }
        }
        for (Robot robot : robotSet) {
            if (!updatedBots.contains(robot)) {
                robot.IDLE();
            }
        }
        // System.out.printf("%d bullets on the map", Bullet.bulletSet.size());
        Bullet.bulletSet.removeIf(bullet -> bullet.updatePos(viewManager));
        for (Robot robot : robotSet) {
            robot.updateShield();
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
    public InGameEntity getEntity(int id) {
        for (InGameEntity InGameEntity : gameEntitySet) {
            if (InGameEntity.getId() == id) {
                return InGameEntity;
            }
        }

        throw new NoSuchElementException("Couldn't find robot from ID");
    }

    private int getBotCount() {
        return Constants.BOT_PER_PLAYER;
    }

    private Point[] getSpawns(long seed, int playerId, int botCount) {
        Point[] spawns = new Point[botCount];
        double y = (Constants.MAP_SIZE.getY()-Constants.LONG_RANGE)/2-1;
        if (playerId != 0) {
            y = Constants.MAP_SIZE.getY() - y;
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
            player.sendInputLine(((int) Constants.MAP_SIZE.getX()) + "");
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
        int entityCount = gameEntitySet.size();
        player.sendInputLine(entityCount + "");
        int robotCount = robotSet.size();

        List<Robot> healthSorted = new ArrayList<>(robotSet);
        healthSorted.sort(Comparator.comparingDouble(Robot::getHealth).thenComparingInt(Robot::getId));
        Map<Integer, Integer> healthRankings = getRanksR(healthSorted, Robot::getHealth);

        List<Robot> shieldSorted = new ArrayList<>(robotSet);
        shieldSorted.sort(Comparator.comparingDouble(Robot::getShield).thenComparingInt(Robot::getId));
        Map<Integer, Integer> shieldRankings = getRanksR(shieldSorted, Robot::getShield);


        List<Robot> totalSorted = new ArrayList<>(robotSet);
        totalSorted.sort(Comparator.comparingDouble(Robot::getTotVitals).thenComparingInt(Robot::getId));
        Function<Robot, Double> f = r -> r.getShield() + r.getHealth();
        Map<Integer, Integer> totalRankings = getRanksR(totalSorted, f);

        List<Robot> borderDistSorted = new ArrayList<>(robotSet);
        borderDistSorted.sort(Comparator.comparingDouble(Robot::getBoarderDist).thenComparingInt(Robot::getId));
        Map<Integer, Integer> borderRankings = getRanksR(borderDistSorted, Robot::getBoarderDist);


        List<Robot> distEnSorted = new ArrayList<>(robotSet);
        distEnSorted.sort(Comparator.comparingDouble((Robot r) -> r.getDist(r.getClosestEntity(new HashSet<>(enemyBots))))
            .thenComparingInt(Robot::getId));
        Map<Integer, Integer> distEnRankings = getRanksR(distEnSorted,
                r -> r.getDist(r.getClosestEntity(new HashSet<>(enemyBots))));


        for (InGameEntity InGameEntity : gameEntitySet) {
            String input = InGameEntity.getSelfInfo(league, enemyBots, player.getIndex())
                    .replaceAll("\\s+$", "");
            if (league > 2 && InGameEntity.getType() == EntityType.ROBOT) {
                int healthRank, shieldRank, totalRank, borderDistRank, distEnRank;
                healthRank = healthRankings.get(InGameEntity.getId());
                shieldRank = shieldRankings.get(InGameEntity.getId());
                totalRank = totalRankings.get(InGameEntity.getId());
                borderDistRank = borderRankings.get(InGameEntity.getId());
                distEnRank = distEnRankings.get(InGameEntity.getId());
                input = String.format(input, healthRank, shieldRank, totalRank, borderDistRank, distEnRank);
            }
            player.sendInputLine(input);
        }

        for (Robot self : myBots) {
            player.sendInputLine(self.giveInfo(league, self, 0, enemyBots));
            List<InGameEntity> rangeSortedEntities = new ArrayList<>(gameEntitySet);
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
            for (InGameEntity InGameEntity : gameEntitySet) {
                if (InGameEntity.getType() == EntityType.ROBOT) {
                    continue;
                }
                String input = InGameEntity.giveInfo(league, self, rangeSortedEntities.indexOf(InGameEntity), enemyBots)
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

    private Map<Integer, Integer> getRanksE(List<InGameEntity> sortedList, Function<InGameEntity, Double> evaluation) {
        Map<Integer, Integer> rankings = new HashMap<>();
        if (sortedList.size() == 0) {
            return rankings;
        }
        double accD = evaluation.apply(sortedList.get(0));
        int accRank = 1;
        rankings.put(sortedList.get(0).getId(), 1);
        for (int i = 1; i < sortedList.size(); i++) {
            InGameEntity InGameEntity = sortedList.get(i);
            if (evaluation.apply(InGameEntity) == accD) {
                rankings.put(InGameEntity.getId(), accRank);
            } else {
                rankings.put(InGameEntity.getId(), ++accRank);
                accD = evaluation.apply(InGameEntity);
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
            InGameEntity controller = getEntity(robotId);
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

    private Set<InGameEntity> getTargets(Player player, String input) {
        Set<InGameEntity> result = new HashSet<>();
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
            InGameEntity target;
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

    private void destroyBots() {
        Iterator<Robot> it = robotSet.iterator();
        while (it.hasNext()) {
            Robot robot = it.next();
            if (!robot.checkActive()) {
                it.remove();
                playersTeam.get(robot.getTeam()).remove(robot);
                gameEntitySet.remove(robot);
            }
        }
    }



}
