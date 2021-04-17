package com.codingame.game;

/**
 * 
 */
public class Entity extends CircularHitBox {

    /**
     * Default constructor
     */
    public Entity(double x, double y) {
        super(x, y);
        this.id = Constants.GLOBAL_ID;
        Constants.GLOBAL_ID++;
    }
    public Entity(){
        this.id = Constants.GLOBAL_ID;
        Constants.GLOBAL_ID++;
    }
    public Entity(double x, double y, double size) {
        super(x, y, size);
        id = Constants.GLOBAL_ID++;
    }
    public Entity(double x, double y, double size, double speed) {
        super(x, y, size,speed);
        id = Constants.GLOBAL_ID++;
    }

    private final int id;

    private boolean isActive = true;

    public int getId() {
        return id;
    }

    public boolean checkActive() {
        return this.isActive;
    }

    public void setActive(boolean a) {
        this.isActive = a;
    }

    public boolean equals(Entity entity){
        return id == entity.getId();
    }

    public int getRange(Point point){
        double d = getDist(point);
        if (d < Constants.SHORT_RANGE){
            return 0;
        } else if (d < Constants.MEDIUM_RANGE){
            return 1;
        } else if (d <= Constants.LONG_RANGE) {
            return 2;
        } else {
            return 3;
        }

    }
}