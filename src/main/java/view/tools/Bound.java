package view.tools;

import com.codingame.game.gameElements.Point;

import static com.codingame.game.Constants.MAP_SIZE;

public class Bound {
    private double maxX = -1,
            maxY = -1,
            minX = MAP_SIZE.getX(),
            minY = MAP_SIZE.getY();

    public Bound() {
    }

    public void add(Point p) {
        maxX = Double.max(maxX, p.getX());
        maxY = Double.max(maxY, p.getY());
        minX = Double.min(minX, p.getX());
        minY = Double.min(minY, p.getY());
    }

    public Point average() {
        return new Point((maxX + minX) / 2, (maxY + minY) / 2);
    }

    public Point size() {
        return new Point(maxX - minX, maxY - minY);
    }

}
