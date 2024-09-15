package com.ridango.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GameStatusDTO {
    private String cocktailName;
    private String hiddenLetters;
    private String instructions;
    private List<String> facts;
    private int score;
    private int attemptsLeft;
    private boolean gameLost;

    public GameStatusDTO(String cocktailName, String hiddenLetters, String instructions, List<String> facts, int score, int attemptsLeft, boolean gameLost) {
        this.cocktailName = cocktailName;
        this.hiddenLetters = hiddenLetters;
        this.instructions = instructions;
        this.facts = facts;
        this.score = score;
        this.attemptsLeft = attemptsLeft;
        this.gameLost = gameLost;
    }
}
