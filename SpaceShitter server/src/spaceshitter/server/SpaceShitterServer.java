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
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

//Imports go here!
public class SpaceShitterServer { //TODO fix so SpaceShitterServer does all of the processing and relays it to clients,fix ID system 

    public static byte lastByteRecived;
    public static double lastDoubleRecived;
    public static Sprite lastSpriteRecived;
    private Canvas canvas;
    private final int xSize = 400;
    private final int ySize = 400;
    private ArrayList<JTextField> outputList = new ArrayList();
    private ArrayList<Sprite> drawables;
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
        drawables = new ArrayList();
        drawables.add(new Sprite(1, (double) 345, (double) 234, 54, 54, null));
        createAndShowGUI();
        output("asdasd");
        output("asdasd2rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        run();
    }

    public void run() {
        System.out.println("entered run()");
        while (true) {

            //This is the main loop for checking if something is hit by a projectile
            if (drawables.size() > 0) { //Prevents it from calling null items
                for (Sprite o : drawables) {
                    System.out.println("o7");

                    System.out.println("Hey, it didn't crash");
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
            //Updates the entities
            for (Sprite o : drawables) {
                if (o.getClass() == Entity.class) {
                    ((Entity) o).update();
                }
                if (o.getClass() == Projectile.class) {
                    ((Projectile) o).update();
                }
            }
            
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
