import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

public class Ghosts {
    private int pixelX;
    private int pixelY;
    private int directionX;
    private int directionY;
    private Map map;
    private Image ghostImage;
    private final int moveSpeed = 4; // Speed of movement in pixels
    private Random random;

    public Ghosts(Map map, Image ghostImage) {
        this.map = map;
        this.ghostImage = ghostImage;
        this.random = new Random();
        setInitialPosition(map);
        setRandomDirection();
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

    public void move() {
        int newPixelX = pixelX + directionX;
        int newPixelY = pixelY + directionY;

        if (map.isWalkable(newPixelX, newPixelY)) {
            pixelX = newPixelX;
            pixelY = newPixelY;
        } else {
            setRandomDirection();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(ghostImage, pixelX, pixelY, null);
    }

    private void setInitialPosition(Map map) {
        char[][] grid = map.getGrid();
        int tileSize = 32; // Size of a tile in pixels

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == ' ') { // Assuming ' ' represents a walkable path
                    this.pixelX = x * tileSize;
                    this.pixelY = y * tileSize;
                    return;
                }
            }
        }
    }

    private void setRandomDirection() {
        int[] directions = {-moveSpeed, 0, moveSpeed};
        directionX = directions[random.nextInt(3)];
        directionY = directions[random.nextInt(3)];

        // Ensure the ghost moves in one direction at a time
        if (directionX != 0) {
            directionY = 0;
        } else if (directionY != 0) {
            directionX = 0;
        }
    }
}