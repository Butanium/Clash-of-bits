package agents;

import java.util.*;

/**
 * Control your bots in order to destroy the enemy team !
 **/
@SuppressWarnings("InfiniteLoopStatement")
public
class Agent1_mediumADAKASUICIDE {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int botPerPlayer = in.nextInt(); // the amount of bot you control
        int mapSize = in.nextInt();
        // game loop
        while (true) {
            StringBuilder result = new StringBuilder();
            int allyBotAlive = in.nextInt(); // the amount of your bot which are still alive
            int totalEntities = in.nextInt(); // the amount of entities in the arena
            Map<Integer, Integer> shieldMap = new HashMap<>();
            Map<Integer, Set<Integer>> attackersMap = new HashMap<>();
            Map<Integer, Integer> attackTargetMap = new HashMap<>();
            for (int i = 0; i < totalEntities; i++) {
                int entId = in.nextInt(); // the unique entity id, stay the same for the whole game
                String entType = in.next(); // the entity type in a string. It can be ALLY | ENEMY
                int health = in.nextInt(); // the approximate entity health. Can be 0 | 25 | 50 | 75 | 100, 25 meaning that your life is >= 25% and < 50% of your max life
                int shield = in.nextInt(); // the approximate entity shield. Can be 0 | 1 | 25 | 50 | 75 | 100, 1 meaning that your shield is >= 1% and < 25% of your max shield and 0 that you have no more shield left
                String action = in.next(); // action executed by the entity last turn
                String targets = in.next(); // list of the targets id targeted by the robot last turn ("id1;id2;id3...") if the entity is a robot not idling, else -1
                int distEn = in.nextInt();
                int borderDist = in.nextInt(); // approximate distance between the entity and the closest border
                int borderDistRank = in.nextInt(); // entity are sorted in ascending order based on their distance to the closest border
                int distEnRank = in.nextInt(); // entities are sorted by ascending order based on their distance to the closest enemy
                int healthRank = in.nextInt(); // entities are sorted in ascendant order based on their amount of health, this is the rank of the current entity in the sorted list
                int shieldRank = in.nextInt(); // entities are sorted in ascendant order based on their amount of shield
                int totalRank = in.nextInt(); // entities are sorted in ascendant order based on their amount of health + shield
                if (action.equals("ATTACK")) {
                    Set<Integer> r = attackersMap.getOrDefault(Integer.parseInt(targets), new HashSet<>());
                    r.add(entId);
                    attackersMap.put(Integer.parseInt(targets), r);
                    attackTargetMap.put(entId, Integer.parseInt(targets));
                }
                shieldMap.put(entId, shield);
            }
            for (int i = 0; i < allyBotAlive; i++) {
                int accRank = totalEntities;
                int accId = 0;
                int accDist = 0;
                int selfId = 0;
                int targetId = -1;
                int targetDist = 3;
                for (int j = 0; j < totalEntities; j++) {
                    int entId = in.nextInt(); // the unique entity id
                    String entType = in.next(); // the entity type in a string. It can be SELF | ALLY | ENEMY
                    int distMe = in.nextInt(); // approximate distance between the target and the current bot. Can be 0 to 3 for short, medium, long and out of range
                    int distMeRank = in.nextInt(); // entities are sorted by ascending order based on their distance to the current bot
                    int shieldComp = in.nextInt(); // -1 if the entity has more shield than the current bot, 0 if it's equal, 1 if your bot as more shield
                    int healthComp = in.nextInt(); // same as shieldComp but for the health
                    int totComp = in.nextInt(); // same as shieldComp but based on the sum of health+shield
                    if(entType.equals("ENEMY") && distMeRank<accRank) {
                        accId = entId;
                        accRank = distMeRank;
                        accDist = distMe;
                        if (entId == targetId) {
                            targetDist = distMe;
                            System.err.printf("target dist : %d\n", targetDist);
                        }
                    }

                    if (entType.equals("SELF")) {
                        selfId = entId;
                        targetId =  attackTargetMap.getOrDefault(selfId, -1);
                        System.err.printf("target ID : %d ", targetId);
                    }
                }
                if (shieldMap.get(selfId) <= 25 && accDist == 1) {
                    result.append(selfId).append(" MOVE ").append(accId).append(";");
                } else
                if (targetDist < 3 && shieldMap.getOrDefault(targetId, 100) <25) {
                    result.append(selfId).append(" ATTACK ").append(targetId).append(";");
                    System.err.println("OK JE CONTINUE");
                }
                else if (accDist < 2) {
                    result.append(selfId).append(" ATTACK ").append(accId).append(";");
                }else {
                    result.append(selfId).append(" MOVE ").append(accId).append(";");
                }
            }
            System.out.println(result);
        }
    }
}