package spaceshitter.server;

public class LocationData {
// LocationData and  belongs to David Johansson Te2
private double x;
private double y;
private int identifier;//this is bound to the entity that sent the locationData, it is ment to remove confusion around what entity sent it.
    
    public LocationData(int identifier,double x,double y){ //In hindsight, this is basicly a stripped down verion of a point. but with a identifier
    this.x = x;                                             //it is used to transfer the location of a entity
    this.y = y;
    this.identifier= identifier;
    
    }

    public double getX(){
    return x;
    }

    public double getY(){
    return y;
    }
    public int getIdentifier(){
    return identifier;
    }

}