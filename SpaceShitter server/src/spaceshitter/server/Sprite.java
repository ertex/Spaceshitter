package spaceshitter.server;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {

    protected double x, y;
    protected int width, height;
    protected BufferedImage img;
       private int identifier;//this is bound to the Sprite that sent the locationData, it is ment to remove confusion around what entity sent it.


    public Sprite(int identifier ,double x, double y, int width, int height, BufferedImage img) {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (img != null) { //Security mesure to ensure that there is a picture, if the provided picture is gone, it replaces it with a generic picture.
            this.img = img;
        } else {
            System.out.println("ERROR no BufferedImage in Sprite");
            try {
                this.img = ImageIO.read(new File("src\\Images\\Default.jpg"));//This sets the default image of a newly created image
                System.out.println("  Replaced null Image with Default.jpg");
            } catch (IOException e) {
                System.out.println(" Error Default Image not found!!");
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(img, (int)x, (int)y, width, height, null);
    }

        public LocationData getLocationData(){
    return new LocationData(identifier,x,y);
    }
        
        public int getIdentification(){
        return identifier;
        }
        
        public void setLocation(LocationData loc){
        if(loc.getIdentifier()== identifier){
        x = loc.getX();
        y = loc.getY();
        }
        }
}
