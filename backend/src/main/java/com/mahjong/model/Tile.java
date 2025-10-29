package com.mahjong.model;

import java.util.Objects;
import com.mahjong.util.TileType;

// Tile class
public class Tile {
    private TileType type;
    private int number; // 1-9 (or 1-4 for flowers to simplify)
    private boolean isFlower; // true if flower tile

    public Tile() {}

    public Tile(TileType type, int number, boolean isFlower) {
        this.type = type;
        this.number = number;
        this.isFlower = isFlower;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFlower() {
        return isFlower;
    }

    public void setFlower(boolean flower) {
        isFlower = flower;
    }

    @Override
    public String toString() {
        return type + "-" + number + (isFlower ? " (Flower)" : "");
    }

    // In Tile.java
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return number == tile.number &&
                isFlower == tile.isFlower &&
                type == tile.type;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, number, isFlower);
    }
}
