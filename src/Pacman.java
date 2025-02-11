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
    private final int moveSpeed = 2; // Rychlost pohybu v pixelech
    private double rotationAngle = 0; // Úhel rotace Pacmana
    private boolean justTeleported = false;  // Prevents instant back-and-forth teleporting


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
        this.directionX = directionX * moveSpeed;
        this.directionY = directionY * moveSpeed;
    }

    public void setNextDirection(int directionX, int directionY) {
        this.nextDirectionX = directionX * moveSpeed;
        this.nextDirectionY = directionY * moveSpeed;
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

    
        
        // this might do something
        char[][] grid = map.getGrid();
        int row = pixelY / tileSize;
        int col = pixelX / tileSize;

        //debugging
        //System.out.println("row: " + row);
        //System.out.println("col: " + col);


        

        //wrap around logic: '+' to '-' and vice versa
        //if pacman is on the wrap-around tile
  // 🛑 Prevent Out of Bounds Errors
if (row < 0 || row >= grid.length || col < 0 || col >= grid[row].length) {
    System.out.println("ERROR: Pac-Man went out of bounds! row: " + row + ", col: " + col);
    return;
}

// ✅ Prevent looping by checking if Pac-Man was just teleported
if (!justTeleported) {

// 🌀 Wrap-around logic: Teleport Pac-Man from '+' to '-'
// Only teleport if Pac-Man has fully entered the '+' tile (middle or further)
if (grid[row][col] == '+' && !justTeleported) {
    for (int x = 0; x < grid[row].length; x++) {
        if (grid[row][x] == '-') {
            //System.out.println("Teleporting Pac-Man from + to - at (" + row + ", " + x + ")");
            pixelX = x * tileSize;
            justTeleported = true;  // Mark teleport to prevent instant looping
            return;
        }
    }
}

// 🌀 Wrap-around logic: Teleport Pac-Man from '-' to '+'
// Teleports instantly since this already looks correct
if (grid[row][col] == '-' && !justTeleported) {
    for (int x = 0; x < grid[row].length; x++) {
        if (grid[row][x] == '+') {
            //System.out.println("Teleporting Pac-Man from - to + at (" + row + ", " + x + ")");
            pixelX = x * tileSize;
            justTeleported = true;  // Mark teleport to prevent instant looping
            return;
        }
    }
}

// ✅ Reset teleport flag once Pac-Man moves away from teleport tiles
if (grid[row][col] != '+' && grid[row][col] != '-') {
    justTeleported = false;
}

}

// ✅ Reset teleport flag when Pac-Man moves away from the teleport tiles
if (grid[row][col] != '+' && grid[row][col] != '-') {
    justTeleported = false;
}




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