package space.shitter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Program extends JFrame implements KeyListener, Runnable {

    public static byte lastByteRecived;
    public static double lastDoubleRecived;
    public static ArrayList<LocationData> lastLocationsRecived;
    public static int nextIdentifier;
    public static Sprite lastSpriteRecived; 
    Canvas canvas;
    private BufferStrategy bs;
    Graphics g;
    private int xSize, ySize;
    private SpaceShip LocalPlayer;
    private ArrayList<Sprite> drawables;
    private Thread t;
    private boolean foobar = false;
    private ActionHandler actionHandler = new ActionHandler(); //creates the actionhandler to manage all the clicks and such
    private NetworkHandler networkHandler = new NetworkHandler(actionHandler); //creates the Networkhandler to be able to send messages to remote

    public Program() { //Doubles as a Init() since it's called from the main class on startup

        t = new Thread(this, "Main");
        xSize = 600;
        ySize = 600;
        createAndShowGUI();
        drawables = new ArrayList();
        //FIX------- Identification nimber for sapceship
        LocalPlayer = new SpaceShip(1, 20, 20, 50, 50, 20, null, true); //Creates the Locxal player, this will be controlled by keystorkes from the local machine
        drawables.add(LocalPlayer);//adds LocalPlayer in the drawable arraylist, so it will be drawn

        paintComponents();

        t.start();
    }

    public void createAndShowGUI() {
        System.out.println("Creating GUI");
        JFrame frame = new JFrame("space shitter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(xSize, ySize);

        canvas = new Canvas();
        canvas.setSize(xSize, ySize);
        canvas.setVisible(true);
        canvas.setBackground(Color.red);

        frame.add(canvas);
        canvas.addKeyListener(this); //Adds the keylistner to the canvas, without code keylistner won't work
        canvas.createBufferStrategy(2);//creates doublebuffering in canvas object
        bs = canvas.getBufferStrategy();

        System.out.println("GUI created");
    }

    public void paintComponents() {
        g = (Graphics2D) bs.getDrawGraphics();
        g.clearRect(0, 0, xSize, ySize); //clears the canvas
        if (foobar) {
            g.fillRect(20, 20, 50, 50);
        }
        for (Sprite o : drawables) {//draws all the drawable things
            o.draw(g);
        }
        if (!bs.contentsLost()) {
            bs.show();
        }
    }

    public void updateLocations(ArrayList inNewLocations, ArrayList inOldLocations) {//Updates the location of sprites, FIND BETTER NAMES FOR THESE

        ArrayList<LocationData> newLocations = inNewLocations;
        ArrayList<Sprite> oldLocations = inOldLocations;

        for (LocationData o : newLocations) {
            int ident = o.getIdentifier();

            for (Sprite u : oldLocations) {
                if (u.getIdentification() == ident) { //Checks to see if the identifier of the location data is the same as the identifier of the sprite
                    u.setLocation(o); //updates the location of the sprites
                    oldLocations.remove(o); //removes a object from the locations so it wont be itterated though again.
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_Q) {
            LocalPlayer.shoot();
            drawables.addAll(LocalPlayer.fetchProjectileBuffer());//Is this really the best way to do this? is there a better way?
            LocalPlayer.clearProjectileBuffer();

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void run() {
        while (true) {
            if (networkHandler.connected()) {
                updateLocations(lastLocationsRecived, drawables); //updates the locations of all the sprites in drawables
            }
            paintComponents();
            //  System.out.println("running");
            try {

                Thread.sleep(30);

            } catch (InterruptedException io) {
            }

        }
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
