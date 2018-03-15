package hangman;

/*
 * COMP585 Spring 2018
 * Project 3
 * 
 * 
 * WordGenerator Class
 * Read a file, and randomly select a word in the file
 * 
 * Yixin Chen
 * Mar 12, 2018
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WordGenerator {
	
	//list of all words in the file
	List<String> words = new ArrayList<String>();
	
	public WordGenerator(String file){
		Scanner inFile;
		//read a file word by word, and store each word in the list
		try {
			inFile = new Scanner(new File(file));
			while(inFile.hasNext()) {
				words.add(inFile.next());
			}
			inFile.close();
		} catch (FileNotFoundException e) {
			//javaFX alert
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("IO Error");
			alert.setHeaderText(null);
			alert.setContentText("File Not Found!\n" + System.getProperty("user.dir") + file);
			alert.showAndWait();
		}
	}
	
	public String get() {
		//randomly select a word
		int i = (int) (Math.random()*words.size());
		return words.get(i);
	}
	
}
