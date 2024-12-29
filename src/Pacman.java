public class Pacman {

    private int x;
    private int y;
    private Map map;
    private String direction;


    public Pacman(Map map) { // počáteční pozice Pacmana
        this.map = map;
        this.x = 7;
        this.y = 5;
        this.direction = "right"; //výchozí smě
        this.map.setTile(this.x, this.y, 'P');
    }

    public void move(int dx, int dy) {
        int newX = this.x + dx;
        int newY = this.y + dy;
    
        // Zkontroluj, jestli je nové místo průchozí
        if (map.isWalkable(newX, newY)) {
            // Nastav novou pozici Pacmana
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

