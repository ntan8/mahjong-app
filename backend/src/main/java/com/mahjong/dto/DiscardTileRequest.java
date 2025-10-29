package com.mahjong.dto;

import com.mahjong.util.TileType;

public class DiscardTileRequest {
    private TileType type;
    private int number;
    private boolean isFlower;

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

    public void setFlower(boolean isFlower) {
        this.isFlower = isFlower;
    }

    @Override
    public String toString() {
        return "DiscardTileRequest{" +
                "type=" + type +
                ", number=" + number +
                '}';
    }
}
