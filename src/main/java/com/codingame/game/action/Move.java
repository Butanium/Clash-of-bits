package com.codingame.game.action;
import com.codingame.game.entities.InGameEntity;
import com.codingame.game.entities.Robot;

import java.util.*;

public class Move extends Action{
    public Move(Robot executor, Set<InGameEntity> target){
        super(executor);
        this.target = target;
    }
    private final Set<InGameEntity> target;

    @Override
    public void performAction(){
        getExecutor().MOVE(target);
    }
}
