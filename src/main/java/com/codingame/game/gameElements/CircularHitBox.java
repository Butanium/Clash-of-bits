package com.codingame.game.gameElements;


import com.codingame.game.Constants;

public class CircularHitBox extends Point {

    public CircularHitBox() {
        hitBoxSize = 0;
        speed = 0;
    }
    public CircularHitBox(double x, double y) {
        super(x, y);
        hitBoxSize = speed = 0;
    }
    public CircularHitBox(Point point){
        super(point);
        hitBoxSize = 0;
        speed = 0;
    }
    public CircularHitBox(double x, double y, double size) {
        super(x, y);
        hitBoxSize = size;
        speed = 0;
    }

    public CircularHitBox(Point point, double x){
        super(point);
        hitBoxSize = x;
        speed = 0;
    }
    public CircularHitBox(double x, double y, double size, double speed) {
        super(x, y);
        hitBoxSize = size;
        this.speed = speed;
    }

    public CircularHitBox(Point point, double size, double speed){
        super(point);
        hitBoxSize = size;
        this.speed = speed;
    }



    public double getSize() {
        return hitBoxSize;
    }

    private final double hitBoxSize;

    private final double speed;

    public boolean checkCollide(CircularHitBox point) {
        return this.getDist(point)<this.hitBoxSize+ point.hitBoxSize;
    }

    public Point clampToMap(Point point){
        Point s = new Point(hitBoxSize);
        return point.clamp(s, Constants.MAP_SIZE.add(s.multiply(-1)));
    }

    public Point clampToMap(Point point, Point direction){
        Point s = new Point(hitBoxSize);
        double boarderDist = getBorderDist();
        return point.clamp(s, Constants.MAP_SIZE.subtract(s));
    }


    public double getSpeed() {
        return speed;
    }

    public boolean isInsideMap() {
        return getX() >= hitBoxSize && getY() >= hitBoxSize &&
                getX() <= Constants.MAP_SIZE.getX() - hitBoxSize && getY() <= Constants.MAP_SIZE.getY() - hitBoxSize;
    }




}