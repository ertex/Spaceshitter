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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetworkHandler implements Runnable { //TODO fix so all clients can connect on demand,infinite connections, no packet losses from overwritten messages

    ServerSocket serverSocket;
    private ArrayList<Socket> connections = new ArrayList();
    private ArrayList<ObjectOutputStream> outputs = new ArrayList();
    private ArrayList<ObjectInputStream> inputs = new ArrayList();
    // ObjectOutputStream output;

    private JTextField ipFeild, portFeild, openPortFeild;
    private JLabel localIp, localPort;
    String serverIP;
    int port;
    private JPanel networkPanel;
    private JButton connectButton;
    private Thread t;
    private boolean running, connected;

    private long pingSent, pingRecived; //this is used to get the ping to remote
    private int pingTime; //the current pingTime
    private int foobar = 0;//This is used in pingRemote, you might want to look the other way?
    private ConnectionHandler connectionHandler;

    public NetworkHandler(ActionListener actionHandler) {
        running = true;
        connected = false; //wether or not local is connected to remote
        port = 33678; //the port that will be used to connect to the server
        connectionHandler = new ConnectionHandler();
        t = new Thread(this, "NetworkHandler");

        createGUI(actionHandler);
        t.start();//also calls for the run
    }

    public void run() { //This is the core of the network, it makes sure that everything is executed in the right order

        try {
            serverSocket = new ServerSocket(port, 100); //creates the server socket that the remote will connect to
        } catch (IOException ex) {

        }
        while (running) {

            if (connections.size() != connectionHandler.getNumberOfConnections()) {
                //ADD OVERRIDE ARRAYLIST STUFF--------------------------------------------------
            }

            if (connections.size() > 0) { //checks if there is a estblished connection, if not: check for incoming connections

                try {

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
                    for (ObjectInputStream i : inputs) {
                        message = i.readObject();
                        if (message != null) {
                            break;
                        }
                    }
                    if (message != null) {
                        if (message.getClass() == byte.class & SpaceShitterServer.lastByteRecived == 0) {

                            SpaceShitterServer.lastByteRecived = (byte) message; //saves the last recived message/input in a static variable, this might not be the safest approach but it works for this application
                            message = null; //makes message null, this is to minimize packetloss by not overwriting any packets

                        } else if (message.getClass() == double.class & SpaceShitterServer.lastDoubleRecived == 0) {
                            SpaceShitterServer.lastDoubleRecived = (double) message; //saves the last recived message/input in a static variable, this might not be the safest approach but it works for this application
                            message = null; //makes message null, this is to minimize packetloss by not overwriting any packets

                        } else if (message instanceof Sprite & SpaceShitterServer.lastSpriteRecived == null) { //Checks to see if it is a lone object that extends Sprite, if so it will be put into the local Drawables array
                            SpaceShitterServer.lastSpriteRecived = (Sprite) message;
                            message = null; //makes message null, this is to minimize packetloss by not overwriting any packets
                        }
                    }
                }
            } catch (ClassNotFoundException n) {
                System.out.println("Could not read this");
            }

        } while (true);

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

    public void closeStreams() throws IOException { //yep, this turns of the streams, seems like it's a good thing to do

        for (ObjectOutputStream o : outputs) {
            o.close();

        }
        for (ObjectInputStream i : inputs) {
            i.close();
        }

        for (Socket c : connections) {
            c.close();
            c = null;
        }
    }

    public void sendMessage(Object message) { //sends a byte message to remote, a shorte less demanding message

        try {
            for (ObjectOutputStream o : outputs) {
                o.writeObject(message);
                o.flush();

            }

        } catch (IOException e) {
            System.out.println("Could not send that message");

        }

    }

    public void createGUI(ActionListener actionHandler) {

        networkPanel = new JPanel();
        networkPanel.setLayout(new FlowLayout());

        localIp = new JLabel("Enter a Ip adress:");
        localPort = new JLabel("your Port:"); //To make it clearer that the textfeild is the server port

        ipFeild = new JTextField("IP");
        ipFeild.setPreferredSize(new Dimension(100, 20));
        ipFeild.setVisible(true);

        portFeild = new JTextField("Port");
        portFeild.setPreferredSize(new Dimension(50, 20));
        portFeild.setVisible(true);

        openPortFeild = new JTextField("Port");
        openPortFeild.setText(port + "");
        openPortFeild.setPreferredSize(new Dimension(50, 20));
        openPortFeild.setVisible(true);
        openPortFeild.addActionListener(actionHandler);//This actionListner won't work

        connectButton = new JButton("Connect");
        connectButton.setVisible(true);
        connectButton.setText("Connect");
        connectButton.addActionListener(actionHandler);

        networkPanel.add(localIp); //adding all the Jcomponents in the right order
        networkPanel.add(ipFeild);
        networkPanel.add(portFeild);
        networkPanel.add(connectButton);
        networkPanel.add(localPort);
        networkPanel.add(openPortFeild);

    }

    public JPanel getNetworkPanel() { //This returns the networkpanel so it can be used in the main GUI
        return networkPanel;
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

}
