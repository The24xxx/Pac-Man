import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
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
            chooseNewDirection();
        }

        // Always choose a new direction at intersections
        if (isAtIntersection()) {
            chooseNewDirection();
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
                if (grid[y][x] == 'G') { // Assuming 'G' represents Ghost's starting position
                    this.pixelX = x * tileSize;
                    this.pixelY = y * tileSize;
                    return;
                }
            }
        }
    }

    private void chooseNewDirection() {
        List<int[]> possibleDirections = new ArrayList<>();

        if (map.isWalkable(pixelX + moveSpeed, pixelY) && directionX != -moveSpeed) {
            possibleDirections.add(new int[]{moveSpeed, 0});
        }
        if (map.isWalkable(pixelX - moveSpeed, pixelY) && directionX != moveSpeed) {
            possibleDirections.add(new int[]{-moveSpeed, 0});
        }
        if (map.isWalkable(pixelX, pixelY + moveSpeed) && directionY != -moveSpeed) {
            possibleDirections.add(new int[]{0, moveSpeed});
        }
        if (map.isWalkable(pixelX, pixelY - moveSpeed) && directionY != moveSpeed) {
            possibleDirections.add(new int[]{0, -moveSpeed});
        }

        if (!possibleDirections.isEmpty()) {
            int[] newDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
            directionX = newDirection[0];
            directionY = newDirection[1];
        } else {
            // No other direction possible, reverse
            directionX = -directionX;
            directionY = -directionY;
        }
    }

    private boolean isAtIntersection() {
        int paths = 0;

        if (map.isWalkable(pixelX + moveSpeed, pixelY)) paths++;
        if (map.isWalkable(pixelX - moveSpeed, pixelY)) paths++;
        if (map.isWalkable(pixelX, pixelY + moveSpeed)) paths++;
        if (map.isWalkable(pixelX, pixelY - moveSpeed)) paths++;

        return paths >= 3; // Consider an intersection if there are three or more paths
    }

    private void setRandomDirection() {
        int[] directions = {-moveSpeed, 0, moveSpeed};
        do {
            directionX = directions[random.nextInt(3)];
            directionY = directions[random.nextInt(3)];
        } while (directionX == 0 && directionY == 0);
    }
}
