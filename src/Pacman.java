import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class Pacman {
    private int pixelX;
    private int pixelY;
    private int directionX;
    private int directionY;
    private int nextDirectionX;
    private int nextDirectionY;
    private Map map;
    private Points pointCounter;
    private final int moveSpeed = 4; // Rychlost pohybu v pixelech
    private double rotationAngle = 0; // Úhel rotace Pacmana

    public Pacman() {
    }

    public Pacman(Map map) {
        this(map, new Points());
    }

    public Pacman(Map map, Points pointCounter) {
        this.map = map;
        this.pointCounter = pointCounter;
        setInitialPosition(map);
    }

    public int getPixelX() {
        return pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public void setPixelX(int pixelX) {
        this.pixelX = pixelX;
    }

    public void setPixelY(int pixelY) {
        this.pixelY = pixelY;
    }

    public void setDirection(int directionX, int directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public void setNextDirection(int directionX, int directionY) {
        this.nextDirectionX = directionX;
        this.nextDirectionY = directionY;
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public void movePixel(int dx, int dy) {
        this.pixelX += dx;
        this.pixelY += dy;
    }

    public void draw(Graphics g, Image pacmanImage) {
        Graphics2D g2d = (Graphics2D) g;

        // Calculate the center of Pacman
        int centerX = pixelX + pacmanImage.getWidth(null) / 2;
        int centerY = pixelY + pacmanImage.getHeight(null) / 2;

        // Apply rotation
        g2d.rotate(rotationAngle, centerX, centerY);
        g2d.drawImage(pacmanImage, pixelX, pixelY, null);
        g2d.rotate(-rotationAngle, centerX, centerY); // Reset rotation
    }

    public void handleKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // ovládání, rotace a pohyb Pacmana
        switch (key) {
            case KeyEvent.VK_UP:
                rotationAngle = -Math.PI / 2; // Up
                setNextDirection(0, -moveSpeed);
                break;
            case KeyEvent.VK_DOWN:
                rotationAngle = Math.PI / 2; // Down
                setNextDirection(0, moveSpeed);
                break;
            case KeyEvent.VK_LEFT:
                rotationAngle = Math.PI; // Left
                setNextDirection(-moveSpeed, 0);
                break;
            case KeyEvent.VK_RIGHT:
                rotationAngle = 0; // Right (default)
                setNextDirection(moveSpeed, 0);
                break;
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        // Nepoužívá se
    }

    public void move(Map map) {
        int tileSize = 32;
        int newPixelX = pixelX + directionX;
        int newPixelY = pixelY + directionY;

        // Check if the next direction is walkable
        int nextPixelX = pixelX + nextDirectionX;
        int nextPixelY = pixelY + nextDirectionY;
        if (map.isWalkable(nextPixelX, nextPixelY)) {
            setDirection(nextDirectionX, nextDirectionY);
        }

        // Ověření, zda je průchozí celá hitbox
        if (map.isWalkable(newPixelX, newPixelY)) {
            movePixel(directionX, directionY); // Pohyb Pacmana
            if (map.getTile(pixelX / tileSize, pixelY / tileSize) == ' ') {
                pointCounter.increment(); // Increment the point counter
                map.clearTile(pixelX, pixelY); // Clear the tile when Pacman moves over it
            }
        } else {
            // Pokus o sklouznutí kolem zdi, pokud je Pacman příliš blízko
            int tileCenterX = (pixelX / tileSize) * tileSize + tileSize / 2;
            int tileCenterY = (pixelY / tileSize) * tileSize + tileSize / 2;

            // Zarovnání na střed, pokud je blízko
            if (Math.abs(pixelX - tileCenterX) < 5) {
                setPixelX(tileCenterX);
            }
            if (Math.abs(pixelY - tileCenterY) < 5) {
                setPixelY(tileCenterY);
            }
        }

        // Snap to grid
        if (directionX != 0 && pixelX % tileSize == 0) {
            setPixelX((pixelX / tileSize) * tileSize);
        }
        if (directionY != 0 && pixelY % tileSize == 0) {
            setPixelY((pixelY / tileSize) * tileSize);
        }
    }

    private void setInitialPosition(Map map) {
        char[][] grid = map.getGrid();
        int tileSize = 32; // Velikost políčka v pixelech

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 'P') { // Assuming 'P' represents Pacman's starting position
                    this.pixelX = x * tileSize;
                    this.pixelY = y * tileSize;
                    return;
                }
            }
        }
    }
}