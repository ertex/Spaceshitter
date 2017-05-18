package space.shitter;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Projectile extends Entity {
// Projectile and space.shitter belongs to David Johansson Te2

    private Point direction;

    public Projectile(double x, double y, byte type, Point direction) {
        super(x, y, 0, 0, null);
        this.direction = direction;
        if (type == 0) {
            speed = 5;
            super.width = 5;
            super.height = 5;
            super.img = null; //TEMPORARY!
        } else {
            super.width = 1;
            super.height = 1;
            super.img = null;
        }

    }

    public void update() {
        super.translate(speed * direction.getX(), speed * direction.getY()); //moves the Projectile in a constant speed towards a certain direction
    }

}
