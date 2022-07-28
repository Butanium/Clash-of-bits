package com.codingame.game;

import com.codingame.game.gameEntities.Robot;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SymmetryAssert {
    public static void assertShieldHealthSymmetry(List<Robot> team1, List<Robot> team2) {
        if (team1.size() != team2.size()) {
            System.err.println("Team size is not symmetric");
            return;
        }
        for (int i = 0; i < team1.size(); i++) {
            Robot robot1 = team1.get(i);
            Robot robot2 = team2.get(i);
            if (robot1.getShield() != robot2.getShield()) {
//                throw new AssertionError("Shield health is not symmetric");
                System.err.println("Shield health is not symmetric");
            }
            if (robot1.getHealth() != robot2.getHealth()) {
//                throw new AssertionError("Health is not symmetric");
                System.err.println("Health is not symmetric");
            }
        }
    }

    public static void assertActionSymmetry(List<Robot> team1, List<Robot> team2) {
        if (team1.size() != team2.size()) {
            System.err.println("Team size is not symmetric");
            return;
        }
        for (int i = 0; i < team1.size(); i++) {
            Robot robot1 = team1.get(i);
            Robot robot2 = team2.get(i);
            if (!Objects.equals(robot1.getLastAction(), robot2.getLastAction())) {
//                throw new AssertionError("Action is not symmetric");
                System.err.println("Actions are not symmetric :\n" + robot1.getId()+ " : " + robot1.getLastAction() + " <> " + robot2.getId() + " : " + robot2.getLastAction());


            }
            if (!Arrays.equals(robot1.getTargets().stream().map(r -> r.getId() % Constants.BOT_PER_PLAYER).sorted().toArray(), robot2.getTargets().stream().map(r -> r.getId() % Constants.BOT_PER_PLAYER).sorted().toArray())) {
//                throw new AssertionError("Targets are not symmetric");
                System.err.println("Targets are not symmetric :\n" + robot1.getId() + " : " + robot1.getLastActionWithTarget() + " <> " + robot2.getId() + " : " + robot2.getLastActionWithTarget());
            }

        }
    }
}
