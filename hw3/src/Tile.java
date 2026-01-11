// Ahmet Eren Aslan - 02/05/2005
// CmpE160 Assignment 3 Bonus - Optimized Gold Trail: Shortest Route with StdDraw

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Tile {
    // Coordinates and type of the tile (e.g., terrain type)
    int column, row, type;

    // Stores neighboring tiles and the cost to move to them
    Map<Tile, Double> neighbors = new HashMap<>();

    public Tile(int column, int row, int type) {
        this.column = column;
        this.row = row;
        this.type = type;
    }

    // Two tiles are considered equal if their column and row are the same
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return column == tile.column && row == tile.row;
    }

    // Hash code is based on column and row values
    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }
}