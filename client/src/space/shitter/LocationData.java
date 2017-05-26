package space.shitter;

public class LocationData {
// LocationData and  belongs to David Johansson Te2

    private double x;
    private double y;
    private int identifier;//this is bound to the entity that sent the locationData, it is ment to remove confusion around what entity sent it.
    private String data;

    public LocationData(int identifier, double x, double y) { //In hindsight, this is basicly a stripped down verion of a point. but with a identifier
        this.x = x;                                             //it is used to transfer the location of a entity
        this.y = y;
        this.identifier = identifier;
        data = "S,LD," + x + "," + y + "," + identifier;//This is the sring that will be sent over the internet to remote
        //The reasoning for this approach contruary to just sending LocationDataArray is that I had trouble
        //with reciving the Objects, also reciving primitive objects are less vurnble in compatability errors
        //although not as safe since a fake LocationData can be sent very easily
        //so the sring will just be sent over nd then reconstructed into a LocationData

    }

    public LocationData(String data) {
        String[] parts = data.split(",");
        if (parts[0].equals("S") & parts[1].equals("LD")) {//makes sure the data string is a compatible type
            //S is for start, LD is for LocationData
            try {
                x = (Double.parseDouble(parts[2]));
                y = (Double.parseDouble(parts[3]));
                identifier = (int) Float.parseFloat(parts[4]);
            } catch (NumberFormatException e) {
                System.out.println("LD did not parse");
            }
        }

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getData() {
        return data;
    }

}
