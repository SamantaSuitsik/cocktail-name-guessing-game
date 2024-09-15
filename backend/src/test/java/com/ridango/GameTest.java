package com.ridango;

import com.ridango.domain.Cocktail;
import com.ridango.game.services.CocktailService;
import com.ridango.game.services.GameService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private CocktailService cocktailService;
    private GameService gameService;
    private final static int ATTEMPTS = 5;
    private Cocktail mockCocktail;

    @BeforeEach
    void setUp() {
        cocktailService = mock(CocktailService.class);
        gameService = new GameService(cocktailService);
        mockCocktail = new Cocktail("Mojito", "Muddle mint leaves with sugar and lime juice. Add a splash of soda water and fill the glass with cracked ice. Pour the rum and top with soda water. Garnish and serve with straw.", "Cocktail", "Collins glass", List.of("Mint","Rum"),"https:/www.thecocktaildb.com/images/media/drink/07iep51598719977.jpg");

    }

    @Test
    void initializeGameTest() throws JSONException {
        when(cocktailService.getCocktail()).thenReturn(mockCocktail);

        gameService.intializeGame();
        var gameStatus = gameService.getGameStatus();

        assertNotNull(gameStatus);
        assertFalse(gameStatus.isGameLost());
        assertEquals(0, gameStatus.getScore());
        assertEquals(ATTEMPTS, gameStatus.getAttemptsLeft());
        assertNotNull(gameStatus.getCocktailName());
    }

    @Test
    void showInstructionsTest() throws JSONException {
        when(cocktailService.getCocktail()).thenReturn(mockCocktail);

        gameService.intializeGame();

        assertNotNull(gameService.getGameStatus().getInstructions());
    }

    @Test
    void hideAllAlphanumericalsTest() throws JSONException {
        Cocktail cocktailNameWithSpecialCharacters = new Cocktail("9 1/2 Weeks", "Muddle mint leaves with sugar and lime juice. Add a splash of soda water and fill the glass with cracked ice. Pour the rum and top with soda water. Garnish and serve with straw.", "Cocktail", "Collins glass", List.of("Mint","Rum"),"https:/www.thecocktaildb.com/images/media/drink/07iep51598719977.jpg");
        when(cocktailService.getCocktail()).thenReturn(cocktailNameWithSpecialCharacters);

        gameService.intializeGame();
        String hiddenLetters = gameService.getGameStatus().getHiddenLetters();
        assertEquals("_ _/_ _____", hiddenLetters);
    }

    @Test
    void correctGuessTest() throws JSONException {
        var newMockCocktail = new Cocktail("Margarita", "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.", "Ordinary Drink", "Cocktail glass", List.of("Tequila","Triple sec"),"https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg");
        when(cocktailService.getCocktail())
                .thenReturn(mockCocktail)
                .thenReturn(newMockCocktail);

        gameService.intializeGame();
        gameService.checkGuess("mojito");
        var gameStatus = gameService.getGameStatus();
        assertEquals(5, gameStatus.getScore());
    }

    @Test
    void incorrectGuessTest() throws JSONException {
        when(cocktailService.getCocktail()).thenReturn(mockCocktail);

        gameService.intializeGame();
        gameService.checkGuess("something different");
        var gameStatus = gameService.getGameStatus();
        assertEquals(ATTEMPTS-1, gameStatus.getAttemptsLeft());
        assertEquals(0, gameStatus.getScore());
    }

    @Test
    void allAttemptsUsedTest() throws JSONException {
        when(cocktailService.getCocktail()).thenReturn(mockCocktail);

        gameService.intializeGame();
        for (int i = 0; i < ATTEMPTS; i++) {
            gameService.checkGuess("wrong guess");
        }
        assertTrue(gameService.getGameStatus().isGameLost());
    }

    @Test
    void showHintTest() throws JSONException {
        when(cocktailService.getCocktail()).thenReturn(mockCocktail);

        gameService.intializeGame();
        gameService.checkGuess("wrong guess");
        assertEquals("https:/www.thecocktaildb.com/images/media/drink/07iep51598719977.jpg", gameService.getGameStatus().getFacts().getFirst());
    }

    @Test
    void lettersAreRevealedTest() throws JSONException {
        when(cocktailService.getCocktail()).thenReturn(mockCocktail);

        gameService.intializeGame();
        gameService.checkGuess("wrong guess");
        var gameStatus = gameService.getGameStatus();
        var hiddenLetters = gameStatus.getHiddenLetters();
        assertTrue(hiddenLetters.matches(".*[A-Za-z0-9].*"));
    }

    @Test
    void doesNotShowWholeWordTest() throws JSONException {
        var shortNamedCocktail = new Cocktail("Ab", "Muddle mint leaves with sugar and lime juice. Add a splash of soda water and fill the glass with cracked ice. Pour the rum and top with soda water. Garnish and serve with straw.", "Cocktail", "Collins glass", List.of("mint","rum"),"https:/www.thecocktaildb.com/images/media/drink/07iep51598719977.jpg");
        when(cocktailService.getCocktail()).thenReturn(shortNamedCocktail);

        gameService.intializeGame();
        for (int i = 0; i < ATTEMPTS-1; i++) {
            gameService.checkGuess("wrong guess");
        }
        assertTrue(gameService.getGameStatus().getHiddenLetters().matches(".*_.*"));
    }

    @Test
    void factsAreResetWhenNewCocktailIsGivenTest() throws JSONException {
        var newMockCocktail = new Cocktail("Margarita", "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.", "Ordinary Drink", "Cocktail glass", List.of("Tequila","Triple sec"),"https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg");
        var newCocktailFacts = List.of("Categorized as: Ordinary Drink", "Served in: Cocktail glass", "Consists of: Tequila, Triple sec", "https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg");
        when(cocktailService.getCocktail())
                .thenReturn(mockCocktail)
                .thenReturn(newMockCocktail);

        gameService.intializeGame();
        // Answer first one right to get new cocktail
        gameService.checkGuess("mojito");
        // Guess the new cocktail wrong to access all facts
        for (int i = 0; i < ATTEMPTS-1; i++) {
            gameService.checkGuess("wrong guess");
        }
        var actualFacts = gameService.getGameStatus().getFacts();
        assertTrue(actualFacts.containsAll(newCocktailFacts));
    }

    @Test
    void oldFactsAreClearedWhenNewCocktailIsGivenTest() throws JSONException {
        var oldCocktailFacts = List.of("Categorized as: Cocktail", "Served in: Collins glass", "Consists of: Mint, Rum", "https:/www.thecocktaildb.com/images/media/drink/07iep51598719977.jpg");
        var newMockCocktail = new Cocktail("Margarita", "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.", "Ordinary Drink", "Cocktail glass", List.of("Tequila","Triple sec"),"https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg");
        when(cocktailService.getCocktail())
                .thenReturn(mockCocktail)
                .thenReturn(newMockCocktail);

        gameService.intializeGame();
        gameService.checkGuess("mojito");
        for (int i = 0; i < ATTEMPTS-1; i++) {
            gameService.checkGuess("wrong guess");
        }
        var actualFacts = gameService.getGameStatus().getFacts();
        for (String actualFact : actualFacts) {
            assertFalse(oldCocktailFacts.contains(actualFact));
        }
    }

    @Test
    void instructionsAreResetAfterNewCocktailIsGivenTest() throws JSONException {
        var newMockCocktail = new Cocktail("Margarita","Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.", "Ordinary Drink", "Cocktail glass", List.of("Tequila","Triple sec"),"https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg");
        when(cocktailService.getCocktail())
                .thenReturn(mockCocktail)
                .thenReturn(newMockCocktail);

        gameService.intializeGame();
        gameService.checkGuess("mojito");

        assertEquals(newMockCocktail.instructions(),gameService.getGameStatus().getInstructions());
        assertNotEquals(mockCocktail.instructions(),gameService.getGameStatus().getInstructions());
    }

    @Test
    void oneCocktailDoesNotRepeatTest() throws JSONException {
        var newMockCocktail = new Cocktail("Margarita","Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.", "Ordinary Drink", "Cocktail glass", List.of("Tequila","Triple sec"),"https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg");
        when(cocktailService.getCocktail())
                .thenReturn(mockCocktail)
                .thenReturn(mockCocktail)
                .thenReturn(newMockCocktail);

        gameService.intializeGame();
        gameService.checkGuess("mojito");
        assertEquals(newMockCocktail.name(),gameService.getGameStatus().getCocktailName());
    }

    @Test
    void skippingRoundTest() throws JSONException {
        var newMockCocktail = new Cocktail("Margarita","Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.", "Ordinary Drink", "Cocktail glass", List.of("Tequila","Triple sec"),"https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg");
        when(cocktailService.getCocktail())
                .thenReturn(mockCocktail)
                .thenReturn(newMockCocktail);

        gameService.intializeGame();
        gameService.skipRound();
        var gameStatus = gameService.getGameStatus();
        assertEquals(-1,gameStatus.getScore());
        assertEquals(newMockCocktail.name(),gameStatus.getCocktailName());
    }
}
