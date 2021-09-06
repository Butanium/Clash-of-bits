package view.modules;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * The TooltipModule takes care of displaying tooltips under the mouse cursor when an element has a linked tooltip text.
 */
@Singleton
public class CameraModule implements Module {

    GameManager<AbstractPlayer> gameManager;
    @Inject
    GraphicEntityModule entityModule;
    Map<Integer, Boolean> registered, newRegistration;
    Double cameraOffset;
    Integer container, sizeX, sizeY = -1;
    boolean sentContainer = false;
    double previousOffset = -1;


    @Inject
    CameraModule(GameManager<AbstractPlayer> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
        registered = new HashMap<>();
        newRegistration = new HashMap<>();
        cameraOffset = 10.;

    }

    @Override
    public void onGameInit() {
        sendFrameData();
    }

    @Override
    public void onAfterGameTurn() {
        sendFrameData();
    }

    @Override
    public void onAfterOnEnd() {
    }


    private void sendFrameData() {
        Object[] data = {null, null, null};
        Object[] empty = {null, null, null};
        if (!newRegistration.isEmpty()) {
            data[0] = new HashMap<>(newRegistration);
            System.out.printf("added size : %d\n", newRegistration.size());
            newRegistration.clear();
        }
        if (cameraOffset != previousOffset) {
            data[1] = cameraOffset;
            previousOffset = cameraOffset;
        }
        if (!sentContainer && container >= 0) {
            data[2] = new Integer[]{container, sizeX, sizeY};
            sentContainer = true;
        }
        if (!Arrays.equals(data, empty)) {
            gameManager.setViewData("c", data);
        }
    }

    /**
     * Make the camera include the entity in its field of view
     *
     * @param entity the <code>Entity</code> to link the tooltip to
     */
    public void addTrackedEntity(Entity<?> entity) {
        if (entity.getParent().isPresent() && entity.getParent().get().getId() == container) {
            int id = entity.getId();
            if (!registered.getOrDefault(id, false)) {
                newRegistration.put(id, true);
                registered.put(id, true);
                System.out.printf("registered %d\n", id);
            }
        } else {
            throw new RuntimeException("The entity given can't be track because it's not the son of the container !\n" +
                    "don't forget to init the camera with createCamera");
        }
    }

    /**
     * @param entity the <code>Entity</code> to get the associated tooltip text from
     * @return if the entity is tracked by the camera or not
     */
    public Boolean isTracked(Entity<?> entity) {
        return registered.getOrDefault(entity.getId(), false);
    }

    /**
     * Make the camera stop tracking this entity
     *
     * @param entity the <code>Entity</code> to remove a tooltip from
     */
    public void removeTrackedEntity(Entity<?> entity) {
        if (registered.getOrDefault(entity.getId(), false)) {
            newRegistration.put(entity.getId(), false);
            registered.remove(entity.getId());
        }
    }

    /**
     * Sets the camera offset to the given value
     *
     * @param value the new camera offset, a positive
     */
    public void setCameraOffset(double value) {
        cameraOffset = value;
    }

    /**
     * Initialize the camera with container which has to contain all the other entities tracked by the camera
     *
     * @param container   the <code>Entity</code> to set as the container
     * @param viewerSizeX the x size in the viewer of your container if the scale is 1
     * @param viewerSizeY the y size in the viewer of your container if the sacle is 1
     */
    public void createCamera(Entity<?> container, int viewerSizeX, int viewerSizeY) {
        this.container = container.getId();
        this.sizeX = viewerSizeX;
        this.sizeY = viewerSizeY;
    }
}