package spaceshitter.server;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity extends Sprite {

    protected int speed;
    protected boolean moveable, collideable;
    protected Rectangle rect;
 
    public Entity(int identifier,double x, double y, int width, int height, BufferedImage img, boolean collideable) { //Static Entity
        super(identifier,x, y, width, height, img);
        this.moveable = false;
        this.collideable = collideable;
        speed = 0;
        if (collideable) {
            rect = new Rectangle((int) x, (int) y, width, height);
        }
    }

    public Entity(int identifier,double x, double y, int width, int height, int speed, BufferedImage img, boolean moveable, boolean collideable) {//controllable Entity
        super(identifier,x, y, width, height, img);
        this.moveable = moveable;
        this.collideable = collideable;
        this.speed = speed;
        if (collideable) {
            rect = new Rectangle((int) x, (int) y, width, height);
        }
    }

    public Entity(int identifier,double x, double y, int width, int height, BufferedImage img) {//Projectile entity
        super(identifier,x, y, width, height, img);
        this.moveable = true;
        this.collideable = true;
        this.speed = speed;
        rect = new Rectangle((int) x, (int) y, width, height);

    }
    
    public void update(){
    
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
        if(moveable){
        super.x += x;
        super.y += y;
        rect.setLocation((int) x, (int) y);
        }
    }
    
    


}
