public class Pacman {

    private int x;
    private int y;
    private Map map;
    private String direction;

    public Pacman(Map map) {
        this.map = map;

        // Najdi pozici 'P' v mřížce mapy
        for (int y = 0; y < map.getGrid().length; y++) {
            for (int x = 0; x < map.getGrid()[y].length; x++) {
                if (map.getGrid()[y][x] == 'P') {
                    this.x = x;
                    this.y = y;
                    this.direction = "right"; // výchozí směr
                    return; // Zastav, jakmile najdeš 'P'
                }
            }
        }
    }

    public void move(int dx, int dy) {
        int newX = this.x + dx;
        int newY = this.y + dy;
    
        // Zkontroluj, jestli je nové místo průchozí
        if (map.isWalkable(newX, newY)) {
            // Vyčisti starou pozici a nastav novou
            map.setTile(this.x, this.y, ' '); // Vyčisti starou pozici
            this.x = newX;
            this.y = newY;
            map.setTile(this.x, this.y, 'P'); // Nastav novou pozici Pacmana
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
