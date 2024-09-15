package com.ridango.game.controllers;

import com.ridango.dto.GameStatusDTO;
import com.ridango.game.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "http://localhost:3001")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameStatusDTO> startGame() {
        try {
            gameService.intializeGame();
            return ResponseEntity.ok(gameService.getGameStatus());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/guess")
    public ResponseEntity<GameStatusDTO> guess(@RequestBody String guess) {
        try {
            gameService.checkGuess(guess);
            return ResponseEntity.ok(gameService.getGameStatus());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/skip")
    public ResponseEntity<GameStatusDTO> skipRound() {
        try {
            gameService.skipRound();
            return ResponseEntity.ok(gameService.getGameStatus());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
