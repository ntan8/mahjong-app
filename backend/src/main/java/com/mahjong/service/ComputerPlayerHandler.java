package com.mahjong.service;

import java.util.List;
import com.mahjong.model.Tile;

public class ComputerPlayerHandler {

    private MahjongGame game;

    public ComputerPlayerHandler(MahjongGame game) {
        this.game = game;
    }

    public boolean handleSingleComputerTurn(int playerId) {
        int currentPlayer = game.getCurrentPlayerTurn();
        List<Tile> currentPlayerHand = game.getCurrentPlayerHand(playerId);

        if (currentPlayer != 1) {
            System.out.println("Computer player " + currentPlayer + " taking turn");

            // 1. Draw a tile
            game.drawTile(currentPlayer);

            // if the most recently drawn tile is a flower, discard it immediately
           Tile lastTile = currentPlayerHand.getLast();
           if (lastTile.isFlower()) {
               game.exchangeSingleFlower(currentPlayer, lastTile);
           }

            // 2. Simple AI: discard the last tile (most recently drawn)
            List<Tile> hand = game.getCurrentPlayerHand(currentPlayer);
            if (!hand.isEmpty()) {
                Tile tileToDiscard = hand.get(hand.size() - 1);
                game.discardTile(currentPlayer, tileToDiscard.getType(), tileToDiscard.getNumber());

            }
            // Check first tile. If the same type and number + 1 or -1 exists in current hand, keep it.
            

            // 3. Advance to next player's turn
            game.advanceToNextTurn();
            return true;
        }
        return false;
    }
}
