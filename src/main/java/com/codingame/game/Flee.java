package com.codingame.game;

import java.util.Set;

public class Flee extends Action{
    public Flee(Robot executor, Set<Point> target){
        super(executor);
        this.target = target;
    }
    private final Set<Point> target;

    @Override
    public void performAction(){
        getExecutor().FLEE(target);
    }
}
