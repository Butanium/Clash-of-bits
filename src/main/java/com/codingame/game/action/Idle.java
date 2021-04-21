package com.codingame.game.action;
import com.codingame.game.entities.Robot;

public class Idle extends Action{
    public Idle(Robot executor){
        super(executor);
    }
    @Override
    public void performAction(){
        getExecutor().IDLE();
    }
}
