package agents;

import java.util.Scanner;

/**
 * Control your bots in order to destroy the enemy team !
 **/
@SuppressWarnings("InfiniteLoopStatement")
public
class moveDumb {
    static class Empty {
        public int x;
        public int y;
        public Empty(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int botPerPlayer = in.nextInt(); // the amount of bot you control
        int mapSize = in.nextInt();
        // game loop
        while (true) {
            StringBuilder result = new StringBuilder();
            new Empty(1,2);
            int allyBotAlive = in.nextInt(); // the amount of your bot which are still alive
            int totalEntities = in.nextInt(); // the amount of entities in the arena
            System.err.printf("%d allybots, %d entities", allyBotAlive, totalEntities);
            in.nextLine();
            for (int i = 0; i < totalEntities; i++) {
               in.nextLine();
            }
            for (int i = 0; i < allyBotAlive; i++) {
                int accRank = totalEntities;
                int accId = 0;
                int accDist = 0;
                int selfId = 0;
                for (int j = 0; j < totalEntities; j++) {
                    int entId = in.nextInt(); // the unique entity id
                    String entType = in.next(); // the entity type in a string. It can be ON_AIR | ALLY | ENEMY
                    int distMe = in.nextInt(); // approximate distance between the target and the current bot. Can be 0 to 3 for short, medium, long and out of range
                    int distMeRank = in.nextInt(); // entities are sorted by ascending order based on their distance to the current bot
                    int shieldComp = in.nextInt(); // -1 if the entity has more shield than the current bot, 0 if it's equal, 1 if your bot as more shield
                    int healthComp = in.nextInt(); // same as shieldComp but for the health
                    int totComp = in.nextInt(); // same as shieldComp but based on the sum of health+shield
                    if(entType.equals("ENEMY") && distMeRank<accRank) {
                        accId = entId;
                        accRank = distMeRank;
                        accDist = distMe;
                    }
                    if (entType.equals("ON_AIR")) {
                        selfId = entId;
                    }
                }
                result.append(selfId).append(" MOVE ").append(accId).append(";");
            }
            System.out.println(result);
        }
    }
}