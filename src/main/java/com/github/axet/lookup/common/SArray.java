package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

import com.github.axet.lookup.Capture;

public class SArray {
    public SArray image;

    public int cx;
    public int cy;

    public double s[];

    public SArray() {

    }

    public SArray(SArray buf) {
        image = buf;

        cx = buf.cx;
        cy = buf.cy;

        s = new double[cx * cy];
    }

    public double s(int x, int y) {
        if (x < 0)
            return 0;
        if (y < 0)
            return 0;
        return s[i(x, y)];
    }

    public int i(int x, int y) {
        return y * cx + x;
    }

    /**
     * Math Sigma (sum of the blocks)
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public double sigma(int x1, int y1, int x2, int y2) {
        double a = s(x1 - 1, y1 - 1);
        double b = s(x2, y1 - 1);
        double c = s(x1 - 1, y2);
        double d = s(x2, y2);
        return a + d - b - c;
    }

    public double sigma() {
        return sigma(0, 0, cx - 1, cy - 1);
    }

    public double mean(int x1, int y1, int x2, int y2) {
        int area = (x2 - x1 + 1) * (y2 - y1 + 1);
        return sigma(x1, y1, x2, y2) / (double) area;
    }

    public double mean() {
        return mean(0, 0, cx - 1, cy - 1);
    }

    public void printDebug() {
        for (int y = 0; y < cy; y++) {
            for (int x = 0; x < cx; x++) {
                System.out.print(s(x, y));
                System.out.print("\t");
            }
            System.out.println("");
        }
    }

    public BufferedImage getImage() {
        int[] ss = new int[s.length];
        for (int i = 0; i < ss.length; i++) {
            int g = (int) s[i];

            int argb = 0xff000000 | (g << 16) | (g << 8) | (g);

            ss[i] = argb;
        }

        BufferedImage image = new BufferedImage(cx, cy, BufferedImage.TYPE_INT_ARGB);
        image.getWritableTile(0, 0).setDataElements(0, 0, image.getWidth(), image.getHeight(), ss);
        return image;
    }

    public void writeDesktop() {
        Capture.writeDesktop(getImage());
    }

    public static void main(String[] args) {
        int[] s = new int[] {

        5, 2, 5, 2,

        3, 6, 3, 6,

        5, 2, 5, 2,

        3, 6, 3, 6

        };

        SArray a = new SArray();
        a.cx = 4;
        a.cy = 4;
        a.s = new double[a.cx * a.cy];

        for (int y = 0; y < a.cy; y++) {
            for (int x = 0; x < a.cx; x++) {
                a.s[a.i(x, y)] = s[a.i(x, y)] + a.s(x - 1, y) + a.s(x, y - 1) - a.s(x - 1, y - 1);
            }
        }

        boolean t = a.sigma() == 64;

    }
}