package profile;
/*
 * Controller for profile view (welcome page)
 * 
 */
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.Main;


public class ProfileLayoutController implements Initializable{

	@FXML
	private Pane contentArea;
	
	@FXML
	private Label username;	
	@FXML
	private Label firstName;
	@FXML
	private Label lastName;
	@FXML
	private Label birthday;
	@FXML
	private Label email;
	@FXML
	private Label phone;
	@FXML
	private Label occupation;
	@FXML
	private Label school;
	@FXML
	private Label status;
	
	@FXML
	private Label welcomeLabel;
	
	//setting button listener
	@FXML
	private void setProfile(MouseEvent event) throws IOException {
		contentArea.getChildren().removeAll();
		Parent fxml = FXMLLoader.load(getClass().getResource("EditProfileLayout.fxml"));	
		contentArea.getChildren().setAll(fxml);
		contentArea.requestFocus();
	}

	//friend button listener
	@FXML
	private void listFriends(MouseEvent event) throws IOException {
		contentArea.getChildren().removeAll();
		Parent fxml = FXMLLoader.load(getClass().getResource("../friends/FriendsLayout.fxml"));
		contentArea.getChildren().setAll(fxml);
		contentArea.requestFocus();
	}
	
	//post button listener
	@FXML
	private void listPosts(MouseEvent event) throws IOException {
		contentArea.getChildren().removeAll();
		Parent fxml = FXMLLoader.load(getClass().getResource("../posts/PostsLayout.fxml"));
		contentArea.getChildren().setAll(fxml);
		contentArea.requestFocus();
	}
	
	//display user profile 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Profile profile = ProfileDAO.searchProfile(String.valueOf(Main.userID));
		username.setText(profile.getUsername());
		firstName.setText(profile.getFirstName());
		lastName.setText(profile.getLastName());
		email.setText(profile.getEmail());
		birthday.setText(profile.getBirthday());
		phone.setText(profile.getPhoneNumber());
		occupation.setText(profile.getOccupation());
		school.setText(profile.getSchool());
		status.setText(profile.getStatus());
		
		welcomeLabel.setText(welcomeMessage(profile));
	}
	
	//show welcome message based on username
	private String welcomeMessage(Profile profile) {
		GregorianCalendar time = new GregorianCalendar();
		int hour = time.get(Calendar.HOUR_OF_DAY);

		if (hour < 12) return "GOOD MORNING! " + profile.getUsername().toUpperCase();
		else if (hour < 17 && !(hour == 12)) return "GOOD AFTERNOON! " + profile.getUsername().toUpperCase();
		else if (hour == 12) return "GOOD NOON! " + profile.getUsername().toUpperCase();
		else return "GOOD EVENING! " + profile.getUsername().toUpperCase();
	}
}
	
