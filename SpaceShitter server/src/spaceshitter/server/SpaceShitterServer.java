package spaceshitter.server;

//Author David Johansson Te2
import java.awt.Canvas;
import java.awt.Color;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

//Imports go here!
public class SpaceShitterServer { //TODO fix so SpaceShitterServer does all of the processing and relays it to clients,fix ID system 

    public static byte lastByteRecived;
    public static double lastDoubleRecived;
    private Canvas canvas;
    private final int xSize = 400;
    private final int ySize = 400;
    private ArrayList<JTextField> outputList = new ArrayList();
    JFrame frame;
    JPanel outputPanel;
    private ActionHandler actionHandler = new ActionHandler(); //creates the actionhandler to manage all the clicks and such
    NetworkHandler networkHandler;

    public static void main(String[] args) {

        System.out.println("SpaceShitterServer started");
        SpaceShitterServer server = new SpaceShitterServer();

    }

    public SpaceShitterServer() {
        networkHandler = new NetworkHandler(actionHandler);
        createAndShowGUI();
        output("asdasd");
        output("asdasd2rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");

    }
    
    public void run(){
    while(true){
    
        
        
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

        frame.add(networkHandler.getNetworkPanel());
        frame.add(outputPanel);

        System.out.println("GUI created");
    }
    
    public void output(String string) {
        outputList.add(new JTextField(string));
        outputList.get(outputList.size() - 1).setVisible(true);
        outputList.get(outputList.size() - 1).setSize(xSize, 10);
        outputPanel.add(outputList.get(outputList.size() - 1));

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
