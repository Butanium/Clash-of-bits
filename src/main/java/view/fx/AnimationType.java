package view.fx;

import com.codingame.game.Constants;

public enum AnimationType {
    Explosion(5, "e", 0);

    private final int length;
    private final String prefix;
    private final int animId;
    private final int duration;

    AnimationType(int length, String prefix, int animId) {
        this.length = length;
        this.prefix = prefix;
        this.animId = animId;
        this.duration = Constants.FRAME_DURATION * (length + 1);
    }

    AnimationType(int length, String prefix, int animId, int duration) {
        this.length = length;
        this.prefix = prefix;
        this.animId = animId;
        this.duration = duration;
    }

    public String[] getImages() {
        String[] images = new String[length + 1];
        for (int i = 0; i < images.length - 1; i++) {
            images[i] = prefix + (i + 1) + ".png";
        }
        images[length] = "t.png";
        return images;
    }

    public int getAnimId() {
        return animId;
    }

    public int getDuration() {
        return duration;
    }
}
