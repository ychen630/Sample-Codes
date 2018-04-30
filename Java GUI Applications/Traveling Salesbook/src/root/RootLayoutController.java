package root;
/*
 * root view layout controller
 * Created by Yixin Chen on 4/12/2018  
 * 
 */
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.Main;

public class RootLayoutController implements Initializable{
	@FXML
	BorderPane rootLayout;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
			showProfile();
	}
	
	//load profile layout
	public void showProfile(){
        AnchorPane profileLayout;
		try {
			profileLayout = (AnchorPane) FXMLLoader.load(getClass().getResource("../profile/ProfileLayout.fxml"));
			rootLayout.setCenter(profileLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//exit menu item
	public void handleExit() {
		System.exit(0);
	}
	
	//signout menu item
	public void signout() throws IOException {
		Main.userID = 0;
		Parent root = FXMLLoader.load(getClass().getResource("../login/Login.fxml"));
		Main.stage.getScene().setRoot(root);
	}
	
	//about menu item
	public void about() {
		Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("About");
    	alert.setHeaderText(null);
    	alert.setContentText("Traveling Salesbook\n"
    						+ "Author: Yixin Chen, Rallante Hung, Kyle Rickets, Xiaohan Yang\n"
    						+ "COMP585 Project 4\n"
    						+ "Group 0");

    	alert.showAndWait();
	}
}
