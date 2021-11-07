package com.codingame.game.gameEntities;

import com.codingame.game.gameElements.CircularHitBox;
import com.codingame.game.Constants;
import com.codingame.game.gameElements.Point;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public boolean equals(InGameEntity gameEntity) {
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


    public abstract String giveInfo(int league, Robot asker, int distRank, Set<Robot> enemies);

    public abstract String getSelfInfo(int league, Set<Robot> enemies, int playerId);

    public EntityType getType() { return type; }

    public void setType(EntityType newType) {
        type = newType;
    }
}