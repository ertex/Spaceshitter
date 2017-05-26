package space.shitter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Sprite {

    protected double x, y;
    protected int width, height;
    protected BufferedImage img;
    private int identifier;//this is bound to the Sprite that sent the locationData, it is ment to remove confusion around what entity sent it.
    protected String data, imgURL;

    public Sprite(double x, double y, int width, int height, BufferedImage img, String imgURL) {
        while (Program.nextIdentifier == 0) {
            //This while loop is just to hold the program still for a while until a new nextIdentifier has arrived
           int i = 0;//This is usefull somehow? well for somereason it wont work without something occuping it
           i++;
        }
        this.imgURL = imgURL;
        identifier = Program.nextIdentifier; //This simlyfies the way Sprites are created since it requiers less effort in code in other places
        //In hindsight Should I have made a Arraylist here aswell in order to have a buffert to smooth out the program.
        Program.nextIdentifier = 0;

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
        data = "S" + "S," + x + "," + y + "," + width + "," + height + "," + imgURL + "," + identifier;
    }

    public Sprite(String data) {//A Sprite created from a string of data, probobly from a InputStream
        String[] parts = data.split(",");
        if (parts[0].equals("S") & parts[1].equals("S")) {//makes sure the data string is a compatible type
            //(first)S is for start, S is for Sprite
            try {

                x = (Double.parseDouble(parts[2]));
                y = (Double.parseDouble(parts[3]));
                width = (int) Float.parseFloat(parts[4]);
                height = (int) Float.parseFloat(parts[5]);
                imgURL = parts[6];
                identifier = (int) Float.parseFloat(parts[7]);

                try {
                    this.img = ImageIO.read(new File(imgURL));//tries to find the same imgage as was used for the recieved sprite data
                } catch (IOException ex) {
                    try {

                        this.img = ImageIO.read(new File("src\\Images\\Default.jpg"));//This sets the default image of a newly created image
                        System.out.println("  Replaced null Image with Default.jpg");

                    } catch (IOException e) {
                        System.out.println(" Error Default Image not found!!");
                    }
                }
            } catch (NumberFormatException e) {

            }
        }
        this.data = data;
    }

    public String getData() { //returns the sprites data
        return data;
    }

    public void draw(Graphics g) {
        g.drawImage(img, (int) x, (int) y, width, height, null);
    }

    public LocationData getLocationData() {
        return new LocationData(identifier, x, y);
    }

    public int getIdentification() {
        return identifier;
    }

    public void setLocation(LocationData loc) {
        if (loc.getIdentifier() == identifier) {
            x = loc.getX();
            y = loc.getY();
        }
    }
}
