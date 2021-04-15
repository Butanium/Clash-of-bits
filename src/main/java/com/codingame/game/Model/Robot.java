
import java.util.*;

/**
 * 
 */
public class Robot extends Entity {

    /**
     * Default constructor
     */
    public Robot() {
    }

    /**
     * 
     */
    private float health;

    /**
     * 
     */
    private float shieldHealth;

    /**
     * 
     */
    private void shotManager;

    /**
     * 
     */
    private float maxHealth;

    /**
     * 
     */
    private float maxShieldHealth;

    /**
     * number of frame to aim
     */
    private int aimTime;

    /**
     * 
     */
    private float speed;

    /**
     * 
     */
    private Robot attackTarget;

    /**
     * 
     */
    private int shotState;

    /**
     * 
     */
    private float[] shotRangeProb;

    /**
     * number of bullet fired per frame
     */
    private int bulletPerShot;

    /**
     * number of frame during the shot
     */
    private int shotTime;

    /**
     * 
     */
    private float damagePerBullet;

    /**
     * 
     */
    private float shieldRegenPerFrame;

    /**
     * 
     */
    private int shieldRegenCooldown;

    /**
     * 
     */
    private int shieldState;

    /**
     * 
     */
    private int teamId;

    /**
     * @return
     */
    public float getHealth() {
        // TODO implement here
        return 0.0f;
    }

    /**
     * @return
     */
    public float getShield() {
        // TODO implement here
        return 0.0f;
    }

    /**
     * @param targ
     */
    public void attack(Robot targ) {
        // TODO implement here
    }

    /**
     * @param entity
     */
    public void move(ArrayList<Point> entity) {
        // TODO implement here
    }

    /**
     * @param entity
     */
    public void flee(ArrayList<Point> entity) {
        // TODO implement here
    }

    /**
     * @param robot
     */
    public void checkCollision(Robot robot) {
        // TODO implement here
    }

    /**
     * @param amount
     */
    public void takeDamge(float amount) {
        // TODO implement here
    }

    /**
     * @return
     */
    public int getTeam() {
        // TODO implement here
        return 0;
    }

}