package agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Control your bots in order to destroy the enemy team !
 **/
class league2outsider {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int botPerPlayer = in.nextInt(); // the amount of bot you control

        // game loop
        while (true) {
            Map<Integer, Integer> shieldMap = new HashMap<>();
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
                int distEn = in.nextInt();
                int borderDist = in.nextInt(); // approximate distance between the gameEntity and the closest border
                shieldMap.put(entId, shield);
            }
            for (int i = 0; i < allyBotAlive; i++) {
                int accRank = totalEntities;
                int accId = 0;
                int accDist = 0;
                int selfId = 0;
                for (int j = 0; j < totalEntities; j++) {
                    int entId = in.nextInt(); // the unique gameEntity id
                    String entType = in.next(); // the gameEntity type in a string. It can be ON_AIR | ALLY | ENEMY
                    int distMe = in.nextInt(); // approximate distance between the target and the current bot. Can be 0 to 4 for short, medium, long and out of range
                    int distMeRank = in.nextInt(); // entities are sorted by ascending order based on their distance to the current bot
                    int shieldComp = in.nextInt(); // -1 if the gameEntity has more shield than the current bot, 0 if it's equal, 1 if your bot as more shield
                    int healthComp = in.nextInt(); // same as shieldComp but for the health
                    int totComp = in.nextInt(); // same as shieldComp but based on the sum of health+shield
                    if (entType.equals("ENEMY") && distMeRank < accRank) {
                        accId = entId;
                        accRank = distMeRank;
                        accDist = distMe;
                    }
                    if (entType.equals("ON_AIR")) {
                        selfId = entId;
                    }
                }
                if (shieldMap.get(selfId) == 0){
                    result.append(selfId).append(" FLEE ").append(accId).append(";");
                }
                else if (accDist < 3) {
                    result.append(selfId).append(" ATTACK ").append(accId).append(";");
                } else {
                    result.append(selfId).append(" MOVE ").append(accId).append(";");
                }
            }

            // Write an answer using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.out.println(result);
        }
    }
}