package com.mahjong.controller;

import com.mahjong.service.*;
import com.mahjong.model.Tile;
import com.mahjong.dto.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/api/hello")
    public Response<String> hello() {
        Response<String> response = new Response<>();
        response.setData("Hello, Mahjong!");
        return response;
    }

    private MahjongGame mahjongGame;

    private MahjongGame getGame() {
        if (mahjongGame == null) {
            mahjongGame = new MahjongGame();
            mahjongGame.initializeTiles();
            mahjongGame.dealTiles();
            System.out.println("Created a new Mahjong game in the getGame method.");
        }
        return mahjongGame;
    }

    private GameStateResponse getCurrentGameState() {
        GameStateResponse gameStateResponse = new GameStateResponse();
        MahjongGame game = getGame();

        gameStateResponse.setCurrentPlayerHand(game.getCurrentPlayerHand(1));
        List<List<Tile>> opponentHands = new ArrayList<>();
        opponentHands.add(game.getCurrentPlayerHand(2));
        opponentHands.add(game.getCurrentPlayerHand(3));
        opponentHands.add(game.getCurrentPlayerHand(4));
        gameStateResponse.setOpponentHands(opponentHands);

        gameStateResponse.setDiscardedTiles(game.getDiscardedTiles());
        gameStateResponse.setCurrentPlayerTurn(game.getCurrentPlayerTurn());
        gameStateResponse.setYourTurn(game.isPlayerTurn(1));
        gameStateResponse.setCurrentPlayerFinishedHand(game.getCurrentPlayerFinishedHand(1));
        gameStateResponse.setWinnerId(game.getWinnerId());
        return gameStateResponse;
    }

    @GetMapping("/api/game/new")
    public Response<GameStateResponse> newGame() {

        try {
            int gameId = gameService.createNewGame(); // Call the service to create a new game
            GameStateResponse gameState = gameService.getGameState(gameId);
            gameState.setGameId(gameId);
            return Response.success(gameState);
        } catch (Exception e) {
            return Response.error("Failed to create game: " + e.getMessage());
        }
    }

    @GetMapping("/api/game/getGameState")
    public Response<GameStateResponse> getGameState(@RequestParam("gameId") int gameId) {
        GameStateResponse gameState = gameService.getGameState(gameId);
        return Response.success(gameState);
    }

    @GetMapping("/api/game/newTestGame")
    public Response<GameStateResponse> newTestGame() {
        mahjongGame = new MahjongGame();
        mahjongGame.initializeTiles();
        mahjongGame.dealSameTilesToAllPlayers();
        System.out.println("Created a new Mahjong game in the endpoint.");

        GameStateResponse gameStateResponse = getCurrentGameState();
        return Response.success(gameStateResponse);
    }

    @PostMapping("/api/game/exchangeFlowers")
    public Response<GameStateResponse> exchangeFlowers(@RequestParam("gameId") int gameId, Integer playerId) {

        GameStateResponse gameState = gameService.exchangeFlowers(gameId, playerId);
        gameState.setGameId(gameId);
        return Response.success(gameState);

    }

    @GetMapping("/api/game/sortTiles")
    public Response<GameStateResponse> sortTiles(@RequestParam("gameId") int gameId, Integer playerId) {
        GameStateResponse gameState = gameService.sortTiles(gameId, playerId);
        gameState.setGameId(gameId);
        return Response.success(gameState);
    }

    @GetMapping("/api/game/drawTile")
    public Response<GameStateResponse> drawTile(@RequestParam("gameId") int gameId,
            @RequestParam("playerId") int playerId) {
        GameStateResponse gameState = gameService.drawTile(gameId);
        gameState.setGameId(gameId);
        return Response.success(gameState);

    }

    @PostMapping("/api/game/discardTile")
    public Response<GameStateResponse> discardTile(@RequestParam("gameId") int gameId,
            @RequestParam("playerId") int playerId, @RequestBody DiscardTileRequest request) {
        System.out.println(request);

        GameStateResponse gameState = gameService.discardTile(gameId, playerId, request);
        gameState.setGameId(gameId);
        return Response.success(gameState);
    }

    @PostMapping("/api/game/chow")
    public Response<GameStateResponse> chow(@RequestBody ChowRequest request) {
        int playerId = request.getPlayerId();
        MahjongGame game = getGame();

        // Check if request.tiles exist in player's hand
        boolean doesRequestTilesExistInPlayerHand = requestTilesExistInPlayerHand(game, playerId, request.getTiles());

        if (!doesRequestTilesExistInPlayerHand) {

            GameStateResponse gameStateResponse = getCurrentGameState();
            Response<GameStateResponse> response = new Response<>();
            gameStateResponse.setValidMove(false);

            response.setData(gameStateResponse);
            response.addError("Invalid move: tiles not in hand");
            return response;
        }

        boolean isValidChow = game.checkChow(request.getSelectedTile(), request.getTiles(), playerId);
        if (isValidChow) {
            game.setCurrentPlayerTurn(playerId);
        }
        GameStateResponse gameStateResponse = getCurrentGameState();
        gameStateResponse.setValidMove(isValidChow);
        return Response.success(gameStateResponse);

    }

    @PostMapping("/api/game/pong")
    public Response<GameStateResponse> pong(@RequestBody PongRequest request) {
        int playerId = request.getPlayerId();
        MahjongGame game = getGame();
        GameStateResponse gameStateResponse = getCurrentGameState();
        Response<GameStateResponse> response = new Response<>();

        // Check if request.tiles exist in player's hand
        boolean doesRequestTilesExistInPlayerHand = requestTilesExistInPlayerHand(game, playerId, request.getTiles());

        if (!doesRequestTilesExistInPlayerHand) {

            gameStateResponse.setValidMove(false);
            response.setData(gameStateResponse);
            response.addError("Invalid move: tiles not in hand");
            return response;
        }

        boolean isValidPong = game.checkPong(request.getSelectedTile(), request.getTiles(), playerId);

        if (isValidPong) {
            gameStateResponse.setCurrentPlayerTurn(playerId);
        }
        gameStateResponse.setValidMove(isValidPong);
        response.setData(gameStateResponse);
        return response;

    }

    @PostMapping("/api/game/mahjong")
    public Response<GameStateResponse> mahjong(@RequestBody PongRequest request) {
        int playerId = request.getPlayerId();
        MahjongGame game = getGame();

        boolean isValidMahjong = game.checkMahjong(playerId);

        if (isValidMahjong) {
            game.setWinnerId(playerId);
            GameStateResponse gameStateResponse = new GameStateResponse();
            gameStateResponse.setValidMove(isValidMahjong);
            Response<GameStateResponse> response = new Response<>();
            response.setData(getCurrentGameState());
            return response;
        } else {
            GameStateResponse gameStateResponse = getCurrentGameState();
            Response<GameStateResponse> response = new Response<>();
            gameStateResponse.setValidMove(isValidMahjong);
            response.setData(gameStateResponse);
            response.addError("Invalid move: cannot declare Mahjong now");
            return response;

        }

    }

    @GetMapping("/api/game/computerTurn")
    public Response<GameStateResponse> computerTurn(@RequestParam("gameId") int gameId,
            @RequestParam("playerId") int playerId) {
        GameStateResponse gameState = gameService.handleComputerTurn(gameId, playerId);
        return Response.success(gameState);

    }

    private boolean requestTilesExistInPlayerHand(MahjongGame game, int playerId, List<Tile> requestTiles) {
        List<Tile> hand = game.getCurrentPlayerHand(playerId);

        for (Tile tile : requestTiles) {
            if (!hand.contains(tile)) {

                return false;
            }
        }
        return true;
    }
}
