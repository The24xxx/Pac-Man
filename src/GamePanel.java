import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private Image pacmanImage;
    private Image wallImage;

    public GamePanel() {
        // Načtení obrázků
        pacmanImage = new ImageIcon("sprites/pacman1.png").getImage();
        wallImage = new ImageIcon("sprites/wall.png").getImage();

        // nastavení velikosti pozadí
        this.setBackground(Color.BLACK);
    }

    // stará se o správné vykreslení pozadí a překreslení obrázků
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Ukázka vykreslení obrázků
        g.drawImage(pacmanImage, 50, 50, this); // Pacman na pozici 50x50
        g.drawImage(wallImage, 100, 50, this); // Zeď na pozici 100x50
    }
}


