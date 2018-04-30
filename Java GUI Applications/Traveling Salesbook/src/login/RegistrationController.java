package login;
/*
 * Controller for registration view
 * 
 */
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import main.Main;
import profile.Profile;
import static util.DataUtil.isValidEmailAddress;
import static util.DataUtil.returnHash;

public class RegistrationController implements Initializable{

	@FXML
	private TextField usernameField, firstNameField, lastNameField, emailField, answerField;
	@FXML
	private DatePicker DOBField;
	@FXML
	private PasswordField passField, confirmPassField;
	@FXML
	private ComboBox<String> security;
	
	String username, firstName, lastName, email, password, confirmPassword, securityQuestion, securityAnswer, birthday;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		security.getItems().setAll("What was the name of your first pet?", "In what city were you born?");
		setDOBField();

		answerField.setOnAction(event -> {
			try {
				register();
			}
			catch (IOException | SQLException ex) {
				System.err.println("Something went wrong setting the Action event for the answer field!");
				ex.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}
	
	//already have an account, open login view
	@FXML
	private void openLogin(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Main.stage.getScene().setRoot(root);
	}
	
	//process registration
	@FXML
	private void register() throws IOException, SQLException, ClassNotFoundException {
		//grap user inputs
		username = usernameField.getText().toLowerCase();
		firstName = firstNameField.getText();
		lastName = lastNameField.getText();
		email = emailField.getText().toLowerCase();
		password = passField.getText();
		confirmPassword = confirmPassField.getText();
		securityQuestion = security.getValue();
		securityAnswer = answerField.getText().toLowerCase();
//		System.out.println(DOBField.getValue());
		if(DOBField.getValue() != null)
			birthday = DOBField.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE);
		else birthday = "";
		if(!validateInput()) return;
		
		//set a Profile object
		Profile profile = new Profile();
		profile.setUsername(username);
		profile.setFirstName(firstName);
		profile.setLastName(lastName);
		profile.setEmail(email);
		profile.setBirthday(birthday);
		profile.setSecurityQuestion(securityQuestion);
		profile.setSecurityAnswer(securityAnswer);
		
		//password
		String hashedPass = returnHash(password);
		
		//store data to database and get the userID 
		int userID = LoginDAO.processRegistration(profile, hashedPass);
		//registration successed
		if(userID != 0) {
			Main.userID = userID;
			profile.setUserID(userID);
			showInformAlert("Registration Succeeded!", "You've successfully been registered in the Salesbook.");
			Parent fxml = FXMLLoader.load(getClass().getResource("../root/RootLayout.fxml"));
			Main.stage.getScene().setRoot(fxml);
		}
		//registration failed
		else {
			showAlert("Registration Failed", "Uh oh! It seems we already have that user in our database. Please try again.");
		}		
	}

	//show error alert
	private void showAlert(String string, String string2) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(string);
		alert.setHeaderText(null);
		alert.setContentText(string2);
		alert.showAndWait();
	}
	
	//show information alert
	private void showInformAlert(String string, String string2) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(string);
		alert.setHeaderText(null);
		alert.setContentText(string2);
		alert.showAndWait();
	}
	
	//validate user inputs
	private boolean validateInput() {
		if(username.length() == 0) {
			showAlert("Input error", "Please input a username!");
			return false;
		}
		
		if(email.length() == 0) {
			showAlert("Input error", "Please input an Email address!");
			return false;
		}
		
		if(!isValidEmailAddress(email)) {
			showAlert("Input error", "Please input a valid Email address!");
			return false;
		}
		
		if(password.length() == 0) {
			showAlert("Input error", "Please input a password!");
			return false;
		}
		
		if(securityQuestion == null) {
			showAlert("Input error", "Please select a security question!");
			return false;
		}
		
		if(securityAnswer.length() == 0) {
			showAlert("Input error", "Please input a security answer!");
			return false;
		}

		if (!password.equals(confirmPassword)) { // if pass does not match confirmation
			// passwords did not match, popup an alert window
			showAlert("Password Mismatch", "Your passwords did not match! Please correct this and try again.");
			return false;
		}
		
		return true;
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
        DOBField.setConverter(converter);
        DOBField.setStyle("-fx-font-size: 14px ;");
	}
}
