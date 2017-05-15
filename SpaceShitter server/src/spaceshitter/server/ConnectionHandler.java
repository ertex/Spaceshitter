package spaceshitter.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {
// ConnectionHandler and spaceshitter.server belongs to David Johansson Te2
//the entire purpose of this class is to be another thread that checks for new connections

    private ArrayList<Socket> connections = new ArrayList();
    private ArrayList<ObjectOutputStream> outputs = new ArrayList();
    private ArrayList<ObjectInputStream> inputs = new ArrayList();
    private ServerSocket serverSocket;
    private Thread t;

    public ConnectionHandler() {
        t = new Thread(this, "ConnectionHandler");

    }

    @Override
    public void run() {
    while(true){
        try {
            waitForConnect();
        } catch (IOException ex) {
          
        }
    }    
    }

    public void addConnection(Socket socket) throws IOException {//creates the streams 
        System.out.println("NEW CONNECTION!!!");
        connections.add(socket);
        outputs.add(new ObjectOutputStream(socket.getOutputStream()));
        inputs.add(new ObjectInputStream(socket.getInputStream()));
    }

 

    public int getNumberOfConnections() {
        return connections.size();
    }

    public ArrayList getOutputs() {
        return outputs;
    }

    public ArrayList getInputs() {
        return inputs;
    }

    public ArrayList getConnections() {
        return connections;
    }

}
