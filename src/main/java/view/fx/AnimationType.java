package view.fx;

import com.codingame.game.Constants;

import static com.codingame.game.Constants.FRAME_DURATION;

public enum AnimationType {
    Explosion(5, "e", 0),
    Tesla(6, "t", 1, FRAME_DURATION * 8, true, true);
    private final int length;
    private final String prefix;
    private final int animId;
    private final int duration;
    private final boolean loop;
    private final boolean reverse;

    AnimationType(int length, String prefix, int animId) {
        this.length = length;
        this.prefix = prefix;
        this.animId = animId;
        this.duration = Constants.FRAME_DURATION * (length + 1);
        reverse = loop = false;

    }

    AnimationType(int length, String prefix, int animId, int duration) {
        this.length = length;
        this.prefix = prefix;
        this.animId = animId;
        this.duration = duration;
        reverse = loop = false;
    }


    AnimationType(int length, String prefix, int animId, int duration, boolean loop) {
        this.length = length;
        this.prefix = prefix;
        this.animId = animId;
        this.duration = duration;
        this.loop = loop;
        reverse = false;
    }

    AnimationType(int length, String prefix, int animId, boolean loop) {
        this.length = length;
        this.prefix = prefix;
        this.animId = animId;
        this.duration = Constants.FRAME_DURATION * (length + (loop ? 0 : 1));
        this.loop = loop;
        reverse = false;
    }

    AnimationType(int length, String prefix, int animId, int duration, boolean loop, boolean reverse) {
        this.length = length;
        this.prefix = prefix;
        this.animId = animId;
        this.duration = duration;
        this.loop = loop;
        this.reverse = reverse;
    }

    AnimationType(int length, String prefix, int animId, boolean loop, boolean reverse) {
        this.length = length;
        this.prefix = prefix;
        this.animId = animId;
        int l = loop ? 0 : 1;
        int size = reverse ? 2 * (length - 1) : length + l;
        this.duration = Constants.FRAME_DURATION * size;
        this.loop = loop;
        this.reverse = reverse;
    }




    public String[] getImages() {
        int l = loop ? 0 : 1;
        int size = reverse ? 2 * (length - 1) : length + l;
        String[] images = new String[size];
        for (int i = 0; i < length; i++) {
            images[i] = prefix + (i + 1) + ".png";
        }
        if (!loop) {
            images[length] = "t.png";
        }
        if (reverse) {
            for (int i = length; i < size; i++) {
                images[i] = prefix + (2 * length - i - 1) + ".png";
            }
        }
        return images;
    }

    public int getAnimId() {
        return animId;
    }

    public int getDuration() {
        return duration;
    }

    public boolean getLoop() {
        return loop;
    }
}
