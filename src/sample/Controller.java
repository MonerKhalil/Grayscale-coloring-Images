package sample;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;

public class Controller {

    public void converttoRgb(String input,String output) {
        try{
        ImageInputStream iis = ImageIO.createImageInputStream(input);
        }
        catch (IOException ioException) {
        }
    }
}
