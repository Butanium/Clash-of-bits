package com.codingame.game;
import java.util.*;


public class Point {

    public Point() {
        this.x = this.y = 0;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double v) {
        x = y = v;
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

    public void setXY(Point p) {
        x = p.x;
        y = p.y;
    }

    public double getDist(Point point) {
        return Math.sqrt((point.getX() - this.x) * (point.getX() - this.x) + (point.getY() - this.y) * (point.getY() - this.y));
    }

    public Point getDirection(Point pt) {
        double dist = getDist(pt);
        if (dist == 0) {
            throw new ZeroDivisionException();
        }
        return new Point((pt.x - x) / dist, (pt.y - y) / dist);
    }

    public void move(Point direction) {
        x += direction.x;
        y += direction.y;
    }

    public Point addPoint(Point pt) {
        return new Point(x + pt.x, y + pt.y);
    }

    public Point clamp(Point inf, Point sup) {
        return new Point(Math.min(Math.max(inf.x, x), sup.x), Math.min(Math.max(inf.y, y), sup.y));
    }

    public Point multiply (double u){
        return new Point(x*u, y*u);
    }

    public Point getAveragePoint (Set<Point> points) {
        if (points.size() == 0) {
            throw new IllegalArgumentException("Set of point is empty");
        }
        double x = 0;
        double y = 0;
        for (Point p : points) {
            x += p.getX();
            y += p.getY();
        }
        return new Point(x, y).multiply(1. / points.size());

    }
}