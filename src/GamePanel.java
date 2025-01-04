import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GamePanel extends JPanel implements KeyListener {
    private Pacman pacman;
    private Map map;
    private Image pacmanImage;
    private Image wallImage;
    private Image pathImage;
    private Image pointImage;
    private Timer timer;
    private BufferedImage offscreenImage;
    private Graphics2D offscreenGraphics;


    public GamePanel(Map map, Points pointCounter) {
        this.map = map;

        // Načtení obrázků
        pacmanImage = new ImageIcon("I:\\OSU\\Programko\\Pac-Man\\sprites\\pacman.gif").getImage();
        wallImage = new ImageIcon("I:\\OSU\\Programko\\Pac-Man\\sprites\\wall.png").getImage();
        pathImage = new ImageIcon("I:\\OSU\\Programko\\Pac-Man\\sprites\\path.png").getImage();
        pointImage = new ImageIcon("I:\\OSU\\Programko\\Pac-Man\\sprites\\point.png").getImage();

        // Nastavení barvy pozadí
        this.setBackground(Color.BLACK);

        // Přidání KeyListeneru
        setFocusable(true);
        addKeyListener(this);

        // Vytvoření instance Pacmana
        pacman = new Pacman(map, pointCounter);

        // Časovač pro opakovaný pohyb
        timer = new Timer(16, e -> {
            pacman.move(map);
            repaint();
        }); // Aktualizace každých 16 ms (~60 FPS)
        timer.start();
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

        // Draw the offscreen image to the screen
        g.drawImage(offscreenImage, 0, 0, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
}