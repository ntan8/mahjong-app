package com.mahjong.util;

import com.mahjong.model.Hand;
import com.mahjong.model.Tile;

public class HandEvaluator {
    private TileTracker tracker;

    public HandEvaluator(TileTracker tracker) {
        this.tracker = tracker;
    }

    public int countPotentialSequences(Tile tile, Hand hand) {
        int count = 0;

        // Check if it can form sequences

        int value = tile.getNumber();
        TileType type = tile.getType();

        // Can it form 1-2-3?
        if (value <= 7 && hand.has(value + 1, type) && hand.has(value + 2, type)) {
            count++;
        }
        // Can it form 2-3-4? (where this tile is the 2)
        if (value >= 2 && value <= 8 && hand.has(value - 1, type) && hand.has(value + 1, type)) {
            count++;
        }
        // Can it form 3-4-5? (where this tile is the 5)
        if (value >= 3 && hand.has(value - 1, type) && hand.has(value - 2, type)) {
            count++;
        }

        return count;
    }

    public double evaluateHandWithTile(Tile tile, Hand hand) {
        // How close to winning if you keep this tile?
        // This is more complex - looks at whole hand structure
        double score = 0.0;

        // Add points for existing pairs/sets this tile completes
        score += countPairs(tile, hand) * 10;
        score += countPotentialSequences(tile, hand) * 5;

        return score;
    }

    private int countPairs(Tile tile, Hand hand) {
        return hand.count(tile) >= 2 ? 1 : 0;
    }
}