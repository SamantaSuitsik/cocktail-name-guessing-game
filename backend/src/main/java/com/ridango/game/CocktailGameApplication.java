package com.ridango.game;

import com.ridango.dto.GameStatusDTO;
import com.ridango.game.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {

	@Autowired
	private GameService gameService;

	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		System.out.print("To play the cocktail name guessing game press P: ");
		var input = scanner.nextLine().toLowerCase();
		var hiddenLetters = "";
		if (input.equals("p")) {
			System.out.println("The aim of this game is to guess the cocktail's name by the number of letters shown and additional facts about the cocktail.");
			gameService.startGame();

			System.out.println("Guess the next cocktail!");
			System.out.println("Instructions for making the cocktail: " + gameService.getGameStatus().getInstructions());
			while (true) {
				var facts = gameService.getGameStatus().getFacts();
				for (String fact : facts) {
					System.out.println(fact);
				}
				hiddenLetters = gameService.getGameStatus().getHiddenLetters();
				System.out.println("Cocktail letters: " + hiddenLetters);

				System.out.print("Your guess: ");
				String guess = scanner.nextLine().toLowerCase();
				if (gameService.getGameStatus().getCocktailName().toLowerCase().equals(guess)) {
					System.out.println("You guessed right and gained " + gameService.getGameStatus().getAttemptsLeft() + " points!");
					gameService.checkGuess(guess);
					System.out.println("Current score: " + gameService.getGameStatus().getScore());
					System.out.println("Guess the next cocktail!");
					System.out.println("Instructions for making the cocktail: " + gameService.getGameStatus().getInstructions());
					continue;
				}
				gameService.checkGuess(guess);
				System.out.println("Incorrect!");
				System.out.println("You have " + gameService.getGameStatus().getAttemptsLeft() + " attempt(s) left.");
				if (gameService.getGameStatus().isGameLost()) {
					System.out.println("Actual name was: " + gameService.getGameStatus().getCocktailName());
					System.out.println("Game over! You have used all of your attempts!\nFinal score: " + gameService.getGameStatus().getScore());
					break;
				}

			}
		}
	}
}
