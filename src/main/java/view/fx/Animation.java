package view.fx;

import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Sprite;

import static com.codingame.gameengine.module.entities.Curve.LINEAR;

public class Animation {
    private final Sprite[] sprites;
    private boolean reverse = false;
    private boolean looping = false;
    private boolean active = true;
    private int state = 0;
    private Curve curve = LINEAR;

    private int maxFrameLength = 1;
    private int frameState = 0;

    public Animation(Sprite[] sprites, int init) {
        this.sprites = sprites;
        state = init;
    }

    public Animation(Sprite[] sprites) {
        this.sprites = sprites;
    }

    public Animation(Sprite[] sprites, int init, Curve c) {
        this.sprites = sprites;
        state = init;
        curve = c;
    }

    public Animation(Sprite[] sprites, Curve c) {
        this.sprites = sprites;
        curve = c;
    }

    public void setReverse(boolean r) {
        reverse = r;
    }

    public Animation setLooping(boolean l) {
        looping = l;
        return this;
    }

    public Animation setActive(boolean a) {
        active = a;
        return this;
    }

    public Animation setFrameLength(int frameLength) {
        this.maxFrameLength = frameLength;
        return this;
    }

    public void update() {
        if (!active) {
            return;
        }
        frameState++;
        if (frameState < maxFrameLength) {
            return;
        }
        if (!reverse) {
            if (state < sprites.length - 1) {
                if (state >= 0) {
                    sprites[state].setAlpha(0, curve);
                }
                sprites[++state].setAlpha(1, curve);
            }
        } else {
            if (state > 0) {
                if (state < sprites.length) {
                    sprites[state].setAlpha(0, curve);
                }
                sprites[--state].setAlpha(1, curve);
            }
        }
        if (state == 0 && reverse || state == sprites.length - 1) {
            reverse = !reverse;
        }

        if (frameState == maxFrameLength) {
            frameState = 0;
        }
    }

    public void update(boolean r) {
        this.reverse = r;
        update();
    }
}
