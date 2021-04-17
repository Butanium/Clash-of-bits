package com.codingame.game;

public class Attack extends Action{
    public Attack(Robot executor, Robot target){
        super(executor);
        this.target = target;
    }
    private final Robot target;

    @Override
    public void performAction(){
        getExecutor().ATTACK(target);
    }
}
