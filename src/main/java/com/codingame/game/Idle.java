package com.codingame.game;
import java.util.*;

public class Idle extends Action{
    public Idle(Robot executor){
        super(executor);
    }
    @Override
    public void performAction(){
        getExecutor().IDLE();
    }
}
