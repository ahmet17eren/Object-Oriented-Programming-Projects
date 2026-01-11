// Ahmet Eren Aslan - 02/05/2005
// CmpE160 Assignment 3 Bonus - Optimized Gold Trail: Shortest Route with StdDraw

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

class PathFinder {
    // Reference to the MapHandler object that contains tile and cost data
    MapHandler mapHandler;

    // Constructor initializes the mapHandler and sets up the drawing canvas
    public PathFinder(MapHandler mapHandler) {
        this.mapHandler = mapHandler;
        setupStdDraw();
    }

    // Finds the path from the starting objective to each target objective sequentially
    public void findPaths(List<int[]> objectives) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("out/output.txt"))) {

            // All objectives except the starting point
            List<int[]> allObjectives = new ArrayList<>(objectives.subList(1, objectives.size()));

            // Knight's starting position
            int[] currentObjective = objectives.get(0);
            writer.println("Starting position: (" + currentObjective[0] + ", " + currentObjective[1] + ")");

            double totalCost = 0;  // objective's total path cost
            double totalCostSum = 0; // Accumulated total path cost
            int totalSteps = 0;       // Number of tiles visited

            for (int i = 1; i < objectives.size(); i++) {
                int stepCount = 0;        // Overall step number across all objectives
                Set<String> redDots = new LinkedHashSet<>(); // For visualizing visited tiles
                int[] target = objectives.get(i);

                Result result = dijkstra(currentObjective, target);
                boolean reached = true; // Will track whether the path was successful

                if (result == null) {
                    // If no path found to this objective
                    // Always print the new starting point
                    writer.println("Objective " + i + " cannot be reached!");
                    writer.println("Starting position: (" + currentObjective[0] + ", " + currentObjective[1] + ")");
                } else {
                    Tile previous = null;

                    for (Tile step : result.path) {
                        double stepCost = 0;

                        // Compute actual edge cost between consecutive tiles
                        if (previous != null) {
                            stepCost = previous.neighbors.get(step);
                            totalCost += stepCost;
                        }

                        // Only print the step if cost > 0.0
                        if (stepCost > 0.0) {
                            writer.printf("Step Count: %d, move to (%d, %d). Total Cost: %.2f.%n",
                                    stepCount++, step.column, step.row, totalCost);
                        } else {
                            stepCount++; // Still increment step counter even if skipped
                        }

                        // Visualization code
                        StdDraw.clear();
                        drawMap(allObjectives, new int[]{step.column, step.row});

                        if (previous != null) {
                            redDots.add(previous.column + "," + previous.row);
                        }

                        for (String red : redDots) {
                            String[] parts = red.split(",");
                            int col = Integer.parseInt(parts[0]);
                            int row = Integer.parseInt(parts[1]);
                            Tile base = mapHandler.tiles.get(col + "," + row);
                            if (base != null) {
                                StdDraw.setPenColor(StdDraw.RED);
                                StdDraw.filledCircle(col, mapHandler.height - row - 1, 0.15);
                            }
                        }

                        StdDraw.picture(step.column, mapHandler.height - step.row - 1, "misc/knight.png", 1, 1);
                        StdDraw.show();
                        StdDraw.pause(200);

                        previous = step;
                    }

                    writer.println("Objective " + i + " reached!"); // Log that the current objective has been successfully reached

                    // Flag to check whether the next objective can be reached from the current position
                    boolean nextWillReach = false;

                    // Make sure we're not at the end of the objectives list
                    if (i + 1 < objectives.size()) {
                        int[] nextTarget = objectives.get(i + 1); // Get coordinates of the next objective
                        Result nextResult = dijkstra(currentObjective, nextTarget); // Try to compute a path to the next objective

                        // If a path exists, mark nextWillReach as true
                        if (nextResult == null) {
                            nextWillReach = false; // No path found – unreachable
                        } else {
                            nextWillReach = true;  // Valid path exists – reachable
                        }
                    }

                    // Only print the starting position if the next objective is reachable
                    if (nextWillReach) {
                        writer.println("Starting position: (" + currentObjective[0] + ", " + currentObjective[1] + ")");
                    }

                    // Update path statistics
                    totalCostSum += totalCost;
                    totalCost = 0;
                    totalSteps += stepCount;

                    // Update the knight's current position to the one just reached
                    currentObjective = new int[]{target[0], target[1]};

                    // Remove the reached objective from the remaining list
                    List<int[]> newList = new ArrayList<>();
                    for (int[] obj : allObjectives) {
                        if (!(obj[0] == target[0] && obj[1] == target[1])) {
                            newList.add(obj);
                        }
                    }
                    allObjectives.clear();
                    allObjectives.addAll(newList);
                }

                totalSteps++;
            }

            // Print total stats at the end
            writer.printf("Total Step: %d, Total Cost: %.2f%n", totalSteps - 1, totalCostSum);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Implementation of Dijkstra's algorithm for shortest path
    public Result dijkstra(int[] startCoordinations, int[] endCoordinations) {
        Tile start = mapHandler.tiles.get(startCoordinations[0] + "," + startCoordinations[1]);
        Tile end = mapHandler.tiles.get(endCoordinations[0] + "," + endCoordinations[1]);
        if (start == null || end == null) return null;

        Map<Tile, Double> dist = new HashMap<>(); // Stores minimum distance to each tile
        Map<Tile, Tile> prev = new HashMap<>(); // Tracks previous tile for path reconstruction
        PriorityQueue<Tile> queue = new PriorityQueue<>(Comparator.comparingDouble(dist::get)); // Min-heap based on distance

        // Initialize all distances to infinity
        for (Tile tile : mapHandler.tiles.values()) {
            dist.put(tile, Double.MAX_VALUE);
        }
        dist.put(start, 0.0); // Distance to start is 0
        queue.add(start); // Add starting point to queue

        // Dijkstra's main loop
        while (!queue.isEmpty()) {
            Tile fromTile = queue.poll(); // Get the tile with the smallest distance
            if (fromTile.equals(end)) break; // Stop if we reached the target

            // Explore all neighbors
            for (Map.Entry<Tile, Double> entry : fromTile.neighbors.entrySet()) {
                Tile nextTile = entry.getKey();
                double alt = dist.get(fromTile) + entry.getValue(); // Tentative distance
                if (alt < dist.get(nextTile)) {
                    dist.put(nextTile, alt); // Update distance
                    prev.put(nextTile, fromTile); // Set previous tile for path
                    queue.add(nextTile); // Re-insert to update priority
                }
            }
        }

        // If end is unreachable
        if (!prev.containsKey(end)) {
            return null;
        }

        // Reconstruct path from end to start using prev map
        List<Tile> path = new ArrayList<>();
        Map<Tile, Double> costs = new HashMap<>();
        for (Tile at = end; at != null; at = prev.get(at)) {
            path.add(0, at); // Insert at the beginning (reverse path)
            costs.put(at, dist.get(at));
        }

        return new Result(path, costs); // Return path and cost map
    }

    // Sets up the drawing canvas size and scale
    private void setupStdDraw() {
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(-1, mapHandler.width);
        StdDraw.setYscale(-1, mapHandler.height);
        StdDraw.enableDoubleBuffering(); // Enables smoother animations
    }

    // Draws the full map along with remaining objectives and the knight's position
    private void drawMap(List<int[]> remainingObjectives, int[] knightPos) {
        for (Tile tile : mapHandler.tiles.values()) {
            // Draw tiles based on their type (0 = grass, 1 = sand, others = impassable)
            if (tile.type == 0) StdDraw.picture(tile.column, mapHandler.height - tile.row - 1, "misc/grassTile.jpeg", 1, 1);
            else if (tile.type == 1) StdDraw.picture(tile.column, mapHandler.height - tile.row - 1, "misc/sandTile.png", 1, 1);
            else StdDraw.picture(tile.column, mapHandler.height - tile.row - 1, "misc/impassableTile.jpeg", 1, 1);
        }

        // Draw remaining objectives as coins unless it's the knight's current position
        for (int[] obj : remainingObjectives) {
            if (obj[0] != knightPos[0] || obj[1] != knightPos[1]) {
                StdDraw.picture(obj[0], mapHandler.height - obj[1] - 1, "misc/coin.png", 1, 1);
            }
        }
    }

    // Inner class to store the result of Dijkstra's algorithm
    public static class Result {
        List<Tile> path;               // The shortest path from start to end
        Map<Tile, Double> costs;       // The cost to reach each tile on the path

        public Result(List<Tile> path, Map<Tile, Double> costs) {
            this.path = path;
            this.costs = costs;
        }
    }
}