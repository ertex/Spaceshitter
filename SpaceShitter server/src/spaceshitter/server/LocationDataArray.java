package spaceshitter.server;

import java.util.ArrayList;

public class LocationDataArray {
// LocationDataArray and spaceshitter.server belongs to David Johansson Te2
//The only job for this clas is to hold an arraylist since SendObject complained about arraylists,outputstreams are crybabys
    
    ArrayList<LocationData> array;
    
    public LocationDataArray(){
    array = new ArrayList();
    }
    
    public void add(LocationData loc){
    array.add(loc);
    }

    public LocationData getObject(int n){

    return array.get(n);
}
    public ArrayList getArray(){
    return array;
    }
    
}