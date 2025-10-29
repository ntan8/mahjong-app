package com.mahjong.util;

import java.util.List;

import com.mahjong.model.Tile;

public class TileTracker {
    private List<Tile> drawPile; // Tiles still in bunot
    private List<Tile> discardedTiles; // Tiles thrown away
    private List<Tile> player1Hand; // What player 1 has
    private List<Tile> player2Hand; // etc.
    // ... for all players

    // Methods
    public int getRemainingCount(Tile tile) {
        // How many of this tile type are left unseen?
        int total = 4; // Filipino Mahjong has 4 of each tile, right?
        int seen = countSeenTiles(tile);
        return total - seen;
    }

    public int getDrawPileSize() {
        return drawPile.size();
    }

    private int countSeenTiles(Tile tile) {
        // Count in discarded + exposed melds + your hand
        return ((int) discardedTiles.stream()
                .filter(t -> t.equals(tile))
                .count());
    }
}