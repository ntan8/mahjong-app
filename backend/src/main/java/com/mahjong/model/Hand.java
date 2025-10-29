package com.mahjong.model;

import java.util.ArrayList;
import java.util.List;

import com.mahjong.util.TileType;

public class Hand {
    private List<Tile> tiles;
    private int maxSize; // Usually 13 or 14 in Mahjong

    // Constructor
    public Hand(int maxSize) {
        this.tiles = new ArrayList<>();
        this.maxSize = maxSize;
    }

    // Basic operations
    public void addTile(Tile tile) {
        if (tiles.size() < maxSize) {
            tiles.add(tile);
        }
    }

    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }

    public List<Tile> getTiles() {
        return new ArrayList<>(tiles); // Return a copy to prevent external modification
    }

    public int size() {
        return tiles.size();
    }

    // Checking for specific tiles
    public boolean has(Tile tile) {
        return tiles.contains(tile);
    }

    public boolean has(int value, TileType type) {
        // Check if hand has a tile with this number value
        for (Tile tile : tiles) {
            if (tile.getNumber() == value && tile.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public int count(Tile tile) {
        // How many of this exact tile do we have?
        int count = 0;
        for (Tile t : tiles) {
            if (t.equals(tile)) {
                count++;
            }
        }
        return count;
    }

    // Useful for AI evaluation
    public List<Tile> getPairs() {
        // Find all tiles that appear at least twice
        List<Tile> pairs = new ArrayList<>();
        for (Tile tile : tiles) {
            if (count(tile) >= 2 && !pairs.contains(tile)) {
                pairs.add(tile);
            }
        }
        return pairs;
    }

    public List<Tile> getSingles() {
        // Find tiles that appear only once
        List<Tile> singles = new ArrayList<>();
        for (Tile tile : tiles) {
            if (count(tile) == 1 && !singles.contains(tile)) {
                singles.add(tile);
            }
        }
        return singles;
    }

    // Sorting (useful for display and analysis)
    public void sort() {
        tiles.sort((t1, t2) -> {
            // Sort by suit first, then by value
            if (t1.getType() != t2.getType()) {
                return t1.getType().compareTo(t2.getType());
            }
            return Integer.compare(t1.getNumber(), t2.getNumber());
        });
    }

    // Display
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hand: ");
        for (Tile tile : tiles) {
            sb.append(tile.toString()).append(" ");
        }
        return sb.toString();
    }
}