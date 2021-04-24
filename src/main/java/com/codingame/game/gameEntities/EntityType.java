package com.codingame.game.gameEntities;

public enum EntityType {
    ROBOT,HEALTHPACK,FORCEFIELD;

    @Override
    public String toString() {
        switch (this){
            case ROBOT:
                return "ROBOT";
            case FORCEFIELD:
                return "FORCEFIELD";
            case HEALTHPACK:
                return "HEALTHPACK";
        }
        return "OTHER";
    }
}
