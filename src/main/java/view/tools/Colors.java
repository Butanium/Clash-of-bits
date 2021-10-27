package view.tools;

import java.util.Random;

public class Colors {
    public class RGB {
        private final int r;
        private final int g;
        private final int b;

        RGB(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        RGB(int hex) {
            r = hex / (256 * 256);
            g = (hex / 256) % 256;
            b = hex % 256;
        }

        public int R() {
            return r;
        }

        public int B() {
            return b;
        }

        public int G() {
            return g;
        }

        public RGB lerp(RGB c, double t) {
            return new RGB((int) Math.round((this.r) * (1 - t) - c.r * t),
                    (int) Math.round((this.g) * (1 - t) - c.g * t),
                    (int) Math.round((this.b) * (1 - t) - c.b * t));
        }

        public int hex() {
            return toHex(this.r, this.g, this.b);
        }

    }
    private int invertColor(int color) {
        return color ^ 0x00ffffff;
    }

    public RGB toRGB(int hex) {
        return new RGB(hex);
    }

    public int toHex(int r, int g, int b) {
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }

    public int randomColorChange(int hex, int shift) {
        Random random = new Random();
        RGB rgb = toRGB(hex);
        int r = rgb.r + (int) (random.nextDouble() * (Math.min(255 - rgb.r, shift) + Math.min(rgb.r, shift))) - Math.min(rgb.r, shift);
        int g = rgb.g + (int) (random.nextDouble() * (Math.min(255 - rgb.g, shift) + Math.min(rgb.g, shift))) - Math.min(rgb.g, shift);
        int b = rgb.b + (int) (random.nextDouble() * (Math.min(255 - rgb.b, shift) + Math.min(rgb.b, shift))) - Math.min(rgb.b, shift);
        return toHex(r, g, b);
    }

    private int lerpRGB(int c1, int c2, double t) {
        return new RGB(c1).lerp(new RGB(c2), t).hex();
    }

}
