// Ahmet Eren Aslan - 02/05/2005
// CmpE160 Assignment 3 Bonus - Optimized Gold Trail: Shortest Route with StdDraw

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Bonus {
    // Reference to map data and tile connections
    MapHandler mapHandler;

    public Bonus(MapHandler mapHandler) {
        this.mapHandler = mapHandler;
    }

    // Finds an optimized path that visits all objectives and returns to the start
    public void findShortestRoute(List<int[]> objectives) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("out/bonus.txt"))) {

            int[] start = objectives.get(0); // Starting point
            List<int[]> targets = new ArrayList<>(objectives.subList(1, objectives.size())); // All other objectives

            // Precompute shortest paths between every pair of objectives
            Map<String, PathFinder.Result> pairwiseResults = new HashMap<>();
            for (int i = 0; i < objectives.size(); i++) {
                for (int j = 0; j < objectives.size(); j++) {
                    if (i != j) {
                        int[] from = objectives.get(i);
                        int[] to = objectives.get(j);
                        PathFinder.Result result = new PathFinder(mapHandler).dijkstra(from, to);
                        pairwiseResults.put(key(from, to), result); // Save result for later lookup
                    }
                }
            }

            // Determine a greedy path through all objectives (nearest neighbor heuristic)
            List<Integer> pathOrder = new ArrayList<>(); // Order to visit targets
            List<Integer> remaining = new ArrayList<>();
            for (int i = 1; i < objectives.size(); i++) remaining.add(i);

            int[] current = start;
            double grandTotalCost = 0; // Accumulate total cost
            int grandTotalSteps = 0;   // Accumulate total step count

            while (!remaining.isEmpty()) {
                int nearestIdx = -1;
                double minCost = Double.MAX_VALUE;

                // Find nearest unvisited objective
                for (int idx : remaining) {
                    PathFinder.Result res = pairwiseResults.get(key(current, objectives.get(idx)));
                    if (res != null) {
                        double cost = res.costs.get(res.path.get(res.path.size() - 1));
                        if (cost < minCost) {
                            minCost = cost;
                            nearestIdx = idx;
                        }
                    }
                }

                if (nearestIdx == -1) break; // No reachable objective
                pathOrder.add(nearestIdx);
                current = objectives.get(nearestIdx);
                remaining.remove(Integer.valueOf(nearestIdx)); // Remove visited objective
            }

            // Setup canvas for drawing
            StdDraw.setCanvasSize(600, 600);
            StdDraw.setXscale(-1, mapHandler.width);
            StdDraw.setYscale(-1, mapHandler.height);
            StdDraw.enableDoubleBuffering();

            writer.println("Starting position: (" + start[0] + ", " + start[1] + ")");
            current = start;

            List<int[]> mutableTargets = new ArrayList<>(targets); // Copy for drawing coins

            int objectiveCounter = 1; // Objective number counter

            // Draw animation for each move along the optimized path
            for (int idx : pathOrder) {
                double totalCost = 0; // Reset cost for this objective
                int step = 1;         // Reset step for this objective

                PathFinder.Result res = pairwiseResults.get(key(current, objectives.get(idx)));
                Set<String> redDots = new LinkedHashSet<>();
                Tile previous = null;

                for (Tile tile : res.path) {
                    double currentCost = res.costs.get(tile);
                    if (currentCost != 0) {
                        writer.printf("Step Count: %d, move to (%d, %d). Total Cost: %.2f.%n", step++, tile.column, tile.row, currentCost);
                    }
                    if (previous != null)
                        redDots.add(previous.column + "," + previous.row);

                    StdDraw.clear();
                    drawMap(mutableTargets, new int[]{tile.column, tile.row});

                    // Draw visited tiles as red dots
                    for (String red : redDots) {
                        String[] parts = red.split(",");
                        int col = Integer.parseInt(parts[0]);
                        int row = Integer.parseInt(parts[1]);
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.filledCircle(col, mapHandler.height - row - 1, 0.15);
                    }

                    // Draw knight at current tile
                    StdDraw.picture(tile.column, mapHandler.height - tile.row - 1, "misc/knight.png", 1, 1);
                    StdDraw.show();
                    StdDraw.pause(200);

                    previous = tile;
                }

                // Print objective reached with number
                writer.println("Objective " + objectiveCounter + " reached at (" + objectives.get(idx)[0] + ", " + objectives.get(idx)[1] + ")!");

                current = objectives.get(idx);
                objectiveCounter++;

                // Remove reached target from remaining visual objectives
                for (int i = 0; i < mutableTargets.size(); i++) {
                    int[] obj = mutableTargets.get(i);
                    if (obj[0] == current[0] && obj[1] == current[1]) {
                        mutableTargets.remove(i);
                        break;
                    }
                }

                // Update overall totals
                grandTotalCost += res.costs.get(res.path.get(res.path.size() - 1));
                grandTotalSteps += step - 1;
            }

            // Finally return to the starting point and animate that move
            PathFinder.Result finalRes = pairwiseResults.get(key(current, start));
            if (finalRes != null) {
                Set<String> redDots = new LinkedHashSet<>();
                Tile previous = null;
                int returnStep = 1;

                for (Tile tile : finalRes.path) {
                    // Retrieve the total cost accumulated to reach this specific tile during the return path.
                    // This value represents the cost from the final objective back toward the starting point.
                    double cost = finalRes.costs.get(tile); // Cost to current tile
                    // Log each return step to the output file with detailed information:
                    // Step number in the return journey, the tile coordinates being visited, and the current cumulative cost.
                    if (cost != 0) {
                        writer.printf("Step Count: %d, move to (%d, %d). Total Cost: %.2f.%n", returnStep++, tile.column, tile.row, cost);
                    }

                    if (previous != null)
                        redDots.add(previous.column + "," + previous.row);

                    StdDraw.clear();
                    drawMap(mutableTargets, new int[]{tile.column, tile.row});

                    // Draw red path
                    for (String red : redDots) {
                        String[] parts = red.split(",");
                        int col = Integer.parseInt(parts[0]);
                        int row = Integer.parseInt(parts[1]);
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.filledCircle(col, mapHandler.height - row - 1, 0.15);
                    }

                    // Draw knight
                    StdDraw.picture(tile.column, mapHandler.height - tile.row - 1, "misc/knight.png", 1, 1);
                    StdDraw.show();
                    StdDraw.pause(200);

                    previous = tile;
                }

                grandTotalCost += finalRes.costs.get(finalRes.path.get(finalRes.path.size() - 1));
                grandTotalSteps += finalRes.path.size() - 1;
            }

            // Final report of total cost and steps
            writer.printf("Total Step: %d, Total Cost: %.2f%n", grandTotalSteps, grandTotalCost);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Draws the current state of the map and remaining objectives
    private void drawMap(List<int[]> remainingObjectives, int[] knightPos) {
        for (Tile tile : mapHandler.tiles.values()) {
            // Draw tile based on its type
            if (tile.type == 0)
                StdDraw.picture(tile.column, mapHandler.height - tile.row - 1, "misc/grassTile.jpeg", 1, 1);
            else if (tile.type == 1)
                StdDraw.picture(tile.column, mapHandler.height - tile.row - 1, "misc/sandTile.png", 1, 1);
            else
                StdDraw.picture(tile.column, mapHandler.height - tile.row - 1, "misc/impassableTile.jpeg", 1, 1);
        }

        // Draw coins for remaining objectives
        for (int[] obj : remainingObjectives) {
            if (obj[0] != knightPos[0] || obj[1] != knightPos[1]) {
                StdDraw.picture(obj[0], mapHandler.height - obj[1] - 1, "misc/coin.png", 1, 1);
            }
        }

        StdDraw.show();
    }

    // Generates a unique key for storing pairwise results
    private String key(int[] from, int[] to) {
        return from[0] + "," + from[1] + "->" + to[0] + "," + to[1];
    }
}