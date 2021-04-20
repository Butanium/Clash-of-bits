package com.codingame.game;

import java.util.Set;

public class Flee extends Action{
    public Flee(Robot executor, Set<Entity> target){
        super(executor);
        this.target = target;
    }
    private final Set<Entity> target;

    @Override
    public void performAction(){
        getExecutor().FLEE(target);
    }
}
