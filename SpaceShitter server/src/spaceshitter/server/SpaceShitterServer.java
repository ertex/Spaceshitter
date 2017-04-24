package spaceshitter.server;

//Author David Johansson Te2
import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JTextField;

//Imports go here!
public class SpaceShitterServer { //TODO fix so SpaceShitterServer does all of the processing and relays it to clients,fix ID system 

        public static byte lastByteRecived;
    public static double lastDoubleRecived;
    private Canvas canvas;
    private final int xSize = 400;
    private final int ySize = 400;
        private JTextField outputFeild;



    public static void main(String[] args) {
        System.out.println("SpaceShitterServer started");
SpaceShitterServer server = new SpaceShitterServer();
    }
    public SpaceShitterServer(){
    
    createAndShowGUI();
    output("asdasd");
    output("asdasd2rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        
    
    }



    public void createAndShowGUI() { //The GUI is meant for debug and managing the server, the game will not be able to play from here
        System.out.println("Creating GUI");
        JFrame frame = new JFrame("space shitter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(xSize, ySize);
        
        outputFeild = new JTextField();
        outputFeild.setVisible(true);
        outputFeild.setSize(xSize, ySize);
        outputFeild.setBackground(Color.red);
        outputFeild.setEditable(false);
        frame.add(outputFeild);
        
        frame.pack();
        System.out.println("GUI created");
    }
    
    public void output(String string){
        outputFeild.setText(string);
    }

}
