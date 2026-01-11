// Ahmet Eren Aslan - 02/05/2005
// CmpE160 Assignment 3 - Gold Trail: The Knight’s Path with Bonus Flag Integration

import java.io.*;
import java.util.*;

public class Main {
        public static void main(String[] args) {
                // Check if enough command-line arguments are provided
                // Expected: either [-bonus] flag or three file paths
                if (args.length < 3) {
                        System.out.println("Usage: java Main [-bonus] <mapData.txt> <travelCosts.txt> <objectives.txt>");
                        return;
                }

                // Flag to determine whether bonus functionality should be used
                boolean isBonus = false;
                int offset = 0; // Used to determine the starting index of file paths in args array

                // Check if the first argument is the "-bonus" flag
                if (args[0].equals("-bonus")) {
                        isBonus = true;
                        offset = 1; // Shift file path indices if bonus flag is present
                }

                // Get file paths from command-line arguments
                String mapDataPath = args[offset];
                String travelCostsPath = args[offset + 1];
                String objectivesPath = args[offset + 2];

                // Create a MapHandler instance to load the map and travel costs
                MapHandler mapHandler = new MapHandler();
                mapHandler.loadMap(mapDataPath); // Load map tiles from file
                mapHandler.loadTravelCosts(travelCostsPath); // Load movement costs between tiles
                List<int[]> objectives = mapHandler.loadObjectives(objectivesPath); // Load target coordinates

                // Based on the flag, either use the Bonus class or the standard PathFinder class
                if (isBonus) {
                        Bonus bonus = new Bonus(mapHandler);
                        bonus.findShortestRoute(objectives); // Find optimized route through all objectives
                } else {
                        PathFinder pathFinder = new PathFinder(mapHandler);
                        pathFinder.findPaths(objectives); // Find path step-by-step between objectives
                }
        }
}