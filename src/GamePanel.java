import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {
    private Image pacmanImage;
    private Image wallImage;
    private Image pathImage;
    private Map map;
    private Pacman pacman; // Instance Pacmana
    private int directionX = 0; // Směr pohybu na ose X
    private int directionY = 0; // Směr pohybu na ose Y
    private Timer timer;
    private final int moveSpeed = 2; // Rychlost pohybu v pixelech

    public GamePanel(Map map) {
        this.map = map;

        // Načtení obrázků
        pacmanImage = new ImageIcon("sprites/pacman.gif").getImage();
        wallImage = new ImageIcon("sprites/wall.png").getImage();
        pathImage = new ImageIcon("sprites/path.png").getImage();

        // Nastavení barvy pozadí
        this.setBackground(Color.BLACK);

        // Přidání KeyListeneru
        setFocusable(true);
        addKeyListener(this);

        // Vytvoření instance Pacmana
        pacman = new Pacman(map);

        // Časovač pro opakovaný pohyb
        timer = new Timer(16, e -> movePacman()); // Aktualizace každých 16 ms (~60 FPS)
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

        // Vykreslení Pacmana
        g.drawImage(pacmanImage, pacman.getPixelX(), pacman.getPixelY(), this);
    }

    private void movePacman() {
        int tileSize = 32;
        int newPixelX = pacman.getPixelX() + directionX;
        int newPixelY = pacman.getPixelY() + directionY;
    
        // Ověření, zda je průchozí celá hitbox
        if (map.isWalkable(newPixelX, newPixelY)) {
            pacman.movePixel(directionX, directionY); // Pohyb Pacmana
        } else {
            // Pokus o sklouznutí kolem zdi, pokud je Pacman příliš blízko
            int tileCenterX = (pacman.getPixelX() / tileSize) * tileSize + tileSize / 2;
            int tileCenterY = (pacman.getPixelY() / tileSize) * tileSize + tileSize / 2;
    
            // Zarovnání na střed, pokud je blízko
            if (Math.abs(pacman.getPixelX() - tileCenterX) < 5) {
                pacman.setPixelX(tileCenterX);
            }
            if (Math.abs(pacman.getPixelY() - tileCenterY) < 5) {
                pacman.setPixelY(tileCenterY);
            }
        }
    
        repaint(); // Aktualizace vykreslení
    }
    

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Nastavení směru podle stisknuté klávesy
        switch (key) {
            case KeyEvent.VK_UP:
                directionX = 0;
                directionY = -moveSpeed; // Rychlost v pixelech
                break;
            case KeyEvent.VK_DOWN:
                directionX = 0;
                directionY = moveSpeed;
                break;
            case KeyEvent.VK_LEFT:
                directionX = -moveSpeed;
                directionY = 0;
                break;
            case KeyEvent.VK_RIGHT:
                directionX = moveSpeed;
                directionY = 0;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Zastav pohyb při uvolnění klávesy
        //directionX = 0;
        //directionY = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nepoužívá se
    }
}
