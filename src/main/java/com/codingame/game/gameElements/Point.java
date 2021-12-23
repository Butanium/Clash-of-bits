package com.codingame.game.gameElements;

import com.codingame.game.Constants;
import com.codingame.game.exceptions.ZeroDivisionException;

import java.util.*;


public class Point {

    private double x;
    private double y;

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

    public Point(Point point) {
        x = point.x;
        y = point.y;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMaxCoord() {
        return Math.max(x, y);
    }

    public void setXY(Point p) {
        x = p.x;
        y = p.y;
    }

    public double getDist(Point point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }
        return Math.sqrt((point.getX() - this.x) * (point.getX() - this.x) + (point.getY() - this.y) * (point.getY() - this.y));
    }

    /**
     * @param pt the point you want the direction to go from the point
     * @return the normalized vector indicating the direction to go if you want to go to pt
     */
    public Point getDirection(Point pt) {
        double dist = getDist(pt);
        if (dist == 0) {
            throw new ZeroDivisionException();
        }
        return new Point((pt.x - x) / dist, (pt.y - y) / dist);
    }

    /**
     * @param pt the point you want the direction to go from the point
     * @return the normalized vector indicating the direction to go if you want to go to pt or (0,0) if the
     * distance is zero
     */
    public Point getDirectionNoEx(Point pt) {
        double dist = getDist(pt);
        if (dist == 0) {
            return new Point();
        }
        return new Point((pt.x - x) / dist, (pt.y - y) / dist);
    }


    public void move(Point direction) {
        x += direction.x;
        y += direction.y;
    }

    public Point add(Point pt) {
        return new Point(x + pt.x, y + pt.y);
    }

    public Point add(double s) {
        return new Point(x + s, y + s);
    }

    public Point subtract(Point pt) {
        return new Point(x - pt.x, y - pt.y);
    }

    public Point subtract(double s) {
        return new Point(x - s, y - s);
    }

    public Point divide(double d) {
        if (d == 0) {
            throw new ZeroDivisionException("divide point by 0 is forbidden");
        }
        return multiply(1. / d);
    }

    public double min() {
        return Math.min(x, y);
    }

    public double max() {
        return Math.max(x, y);
    }

    public Point clamp(Point inf, Point sup) {
        return new Point(Math.min(Math.max(inf.x, x), sup.x), Math.min(Math.max(inf.y, y), sup.y));
    }


    public Point multiply(double u) {
        return new Point(x * u, y * u);
    }

    public Point getAveragePoint(Set<Point> points) {
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

    public double magnitude() {
        return getDist(new Point());
    }

    public Point normalize() {
        if (isZero()) {
            throw new ZeroDivisionException("cannot normalize zero vector");
        }
        return multiply(1 / magnitude());
    }

    public double scalar(Point pt) {
        return x * pt.x + y * pt.y;
    }

    public double pointCos(Point pt) {
        double m1 = magnitude();
        double m2 = pt.magnitude();
        if (m1 == 0 || m2 == 0) {
            throw new ZeroDivisionException("cannot get cos with zero vector");
        }
        return scalar(pt) / (m1 * m2);
    }

    public double angle(Point pt) {
        double m1 = magnitude();
        double m2 = pt.magnitude();
        if (m1 == 0 || m2 == 0) {
            throw new ZeroDivisionException("cannot get angle with zero vector");
        }
        return Math.acos(pointCos(pt));
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Point pt = (Point) obj;
        return x == pt.x && y == pt.y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public Point orthogonal() {
        return new Point(y, -x);
    }

    public double getRotation() {
        return Math.atan2(y, x);
    }

    public String toString() {
        return String.format("x : %f, y : %f", x, y);
    }

    public Point centeredSymmetric(Point center) {
        return new Point(2 * center.x - x, 2 * center.y - y);
    }

    public Point horizontalSymmetric(double horizontal_y) {
        return new Point(x, 2 * horizontal_y - y);
    }

    public double getBorderDist() {
        double minX = Math.min(Math.abs(x), Math.abs(Constants.MAP_SIZE.x - x));
        double minY = Math.min(Math.abs(y), Math.abs(Constants.MAP_SIZE.y - y));
        return Math.min(minX, minY);
    }

    public double getBorderDiff() {
        return Math.min(Math.min(x, y), Math.min(Constants.MAP_SIZE.getX() - x, Constants.MAP_SIZE.getY() - y));
    }

    /**
     * @return the distance to the border you're out, if you're out of the 2 border, return the distance to the
     * vertical border
     */
    public double getBorderDistOut() {

        OptionalDouble rx = Arrays.stream(new double[]{x, Constants.MAP_SIZE.getX() - x})
                .filter(x -> x < 0).findFirst();
        if (rx.isPresent()) {
            return Math.abs(rx.getAsDouble());
        }
        OptionalDouble ry = Arrays.stream(new double[]{y, Constants.MAP_SIZE.getY() - y})
                .filter(y -> y < 0).findFirst();
        if (ry.isPresent()) {
            return Math.abs(ry.getAsDouble());
        }
        throw new IllegalArgumentException("cannot get border dist out if the point is in the arena");
    }

}