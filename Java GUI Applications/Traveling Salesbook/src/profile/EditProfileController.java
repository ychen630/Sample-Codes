package profile;
/*
 * controller for edit profile view
 */
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.Main;
import util.ToggleSwitch;

public class EditProfileController implements Initializable{
	
	public static Stage changePasswordStage;
	
	@FXML
	private TextField username;	
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private DatePicker birthday;
	@FXML
	private TextField email;
	@FXML
	private TextField phone;
	@FXML
	private TextField occupation;
	@FXML
	private TextField school;
	@FXML
	private TextField status;
	@FXML
	private GridPane gridPane; 
	@FXML
	private Pane pane;

	private ToggleSwitch hideFirstName;
	private ToggleSwitch hideLastName;
	private ToggleSwitch hideBirthday;
	private ToggleSwitch hideEmail;
	private ToggleSwitch hidePhone;
	private ToggleSwitch hideOccupation;
	private ToggleSwitch hideSchool;
	private ToggleSwitch hideStatus;
	private ToggleSwitch hidePosts;
	private ToggleSwitch hideFriends;
		
	Profile profile = new Profile();
	ProfilePrivacy profilePrivacy = new ProfilePrivacy();
	
//	@FXML
//	private ComboBox<String> onlineStatus;
	
	//initialize profile and user privacy preference
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setDOBField();
		//user privacy settings
		profilePrivacy = ProfileDAO.searchProfilePrivacy(String.valueOf(Main.userID));
		hideFirstName = new ToggleSwitch(profilePrivacy.getFirstName());
		gridPane.add(hideFirstName, 2, 1);
		hideLastName = new ToggleSwitch(profilePrivacy.getLastName());
		gridPane.add(hideLastName, 2, 2);
		hideBirthday = new ToggleSwitch(profilePrivacy.getBirthday());
		gridPane.add(hideBirthday, 2, 3);
		hideEmail = new ToggleSwitch(profilePrivacy.getEmail());
		gridPane.add(hideEmail, 2, 4);
		hidePhone = new ToggleSwitch(profilePrivacy.getPhoneNumber());
		gridPane.add(hidePhone, 2, 5);
		hideOccupation = new ToggleSwitch(profilePrivacy.getOccupation());
		gridPane.add(hideOccupation, 2, 6);
		hideSchool = new ToggleSwitch(profilePrivacy.getSchool());
		gridPane.add(hideSchool, 2, 7);
		hideStatus = new ToggleSwitch(profilePrivacy.getStatus());
		gridPane.add(hideStatus, 2, 8);
		hideFriends = new ToggleSwitch(profilePrivacy.getFriends());
		gridPane.add(hideFriends,  2,  9);
		hidePosts = new ToggleSwitch(profilePrivacy.getPosts());
		pane.getChildren().add(hidePosts);
		
		//profile settings
		profile = ProfileDAO.searchProfile(String.valueOf(Main.userID));
		username.setText(profile.getUsername());
		firstName.setText(profile.getFirstName());
		lastName.setText(profile.getLastName());
		email.setText(profile.getEmail());
		birthday.setValue(LocalDate.parse(profile.getBirthday()));
		phone.setText(profile.getPhoneNumber());
		occupation.setText(profile.getOccupation());
		school.setText(profile.getSchool());
		status.setText(profile.getStatus());
	}
	
	//cancel editing profile, back to welcome page
	@FXML
	private void cancelEditing(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../root/RootLayout.fxml"));
		Main.stage.getScene().setRoot(root);
	}
	
	//update profile
	@FXML
	private void processlEditing(MouseEvent event) throws IOException {
		if(ProfileDAO.checkUsernameAndEmail(username.getText(), email.getText())) {
			showAlert("Input error.", "Username or Email address already exist, please try again.");
			return;
		}
		//store new data to database
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Update profile");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to update your profile?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			
			profilePrivacy.setFirstName(hideFirstName.getStatus());
			profilePrivacy.setLastName(hideLastName.getStatus());
			profilePrivacy.setBirthday(hideBirthday.getStatus());
			profilePrivacy.setEmail(hideEmail.getStatus());
			profilePrivacy.setPhoneNumber(hidePhone.getStatus());
			profilePrivacy.setOccupation(hideOccupation.getStatus());
			profilePrivacy.setSchool(hideSchool.getStatus());
			profilePrivacy.setStatus(hideStatus.getStatus());
			profilePrivacy.setFriends(hideFriends.getStatus());
			profilePrivacy.setPosts(hidePosts.getStatus());
			ProfileDAO.editUserPrivacy(profilePrivacy);
			
			
			profile.setUsername(username.getText());
			profile.setFirstName(firstName.getText());
			profile.setLastName(lastName.getText());
			profile.setEmail(email.getText());
			profile.setBirthday(birthday.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
			profile.setPhoneNumber(phone.getText());
			profile.setOccupation(occupation.getText());
			profile.setSchool(school.getText());
			profile.setStatus(status.getText());
			ProfileDAO.editProfile(profile);
			
			ProfileDAO.editUser(profile);
			
			Alert confirmAlert = new Alert(AlertType.INFORMATION);
			confirmAlert.setTitle("Update confirmation");
			confirmAlert.setHeaderText(null);
			confirmAlert.setContentText("The profile is successfully updated.");
			confirmAlert.showAndWait();
			
			Parent root = FXMLLoader.load(getClass().getResource("../root/RootLayout.fxml"));
			Main.stage.getScene().setRoot(root);
			
		} else {
		    alert.close();
		}

	}
	
	//change password
	@FXML
	private void changePassword(MouseEvent event) throws IOException {
		changePasswordStage = new Stage();
		changePasswordStage.initModality(Modality.APPLICATION_MODAL);
		
		AnchorPane changePasswordFXML = (AnchorPane)FXMLLoader.load(getClass().getResource("ChangePasswordLayout.fxml"));
		Scene scene = new Scene(changePasswordFXML,250,240);
		changePasswordStage.setScene(scene);
		changePasswordStage.getIcons().add(new Image("file:icons/password.png"));
		changePasswordStage.setResizable(false);
		changePasswordStage.setTitle("Change Password");
		changePasswordStage.show();
	}
	
	//error alert
	private void showAlert(String string, String string2) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(string);
		alert.setHeaderText(null);
		alert.setContentText(string2);
		alert.showAndWait();
	}
	
	//format the birthday field as YYYY-MM-DD
	public void setDOBField() {
		final String pattern = "yyyy-MM-dd";
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = 
                DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        }; 
        birthday.setConverter(converter);
        birthday.setStyle("-fx-font-size: 16px ;");
	}
}
