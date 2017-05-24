package spaceshitter.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    private Thread t;
    private boolean running;
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList networks;

    public ConnectionHandler(ArrayList array) {
        running = true;
        this.networks = array;
        
        port = 33678; //the port that will be used to connect to the server, the port is increeseing for each new player so every pleyer gets his own socket
        try {
            serverSocket = new ServerSocket(port,100);
        } catch (IOException ex) {
            
        }
        t = new Thread(this, "ConnectionHandler");
        t.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                waitForConnect();
            } catch (IOException ex) {

            }

        }
    }

    public void waitForConnect() throws IOException { //tries to establish a connection every 1 second with a incoming connection
        //System.out.println("Waiting for sombody to connect...");
        serverSocket.setSoTimeout(1000);
        
        networks.add(new NetworkHandler(serverSocket.accept(),networks.size()));
        System.out.println("connected.");
    }
// ConnectionHandler and spaceshitter.server belongs to David Johansson Te2

}
