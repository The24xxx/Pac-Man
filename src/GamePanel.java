import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener {
    private Pacman pacman;
    private Map map;

    private Image pacmanImage;
    private Image wallImage;
    private Image pathImage;
    private Timer timer;

    public GamePanel(Map map) {
        this.map = map;

        // Načtení obrázků
        pacmanImage = new ImageIcon("I:\\OSU\\Programko\\Pac-Man\\sprites\\pacman.gif").getImage();
        wallImage = new ImageIcon("I:\\OSU\\Programko\\Pac-Man\\sprites\\wall.png").getImage();
        pathImage = new ImageIcon("I:\\OSU\\Programko\\Pac-Man\\sprites\\path.png").getImage();

        // Nastavení barvy pozadí
        this.setBackground(Color.BLACK);

        // Přidání KeyListeneru
        setFocusable(true);
        addKeyListener(this);

        // Vytvoření instance Pacmana
        pacman = new Pacman(map);

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
                    default:
                        img = pathImage;
                        break;
                }

                g.drawImage(img, x * tileSize, y * tileSize, this);
            }
        }

        // Draw Pacman
        pacman.draw(g, pacmanImage);
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