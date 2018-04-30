package profile;
/*
 * 
 * Model of Profile
 * 
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.Main;

public class Profile {
	private IntegerProperty userID;
	private StringProperty username;
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty email;
	private StringProperty birthday;
	private StringProperty securityQuestion;
	private StringProperty securityAnswer;
	private StringProperty phoneNumber;
	private StringProperty occupation;
	private StringProperty school;
	private StringProperty status;
	
	public Profile() {
		this.userID = new SimpleIntegerProperty();
		this.userID.set(Main.userID);
		
		this.username = new SimpleStringProperty();
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.birthday = new SimpleStringProperty();
		this.securityQuestion = new SimpleStringProperty();
		this.securityAnswer = new SimpleStringProperty();
		this.phoneNumber = new SimpleStringProperty();
		this.occupation = new SimpleStringProperty();
		this.school = new SimpleStringProperty();
		this.status = new SimpleStringProperty();
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
	
	//first name
	public String getFirstName() {
		return firstName.get();
	}
	
	public void setFirstName(String str) {
		firstName.set(str);
	}
	
	public StringProperty firstNameProperty() {
		return firstName;
	}
	
	//last name
	public String getLastName() {
		return lastName.get();
	}
	
	public void setLastName(String str) {
		lastName.set(str);
	}
	
	public StringProperty lastNameProperty() {
		return lastName;
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
	
	//birthday
	public String getBirthday() {
		return birthday.get();
	}
	
	public void setBirthday(String str) {
		birthday.set(str);
	}
	
	public StringProperty birthdayProperty() {
		return birthday;
	}
	
	//security question
	public String getSecurityQuestion() {
		return securityQuestion.get();
	}
	
	public void setSecurityQuestion(String str) {
		securityQuestion.set(str);
	}
	
	public StringProperty securityQuestionProperty() {
		return securityQuestion;
	}
	
	//security answer
	public String getSecurityAnswer() {
		return securityAnswer.get();
	}
	
	public void setSecurityAnswer(String str) {
		securityAnswer.set(str);
	}
	
	public StringProperty securityAnswerProperty() {
		return securityAnswer;
	}
	
	//phone number
	public String getPhoneNumber() {
		return phoneNumber.get();
	}
	
	public void setPhoneNumber(String str) {
		phoneNumber.set(str);
	}
	
	public StringProperty phoneNumberProperty() {
		return phoneNumber;
	}
	
	//occupation
	public String getOccupation() {
		return occupation.get();
	}
	
	public void setOccupation(String str) {
		occupation.set(str);
	}
	
	public StringProperty occupationProperty() {
		return occupation;
	}
	
	//school
	public String getSchool() {
		return school.get();
	}
	
	public void setSchool(String str) {
		school.set(str);
	}
	
	public StringProperty schoolProperty() {
		return school;
	}
	
	//status
	public String getStatus() {
		return status.get();
	}
	
	public void setStatus(String str) {
		status.set(str);
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
}
