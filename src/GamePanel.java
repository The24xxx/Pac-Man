import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {
    private Image pacmanImage;
    private Image wallImage;
    private Image pathImage;
    private Map map;
    private Pacman pacman; // Třída Pacman místo souřadnic
    private int directionX = 0; // Směr pohybu na ose X
    private int directionY = 0; // Směr pohybu na ose Y
    private Timer timer;

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

        // Vytvoření instanci Pacmana s mapou
        pacman = new Pacman(map);

        // Časovač pro opakovaný pohyb
        timer = new Timer(100, e -> movePacman());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Získání gridu z mapy
        char[][] grid = map.getGrid();
        int tileSize = 32; // Velikost políčka v px

        // Projdi všechny sloupce a řádky gridu
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char tile = grid[y][x];
                Image img;

                switch (tile) { //rychlejší než if/else
                    case '#':
                        img = wallImage;
                        break;
                    case 'P':
                        img = pacmanImage;
                        break;
                    default:
                        img = pathImage;
                        break;
                }

                g.drawImage(img, x * tileSize, y * tileSize, this);
            }
        }
    }

    private void movePacman() {
        // Používáme třídu Pacman pro pohyb
        pacman.move(directionX, directionY);
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Nastavení směru podle stisknuté klávesy
        switch (key) {
            case KeyEvent.VK_UP:
                directionX = 0;
                directionY = -1;
                break;
            case KeyEvent.VK_DOWN:
                directionX = 0;
                directionY = 1;
                break;
            case KeyEvent.VK_LEFT:
                directionX = -1;
                directionY = 0;
                break;
            case KeyEvent.VK_RIGHT:
                directionX = 1;
                directionY = 0;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        directionX = 0;
        directionY = 0; // Zastav pohyb při uvolnění klávesy
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nepoužívá se
    }
}


