package com.mahjong.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahjong.dto.GameStateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class GameDao {
       
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    // CREATE - Insert new game
    public int createGame(GameStateResponse gameState) {
        String sql = "INSERT INTO games (game_state, current_player_turn, winner_id) VALUES (?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            try {
                ps.setString(1, objectMapper.writeValueAsString(gameState));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to convert game state to JSON", e);
            }
            ps.setInt(2, gameState.getCurrentPlayerTurn());
            ps.setInt(3, gameState.getWinnerId());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().intValue();
    }

     // READ - Get game by ID
    public GameStateResponse getGame(int gameId) {
        String sql = "SELECT game_state FROM games WHERE game_id = ?";
        
        try {
            String json = jdbcTemplate.queryForObject(sql, String.class, gameId);
            return objectMapper.readValue(json, GameStateResponse.class);
        } catch (Exception e) {
            return null;  // Game not found
        }
    }

     // UPDATE - Update existing game
    public void updateGame(long gameId, GameStateResponse gameState) {
        String sql = "UPDATE games SET game_state = ?, current_player_turn = ?, winner_id = ?, updated_at = CURRENT_TIMESTAMP WHERE game_id = ?";
        
        try {
            jdbcTemplate.update(sql, 
                objectMapper.writeValueAsString(gameState),
                gameState.getCurrentPlayerTurn(),
                gameState.getWinnerId(),
                gameId
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to update game", e);
        }
    }
    
}
