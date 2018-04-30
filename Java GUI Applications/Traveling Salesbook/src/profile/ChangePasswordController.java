package profile;
/*
 * controller for change password view
 * 
 */
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

import main.Main;
import static util.DataUtil.returnHash;

public class ChangePasswordController implements Initializable{
	
	@FXML
	AnchorPane changePasswordMain;
	@FXML
	PasswordField currentPassword;
	@FXML
	PasswordField newPassword;
	@FXML
	PasswordField confirmNewPassword;
	
	final BooleanProperty firstTime = new SimpleBooleanProperty(true); 
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//not focus on the Current Password textField on start
		currentPassword.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
            	changePasswordMain.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });	
	}
	
	//check user inputs and process password change
	@FXML
	private void changePassword() {
		if(currentPassword.getText().length() == 0) showAlert("Input error", "Please input password.");
		else if(newPassword.getText().length() == 0) showAlert("Input error", "Please input new password.");
		else if(!newPassword.getText().equals(confirmNewPassword.getText())) showAlert("Input error.", "Passwords not match.");
		else {
			String hashedPass = returnHash(currentPassword.getText());
			// Create and execute sql statement to check credentials and update password.
			int userID = ProfileDAO.checkUser(Main.userID, hashedPass);
			if(userID != 0) {
				hashedPass = returnHash(newPassword.getText());
				ProfileDAO.updatePassword(Main.userID, hashedPass);
				showInformAlert("Password change confirmation", "Your password was successfully changed!");
				EditProfileController.changePasswordStage.hide();
			}
			else { // empty result set.
				System.err.println("Couldn't find a match for your password!");
				// password not match, popup an alert window
				showAlert("Input error", "Invalid password.");

			}
		}
	}

	//error alert
	private void showAlert(String string, String string2) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(string);
		alert.setHeaderText(null);
		alert.setContentText(string2);
		alert.showAndWait();	
	}
	
	//information alert
	private void showInformAlert(String string, String string2) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(string);
		alert.setHeaderText(null);
		alert.setContentText(string2);
		alert.showAndWait();	
	}


	@FXML
	private void cancel() {
		EditProfileController.changePasswordStage.hide();
	}

}
