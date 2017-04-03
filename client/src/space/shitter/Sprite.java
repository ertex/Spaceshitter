package space.shitter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {

    protected double x, y;
    protected int width, height;
    protected BufferedImage img;

    public Sprite(double x, double y, int width, int height, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (img != null) { //Security mesure to ensure that there is a picture, if the provided picture is gone, it replaces it with a generic picture.
            this.img = img;
        } else {
            System.out.println("no BufferedImage in Sprite");
            try {
                this.img = ImageIO.read(new File("src\\Images\\Default.jpg"));//This sets the default image of a newly created image
                System.out.println("Replaced null Image with Default.jpg");
            } catch (IOException e) {
                System.out.println("Default Image not found!");
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(img, (int)x, (int)y, width, height, null);
    }

}
