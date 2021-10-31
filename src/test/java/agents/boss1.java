package agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Control your bots in order to destroy the enemy team !
 **/
public class boss1 {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int botPerPlayer = in.nextInt(); // the amount of bot you control
        int mapSize = in.nextInt();
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
                shieldMap.put(entId, shield);
                int distEn = in.nextInt(); // NOT USED IN THIS LEAGUE (it'll be a RANGE so an int between 0 and 3)
                int borderDist = in.nextInt(); // NOT USED IN THIS LEAGUE (it'll be a RANGE)
                int borderDistRank = in.nextInt(); // NOT USED IN THIS LEAGUE (a RANK)
                int distEnRank = in.nextInt(); // NOT USED IN THIS LEAGUE (it'll be a RANK so an int between 0 and entityCount)
                int healthRank = in.nextInt(); // NOT USED IN THIS LEAGUE (a RANK)
                int shieldRank = in.nextInt(); // NOT USED IN THIS LEAGUE (a RANK)
                int totalRank = in.nextInt(); // NOT USED IN THIS LEAGUE (a RANK)
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
                    if (entType.equals("ENEMY") && distMeRank < accRank) {
                        accId = entId;
                        accRank = distMeRank;
                        accDist = distMe;
                    }
                    if (entType.equals("SELF")) {
                        selfId = entId;
                    }
                    int shieldComp = in.nextInt(); // NOT USED IN THIS LEAGUE (a COMP so either  -1 | 0 | 1)
                    int healthComp = in.nextInt(); // NOT USED IN THIS LEAGUE (a COMP)
                    int totComp = in.nextInt(); // NOT USED IN THIS LEAGUE (a COMP)
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
            System.out.println(result);
        }
    }
}