package com.mahjong.dto;
import com.mahjong.model.Tile;
import java.util.List;

public class Request {

    private List<Tile> tiles;
    private Tile selectedTile;
    private int playerId;

    public List<Tile> getTiles() {
        return tiles;
    }
    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }
    public Tile getSelectedTile() {
        return selectedTile;
    }
    public void setSelectedTile(Tile selectedTile) {
        this.selectedTile = selectedTile;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public int getPlayerId() {
        return playerId;
    }
}
