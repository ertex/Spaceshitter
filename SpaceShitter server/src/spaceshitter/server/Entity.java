package spaceshitter.server;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity extends Sprite {

    protected int speed;
    protected boolean moveable, collideable;
    protected Rectangle rect;

    public Entity(double x, double y, int width, int height, BufferedImage img, boolean collideable, String imgURL) { //Static Entity
        super(x, y, width, height, img, imgURL);
        this.moveable = false;
        this.collideable = collideable;
        speed = 0;
        if (collideable) {
            rect = new Rectangle((int) x, (int) y, width, height);
        }
        data = "S," + "E," + x + "," + y + "," + width + "," + height + "," + imgURL + "," + super.getIdentification() + "," + moveable + "," + collideable + "," + speed;
    }

    public Entity(double x, double y, int width, int height, int speed, BufferedImage img, boolean moveable, boolean collideable, String imgURL) {//controllable Entity
        super(x, y, width, height, img, imgURL);
        this.moveable = moveable;
        this.collideable = collideable;
        this.speed = speed;
        if (collideable) {
            rect = new Rectangle((int) x, (int) y, width, height);
        }
        data = "S," + "E," + x + "," + y + "," + width + "," + height + "," + imgURL + "," + super.getIdentification() + "," + moveable + "," + collideable + "," + speed;
    }

    public Entity(double x, double y, int width, int height, BufferedImage img, String imgURL) {//Projectile entity
        super(x, y, width, height, img, imgURL);
        this.moveable = true;
        this.collideable = true;
        this.speed = speed;
        rect = new Rectangle((int) x, (int) y, width, height);

        data = "S," + "E," + x + "," + y + "," + width + "," + height + "," + imgURL + "," + super.getIdentification() + "," + moveable + "," + collideable + "," + speed;
    }

    public Entity(String data) {
        super(data);
        String[] parts = data.split(",");
        if (parts[0].equals("S") & parts[1].equals("E")) {//makes sure the data string is a compatible type
            moveable = parts[8].equals("True");
            collideable = parts[9].equals("True");
            speed = (int) Float.parseFloat(parts[8]);
            if (collideable) {
                rect = new Rectangle((int) x, (int) y, width, height);
            }
        }
        this.data = data;

    }

    public void update() {

    }

    public Rectangle getRect() {
        if (rect != null) {
            return rect;
        } else {
            System.out.println("tried to call a null rectangle");
            return null;

        }
    }

    public void setPos(int x, int y) {
        rect.setLocation(x, y);
        super.x = x;
        super.y = y;
    }

    public void translate(double x, double y) {
        if (moveable) {
            super.x += x;
            super.y += y;
            rect.setLocation((int) x, (int) y);
        }
    }

}
