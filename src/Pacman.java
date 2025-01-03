public class Pacman {

    private int pixelX; // Aktuální pixelová X souřadnice
    private int pixelY; // Aktuální pixelová Y souřadnice
    private int tileX;  // Dlaždicová X souřadnice
    private int tileY;  // Dlaždicová Y souřadnice
    private Map map;

    public Pacman(Map map) {
        this.map = map;

        // Najdi výchozí dlaždici 'P'
        for (int y = 0; y < map.getGrid().length; y++) {
            for (int x = 0; x < map.getGrid()[y].length; x++) {
                if (map.getGrid()[y][x] == 'P') {
                    this.tileX = x;
                    this.tileY = y;
                    this.pixelX = x * 32;
                    this.pixelY = y * 32;
                    return;
                }
            }
        }
    }

    public void movePixel(int dx, int dy) {
        this.pixelX += dx;
        this.pixelY += dy;

        // Aktualizace dlaždicové pozice při dosažení další dlaždice
        this.tileX = this.pixelX / 32;
        this.tileY = this.pixelY / 32;
    }

    // Nové metody pro nastavení pixelových souřadnic
    public void setPixelX(int pixelX) {
        this.pixelX = pixelX;
        this.tileX = pixelX / 32; // Aktualizace dlaždicové pozice
    }

    public void setPixelY(int pixelY) {
        this.pixelY = pixelY;
        this.tileY = pixelY / 32; // Aktualizace dlaždicové pozice
    }

    // Gettery pro souřadnice
    public int getPixelX() {
        return pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }
}
