package space.shitter;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpaceShip extends Entity{
    private boolean localPlayer;
    private ArrayList<Projectile> projectileBuffer;
    
    public SpaceShip(int identiifier,int x, int y, int width, int height,int speed, BufferedImage img,boolean localPlayer){
    super(identiifier,x, y, width, height,speed, img,true,true);
    projectileBuffer = new ArrayList();
    }
    
    public void update(){
    if(localPlayer){
    
    }
    else{
    
    }
    }
    
    public void shoot(){
    projectileBuffer.add(new Projectile(1,x,y,(byte)0,new Point(1,0)));
    }
    
    public ArrayList fetchProjectileBuffer(){
    return projectileBuffer;
    }
    
    public void clearProjectileBuffer(){
    projectileBuffer.clear();
    }
    
    
}
