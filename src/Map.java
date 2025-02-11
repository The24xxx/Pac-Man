

public class Map {

    private char[][] grid;
    private char[][] initialGrid;

    public Map() {
        // inicializace mapy
        // # znamená zeď, ' ' znamená volné místo, P znamená Pacmana
        grid = new char[][] {
            {'#','#','#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#','#','#', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#'},
            {'#','#','#', ' ', '#', ' ', '#', '#', ' ', '#', ' ', '#', '#', ' ', '#', ' ', '#', '#'},
            {'#','#','#', ' ', '#', ' ', '#', '#', ' ', '#', ' ', '#', '#', ' ', '#', ' ', '#', '#'},
            {'#','#','#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#'},
            {'#','#','#', ' ', '#', ' ', '#', ' ', '#', '#', '#', ' ', '#', ' ', '#', ' ', '#', '#'},
            {'#','#','#', ' ', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', ' ', '#', '#'},
            {'#','#','#', '#', '#', ' ', '#', '#', ' ', '#', ' ', '#', '#', ' ', '#', '#', '#', '#'},
            {'#','#','#', '#', '#', ' ', '#', ' ', ' ', ' ', ' ', ' ', '#', ' ', '#', '#', '#', '#'},
            {'#','#','#', '#', '#', ' ', '#', ' ', '#', ' ', '#', ' ', '#', ' ', '#', '#', '#', '#'},
            {'+',' ',' ', ' ', ' ', ' ', ' ', ' ', '#', 'G', '#', ' ', ' ', ' ', ' ', ' ', ' ', '-'},
            {'#','#','#', '#', '#', ' ', '#', ' ', '#', '#', '#', ' ', '#', ' ', '#', '#', '#', '#'},
            {'#','#','#', '#', '#', ' ', '#', ' ', ' ', 'P', ' ', ' ', '#', ' ', '#', '#', '#', '#'},
            {'#','#','#', '#', '#', ' ', '#', ' ', '#', '#', '#', ' ', '#', ' ', '#', '#', '#', '#'},
            {'#','#','#', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#'},
            {'#','#','#', ' ', '#', ' ', '#', '#', ' ', '#', ' ', '#', '#', ' ', '#', ' ', '#', '#'},
            {'#','#','#', ' ', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' ', '#', '#'},
            {'#','#','#', ' ', ' ', ' ', '#', '#', ' ', '#', ' ', '#', '#', ' ', ' ', ' ', '#', '#'},
            {'#','#','#', '#', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#', '#', '#'},
            {'#','#','#', '#', ' ', ' ', '#', '#', ' ', '#', ' ', '#', '#', ' ', ' ', '#', '#', '#'},
            {'#','#','#', ' ', ' ', '#', ' ', ' ', ' ', '#', ' ', ' ', ' ', '#', ' ', ' ', '#', '#'},
            {'#','#','#', ' ', '#', '#', ' ', '#', ' ', '#', ' ', '#', ' ', '#', '#', ' ', '#', '#'},
            {'#','#','#', ' ', ' ', ' ', ' ', '#', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', '#', '#'},
            {'#','#','#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };

        // store the initial state of the map
        initialGrid = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
           System.arraycopy(grid[i], 0, initialGrid[i], 0, grid[i].length);
        }
    }

    public boolean isWalkable(int pixelX, int pixelY) {
        int tileSize = 32; // Velikost jedné dlaždice v pixelech
    
        // Přepočítání pixelových souřadnic na dlaždice
        int tileX1 = pixelX / tileSize;
        int tileY1 = pixelY / tileSize;
        int tileX2 = (pixelX + tileSize - 1) / tileSize; // Pravý okraj
        int tileY2 = (pixelY + tileSize - 1) / tileSize; // Spodní okraj
    
        // Získání gridu
        char[][] grid = getGrid();
    
        // Ověření, že všechny rohy hitboxu jsou na průchozích dlaždicích
        return isTileWalkable(tileX1, tileY1) && 
               isTileWalkable(tileX2, tileY1) &&
               isTileWalkable(tileX1, tileY2) && 
               isTileWalkable(tileX2, tileY2);
    }
    
    // Pomocná metoda k ověření průchodnosti jedné dlaždice
    private boolean isTileWalkable(int tileX, int tileY) {
        char[][] grid = getGrid();
        // Kontrola, zda jsou souřadnice v mezích gridu
        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[tileY].length) {
            return false; // Mimo mapu není průchozí
        }
    
        return grid[tileY][tileX] != '#'; // '#': neprochozí dlaždice (zeď)
    }

    public char getTile(int x, int y) { //getter pro získání políčka
        return grid[y][x];
    }

    public void setTile(int x, int y, char tile) { //setter pro nastavení políčka
        grid[y][x] = tile;
    }

    public char[][] getGrid() { //getter pro získání celé mapy
        return grid;
    }

    public void clearTile(int pixelX, int pixelY) {
        int tileSize = 32;
        int tileX = pixelX / tileSize;
        int tileY = pixelY / tileSize;
        if (grid[tileY][tileX] == ' ') {
            grid[tileY][tileX] = '.';
        }
    }

    public void resetGrid() {
        for (int i = 0; i < initialGrid.length; i++) {
            System.arraycopy(initialGrid[i], 0, grid[i], 0, initialGrid[i].length);
        }
    }

    public int countEmptySpaces() {
        int count = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == ' ') {
                    count++;
                }
            }
        }
        return count;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }
}