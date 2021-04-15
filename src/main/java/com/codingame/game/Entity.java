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

}