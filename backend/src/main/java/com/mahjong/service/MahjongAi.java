package com.mahjong.service;

import java.util.List;

import com.mahjong.model.Hand;
import com.mahjong.model.Tile;
import com.mahjong.util.HandEvaluator;
import com.mahjong.util.ProbabilityEngine;
import com.mahjong.util.TileTracker;
import com.mahjong.util.TileType;

public class MahjongAi {
    private TileTracker tracker;
    private ProbabilityEngine probEngine;
    private HandEvaluator evaluator;

    public Tile decideDiscard(Hand currentHand) {
        List<Tile> tiles = currentHand.getTiles();
        Tile worstTile = null;
        double lowestScore = Double.MAX_VALUE;

        for (Tile tile : tiles) {
            double score = evaluateTileValue(tile, currentHand);
            if (score < lowestScore) {
                lowestScore = score;
                worstTile = tile;
            }
        }
        return worstTile;
    }
    // **Key insight:** You want to **KEEP** tiles with HIGH scores and **DISCARD**
    // tiles with LOW scores!

    // ## Quick Mental Model
    // ```
    // MahjongAI (the brain)
    // ├── TileTracker (the eyes) - "What tiles exist where?"
    // ├── ProbabilityEngine (the calculator) - "What are the odds?"
    // └── HandEvaluator (the strategist) - "Is this tile useful?"

    private double evaluateTileValue(Tile tile, Hand hand) {
        // Calculate based on:
        // - Can it form sequences? (edge vs middle)
        // Call the HandEvaluator... it takes the tile and the hand and returns a score
        // of how many sequences it can currently make

        // - How many similar tiles remain?
        // Call the TileTracker to see how many similar tiles remain in the game?

        // Call ProbabilityEngine to return a value of the probability that it's in the
        // 'bunot' pile

        // - Does it fit current hand strategy?
        // Call the HandEvaluator... it returns the probability of you winning with this
        // tile?
        return calculateCompositeScore(tile, hand);
    }

    private double calculateCompositeScore(Tile tile, Hand hand) {
        double totalScore = 0.0;

        // Factor 1: How many sequences can it form? (from HandEvaluator)
        int sequences = evaluator.countPotentialSequences(tile, hand);
        totalScore += sequences * 15; // Weight: 15 points per sequence

        // Factor 2: How many similar tiles remain? (from TileTracker)
        int remainingTiles = tracker.getRemainingCount(tile);
        totalScore += remainingTiles * 8; // Weight: 8 points per remaining tile

        // Factor 3: Probability it's in the draw pile (from ProbabilityEngine)
        double probInDrawPile = probEngine.probabilityInDrawPile(tile);
        totalScore += probInDrawPile * 25; // Weight: 25 points (it's a percentage 0-1)

        // Factor 4: Does it fit the overall hand strategy? (from HandEvaluator)
        double handFitScore = evaluator.evaluateHandWithTile(tile, hand);
        totalScore += handFitScore * 1.5; // Weight: multiply by 1.5 since it already returns a score

        // Factor 5: Is it an edge tile? (Your mom's advice!)
        if ((tile.getNumber() == 1 || tile.getNumber() == 9)) {
            totalScore -= 10; // Penalty: edge tiles are less flexible
        }

        // Factor 6: Is it isolated in your hand? (dangerous to keep)
        if (isIsolated(tile, hand)) {
            totalScore -= 12; // Penalty: isolated tiles are harder to use
        }

        // Factor 7: Do you already have a pair/triple of this?
        int countInHand = hand.count(tile);
        if (countInHand >= 2) {
            totalScore += 20; // Bonus: pairs/triples are valuable!
        }

        return totalScore;
    }

    // Helper method
    private boolean isIsolated(Tile tile, Hand hand) {

        int value = tile.getNumber();
        TileType type = tile.getType();

        boolean hasLower = hand.has(value - 1, type) || hand.has(value - 2, type);
        boolean hasHigher = hand.has(value + 1, type) || hand.has(value + 2, type);

        return !hasLower && !hasHigher && hand.count(tile) < 2;
    }
}