package com.codingame.game;
import java.util.*;

public class Move extends Action{
    public Move(Robot executor, Set<Entity> target){
        super(executor);
        this.target = target;
    }
    private final Set<Entity> target;

    @Override
    public void performAction(){
        getExecutor().MOVE(target);
    }
}
