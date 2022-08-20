package sample;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageInfo {

    public int[][] imagePixels;
    public BufferedImage img;
    public int Xpos, Ypos;
    public  int R, G, B,S;
    private BFS_Pixels bfs_pixels;
    public void info()
    {
        System.out.println(R+","+G+","+B);
        System.out.println("x: "+Xpos+" , y:" + Ypos);
    }

    public void ImageProcessing() {
        bfs_pixels = new BFS_Pixels();
        int width = img.getWidth();
        int height = img.getHeight();
        imagePixels = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                imagePixels[x][y] = img.getRGB(x, y);
            }
        }
        bfs_pixels.bfs(imagePixels.length, imagePixels[0].length,
                imagePixels, Xpos, Ypos, R, G, B,S);

        for (int y2 = 0; y2 < height; y2++) {
            for (int x2 = 0; x2 < width; x2++) {
                img.setRGB(x2, y2, imagePixels[x2][y2]);
            }
        }
       // System.out.println("Done...(:");
    }

    public void StoreImage(File file)
    {
        try {
        ImageIO.write(img, "png", file);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
