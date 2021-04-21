package com.codingame.game.action;

import com.codingame.game.entities.Robot;

public class Attack extends Action{
    public Attack(Robot executor, Robot target){
        super(executor,1);
        this.target = target;
    }
    private final Robot target;

    @Override
    public void performAction(){
        getExecutor().ATTACK(target);
    }
}
