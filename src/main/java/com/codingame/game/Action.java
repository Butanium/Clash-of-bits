package com.codingame.game;

public class Action {

    public Action(Robot executor){
        this.executor = executor;
    }

    private final Robot executor;

    public Robot getExecutor(){
        return executor;
    }

    public void performAction(){

    }
}
