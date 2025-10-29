package com.mahjong.util;

import com.mahjong.model.Tile;

public class ProbabilityEngine {
    private TileTracker tracker;

    public ProbabilityEngine(TileTracker tracker) {
        this.tracker = tracker;
    }

    public double probabilityInDrawPile(Tile tile) {
        int remaining = tracker.getRemainingCount(tile);
        int drawPileSize = tracker.getDrawPileSize();

        if (drawPileSize == 0)
            return 0.0;

        return (double) remaining / drawPileSize;
    }

    public double probabilityInOpponentHands(Tile tile, int numOpponents) {
        int remaining = tracker.getRemainingCount(tile);
        // More complex calculation here considering opponent hand sizes
        // This is simplified:
        return 1.0 - probabilityInDrawPile(tile);
    }
}