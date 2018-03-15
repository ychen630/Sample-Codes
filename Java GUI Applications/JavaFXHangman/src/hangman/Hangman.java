package hangman;

/*
 * COMP585 Spring 2018
 * Project 3
 * 
 * Hangman game
 * The user guesses a word by entering one letter at a time. 
 * If the user misses seven times, a hanging man hangs. 
 * Once a word is finished, the user can press the Enter key to start a new game.
 * 
 * Yixin Chen
 * Mar 12, 2018
 */

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Hangman extends Application {

	@Override
	public void start(final Stage primaryStage) throws IOException {
		final Game game = new Game();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Hangman.fxml"));
		loader.setController(new GameController(game));
		Parent root = loader.load();
		Scene scene = new Scene(root, 500, 580);
		scene.getStylesheets().add(getClass().getResource("Hangman.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:icon.png"));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
