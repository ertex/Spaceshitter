package space.shitter;

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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.ArrayList;

public class NetworkHandler implements Runnable {

    ServerSocket serverSocket;
    Socket socket;
    ObjectOutputStream output;
    ObjectInputStream input;

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

    public NetworkHandler(ActionListener actionHandler) {
        running = true;
        connected = false; //wether or not local is connected to remote
        port = 33678; //the port that will be used to connect to the server
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

            if (socket == null) { //checks if there is a estblished connection, if not: check for incoming connections
                try {
                    connectToServer();
                } catch (IOException e) {

                }
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

    public void setupStreams() throws IOException {//creates the streams 
        connected = true;
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());

    }


    public void whileConnected() throws IOException { //this method is the main core of the class, it recives messages
        Object message = null;
        sendByteMessage((byte) 0);//sends a 0 in confirmation that it is connected

        do { //Main loop of networkhandler
            try {
                System.out.println("got a message!");
                message = input.readObject();
                if (message.getClass() == byte.class) {

                    Program.lastByteRecived = (byte) message; //saves the last recived message/input in a static variable, this might not be the safest approach but it works for this application
                } else if (message.getClass() == double.class) {
                    Program.lastDoubleRecived = (double) message; //saves the last recived message/input in a static variable, this might not be the safest approach but it works for this application

                } else if (message.getClass() == int.class) {

                    Program.nextIdentifier = (int) message;

                } else if (message.getClass() == ArrayList.class) { //Checks to see if the recived message is a arraylist
                    System.out.println("got an array! woo!");
                    ArrayList array = (ArrayList) message;
                    if (array.get(0).getClass() == (LocationData.class)) { //if the arraylist is an arraylist that contains LocationData
                        Program.lastLocationsRecived = array;

                    }
                } else if (message instanceof Sprite) { //Checks to see if it is a lone object that extends Sprite, if so it will be put into the local Drawables array
                    Program.lastSpriteRecived = (Sprite) message;
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
                sendByteMessage((byte) 0);//sends the ping
                pingSent = System.currentTimeMillis(); //saves the time it was sent
            }
        }
    }

    public void closeStreams() throws IOException { //yep, this turns of the streams, seems like it's a good thing to do

        output.close();
        input.close();
        socket.close();
        socket = null;
    }

    public void sendGetRequest(int identifier) { //Sends a request to retreive a Object with the inputed id
        try {
            output.writeObject(identifier);
            output.flush();
        } catch (IOException e) {
            System.out.println("Could not send that message");

        }
    }

    public void sendObjectMessage(Object message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("Could not send that message");

        }
    }

    public void sendByteMessage(Byte message) { //sends a byte message to remote, a shorte less demanding message

        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("Could not send that message");

        }

    }

    public void sendDoubleMessage(double message) { //sends a double message to remote 
        //the reson why it's a sepparate method from send byte is to not put such a heavy load on the network by sending shorter messages when possible

        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("Could not send that message");

        }

    }

    public void connectToServer() throws IOException {//tries to send a connection to another client

        System.out.println("Connecting ...");
        try {
            socket = new Socket(InetAddress.getByName("192.1.1.98"), 25565);
            socket.setTcpNoDelay(true);//makes sure the is no delay to the server. 
            System.out.println("Connected!!!! to: " + socket.getInetAddress().getHostName());
        } catch (java.net.UnknownHostException e) {
            System.out.println("conection failed");

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
