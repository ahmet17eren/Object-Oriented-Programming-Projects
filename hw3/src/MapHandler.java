// Ahmet Eren Aslan - 02/05/2005
// CmpE160 Assignment 3 Bonus - Optimized Gold Trail: Shortest Route with StdDraw

import java.io.File;
import java.io.IOException;
import java.util.*;

class MapHandler {
    // Stores tiles on the map using coordinate-based keys in the form "x,y"
    Map<String, Tile> tiles = new HashMap<>();

    // Dimensions of the map grid
    int width, height;

    // Loads the map data from a text file
    public void loadMap(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // First line contains map dimensions (width height)
            String[] size = scanner.nextLine().split(" ");
            height = Integer.parseInt(size[1]);
            width = Integer.parseInt(size[0]);

            // Read each tile from the file
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                int type = Integer.parseInt(parts[2]); // Tile type (e.g., grass, sand, impassable)
                int x = Integer.parseInt(parts[0]);    // Column index
                int y = Integer.parseInt(parts[1]);    // Row index
                Tile tile = new Tile(x, y, type);      // Create a new Tile object
                tiles.put(key(x, y), tile);            // Store it in the map with a unique key
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error if file reading fails
        }
    }

    // Loads movement costs (edges) between adjacent tiles
    public void loadTravelCosts(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Each line defines a movement between two tiles and its cost
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                int x1 = Integer.parseInt(parts[0]);
                int y1 = Integer.parseInt(parts[1]);
                int x2 = Integer.parseInt(parts[2]);
                int y2 = Integer.parseInt(parts[3]);
                double cost = Double.parseDouble(parts[4]);

                // Get the Tile objects from the map using their coordinates
                Tile tile1 = tiles.get(key(x1, y1));
                Tile tile2 = tiles.get(key(x2, y2));

                // If both tiles exist, set them as neighbors with movement cost
                if (tile1 != null && tile2 != null) {
                    tile1.neighbors.put(tile2, cost); // Directed or bidirectional edge
                    tile2.neighbors.put(tile1, cost); // Assuming bidirectional cost is the same
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read errors
        }
    }

    // Loads target objectives (coordinates to visit) from a file
    List<int[]> loadObjectives(String filePath) {
        List<int[]> objectives = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                objectives.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle error if objectives file fails to load
        }
        return objectives;
    }

    // Creates a unique string key for a tile using its (x, y) coordinates
    String key(int x, int y) {
        return x + "," + y;
    }
}