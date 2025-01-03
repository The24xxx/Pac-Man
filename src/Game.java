import javax.swing.JFrame;

public class Game {

    public static void main(String[] args) {
        // Instance mapy
        Map map = new Map();

        // Instance počítadla bodů
        Points pointCounter = new Points();

        // Instance Pacmana
        Pacman pacman = new Pacman(map);

        // Vytisknutí mapy před pohybem
        //printMap(map);

        if (map.isWalkable(1, 1)) {
            System.out.println("Tile [1; 1] is walkable");
        } 


        // Vytvoření okna pro hru
        JFrame frame = new JFrame("Pacman Game");
        GamePanel gamePanel = new GamePanel(map, pointCounter);

        frame.add(gamePanel);
        frame.setSize(530, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        new javax.swing.Timer(200, e -> {
            System.out.println("Points: " + pointCounter.getPoints());
        }).start();
        

        // Testování pohybu
        //pacman.move(1, 0);

        // Vytisknutí mapy po pohybu
        //printMap(map);
    }

    // Pomocná metoda pro vypsání mapy
    public static void printMap(Map map) {
        for (int y = 0; y < map.getGrid().length; y++) {
            for (int x = 0; x < map.getGrid()[y].length; x++) {
                System.out.print(map.getTile(x, y) + " ");
            }
            System.out.println(); // nový řádek pro řadu mapy
        }
    }
}
