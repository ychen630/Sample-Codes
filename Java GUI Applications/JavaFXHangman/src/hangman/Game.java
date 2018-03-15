package hangman;

/*
 * COMP585 Spring 2018
 * Project 3
 * 
 * Game Class
 * perform the game
 * 
 * Yixin Chen
 * Mar 12, 2018
 */

import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

	private String answer; //target word
	private String tmpAnswer; //temp answer by user
	private String[] letterAndPosArray;
	private static String currentInput; //current input by user
	private List<String> allInput; //list of all inputs by user
	private List<String> missedLetters; //missed letter
	boolean repeat = false; //whether user has input a repeat letter
	boolean won, gameOver; //end game status
//	private String[] words;
	private int moves; 
	private int index;
	private final ReadOnlyObjectWrapper<GameStatus> gameStatus;
	private ObjectProperty<Boolean> gameState = new ReadOnlyObjectWrapper<Boolean>();
	boolean isPlaying; //whether game has started

	public enum GameStatus {
		GAME_OVER {
			@Override
			public String toString() {
				return "Game over! Press Enter to try again.";
			}
		},
		BAD_GUESS {
			@Override
			public String toString() { 
				return "Bad guess..."; 
			}
		},
		GOOD_GUESS {
			@Override
			public String toString() {
				return "Good guess!";
			}
		},
		WON {
			@Override
			public String toString() {
				return "You won! Press Enter to start new game.";
			}
		},
		OPEN {
			@Override
			public String toString() {
				return "Game on, let's go!";
			}
		},
		REPEAT{
			@Override
			public String toString() {
				return currentInput.toUpperCase() +" has already been used! Try again.";
			}
		}
	}

	public Game(){
		gameStatus = new ReadOnlyObjectWrapper<GameStatus>(this, "gameStatus", GameStatus.OPEN);
		gameStatus.addListener(new ChangeListener<GameStatus>() {
			@Override
			public void changed(ObservableValue<? extends GameStatus> observable,
								GameStatus oldValue, GameStatus newValue) {
				if (gameStatus.get() != GameStatus.OPEN) {
					log("in Game: in changed");
					//currentPlayer.set(null);
				}
			}

		});
		startGame();
	}
	
	//start a new game, set everything to its default value, generate new target word
	public void startGame(){
		isPlaying = false;
		won = gameOver = false;
		repeat = false;
		
		allInput = new ArrayList<String>();
		missedLetters = new ArrayList<String>();
		currentInput = "";
		
		setRandomWord();
		prepTmpAnswer();
		prepLetterAndPosArray();
		moves = 0;

		gameState.setValue(false); // initial state
		createGameStatusBinding();
	}
	
	//game status
	private void createGameStatusBinding() {
		List<Observable> allObservableThings = new ArrayList<>();
		ObjectBinding<GameStatus> gameStatusBinding = new ObjectBinding<GameStatus>() {
			{
				super.bind(gameState);
			}
			@Override
			public GameStatus computeValue() {
				log("in computeValue");
				if(repeat) {
					log(currentInput.toUpperCase() + " has already been used! Try again.");
					return GameStatus.REPEAT;
				}
				else {
					GameStatus check = checkForWinner(index);
					if(check != null ) {
						return check;
					}
					if(tmpAnswer.trim().length() == 0 && !isPlaying){
						log("new game");
						return GameStatus.OPEN;
					}
					else if (index != -1){
						log("good guess");
						return GameStatus.GOOD_GUESS;
					}
					else {
						moves++;
						check = checkForWinner(index);
						if(check != null ) {
							return check;
						}
						log("moves: " + moves);
						log("bad guess");
						return GameStatus.BAD_GUESS;
					}
				}
			}
		};
		gameStatus.bind(gameStatusBinding);
	}

	public ReadOnlyObjectProperty<GameStatus> gameStatusProperty() {
		return gameStatus.getReadOnlyProperty();
	}
	
	public GameStatus getGameStatus() {
		return gameStatus.get();
	}

	//get a random word from a file
	private void setRandomWord(){
		WordGenerator words = new WordGenerator("dictionary.txt");
		answer = words.get().trim().toLowerCase();
		System.out.println(answer);
	}
	
	//intial the temp answer using space
	private void prepTmpAnswer() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < answer.length(); i++) {
			sb.append(" ");
		}
		tmpAnswer = sb.toString();
	}
	
	//store the target word letter by letter in letterAndPosArray
	private void prepLetterAndPosArray() {
		letterAndPosArray = new String[answer.length()];
		for(int i = 0; i < answer.length(); i++) {
			letterAndPosArray[i] = answer.substring(i,i+1);
		}
	}
	
	//check if the target word contains the input letter, if yes, return its index and delete the related letter in letterAndPosArray
	private int getValidIndex(String input) {
		int index = -1;
		for(int i = 0; i < letterAndPosArray.length; i++) {
			if(letterAndPosArray[i].equals(input)) {
				index = i;
				letterAndPosArray[i] = "";			
				break;
			}				
		}
		return index;
	}
	
	//count the number of letters in target word that equals to the input letter, and return the count 
	private int getLetterCount(String input) {
		int count = 0;
		for(int i = 0; i < letterAndPosArray.length; i++) {
			if(letterAndPosArray[i].equals(input)) {
				count++;
			}
		}
		if(count == 0) {
			missedLetters.add(input);
			log("missed: " + input);
		}
		return count;		
	}

	//update temp answer with current input letter by user 
	private int update(String input) {
		int count = getLetterCount(input);
		int index = -1;
		//if there are duplicate letters in the target word, update all the letters
		if(count > 0){
			for(int i = 0; i < count; i++) {
				index = getValidIndex(input);
				if(index != -1) {
					StringBuilder sb = new StringBuilder(tmpAnswer);
					sb.setCharAt(index, input.charAt(0));
					tmpAnswer = sb.toString();
				}
			}
		}
		return index;
	}
	
	//get current answer by user, to display in game status label
	public String getCurrentAnswer() {
		StringBuilder sb = new StringBuilder(tmpAnswer);
		for(int i = 0; i < tmpAnswer.length(); i++) {
			if(sb.charAt(i) == ' ') {
				sb.setCharAt(i, '*');
			}
		}
		return sb.toString();
	}

	//deal with the input letter
	public void makeMove(String letter) {
		log("\nin makeMove: " + letter);
		//if already won or lost a game, do nothing with input letter
		if(won || gameOver) {}
		else {
			isPlaying = true;
			currentInput = letter;
			//check if the input letter is a duplicate, if not, set repeat to false, update current answer; 
			//if yes, set repeat to true, update game status labels, do nothing with current answer
			if(!allInput.contains(currentInput)) {
				allInput.add(currentInput);
				repeat = false;
				index = update(letter);
				// this will toggle the state of the game
				gameState.setValue(!gameState.getValue());
			}
			else {
				repeat = true;
				gameState.setValue(!gameState.getValue());
			}
		}
	}
	
	//reset the game
	public void reset(){
		startGame();
	}
	
	//number of moves
	private int numOfTries() {
		return 7; // TODO, fix me
	}

	public static void log(String s) {
		System.out.println(s);
	}
	
	//check if the user is still in game, or won a game, or lost a game
	private GameStatus checkForWinner(int status) {
		log("in checkForWinner");
		//temp answer equals to answer, win
		if(tmpAnswer.equals(answer)) {
			log("won");
			isPlaying = false;
			won = true;
			return GameStatus.WON;
		}
		//run out of moves, lose
		else if(moves == numOfTries()) {
			log("game over");
			isPlaying = false;
			gameOver = true;
			return GameStatus.GAME_OVER;
		}
		//still in game, do nothing
		else {
			return null;
		}
	}
	
	//get moves
	public int getMove() {
		return moves;
	}
	
	//get answer
	public String getAnswer() {
		return answer;
	}
	
	//get missed letters
	public String getMissedLetters() {
		String ret = "";
		for(String s : missedLetters) {
			ret += s;
		}
		return ret;
	}
	
	//check user is playing game or not
	public boolean getPlayingStatus() {
		return isPlaying;
	}
	
	//get number of left moves
	public int getRemainMoves() {
		return numOfTries() - moves;
	}
}