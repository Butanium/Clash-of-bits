import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Control your bots in order to destroy the enemy team !
 **/
class fleeDumb {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int botPerPlayer = in.nextInt(); // the amount of bot you control
        int mapSize = in.nextInt(); // the map size in meters (just here in case the creator change the map size during the contest)

        Map<Integer, Integer> shieldMap = new HashMap<>();
        // game loop
        while (true) {
            StringBuilder result = new StringBuilder();
            int allyBotAlive = in.nextInt(); // the amount of your bot which are still alive
            int totalEntities = in.nextInt(); // the amount of entities in the arena
            for (int i = 0; i < totalEntities; i++) {
                int entId = in.nextInt(); // the unique gameEntity id, stay the same for the whole game
                String entType = in.next(); // the gameEntity type in a string. It can be ALLY | ENEMY
                int health = in.nextInt(); // the approximate gameEntity health. Can be 0 | 25 | 50 | 75 | 100, 25 meaning that your life is >= 25% and < 50% of your max life
                int shield = in.nextInt(); // the approximate gameEntity shield. Can be 0 | 1 | 25 | 50 | 75 | 100, 1 meaning that your shield is >= 1% and < 25% of your max shield and 0 that you have no more shield left
                String action = in.next(); // action executed by the gameEntity last turn
                String targets = in.next(); // list of the targets id targeted by the robot last turn ("id1;id2;id3...") if the gameEntity is a robot, else -1 (the target for IDLE is the robot itself)
                int distEn = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ then, the RANGE from to the closest enemy (if the entity is an enemy it returns
                int borderDist = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ approximate distance between the gameEntity and the closest border
                int borderDistRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ gameEntity are sorted in ascending order based on their distance to the closest border
                int distEnRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ entities are sorted by ascending order based on their distance to the closest enemy
                int healthRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ entities are sorted in ascendant order based on their amount of health, this is the rank of the current gameEntity in the sorted list
                int shieldRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ entities are sorted in ascendant order based on their amount of shield
                int totalRank = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ entities are sorted in ascendant order based on their amount of health + shield

                shieldMap.put(entId, shield);
            }
            for (int i = 0; i < allyBotAlive; i++) {
                int accRank = totalEntities;
                int accId = 0;
                int accDist = 0;
                int selfId = 0;
                for (int j = 0; j < totalEntities; j++) {
                    int entId = in.nextInt(); // the unique gameEntity id
                    String entType = in.next(); // the gameEntity type in a string. It can be SELF | ALLY | ENEMY
                    int distMe = in.nextInt(); // approximate distance between the target and the current bot. Can be 0 to 4 for short, medium, long and out of range
                    int distMeRank = in.nextInt(); // entities are sorted by ascending order based on their distance to the current bot
                    int shieldComp = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ -1 if the gameEntity has more shield than the current bot, 0 if it's equal, 1 if your bot as more shield
                    int healthComp = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ same as shieldComp but for the health
                    int totComp = in.nextInt(); // /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ same as shieldComp but based on the sum of health+shield

                    if (entType.equals("ENEMY") && distMeRank < accRank) {
                        accId = entId;
                        accRank = distMeRank;
                        accDist = distMe;
                    }
                    if (entType.equals("SELF")) {
                        selfId = entId;
                    }
                }
                result.append(selfId).append(" FLEE ").append(accId).append(";");
            }
            System.out.println(result);
        }
    }
}