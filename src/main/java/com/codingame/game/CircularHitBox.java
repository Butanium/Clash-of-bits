package com.codingame.game;


public class CircularHitBox extends Point {

    public CircularHitBox() {
        this.hitBoxSize = 0;
    }
    public CircularHitBox(double x, double y) {
        super(x, y);
        this.hitBoxSize = 0;
    }
    public CircularHitBox(double x, double y, double size) {
        super(x, y);
        this.hitBoxSize = size;
    }
    public double getSize() {
        return hitBoxSize;
    }

    private final double hitBoxSize;


    private boolean checkCollide(CircularHitBox point) {
        return this.getDist(point)<this.hitBoxSize+ point.hitBoxSize;
    }

}