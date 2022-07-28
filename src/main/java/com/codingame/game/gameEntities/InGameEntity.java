package com.codingame.game.gameEntities;

import com.codingame.game.gameElements.CircularHitBox;
import com.codingame.game.Constants;
import com.codingame.game.gameElements.Point;

import java.util.*;

/**
 *
 */
public abstract class InGameEntity extends CircularHitBox {

    /**
     * Default constructor
     */
    public InGameEntity(double x, double y) {
        super(x, y);
        this.id = Constants.GLOBAL_ID;
        Constants.GLOBAL_ID++;
    }

    public InGameEntity() {
        this.id = Constants.GLOBAL_ID;
        Constants.GLOBAL_ID++;
    }

    public InGameEntity(double x, double y, double size) {
        super(x, y, size);
        id = Constants.GLOBAL_ID++;
    }

    public InGameEntity(double x, double y, double size, double speed) {
        super(x, y, size, speed);
        id = Constants.GLOBAL_ID++;
    }

    public InGameEntity(Point pt) {
        super(pt);
        id = Constants.GLOBAL_ID++;
    }

    public InGameEntity(Point pt, double size) {
        super(pt, size);
        id = Constants.GLOBAL_ID++;
    }

    public InGameEntity(Point pt, double size, double speed) {
        super(pt, size, speed);
        id = Constants.GLOBAL_ID++;
    }


    private final int id;
    private EntityType type;
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

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        InGameEntity gameEntity = (InGameEntity) obj;
        return id == gameEntity.getId();

    }


    public int getRange(Point point) {
        return getRange(getDist(point));
    }

    public int getRange(double d) {
        if (d < Constants.SHORT_RANGE) {
            return 0;
        } else if (d < Constants.MEDIUM_RANGE) {
            return 1;
        } else if (d <= Constants.LONG_RANGE) {
            return 2;
        } else {
            return 3;
        }
    }

    public InGameEntity getClosestEntity(Set<InGameEntity> entities) {
        Set<InGameEntity> InGameEntitySet = new HashSet<>(entities);
        InGameEntitySet.remove(this);
        Optional<InGameEntity> res = InGameEntitySet.stream().min(Comparator.comparingDouble(this::getDist));
        return res.orElse(this);
    }

    private int round(double x) {
        int s = x < 0 ? -1 : 1;
        return s * (int) Math.round(s * x);
    }

    private int truncate(double xy) {
        double roundedX = round(xy);
        if (Math.abs(roundedX - xy) < Constants.MOVE_PRECISION) {
            return (int) roundedX;
        }

        return (int) (xy < 0 ? Math.ceil(xy) : Math.floor(xy));
    }

    public void adjustPosition() {
        // Trying to make the games symmetric
        double dx = Constants.MAP_SIZE.getX() / 2;
        double dy = Constants.MAP_SIZE.getY() / 2;
        double x2 = getX() - dx;
        double y2 = getY() - dy;
        double f = Constants.INVERSE_MOVE_PRECISION;
        this.setXY(new Point(truncate(x2 * f) / f + dx, truncate(y2 * f) / f + dy));

    }


    public abstract String giveInfo(int league, Robot asker, int distRank);

    public abstract String getSelfInfo(int league, ArrayList<Robot> enemies, int playerId);

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType newType) {
        type = newType;
    }
}