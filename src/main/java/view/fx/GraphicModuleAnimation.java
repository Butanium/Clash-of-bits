package view.fx;

import com.codingame.game.Constants;
import com.codingame.gameengine.module.entities.SpriteAnimation;
import view.entitiesSprites.ViewPart;
import view.managers.ViewManager;

public class GraphicModuleAnimation extends ViewPart {
    private boolean active = true;
    private int state = 0;
    private SpriteAnimation anim;
    private final ViewManager viewManager;
    private AnimationType animType;

    public GraphicModuleAnimation(ViewManager viewManager, AnimationType animType, int x, int y, int z, double scale, double alpha) {
        this.viewManager = viewManager;
        this.animType = animType;
        init(x, y, z, scale, alpha, animType);
    }

    public void reInit(int x, int y, int z, double scale, AnimationType animType) {
        this.animType = animType;
        anim.reset()
                .setImages(animType.getImages())
                .setX(x).setY(y).setScale(scale)
                .setDuration(animType.getDuration())
                .setZIndex(z)
                .setLoop(animType.getLoop());
    }


    private void init(int x, int y, int z, double scale, double alpha, AnimationType animType) {
        state = 0;
        anim = viewManager.graphicEntityModule.createSpriteAnimation()
                .setImages(animType.getImages())
                .setX(x)
                .setY(y)
                .setAnchor(.5)
                .setDuration(animType.getDuration())
                .setZIndex(z).setScale(scale).reset().setLoop(animType.getLoop())
                .setAlpha(alpha);
        viewManager.removeForDebug(anim);
        viewManager.addToArena(anim);
    }

    public void reset(int x, int y, double scale) {
        state = 0;
        anim.reset().setX(x).setY(y).setScale(scale);
        this.setVisible(true);
        updateVisibility();

    }

    @Override
    public void update() {
        state++;

    }

    @Override
    public boolean isActive() {
        return animType.getLoop() || state * Constants.FRAME_DURATION <= animType.getDuration();
    }

    @Override
    public void onRemove() {
        this.setVisible(false);
        this.updateVisibility();
    }

    @Override
    public SpriteAnimation getSprite() {
        return anim;
    }

    @Override
    public SpriteAnimation getDebugSprite() {return  getSprite();}
}
