import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class StegResultProcessor {
    private Pixel[][] pixels;
    private int width, height;
    private String name;

    public StegResultProcessor(String name) {
        this.name = "StegResults/" + name;
    }

    public StegResultProcessor() {
        this("Result.png");
    }

    public void processPlanes(Plane[] rgbPlanes, Plane[] alphaPlanes) {
        height = rgbPlanes[0].getHeight();
        width = rgbPlanes[0].getWidth();
        pixels = new Pixel[height][width];

        for(int j = 0; j < height; j++) {
            for(int k = 0; k < width; k++) {
                int[] rgb = new int[24];
                for(int i = 0; i < 24; i++) rgb[i] = rgbPlanes[i].getBit(j, k);

                int[] alpha = new int[8];
                for(int i = 0; i < 8; i++) alpha[i] = alphaPlanes[i].getBit(j, k);

                pixels[j][k] = new Pixel(rgb, alpha);
            }
        }
    }

    public void constructImage() throws IOException {
        BufferedImage img = new BufferedImage(width, height, 2);

        for(int j = 0; j < height; j++) for(int k = 0; k < width; k++) img.setRGB(k, j, pixels[j][k].getRGB());
        //for every pixel, sets RGB to be based on our pixel array

        ImageIO.write(img, "png", new File(name));
    }
}
