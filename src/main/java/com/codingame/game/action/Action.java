package com.codingame.game.action;

import com.codingame.game.gameEntities.Robot;

public class Action {

    public Action(Robot executor, int priority){
        this.executor = executor;
        this.priority = priority;
    }
    public Action(Robot executor){
        this.executor = executor;
        this.priority = 0;
    }

    private final Robot executor;
    private final int priority;
    public Robot getExecutor(){
        return executor;
    }

    public void performAction(){

    }

    public int getPriority() {
        return priority;
    }
}
