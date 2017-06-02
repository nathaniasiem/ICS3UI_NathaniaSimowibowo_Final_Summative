
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author simon7323
 */
public class SleepingPandaAdventure extends JComponent {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    
    //Title of the window
    String title = "Sleepy Panda Adventure";

    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;


    // YOUR GAME VARIABLES WOULD GO HERE
    Color pipeColor = new Color (120,156,235);
    Color skyColor = new Color (0,64,184);
    //load image into game
    BufferedImage background = loadImage("bg.png");
    BufferedImage tTube = loadImage("toptube.png");
    BufferedImage bTube = loadImage("bottomtube.png");
    BufferedImage pandaPic = loadImage("panda.png");
    Rectangle panda = new Rectangle(100, 200, 50, 50);
    //set the gravity
    int gravity = 1;
    //set how high the character jumps
    int dy = 0;
    //how hard the character jumps
    int jumpVelocity = -12;
    //jump key variable
    boolean jump = false;
    boolean lastjump = false;
    //wait to start
    boolean start = false;
    boolean dead = false;
    
    //sets array of pipes
    //makes it easier to set up moving pipes later on
    Rectangle[] topPipe = new Rectangle[5];
    Rectangle[] bottomPipe = new Rectangle[5];
    //new variable when character passes a pipe
    boolean[] passedPipe = new boolean[5];
    
    //sets up the score counter display
    int score = 0;
    Font scoreFont = new Font("Arial", Font.BOLD, 42);
    
    // the gap between top and bottom
    int pipeGap = 200;
    // distance between the pipes
    int pipeSpacing = 200;
    // the width of a single pipe
    int pipeWidth = 100;
    // the height of a pipe
    int pipeHeight = HEIGHT - 50;
    // minimum distance from edge
    int minDistance = 200;

    // speed of the game
    int speed = 3;
    // GAME VARIABLES END HERE   

    
    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public SleepingPandaAdventure(){
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        
        // add listeners for keyboard and mouse
        frame.addKeyListener(new SleepingPandaAdventure.Keyboard());
        SleepingPandaAdventure.Mouse m = new SleepingPandaAdventure.Mouse();
        
        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);
    }
    
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE
        
        
        //draw background
        g.setColor(skyColor);
        g.drawImage(background,0,0,WIDTH,HEIGHT,null);
        
        //draw pipes
        g.setColor(pipeColor);
        //for loop to draw out pipes according to 
        for (int i = 0; i < topPipe.length; i++) {
            //draws image for top pipe;
            g.drawImage(tTube, topPipe[i].x, topPipe[i].y, topPipe[i].width, topPipe[i].height, null);
            //draws image for bottom pipe
            g.drawImage(bTube, bottomPipe[i].x, bottomPipe[i].y, bottomPipe[i].width, bottomPipe[i].height, null);
        }
        //draw panda
        g.setColor(Color.WHITE);
        g.drawImage(pandaPic, panda.x, panda.y, panda.width, panda.height, null);
        
        
        //draw score counter
        g.setColor(Color.WHITE);
        g.setFont(scoreFont);
        g.drawString("" + score, WIDTH/2, 50);
        
        // GAME DRAWING ENDS HERE
    }

 public void reset() {
        // set up the pipes
        score = 0;
        
        int pipeX = 600;
        Random randGen = new Random();
        for (int i = 0; i < topPipe.length; i++) {
            // generating a random y position
            int pipeY = randGen.nextInt(HEIGHT - 2 * minDistance) + minDistance;
            bottomPipe[i] = new Rectangle(pipeX, pipeY, pipeWidth, pipeHeight);
            topPipe[i] = new Rectangle(pipeX, pipeY - pipeGap - pipeHeight, pipeWidth, pipeHeight);
            // move the pipeX value over
            pipeX = pipeX + pipeWidth + pipeSpacing;
            passedPipe[i] = false;
        }

        // reset the panda
        panda.y = 200;
        dy = 0;
        start = false;
        dead = false;
    }

    public void setPipe(int pipePosition) {
        // a random number generator
        Random randGen = new Random();
        // generate a random Y position
        int pipeY = randGen.nextInt(HEIGHT - 2 * minDistance) + minDistance;
        // generate the new pipe X coordinate
        int pipeX = topPipe[pipePosition].x;
        pipeX = pipeX + (pipeWidth + pipeSpacing) * topPipe.length;

        bottomPipe[pipePosition].setBounds(pipeX, pipeY, pipeWidth, pipeHeight);
        topPipe[pipePosition].setBounds(pipeX, pipeY - pipeGap - pipeHeight, pipeWidth, pipeHeight);
        
        passedPipe[pipePosition] = false;
    }

    
    
    
    
    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void  preSetup(){
       // Any of your pre setup before the loop starts should go here

       // set up the pipes
        int pipeX = 600;
        Random randGen = new Random();
        for (int i = 0; i < topPipe.length; i++) {
            // generating a random y position
            int pipeY = randGen.nextInt(HEIGHT - 2 * minDistance) + minDistance;
            bottomPipe[i] = new Rectangle(pipeX, pipeY, pipeWidth, pipeHeight);
            topPipe[i] = new Rectangle(pipeX, pipeY - pipeGap - pipeHeight, pipeWidth, pipeHeight);
            // move the pipeX value over
            pipeX = pipeX + pipeWidth + pipeSpacing;
            passedPipe[i] = false;
        }

    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        preSetup();

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            if (start) {
                // get the pipes moving
                if (!dead) {
                    for (int i = 0; i < topPipe.length; i++) {
                        topPipe[i].x = topPipe[i].x - speed;
                        bottomPipe[i].x = bottomPipe[i].x - speed;
                        // check if a pipe is off the screen
                        if (topPipe[i].x + pipeWidth < 0) {
                            // move the pipe
                            setPipe(i);
                        }
                    }
                }
                
                // see if we passed a pipe
                for(int i = 0; i < topPipe.length; i++){
                    if(!passedPipe[i] && panda.x > topPipe[i].x + pipeWidth){
                        score++;
                        passedPipe[i] = true;
                    }
                }

                // get the bird to fall
                // apply gravity
                dy = dy + gravity;
                // make the bird fly
                if (jump && !lastjump && !dead) {
                    dy = jumpVelocity;
                }
                lastjump = jump;

                // apply the change in y to the bird
                panda.y = panda.y + dy;

                // check if bird hits top or bottom of screen
                if (panda.y < 0) {
                    panda.y = 0;
                    dead = true;
                } else if (panda.y + panda.height > HEIGHT) {
                    dead = true;
                    panda.y = HEIGHT - panda.height;
                    reset();
                }

                // did the bird hit a pipe?
                // go through all the pipes
                for (int i = 0; i < topPipe.length; i++) {
                    // did the bird hit one of the top pipes
                    if (panda.intersects(topPipe[i])) {
                        dead = true;
                        // did the bird hit a bottom pipe
                    } else if (panda.intersects(bottomPipe[i])) {
                        dead = true;
                    }
                }
            }
            
            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                if (deltaTime > desiredTime) {
                    //took too much time, don't wait
                    Thread.sleep(1);
                } else {
                    // sleep to make up the extra time
                    Thread.sleep(desiredTime - deltaTime);
                }
            } catch (Exception e) {
            };
        }
    }

    

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {
        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e){
            jump = true;
            start = true;
        }
        
        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e){
            jump = false;
        }
        
        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e){
            
        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e){
            
        }
    }
    
    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter{
        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e){
            
        }
        
        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e){
            
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        SleepingPandaAdventure game = new SleepingPandaAdventure();
                
        // starts the game loop
        game.run();
    }
    
   
     // A method used to load in an image
    // The filname is used to pass in the EXACT full name of the image from the src folder
    // i.e.  images/picture.png
    public BufferedImage loadImage(String filename) {
        
        BufferedImage img = null;

        try {
            // use ImageIO to load in an Image
            // ClassLoader is used to go into a folder in the directory and grab the file
            img = ImageIO.read(ClassLoader.getSystemResourceAsStream(filename));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return img;
    }
}
