import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private Image pacmanImage;
    private Image wallImage;
    private Image pathImage;
    private Map map;

    public GamePanel(Map map) {
        this.map = map;

        // Načtení obrázků
        pacmanImage = new ImageIcon("sprites/pacman1.png").getImage();
        wallImage = new ImageIcon("sprites/wall.png").getImage();
        pathImage = new ImageIcon("sprites/path.png").getImage();

        // nastavení velikosti pozadí
        this.setBackground(Color.BLACK);
    }

    // stará se o správné vykreslení pozadí a překreslení obrázků
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Ukázka vykreslení obrázků
       // g.drawImage(pacmanImage, 50, 50, this); // Pacman na pozici 50x50
       // g.drawImage(wallImage, 100, 50, this); // Zeď na pozici 100x50
        
       //Získání gridu z mapy
        char[][] grid = map.getGrid();
        int tileSize = 32; //velikost políčka v px
        
        
        //projdi všechny sloupce a řádky gridu
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                //zjisti, jaký znak je na dané pozici
                char tile = grid[y][x];
                Image img;
                
                //podle znaku vykresli obrázek
                if (tile == '#') {
                    img = wallImage;
                } else if (tile == 'P') {
                    img = pacmanImage;
                } else {
                    img = pathImage;
                }

                //vykresli obrázek na správné pozici
                g.drawImage(img, x * tileSize, y * tileSize, this);
            }
        }
    }
}


