package main;
/*
 * Traveling Salesbook lite
 * This app allow users to register, login and set profile
 * User can add or delete friends which are also users of this app
 * User can view friends's profiles (some fields may not be visible according to user's settings)
 * User can post, edit or delete a post. If user set post to be visible, friends can see your posts.
 * 
 */
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	public static Stage stage;
	
	//userID from LoginController, this should be pass to other classes
	public static int userID = 0; 

	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../login/Login.fxml"));
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("../login/stylesheet.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("file:icons/traveling_salesman.jpg"));
			primaryStage.setResizable(false);
			primaryStage.setTitle("Traveling Salesbook");
			stage = primaryStage;
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
