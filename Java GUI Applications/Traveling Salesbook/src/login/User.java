package login;
/*
 * model for login, not used in the current version
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
	private IntegerProperty userID;
	private StringProperty username;
	private StringProperty email;
	private StringProperty password;
	
	public User() {
		this.userID = new SimpleIntegerProperty();		
		this.username = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
	}
	
	//userID
	public void setUserID(int num) {
		userID.set(num);
	}
	public int getUserID() {
		return userID.get();
	}
	
	public IntegerProperty userIDProperty() {
		return userID;
	}
	
	//username
	public String getUsername() {
		return username.get();
	}
	
	public void setUsername(String str) {
		username.set(str);
	}
	
	public StringProperty UsernameProperty() {
		return username;
	}
		
	//emal
	public String getEmail() {
		return email.get();
	}
	
	public void setEmail(String str) {
		email.set(str);
	}
	
	public StringProperty emailProperty() {
		return email;
	}
	
	//password
	public String getPassword() {
		return password.get();
	}
	
	public void setPassword(String str) {
		password.set(str);
	}
	
	public StringProperty passwordProperty() {
		return password;
	}

}
