import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener {
    private static final int TILE_SIZE = 32; // Define the size of each tile
    private Pacman pacman;
    private Map map;
    private Image pacmanImage;
    private Image wallImage;
    private Image pathImage;
    private Image pointImage;
    private Timer gameTimer;
    private Image ghostBlueImage;
    private BufferedImage offscreenImage;
    private Graphics2D offscreenGraphics;
    private Points pointCounter;
    private long startTime;
    private long elapsedTime;
    private boolean timerStarted = false;
    private List<Ghosts> ghosts;
    private boolean showTextBox = true;  // Flag to show/hide the text box

    public GamePanel(Map map, Points pointCounter) {
        this.map = map;
        this.pointCounter = pointCounter;

        // Načtení obrázků
        pacmanImage = new ImageIcon("sprites\\pacman.gif").getImage();
        wallImage = new ImageIcon("sprites\\wall.png").getImage();
        pathImage = new ImageIcon("sprites\\path.png").getImage();
        pointImage = new ImageIcon("sprites\\point.png").getImage();
        ghostBlueImage = new ImageIcon("sprites\\ghost_blue.gif").getImage();

        // Nastavení barvy pozadí
        this.setBackground(Color.BLACK);

        // Přidání KeyListeneru
        setFocusable(true);
        addKeyListener(this);

        // Vytvoření instance Pacmana
        pacman = new Pacman(map, pointCounter);

        //create ghost instance
        ghosts = new ArrayList<>();
        for (int i = 0; i < 4; i++) { //add 4 ghosts
            ghosts.add(new Ghosts(map, ghostBlueImage));
        }

        // strat timer
        startTime = System.currentTimeMillis();
        
        // Časovač pro opakovaný pohyb
        gameTimer = new Timer(16, e -> {
            pacman.move(map);
            for (Ghosts ghost : ghosts) {
                ghost.move();
            }
            elapsedTime = System.currentTimeMillis() - startTime;
            repaint();
        }); // Aktualizace každých 16 ms (~60 FPS)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Initialize offscreen image and graphics if not already done
        if (offscreenImage == null) {
            offscreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            offscreenGraphics = offscreenImage.createGraphics();
        }

        // Clear the offscreen image
        offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());

        // Získání gridu z mapy
        char[][] grid = map.getGrid();
        int tileSize = 32; // Velikost políčka v pixelech

        // Projdi všechny sloupce a řádky gridu
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char tile = grid[y][x];
                Image img;

                switch (tile) {
                    case '#':
                        img = wallImage;
                        break;
                    case ' ':
                        img = pointImage;
                        break;
                    default:
                        img = pathImage;
                        break;
                }

                offscreenGraphics.drawImage(img, x * tileSize, y * tileSize, this);
            }
        }

        // Draw Pacman
        pacman.draw(offscreenGraphics, pacmanImage);

        //draw ghosts
        for (Ghosts ghost : ghosts) {
            ghost.draw(offscreenGraphics);
        }

        // draw points
        offscreenGraphics.setColor(Color.WHITE);
        offscreenGraphics.drawString("Points: " + pointCounter.getPoints(), 10, 20);

        //draw timer
        offscreenGraphics.drawString("Time: " + elapsedTime / 1000.0 + "s", 400, 20);


        // check if the dots were eaten
        if (map.countEmptySpaces() == 0) {
            offscreenGraphics.setColor(Color.WHITE);
            offscreenGraphics.drawString("You won!", 200, 20);
            gameTimer.stop();
        }

        if (showTextBox) {
            // Draw the text box in the middle of the game map
            int boxWidth = 300;
            int boxHeight = 100;
            int x = (getWidth() - boxWidth) / 2;
            int y = (getHeight() - boxHeight) / 2;

            offscreenGraphics.setColor(Color.BLACK);
            offscreenGraphics.fillRect(x, y, boxWidth, boxHeight);

            offscreenGraphics.setColor(Color.WHITE);
            offscreenGraphics.drawRect(x, y, boxWidth, boxHeight);

            offscreenGraphics.setFont(offscreenGraphics.getFont().deriveFont(24f)); // Set font size to 24
            offscreenGraphics.drawString("Pac-Man", x + 100, y + 50);
    
            // Reset font size back to 12f for other text
            offscreenGraphics.setFont(offscreenGraphics.getFont().deriveFont(12f));
            offscreenGraphics.drawString("from Temu", x + 116, y + 70);
            offscreenGraphics.drawString("Press arrow keys to start", x + 80, y + 90);
        }

        // Draw the offscreen image to the screen
        g.drawImage(offscreenImage, 0, 0, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Hide the text box and start the timer when an arrow key is pressed
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
            e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (!timerStarted) {
                startTime = System.currentTimeMillis();
                gameTimer.start();
                timerStarted = true;
            }
            showTextBox = false;
            repaint();
        }

        pacman.handleKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pacman.handleKeyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nepoužívá se
    }

    public void updateGame() {
        // Game update logic
        pacman.move(map);
        for (Ghosts ghost : ghosts) {
            ghost.move();
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        repaint(); // Force repaint every tick
    }
}