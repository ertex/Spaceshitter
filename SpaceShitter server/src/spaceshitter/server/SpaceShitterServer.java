package spaceshitter.server;

//Author David Johansson Te2
import java.awt.Canvas;
import java.awt.Color;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SpaceShitterServer { //TODO fix so SpaceShitterServer does all of the processing and relays it to clients,fix ID system 

    //This is the server of the SpaceShitter game created by David Johansson Te3 Sundergymnasiet.
    //the way this is built is that all of the clients send their data to the server, then the server sends locations to the clients in form of LocationData.
    //LocationData has a ID connected to a ID of a sprite, the sprites and LocationDatas gets compared and the Sprite gets matched and updated
    //In hindsight I should have made almost every variable an arraylist, well not evry variable but those thats used by multiple sources.
    //I should also have made a generic Data package class so I would not have to rely on controll wether a obejct is a byte with value 1 to controll EVERYTHING
    public static int nextIdentifier;
    public static ArrayList<DataRequest> requests = new ArrayList(); //This is the new and impoved verison of the no loss data transfer that I am working on.
    //all of the netwok handlers will put their own Datarequests and then the server will process them one by one, returning what they want to have
    //This is hopefully superior to just having a single variable since this supports multiple networkhandlers and wont risk data loss.
    public static ArrayList<Sprite> newSprites = new ArrayList(); //could have fixed a better name but this is the best that I could think of
    //newSprites is here to act as a buffer for when newly created sprites is sent over from a client before they get added to the main "drawables" ArrayList

    private boolean running;
    private Canvas canvas;
    private final int xSize = 400;
    private final int ySize = 400;
    private ArrayList<JTextField> outputList = new ArrayList();
    private ArrayList<Sprite> drawables;
    JFrame frame;
    JPanel outputPanel;
    private ActionHandler actionHandler = new ActionHandler(); //creates the actionhandler to manage all the clicks and such
    private ArrayList<NetworkHandler> networkHandlers = new ArrayList();
    private ArrayList<LocationData> locations = new ArrayList();
    private ConnectionHandler connectionHandler;

    private Connection con;
    private Statement st;
    private ResultSet rs;
    private String tableName;
    private String tableDataID;
    private String tableNameID;

    private String driver = "org.apache.derby.jdbc.ClientDriver";
    private String url = "jbdc:derby://localhost:1527/SpaceShitterDB";
    private String username = "root";
    private String password = "";

    public static void main(String[] args) {

        System.out.println("SpaceShitterServer started");
        SpaceShitterServer server = new SpaceShitterServer();

    }

    public SpaceShitterServer() {
        connectionHandler  = new ConnectionHandler(networkHandlers);//creates the handler for new connections, with a reference to networkhandler 
       
        drawables = new ArrayList(); //This arraylist contains almost everything that is of importance and will be able to be drawn
        nextIdentifier = 1;
        drawables.add(new Sprite((double) 345, (double) 234, 54, 54, null));

        createAndShowGUI();
        running = true;
        output("asdasd");
        output("asdasd2rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        run();
    }

    public void run() {
        System.out.println("entered run()");
        while (running) {

         
            //Adds all of the new sprites to drawables
            drawables.addAll(newSprites);
            newSprites.clear();

            //if there are any data requests, the appropriate action will happen, for example sends away a specific sprite 
            //if a request for a specific sprite was requested
            for (DataRequest o : requests) {
                if (o.getData().getClass() == byte.class) {
                    byte data = (byte) o.getData();
                    if (data == 0) {
                        output("connected! to someone!, or maby just ping?");
                    }

                    if (data == 1) {
                        for (NetworkHandler n : networkHandlers) {
                            if (n.getNetworkID() == o.getNetworkID()) { //Makes sure it gets returned to the correct NetworkHandler
                                n.sendMessage(nextIdentifier);
                                nextIdentifier++; //So the same Identifier does not get used again
                            }
                        }

                    }

                }
                if (o.getData().getClass() == int.class) { //if a Int is recived it means a client needs a sprite with a specific Identification
                    //Then the specific sprite will be sent via networkhandler to the client that requested it
                    int data = (byte) o.getData();
                    for (Sprite u : drawables) {
                        if (u.getIdentification() == data) {
                            for (NetworkHandler n : networkHandlers) {
                                if (n.getNetworkID() == o.getNetworkID()) {
                                    n.sendMessage(u);
//PHEW! this nest of if and For:s lead up to this, all of the ID's match now and the Sprite can be sent to the correct networkHandler
                                }
                            }

                        }
                    }
                }

                if (o.getData().getClass() == double.class) {

                }

            }
            //This is the main loop for checking if something is hit by a projectile---
            if (drawables.size() > 0) { //Prevents it from calling null items
                for (Sprite o : drawables) {

                    if (o.getClass() == Projectile.class) { //Finds the first Projectile that will be matched up with a entity
                        for (Sprite u : drawables) {
                            if (u.getClass() == Entity.class & o != u) { //Finds the entity that will be compared
                                Projectile p = (Projectile) o;
                                Entity e = (Entity) u;
                                if (p.getRect().intersects(e.getRect())) {
                                    //Kill off(damage) e if p intersects with it
                                }
                            }
                        }
                    }
                }
            }

            //Updates the entities---
            for (Sprite o : drawables) {
                if (o.getClass() == Entity.class) {
                    ((Entity) o).update();
                }
                if (o.getClass() == Projectile.class) {
                    ((Projectile) o).update();
                }
            }

            //Sends of new locationdata to clients
            if (drawables.size() > 0 & networkHandlers.size() > 0) {

                for (Sprite o : drawables) {//generates a new array with locations to be sent 
                    locations.add(o.getLocationData());
                }
                for (NetworkHandler o : networkHandlers) {
                    if (o.connected()) {//Security!
                        o.sendMessage(locations); //sends the locations to the clients
                    }

                }
                locations.clear();//clears the locations for the next itteration.
                //Optimising suggestion here would be to only send updates to sprites that was moved.
            }

        }
    }

    public void saveProgress() {
//saves arraylists into SQL so it can be loaded next time the server starts.

    }

    public void getData() {

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            st = con.createStatement();

            String[] name = new String[255];
            String[] data = new String[255];

            int n = 0;
            String query = "select * from " + tableName;
            rs = st.executeQuery(query);
            while (rs.next()) {
                name[n] = rs.getString(tableNameID);
                data[n] = rs.getString(tableDataID);
                n++;

            }

            con.close();
            st.close();
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void instertData(String name, String data, String Table) {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            st = con.createStatement();

            String update = "INSERT INTO " + Table + " VALUES('" + data + "', '" + name + "')";
            st.executeUpdate(update);

            con.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAndShowGUI() { //The GUI is meant for debug and managing the server, the game will not be able to play from here
        System.out.println("Creating GUI");
        JFrame frame = new JFrame("space shitter");
        frame = new JFrame("space shitter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(xSize, ySize);

        outputPanel = new JPanel();
        outputPanel.setVisible(true);
        outputPanel.setSize(xSize, ySize);
        outputPanel.setBackground(Color.red);
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        outputPanel.setAlignmentX(LEFT_ALIGNMENT);

        frame.add(outputPanel);

        System.out.println("GUI created");
    }

    public void output(String string) {
        outputList.add(new JTextField(string));
        outputList.get(outputList.size() - 1).setVisible(true);
        outputList.get(outputList.size() - 1).setSize(xSize, 10);
        outputPanel.add(outputList.get(outputList.size() - 1));
        outputPanel.setSize(xSize, 30 * outputList.size());

    }

    private class ActionHandler implements ActionListener//this listens if a action is performed and exceutes the linked action 
    {

        public void actionPerformed(ActionEvent e) {

            try {

                String cmd = e.getActionCommand();
                switch (cmd) {

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //   }
        }
    }

}
