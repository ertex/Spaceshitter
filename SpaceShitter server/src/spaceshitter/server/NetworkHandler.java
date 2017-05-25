package spaceshitter.server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetworkHandler implements Runnable{ //TODO fix so all clients can connect on demand,infinite connections, no packet losses from overwritten messages

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    String serverIP;
    int port;

    private Thread t;
    private boolean running, connected;

    private long pingSent, pingRecived; //this is used to get the ping to remote
    private int pingTime; //the current pingTime
    private int foobar = 0;//This is used in pingRemote, you might want to look the other way?
    private int networkID; //The uniqe ID for this network handler. this is here so get requests(made by sending a DataRequest to Server.requests) 
    private int timeoutAmmount; //How many Cocurrent IOExceptions can be made before the sockets termintes, this is so there are no unneccesarry sockets open.
    private int timeoutCount; //Complementry to above
    //returns to the correct networkhandler

    public NetworkHandler(Socket socket, int networkID) {

        System.out.println("A new network handler has been created");
        timeoutAmmount=200; 
        this.networkID = networkID;
        this.socket = socket;
        running = true;
        connected = false; //wether or not local is connected to remote
        port = 33678; //the port that will be used to connect to the server, the port is increeseing for each new player so every pleyer gets his own socket
        t = new Thread(this, "NetworkHandler");
        t.start();//also calls for the run
    }

    public void run() { //This is the core of the network, it makes sure that everything is executed in the right order

        while (running) {

            if (socket == null) { //checks if there is a estblished connection, if not: check for incoming connections

            } else {

                try {
                    setupStreams(); //creates the sockets and connects them
                    whileConnected();//The program stays here as long as the progrm is running
                    closeStreams(); //closes all the sokets nice and easy so nothing breaks
                } catch (IOException ex) {

                }

            }
        }

    }

    public void whileConnected() throws IOException { //this method is the main core of the class, it recives messages
        Object message = null;
        sendMessage((byte) 0);//sends a 0 in confirmation that it is connected

        do {

            try {
                if (message == null) {//Makes sure it does not overwrite anything
                    message = null; //removes some nasty nullpointers when there are no connections

                    message = input.readObject(); //reads the inputstream

                }
                if (message != null) {
                    if (message.getClass() == byte.class) {

                        SpaceShitterServer.requests.add(new DataRequest(networkID, (byte) message)); //saves the last recived message/input in a static variable, this might not be the safest approach but it works for this application
                        message = null; //makes message null, this is to minimize packetloss by not overwriting any packets

                    } else if (message.getClass() == int.class) {
                        SpaceShitterServer.requests.add(new DataRequest(networkID, (int) message)); //saves the last recived message/input in a static variable, this might not be the safest approach but it works for this application
                        message = null; //makes message null, this is to minimize packetloss by not overwriting any packets
                        System.out.println("Int recived");
                    } else if (message.getClass() == double.class) {

                        SpaceShitterServer.requests.add(new DataRequest(networkID, (double) message)); //saves the last recived message/input in a static variable, this might not be the safest approach but it works for this application
                        message = null; //makes message null, this is to minimize packetloss by not overwriting any packets

                    } else if (message instanceof Sprite) { //Checks to see if it is a lone object that extends Sprite, if so it will be put into the local Drawables array

                        SpaceShitterServer.newSprites.add((Sprite) message);
                        message = null; //makes message null, this is to minimize packetloss by not overwriting any packets
                    }
                }

            } catch (ClassNotFoundException n) {
                System.out.println("Could not read this");
            }
            if(timeoutCount> timeoutAmmount){
            
            connected = false; //breaks the loop Hence closing the streams
            System.out.println("A client disconnected!");
            }

        } while (connected);

    }

    public void pingRemote() {//sends a mesge that bounces on remote as "43" nd time gets recorded, see Program.run() "case 42" & "case 43"
        if (connected) {//won't ping unless remote is connected
            foobar++;
            if (foobar >= 120) {//this is a way that makes it only ping every 60 ittertions, hence not drawing stupid ammounts of power
                foobar = 0;
                //this solotion is horrible, if I forget to ask you how to do this in  different way, take contact
                pingTime = (int) (pingRecived - pingSent);//this calculates the ping by taking the diference in time between reciving and sending a message
                //this means it lags behind by one tick, but that is close enogh
                sendMessage((byte) 0);//sends the ping
                pingSent = System.currentTimeMillis(); //saves the time it was sent
            }
        }
    }

    public void setupStreams() {
        try {
            //creates the streams

            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            connected = true;
            sendMessage((byte) 0);
            System.out.println("Streams are setup! ready to go!");
        } catch (IOException ex) {
            
            System.out.println("strems did not get setup");
        }

    }

    public void closeStreams() throws IOException { //yep, this turns of the streams, seems like it's a good thing to do

        output.close();
        input.close();
        socket.close();

    }

    public void sendMessage(Object message) { //sends a Object message to remote
        if (output != null) {
            try {
                
                output.writeObject(message);
                output.flush();
                
                timeoutCount=0;

            } catch (IOException e) {
                System.out.println("Could not send that message");
                timeoutCount++;
            }

        } else {
            System.out.println("Output is null");
        }
    }

    public boolean connected() {
        return connected;
    }

    public int getPingTime() {
        return pingTime;
    }

    public void setPingRecived(long time) {
        pingRecived = time;
    }

    public int getNetworkID() {
        return networkID;
    }

}
