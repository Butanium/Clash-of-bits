package com.codingame.game;


public class Point {

    public Point() {
        this.x = this.y = 0;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private double x;
    private double y;


    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDist(Point point) {
        return Math.sqrt((point.getX() - this.x) * (point.getX() - this.x) + (point.getY() - this.y) * (point.getY() - this.y));
    }

    public Point getDirection(Point pt) {
        double dist = getDist(pt);
        if (dist == 0){
            throw new ZeroDivisionException();
        }
        return new Point((pt.x - x)/dist, (pt.y - y)/dist);
    }

}