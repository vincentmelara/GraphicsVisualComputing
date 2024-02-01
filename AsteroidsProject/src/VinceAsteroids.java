// Mario Kart style 2D game By Vincent Melara
//import statements

import java.util.Vector ;
import java.util.Random;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class VinceAsteroids
{
    public VinceAsteroids()
    {
        setup();
    }

    public static void setup()
    {
        appFrame = new JFrame("Kartan");
        XOFFSET = 0;
        YOFFSET = 40;
        WINWIDTH = 1500;
        WINHEIGHT = 800;
        pi = 3.14159265358979;
        twoPi = 2.0 * 3.14159265358979;
        endgame = false;
        p1width = 25; // 18.5;
        p1height = 25; // 25;
        p1originalX = 70;
        p1originalY = 400;
        playerBullets = new Vector<ImageObject>();
        playerBulletsTimes = new Vector<Long>();
        bulletWidth = 5;
        playerbulletlifetime = 1600L;
        enemybulletlifetime = 1600L;
        explosionlifetime = 800L;
        playerbulletgap = 1;
        flamecount = 1;
        flamewidth = 2.0;
        expcount = 1;
        level = 3;
        wallVector = new Vector<ImageObject>();
        asteroidsTypes = new Vector<Integer>();
        ast1width = 32;
        ast2width = 21;
        ast3width = 26;
        try
        {
            background = ImageIO.read(new File("AsteroidsProject\\MarioCircuitFinal.png"));
            player = ImageIO.read(new File("AsteroidsProject\\\\tinyVeneno.png"));
            flame1 = ImageIO.read(new File("AsteroidsProject\\\\flameleft.png"));

            WallDisplayImage = ImageIO.read(new File("AsteroidsProject\\\\walls.png"));
            flame2 = ImageIO.read(new File("AsteroidsProject\\\\flamecenter.png"));
            flame3 = ImageIO.read(new File("AsteroidsProject\\\\flameright.png"));
            flame4 = ImageIO.read(new File("AsteroidsProject\\\\blueflameleft.png"));
            flame5 = ImageIO.read(new File("AsteroidsProject\\\\blueflamecenter.png"));
            flame6 = ImageIO.read(new File("AsteroidsProject\\\\blueflameright.png"));
            exp1 = ImageIO.read(new File("AsteroidsProject\\\\explosion1.png"));
            exp2 = ImageIO.read(new File("AsteroidsProject\\\\explosion2.png"));
        }
        catch (IOException ioe)
        {

        }
    }

    private static class Animate implements Runnable
    {
        public void run()
        {
            while (endgame == false)
            {
                backgroundDraw();
                //generateWalls();
                wallsDraw();
                playerDraw();

                try
                {
                    Thread.sleep(32);
                }
                catch (InterruptedException e)
                {

                }
            }
        }
    }


    private static class PlayerMover implements Runnable
    {
        public PlayerMover()
        {
            velocitystep = 0.03;
            rotatestep = 0.04;
            maxVelocity = 5.00;
        }

        public void run()
        {
            while (endgame == false)
            {
                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                }
                if (upPressed == true && p1velocity < maxVelocity)
                {
                    p1velocity = p1velocity + velocitystep;
                }
                if (downPressed == true)
                {
                    p1velocity = p1velocity - velocitystep;
                }
                if (leftPressed == true)
                {
                    if (p1velocity < 0)
                    {
                        p1.rotate(rotatestep);
                    } else
                    {
                        p1.rotate(-rotatestep);
                    }
                }
                if (rightPressed == true)
                {
                    if (p1velocity < 0)
                    {
                        p1.rotate(-rotatestep);
                    }
                    else
                    {
                        p1.rotate(rotatestep);
                    }
                }
                if (boostPressed == true && p1velocity < maxVelocity)
                {
                    p1velocity = p1velocity + (velocitystep*2);
                }
                if (boostPressed == false && brakePressed == false && upPressed == false && downPressed == false && leftPressed == false && rightPressed == false)
                {
                    if (p1velocity > 0) {
                        p1velocity = p1velocity - 0.03;
                    }
                }
                if (brakePressed == true) {
                    // Apply gradual deceleration when braking
                    p1velocity -= brakeDeceleration;
                    if (p1velocity < 0) {
                        p1velocity = 0;  // Ensure velocity doesn't go below zero
                    }
                }
                p1.move(p1velocity * Math.cos(p1.getAngle() - pi / 2.0), p1velocity * Math.sin(p1.getAngle() - pi / 2.0));
                p1.screenWrap(XOFFSET, XOFFSET + WINWIDTH, YOFFSET, YOFFSET + WINHEIGHT);
            }
        }
        private double velocitystep;
        private double rotatestep;
        private double maxVelocity;
        private double brakeDeceleration;  // Added variable for brake deceleration
    }



    private static class CollisionChecker implements Runnable
    {
        public void run()
        {
            Random randomNumbers = new Random(LocalTime.now().getNano());
            while (endgame == false)
            {
                // compare enemy ship to player
                    if (collisionOccurs(walls, p1)) {
                        endgame = true;
                        System.out.println("Game Over. You hit the wall!");
                    }
            }
        }
    }
    // private static class WinChecker implements Runnable
    // {
    //     public void run()
    //     {
    //         while (endgame == false)
    //         {
    //             if (asteroids.size() == 0)
    //             {
    //                 endgame = true;
    //                 System.out.println("Game Over You Win");
    //             }
    //         }
    //     }
    // }
    private static void generateAsteroids()
    {
        asteroids = new Vector<ImageObject>();
        asteroidsTypes = new Vector<Integer>();
        Random randomNumbers = new Random(LocalTime.now().getNano());
        for (int i = 0; i < level; i++)
        {
            asteroids.addElement(new ImageObject(XOFFSET + (double) (randomNumbers.nextInt(WINWIDTH)), YOFFSET + (double) (randomNumbers.nextInt(WINHEIGHT)), ast1width, ast1width, (double) (randomNumbers.nextInt(360))));
            asteroidsTypes.addElement(1);
        }
    }

    //switch this to generate the walls for the track
    private static void generateWalls() 
    {
        try
        {
            Random randomNumbers = new Random(LocalTime.now().getNano());

            walls = new ImageObject(100,100,0,0,0);

        } catch (java.lang.IllegalArgumentException jliae)
        {
        }
    }

    // TODO make one lock rotate function which takes as input objInner,
    // objOuter, and point relative to objInner’s x, y that objOuter must
    // rotate around.
    // dist is a distance between the two objects at the bottom of objInner.
    private static void lockrotateObjAroundObjbottom(ImageObject objOuter, ImageObject objInner, double dist) {
        objOuter.moveto(objInner.getX() + (dist + objInner.getWidth() / 2.0) * Math.cos(objInner.getAngle() + Math.PI / 2.0) + objOuter.getWidth() / 2.0, objInner.getY() + (dist + objInner.getHeight() / 2.0) * Math.sin(objInner.getAngle() + Math.PI / 2.0) + objOuter.getHeight() / 2.0);objOuter.setAngle(objInner.getAngle());
    }

    // dist is a distance between the two objects at the top of the inner
    // object.
    private static void lockrotateObjAroundObjtop(ImageObject objOuter, ImageObject objInner, double dist)
    {
        objOuter.moveto(
                objInner.getX() + objOuter.getWidth()
                        + (objInner.getWidth() / 2.0 + (dist + objInner.getWidth() / 2.0) * Math.cos(objInner.getAngle()
                        + Math.PI / 2.0)) / 2.0,
                objInner.getY() + objOuter.getHeight()
                        + (dist + objInner.getHeight() / 2.0) * Math.sin(objInner.getAngle() / 2.0));
        objOuter.setAngle(objInner.getAngle());
    }

    private static AffineTransformOp rotateImageObject(ImageObject obj) {
        AffineTransform at = AffineTransform.getRotateInstance(obj.getAngle(), obj.getWidth() / 2.0,
                obj.getHeight() / 2.0);
        AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        return atop;
    }

    private static AffineTransformOp spinImageObject(ImageObject obj) {
        AffineTransform at = AffineTransform.getRotateInstance(obj.getInternalAngle(), obj.getWidth() / 2.0,
                obj.getHeight() / 2.0);
        AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return atop;
    }

    private static void backgroundDraw() {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background, XOFFSET, YOFFSET, null);
    }


    private static void playerDraw()
    {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(rotateImageObject(p1).filter(player, null), (int) (p1.getX() + 0.5), (int) (p1.getY() + 0.5), null);
    }
    private static void wallsDraw() {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(WallDisplayImage, 193, 38, null);
    }

    private static class KeyPressed extends AbstractAction {
        public KeyPressed() {
            action = "";
        }

        public KeyPressed(String input) {
            action = input;
        }

        public void actionPerformed(ActionEvent e) {
            if (action.equals("UP")) {
                upPressed = true;
            }
            if (action.equals("DOWN")) {
                downPressed = true;
            }
            if (action.equals("LEFT")) {
                leftPressed = true;
            }
            if (action.equals("RIGHT")) {
                rightPressed = true;
            }
            if (action.equals("B")) {
                boostPressed = true;
            }
            if (action.equals("V")) {
                brakePressed = true;
            }
        }

        private String action;
    }

    private static class KeyReleased extends AbstractAction {
        public KeyReleased() {
            action = "";
        }

        public KeyReleased(String input) {
            action = input;
        }

        public void actionPerformed(ActionEvent e) {
            if (action.equals("UP")) {
                upPressed = false;
            }
            if (action.equals("DOWN")) {
                downPressed = false;
            }
            if (action.equals("LEFT")) {
                leftPressed = false;
            }
            if (action.equals("RIGHT")) {
                rightPressed = false;
            }
            if (action.equals("B")) {
                boostPressed = false;
            }
            if (action.equals("V")) {
                brakePressed = false;
            }
        }

        private String action;
    }
    private static class QuitGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            endgame = true;
        }
    }

    private static class StartGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            endgame = true;
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;
            boostPressed = false;
            brakePressed = false;

            //modify the starting position of player 1
            p1 = new ImageObject(p1originalX, p1originalY, p1width, p1height, 0.0);

            p1velocity = 0.0;
            generateWalls();

            //modify the starting position of the flames
            flames = new ImageObject(p1originalX, p1originalY, 25, 25, 0.0);
            flamecount = 1;
            expcount = 1;
            try {
                Thread.sleep(20);
            } catch (InterruptedException ie) {

            }
            playerBullets = new Vector<ImageObject>();
            playerBulletsTimes = new Vector<Long>();
            enemyBullets = new Vector<ImageObject>();
            enemyBulletsTimes = new Vector<Long>();
            explosions = new Vector<ImageObject>();
            explosionsTimes = new Vector<Long>();
            generateAsteroids();
            endgame = false;
            Thread t1 = new Thread(new Animate());
            Thread t2 = new Thread(new PlayerMover());
            Thread t8 = new Thread(new CollisionChecker());
            //Thread t9 = new Thread(new WinChecker());
            t1.start();
            t2.start();
            t8.start();
            //t9.start();
        }
    }
        private static class GameLevel implements ActionListener {
            public int decodeLevel(String input) {
                int ret = 3;
                if (input.equals("One")) {
                    ret = 1;
                } else if (input.equals("Two")) {
                    ret = 2;
                } else if (input.equals("Three")) {
                    ret = 3;
                }
                return ret;
            }

            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String textLevel = (String) cb.getSelectedItem();
                level = decodeLevel(textLevel);
            }
        }

        // private static Boolean isInside(double p1x, double p1y, double p2x1, double p2y1, double p2x2, double p2y2) {
        //     Boolean ret = false;
        //     if (p1x > p2x1 && p1x < p2x2) {
        //         if (p1y > p2y1 && p1y < p2y2) {
        //             ret = true;
        //         } else if (p1y > p2y2 && p1y < p2y1) {
        //             ret = true;
        //         }
        //     }
        //     if (p1x > p2x2 && p1x < p2x1) {
        //         if (p1y > p2y1 && p1y < p2y2) {
        //             ret = true;
        //         } else if (p1y > p2y2 && p1y < p2y1) {
        //             ret = true;
        //         }
        //     }
        //     return ret;
        // }

        private static Boolean collisionOccursCoordinates(double p1x1, double p1y1, double p1x2, double p1y2,
                                                          double p2x1, double p2y1, double p2x2, double p2y2) {
            Boolean ret = false;
            // if (isInside(p1x1, p1y1, p2x1, p2y1, p2x2, p2y2)) {
            //     ret = true;
            // }
            // if (isInside(p1x1, p1y2, p2x1, p2y1, p2x2, p2y2)) {
            //     ret = true;
            // }
            // if (isInside(p1x2, p1y1, p2x1, p2y1, p2x2, p2y2)) {
            //     ret = true;
            // }
            // if (isInside(p1x2, p1y2, p2x1, p2y1, p2x2, p2y2)) {
            //     ret = true;
            // }
            // if (isInside(p2x1, p2y1, p1x1, p1y1, p1x2, p1y2)) {
            //     ret = true;
            // }
            // if (isInside(p2x1, p2y2, p1x1, p1y1, p1x2, p1y2)) {
            //     ret = true;
            // }
            // if (isInside(p2x2, p2y1, p1x1, p1y1, p1x2, p1y2)) {
            //     ret = true;
            // }
            // if (isInside(p2x2, p2y2, p1x1, p1y1, p1x2, p1y2)) {
            //     ret = true;
            // }
            return ret;
        }

        private static Boolean collisionOccurs(ImageObject obj1, ImageObject obj2) {
            Boolean ret = false;
            if (collisionOccursCoordinates(
                    obj1.getX(), obj1.getY(),
                    obj1.getX() + obj1.getWidth(), obj1.getY() + obj1.getHeight(),
                    obj2.getX(), obj2.getY(),
                    obj2.getX() + obj2.getWidth(), obj2.getY() + obj2.getHeight()
            )) {
                ret = true;
            }
            return ret;
        }


    private static class ImageObject {
        public ImageObject() {
        }

        public ImageObject(double xinput, double yinput, double xwidthinput, double yheightinput, double angleinput) {
            x = xinput;
            y = yinput;
            xwidth = xwidthinput;
            yheight = yheightinput;
            angle = angleinput;
            internalangle = 0.0;
            coords = new Vector<Double>();
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getWidth() {
            return xwidth;
        }

        public double getHeight() {
            return yheight;
        }

        public double getAngle() {
            return angle;
        }

        public double getInternalAngle() {
            return internalangle;
        }

        public void setAngle(double angleinput) {
            angle = angleinput;
        }

        public void setInternalAngle(double internalangleinput) {
            internalangle = internalangleinput;
        }

        public Vector<Double> getCoords() {
            return coords;
        }

        public void setCoords(Vector<Double> coordsinput) {
            coords = coordsinput;
            generateTriangles();
            // printTriangles();
        }

        public void generateTriangles() {
            triangles = new Vector<Double>();
            // format: (0, 1), (2, 3), (4, 5) is the (x, y) coords of a triangle.
            // get center point of all coordinates.
            comX = getComX();
            comY = getComY();

            for (int i = 0; i < coords.size(); i = i + 2) {
                triangles.addElement(coords.elementAt(i));
                triangles.addElement(coords.elementAt(i + 1));
                triangles.addElement(coords.elementAt((i + 2) % coords.size()));
                triangles.addElement(coords.elementAt((i + 3) % coords.size()));
                triangles.addElement(comX);
                triangles.addElement(comY);
            }
        }

        public void printTriangles() {
            for (int i = 0; i < triangles.size(); i = i + 6) {
                System.out.print("p0x: " + triangles.elementAt(i) + ", p0y: " + triangles.elementAt(i + 1));
                System.out.print("p1x: " + triangles.elementAt(i + 2) + ", p1y: " + triangles.elementAt(i + 3));
                System.out.println("p2x: " + triangles.elementAt(i + 4) + ", p2y: " + triangles.elementAt(i + 5));
            }
        }

        public double getComX() {
            double ret = 0;
            if (coords.size() > 0) {
                for (int i = 0; i < coords.size(); i = i + 2) {
                    ret = ret + coords.elementAt(i);
                }
                ret = ret / (coords.size() / 2.0);
            }
            return ret;
        }

        public double getComY() {
            double ret = 0;
            if (coords.size() > 0) {
                for (int i = 1; i < coords.size(); i = i + 2) {
                    ret = ret + coords.elementAt(i);
                }
                ret = ret / (coords.size() / 2.0);
            }
            return ret;
        }

        public void move(double xinput, double yinput) {
            x = x + xinput;
            y = y + yinput;
        }

        public void moveto(double xinput, double yinput) {
            x = xinput;
            y = yinput;
        }

        public void screenWrap(double leftEdge, double rightEdge, double topEdge, double bottomEdge) {
            if (x > rightEdge) {
                moveto(leftEdge, getY());
            }
            if (x < leftEdge) {
                moveto(rightEdge, getY());
            }

            //no bottom to top wrapping
            if (y > bottomEdge) {
                moveto(getX(), topEdge);
            }
            if (y < topEdge) {
                moveto(getX(), bottomEdge);
            }
        }

        public void rotate(double angleinput) {
            angle = angle + angleinput;
            while (angle > twoPi) {
                angle = angle % twoPi;
            }
            while (angle < 0) {
                angle = angle + twoPi;
            }
        }

        public void spin(double internalangleinput) {
            internalangle = internalangle + internalangleinput;
            while (internalangle > twoPi) {
                internalangle = internalangle % twoPi;
            }
            while (internalangle < 0) {
                internalangle = internalangle + twoPi;
            }
        }

        private double x;
        private double y;
        private double xwidth;
        private double yheight;
        private double angle; // in Radians
        private double internalangle; // in Radians
        private Vector<Double> coords;
        private Vector<Double> triangles;
        private double comX;
        private double comY;
    }

    private static void bindKey(JPanel myPanel, String input) {
        myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("pressed " + input), input + " pressed");
        myPanel.getActionMap().put(input + " pressed", new KeyPressed(input));
        myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + input), input + " released");
        myPanel.getActionMap().put(input + " released", new KeyReleased(input));
    }

    public static void main(String[] args) {
        setup();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(1500, 800);

        JPanel myPanel = new JPanel();

        String[] levels = {"One", "Two", "Three"};
        JComboBox<String> levelMenu = new JComboBox<String>(levels);
        levelMenu.setSelectedIndex(2);
        levelMenu.addActionListener(new GameLevel());
        myPanel.add(levelMenu);
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new StartGame());
        myPanel.add(newGameButton);
        JButton quitButton = new JButton("Quit Game");
        quitButton.addActionListener(new QuitGame());
        myPanel.add(quitButton);
        bindKey(myPanel, "UP");
        bindKey(myPanel, "DOWN");
        bindKey(myPanel, "LEFT");
        bindKey(myPanel, "RIGHT");
        bindKey(myPanel, "B");
        bindKey(myPanel, "V");


        appFrame.getContentPane().add(myPanel, "South");
        appFrame.setVisible(true);
    }

    private static Boolean endgame;
    private static Boolean enemyAlive;

    private static BufferedImage background;

    private static BufferedImage WallDisplayImage;

    private static BufferedImage player;
    private static Boolean upPressed;
    private static Boolean downPressed;
    private static Boolean leftPressed;
    private static Boolean rightPressed;
    private static Boolean boostPressed;
    private static Boolean brakePressed;
    private static ImageObject p1;
    private static double p1width;
    private static double p1height;
    private static double p1originalX;
    private static double p1originalY;
    private static double p1velocity;
    // private static ImageObject enemy;
    // private static BufferedImage enemyShip;

    //replace with
    private static ImageObject walls;
    private static BufferedImage wallPicture;
    private static BufferedImage enemyBullet;
    private static Vector<ImageObject> enemyBullets;
    private static Vector<Long> enemyBulletsTimes;
    private static Long enemybulletlifetime;
    private static Vector<ImageObject> playerBullets;
    private static Vector<Long> playerBulletsTimes;
    private static double bulletWidth;
    private static BufferedImage playerBullet;
    private static Long playerbulletlifetime;
    private static double playerbulletgap;
    private static ImageObject flames;
    private static BufferedImage flame1;
    private static BufferedImage flame2;
    private static BufferedImage flame3;
    private static BufferedImage flame4;
    private static BufferedImage flame5;
    private static BufferedImage flame6;
    private static int flamecount;
    private static double flamewidth;
    private static int level;
    private static Vector<ImageObject> asteroids;

    private static Vector<ImageObject> wallVector;

    private static Vector<Integer> asteroidsTypes;
    private static BufferedImage ast1;
    private static BufferedImage ast2;
    private static BufferedImage ast3;
    private static double ast1width;
    private static double ast2width;
    private static double ast3width;
    private static Vector<ImageObject> explosions;
    private static Vector<Long> explosionsTimes;
    private static Long explosionlifetime;
    private static BufferedImage exp1;
    private static BufferedImage exp2;
    private static int expcount;
    private static int XOFFSET;
    private static int YOFFSET;
    private static int WINWIDTH;
    private static int WINHEIGHT;
    private static double pi;
    private static double twoPi;
    private static JFrame appFrame;
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
}