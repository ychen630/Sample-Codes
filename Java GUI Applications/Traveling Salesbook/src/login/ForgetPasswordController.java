package login;
/*
 * controller for forget password view
 * 
 */
import static util.DataUtil.returnHash;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import main.Main;
import profile.Profile;
import profile.ProfileDAO;

public class ForgetPasswordController implements Initializable{
	@FXML
	AnchorPane forgetPasswordMain;
	@FXML
	TextField securityAnswer;
	@FXML
	Label securityQuestion;
	@FXML
	PasswordField newPassword;
	@FXML
	PasswordField confirmNewPassword;
	
	Profile profile = new Profile();
	
	final BooleanProperty firstTime = new SimpleBooleanProperty(true); 
	
	//check inputs and update password in database
	@FXML
	public void confirmNewPassword() {
		if(securityAnswer.getText().toLowerCase().equals(profile.getSecurityAnswer())) {
			if(newPassword.getText().length() == 0) {
				showAlert("Input error", "Please input a new password.");
			}
			else {
				if(!newPassword.getText().equals(confirmNewPassword.getText())){
					showAlert("Input error", "Your passwords did not match! Please correct this and try again.");
				}
				else {
					String hashedPass = returnHash(newPassword.getText());
					LoginDAO.updatePassword(Main.userID, hashedPass);
					showAlert("Input error", "Password changed successfully. Please login using new password to begin.");
					LoginController.forgetPasswordStage.hide();
				}
			}
		}
		else {
			showAlert("Input error", "Incorrect security answer. Please try again.");
		}
		
	}
	
	//cancel
	@FXML
	public void cancel() {
		LoginController.forgetPasswordStage.hide();
	}
	
	//set security question
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		profile = ProfileDAO.searchProfile(String.valueOf(Main.userID));
		securityQuestion.setText(profile.getSecurityQuestion());
		//not focus on the username textField on start
		securityAnswer.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
            	forgetPasswordMain.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });			
	}
	
	//error alert
	private void showAlert(String string, String string2) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(string);
		alert.setHeaderText(null);
		alert.setContentText(string2);
		alert.showAndWait();
	}
}
