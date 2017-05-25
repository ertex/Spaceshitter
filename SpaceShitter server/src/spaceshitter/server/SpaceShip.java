package spaceshitter.server;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpaceShip extends Entity {

    private boolean localPlayer;
    private ArrayList<Projectile> projectileBuffer;

    public SpaceShip(int x, int y, int width, int height, int speed, BufferedImage img, boolean localPlayer, String imgURL) {
        super(x, y, width, height, speed, img, true, true, imgURL);
        projectileBuffer = new ArrayList();
         data = "S" + "SS" + x + "," + y + "," + width + "," + height + "," + imgURL + "," + super.getIdentification() + "," + moveable + "," + collideable + "," + speed;

    }

    public SpaceShip(String data) {
        super(data);
        String[] parts = data.split(",");

        if (parts[0].equals("S") & parts[1].equals("SS")) {//makes sure the data string is a compatible type

        }
    }

    public void update() {
        if (localPlayer) {

        } else {

        }
    }

    public void shoot() {
        projectileBuffer.add(new Projectile(x, y, (byte) 0, new Point(1, 0)));
    }

    public ArrayList fetchProjectileBuffer() {
        return projectileBuffer;
    }

    public void clearProjectileBuffer() {
        projectileBuffer.clear();
    }

}
