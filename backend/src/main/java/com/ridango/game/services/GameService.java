package com.ridango.game.services;

import com.ridango.domain.Cocktail;
import com.ridango.dto.GameStatusDTO;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    private static final String GLASS = "glass";
    private static final String CATEGORY = "category";
    private static final String INGREDIENTS = "ingredient";
    private static final String IMAGE = "imgurl";
    private static final char UNDERSCORE = '_';
    private static final int ATTEMPTS = 5;

    private int score;
    private int attemptsLeft;
    private Cocktail currentCocktail;
    private String currentCocktailName;
    private char[] actualLetters;
    private String lettersShownToPlayer;
    private long lettersShownEachRound;
    private List<String> facts = new ArrayList<>();
    private List<String> factProperties;
    private List<String> usedCocktails = new ArrayList<>();
    private final CocktailService cocktailService;

    public GameService(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    public void intializeGame() throws JSONException {
        score = 0;
        startGame();
    }

    public void startGame() throws JSONException {
        attemptsLeft = ATTEMPTS;
        do {
            currentCocktail = cocktailService.getCocktail();
        }
        while (usedCocktails.contains(currentCocktail.name()));

        usedCocktails.add(currentCocktail.name());
        currentCocktailName = currentCocktail.name().toLowerCase();
        prepareShowingHints();
    }

    private boolean isGameLost() {
        return attemptsLeft == 0;
    }

    private void prepareShowingHints() {
        actualLetters = currentCocktailName.toCharArray();
        lettersShownToPlayer = currentCocktailName.replaceAll("[A-Za-z0-9]", String.valueOf(UNDERSCORE));
        long howManyUnderscores = howManyUnderscoresInHiddenName();
        lettersShownEachRound = Math.max(1,howManyUnderscores/ATTEMPTS);

        prepareFacts();
    }

    private long howManyUnderscoresInHiddenName() {
        return lettersShownToPlayer.chars().filter(c -> c == UNDERSCORE).count();
    }

    private void prepareFacts() {
        facts = new ArrayList<>();
        factProperties = new ArrayList<>(List.of(GLASS,CATEGORY,INGREDIENTS));
        Collections.shuffle(factProperties);
        factProperties.addFirst(IMAGE);
    }

    public void checkGuess(String guess) throws JSONException {
        if (guess.toLowerCase().equals(currentCocktailName)) {
            score += attemptsLeft;
            startGame();
        }
        else {
            attemptsLeft--;
            showHint();
            addFactToList();
        }
    }

    private void showHint() {
        long lettersLeftToShow = howManyUnderscoresInHiddenName();

        // Ensure that at least one letter stays hidden
        if (lettersLeftToShow - lettersShownEachRound <= 1) {
            lettersShownEachRound = Math.max(lettersLeftToShow - 1, 0);
        }
        for (int i = 0; i < lettersShownEachRound; i++) {
            showRandomLetter();
        }

    }

    private void showRandomLetter() {
        char[] lettersShownToPlayerArray = lettersShownToPlayer.toCharArray();
        Random rand = new Random();
        while (true) {
            int randomIndex = rand.nextInt(lettersShownToPlayerArray.length);
            if (lettersShownToPlayerArray[randomIndex] == UNDERSCORE) {
                lettersShownToPlayerArray[randomIndex] = actualLetters[randomIndex];
                lettersShownToPlayer = new String(lettersShownToPlayerArray);
                break;
            }
        }
    }

    private String getCocktailFact() {
        if (factProperties.isEmpty()) {
            return "";
        }
        return switch (factProperties.removeFirst()) {
            case GLASS -> "Served in: "  + currentCocktail.glass();
            case CATEGORY -> "Categorized as: " + currentCocktail.category();
            case INGREDIENTS ->
                    "Consists of: " + String.join(", ", currentCocktail.ingredients());
            case IMAGE -> currentCocktail.imgUrl();
            default -> "";
        };
    }

    private void addFactToList() {
        String fact = getCocktailFact();
        if (!fact.isEmpty()) {
            facts.add(fact);
        }
    }

    public void skipRound() throws JSONException {
        startGame();
        score--;
    }

    public GameStatusDTO getGameStatus() {
        return new GameStatusDTO(currentCocktail.name(), lettersShownToPlayer, currentCocktail.instructions(), facts, score, attemptsLeft, isGameLost());
    }
}
