package agents;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Control your bots in order to destroy the enemy team !
 **/
public
class starter {
    static String orders = "";
    static Set<Bot> enemyBots = new HashSet<>();
    static Set<Bot> allyBots = new HashSet<>();
    static final String ALLY = "ALLY";
    static final String ENEMY = "ENEMY";
    static final String ATTACK = "ATTACK";
    static final String MOVE = "MOVE";
    static final String IDLE = "IDLE";
    static final String FLEE = "FLEE";

    private static class BotInfo {
        int rangeFromBot;
        int distBotRank;
        int shieldComp;
        int healthComp;
        int totalComp;

        BotInfo(int rangeFromBot, int distBotRank, int shieldComp, int healthComp, int totalComp) {
            this.rangeFromBot = rangeFromBot;
            this.distBotRank = distBotRank;
            this.shieldComp = shieldComp;
            this.healthComp = healthComp;
            this.totalComp = totalComp;
        }
    }

    private static class Bot {
        int id;
        String team;
        int shield;
        int health;
        String action;
        private final IntStream targetIds;
        Set<Bot> targets;
        int distEn;
        int distMe;
        int distMeRank;
        int distEnRank;
        int shieldRank;
        int totalRank;
        int borderDist;
        int borderDistRank;
        Set<Bot> attackingMe = new HashSet<>();
        Set<Bot> fleeingMe = new HashSet<>();
        Set<Bot> approachingMe = new HashSet<>();
        Map<Integer, BotInfo> infoFromMyPerspective = new HashMap<>();

        Bot(int id, String team, int shield, int health, String action, String targets, int distEn, int distMe, int distMeRank, int distEnRank, int shieldRank, int totalRank, int borderDist, int borderDistRank) {
            this.id = id;
            this.team = team;
            this.shield = shield;
            this.health = health;
            this.action = action;
            this.targetIds = Arrays.stream(targets.split(",")).mapToInt(Integer::parseInt);
            this.distEn = distEn;
            this.distMe = distMe;
            this.distMeRank = distMeRank;
            this.distEnRank = distEnRank;
            this.shieldRank = shieldRank;
            this.totalRank = totalRank;
            this.borderDist = borderDist;
            this.borderDistRank = borderDistRank;
        }

        public void getTargets(Map<Integer, Bot> bots) {
            // If one of the target is dead, we add a deadBot to the targets
            this.targets = this.targetIds.mapToObj(id -> bots.getOrDefault(id, deadBot(id))).collect(Collectors.toSet());
            this.targets.forEach(bot -> bot.addTargetingMe(this));
        }

        public void attack(int targetId) {
            orders += String.format("%d %s %d;", id, ATTACK, targetId);
        }

        private void movement(String action, int... targetIds) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < targetIds.length; i++) {
                sb.append(targetIds[i]);
                if (i < targetIds.length - 1) {
                    sb.append(",");
                }
            }
            orders += String.format("%d %s %s;", id, action, sb);
        }

        private void movement(String action, Bot... targetIds) {
            movement(action, Arrays.stream(targetIds).mapToInt(bot -> bot.id).toArray());
        }

        public void move(int... targetIds) {
            movement(MOVE, targetIds);
        }

        public void move(Bot... targetBots) {
            movement(MOVE, targetBots);
        }

        public void flee(int... targetIds) {
            movement(FLEE, targetIds);
        }

        public void flee(Bot... targetBots) {
            movement(FLEE, targetBots);
        }

        public void idle() {
            orders += String.format("%d %s;", id, IDLE);
        }

        public void addTargetingMe(Bot targetingMe) {
            switch (targetingMe.action) {
                case ATTACK:
                    this.attackingMe.add(targetingMe);
                    break;
                case FLEE:
                    this.fleeingMe.add(targetingMe);
                    break;
                case MOVE:
                    this.approachingMe.add(targetingMe);
                    break;
            }
        }

        public void addInfoFromMyPerspective(int botId, BotInfo info) {
            this.infoFromMyPerspective.put(botId, info);
        }

        public BotInfo viewedBy(Bot bot) {
            return bot.infoFromMyPerspective.get(this.id);
        }

        public Bot getClosestEnemy() {
            assert this.team.equals(ALLY) : "You can only call closestEnemy method on an ally bot";
            return enemyBots.stream().min(Comparator.comparingInt(bot -> bot.viewedBy(this).distBotRank)).get();
        }

        public boolean isDead() {
            return this.health == -1;
        }
    }

    static Bot deadBot(int id) {
        return new Bot(id, "", 0, -1, "", "", 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int botPerPlayer = in.nextInt(); // the amount of bot you control
        int arenaSize = in.nextInt(); // the arena size in meters (just here in case the creator change the arena size during the contest)

        // game loop
        while (true) {
            orders = "";
            int allyBotAlive = in.nextInt(); // the amount of your bot which are still alive
            int botCount = in.nextInt(); // the amount of bots in the arena
            Map<Integer, Bot> bots = new HashMap<>();
            allyBots = new HashSet<>();
            enemyBots = new HashSet<>();
            for (int i = 0; i < botCount; i++) {
                int botId = in.nextInt(); // the unique bot id, stay the same for the whole game
                String botType = in.next(); // the bot type in a string. It can be ALLY | ENEMY
                int health = in.nextInt(); // the approximate bot health. Can be 0 | 25 | 50 | 75 | 100, 25 meaning that your life is >= 25% and < 50% of your max life
                int shield = in.nextInt(); // the approximate bot shield. Can be 0 | 1 | 25 | 50 | 75 | 100, 1 meaning that your shield is >= 1% and < 25% of your max shield and 0 that you have no more shield left
                String action = in.next(); // action executed by the bot last turn
                String targets = in.next(); // list of the targets id targeted by the bot last turn ("id1;id2;id3...") if the bot is a bot, else -1 (the target for IDLE is the bot itself)
                int enemyRange = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ then, the RANGE from to the closest enemy (if the bot is an enemy it returns the range to its closest ally)
                int borderRange = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ approximate distance between the bot and the closest border
                int borderDistRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bot are sorted in ascending order based on their distance to the closest border
                int distEnRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bots are sorted by ascending order based on their distance to the closest enemy
                int healthRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bots are sorted in ascendant order based on their amount of health, this is the rank of the current bot in the sorted list
                int shieldRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bots are sorted in ascendant order based on their amount of shield
                int totalRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bots are sorted in ascendant order based on their amount of health + shield
                Bot bot = new Bot(botId, botType, shield, health, action, targets, enemyRange, distEnRank,
                        healthRank, distEnRank, shieldRank, totalRank, borderRange, borderDistRank);
                bots.put(botId, bot);
                if (botType.equals(ALLY)) {
                    allyBots.add(bot);
                } else {
                    enemyBots.add(bot);
                }
            }
            bots.values().forEach(bot -> bot.getTargets(bots));
            for (int i = 0; i < allyBotAlive; i++) {
                in.nextLine(); // skip to the next line
                int onAirId = Integer.parseInt(in.nextLine().split(" ")[0]); // the id of the ON AIR bot
                for (int j = 0; j < botCount - 1; j++) {
                    int botId = in.nextInt(); // the unique bot id
                    in.next(); // the bot type in a string. It can be ALLY | ENEMY
                    int rangeFromOnAirBot = in.nextInt(); // approximate distance between the target and the ON AIR bot. Can be 0 to 3 for short, medium, long and out of range
                    int distMeRank = in.nextInt(); // bots are sorted by ascending order based on their distance to the ON AIR bot
                    int shieldComp = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ -1 if the bot has more shield than the ON AIR bot, 0 if it's equal, 1 if the ON AIR bot has more shield
                    int healthComp = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ same as shieldComp but for the health
                    int totComp = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ same as shieldComp but based on the sum of health+shield
                    bots.get(onAirId).addInfoFromMyPerspective(botId, new BotInfo(rangeFromOnAirBot, distMeRank, shieldComp, healthComp, totComp));
                }
            }
            // All bots move to the closest enemy
            for (Bot allyBot : allyBots) {
                allyBot.move(allyBot.getClosestEnemy());
            }
            System.err.println(allyBots);

            // Write an answer using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println(orders);
        }
    }
}