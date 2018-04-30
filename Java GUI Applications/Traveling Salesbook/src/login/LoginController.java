package login;
/*
 * Controller for login view
 * 
 */
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import main.Main;
import static util.DataUtil.returnHash;

public class LoginController implements Initializable{
	public static Stage forgetPasswordStage;
	
	@FXML
	public PasswordField pwBox;
	@FXML
	public Button btnLogin;
	@FXML
	private Pane content_area;
	@FXML
	private TextField username;
	
	public String hashedPass = new String();

	final BooleanProperty firstTime = new SimpleBooleanProperty(true); 
	
	//open registration view
	@FXML
	private void openReg(MouseEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("Registration.fxml"));
		content_area.getChildren().removeAll();
		content_area.getChildren().setAll(fxml);
	}

	@FXML
	private void passEntered() {
		btnLogin.fire();
	}
	
	//check inputs and process login
	@FXML
	private void login() throws IOException{
		if(username.getText().length() == 0) showAlert("Input error", "Please input a username or Email address.");
		else if(pwBox.getText().length() == 0) showAlert("Input error", "Please input password.");
		else {
			// grab the hashed password.
			hashedPass = returnHash(pwBox.getText());
			Main.userID = LoginDAO.processLogin(username.getText().toLowerCase(), hashedPass);
			if(Main.userID != 0) {
				// log success!
				Parent fxml = FXMLLoader.load(getClass().getResource("../root/RootLayout.fxml"));
				Main.stage.getScene().setRoot(fxml);
			}
			else { // empty result set.
				System.err.println("Couldn't find a match for your login info!");
				// username or password not match, popup an alert window
				showAlert("Login Failed", "Username/Email or password not match.");
			}
		}
	}
	
	//open forget password view
	@FXML
	private void forgetPassword() throws IOException{
		String usernameInput = username.getText().toLowerCase();
		if(usernameInput.length() == 0) {
			showAlert("Input error", "Please input a username or Email.");
		}
		else {		
			if(validUsername(usernameInput)) {
				forgetPasswordStage = new Stage();
				forgetPasswordStage.initModality(Modality.APPLICATION_MODAL);
				
				AnchorPane changePasswordFXML = (AnchorPane)FXMLLoader.load(getClass().getResource("forgetPasswordLayout.fxml"));
				Scene scene = new Scene(changePasswordFXML,250,270);
		
				forgetPasswordStage.setScene(scene);
				forgetPasswordStage.getIcons().add(new Image("file:icons/forgetPassword.png"));
				forgetPasswordStage.setResizable(false);
				forgetPasswordStage.setTitle("Forget Password");
				forgetPasswordStage.show();
			}			
			else {
				showAlert("Input error", "Username or Email address does not exist. Please try again.");
			}
		}
	}
	
	//check is username is valid
	private boolean validUsername(String usernameInput) {
		Main.userID = LoginDAO.validUser(usernameInput);
		if(Main.userID == 0) return false;
		else return true;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//not focus on the username textField on start
        username.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                content_area.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
        
        //set event listener 
		btnLogin.setOnAction(event -> {
			try {
				login();
			}
			catch (IOException ex) {
				System.err.println("We encountered an error parsing input/output. This is a serious problem... Please contact your nearest salesman.");
			}
		});

	}
	
	//show error alert
	private void showAlert(String str1, String str2) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(str1);
		alert.setHeaderText(null);
		alert.setContentText(str2);
		alert.showAndWait();
	}

}
