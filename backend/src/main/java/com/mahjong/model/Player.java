package com.mahjong.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Tile> hand;
    private List<List<Tile>> finishedHand;
    private int id;

    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
        this.finishedHand = new ArrayList<>();
    }

    public void addTile(Tile tile) {
        hand.add(tile);
    }

    public void removeTile(Tile tile) {
        hand.remove(tile);
    }

    public List<Tile> getHand() {
        return hand;
    }

    public void setHand(List<Tile> hand) {
        this.hand = hand;
    }

    public List<List<Tile>> getFinishedHand() {
        return finishedHand;
    }
    public void setFinishedHand(List<List<Tile>> finishedHand) {
        this.finishedHand = finishedHand;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Player " + id;
    }

}
