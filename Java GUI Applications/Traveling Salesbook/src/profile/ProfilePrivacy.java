package profile;
/*
 * model of profile privacy
 * 
 */
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import main.Main;

public class ProfilePrivacy {
	private IntegerProperty userID;
	private BooleanProperty firstName;
	private BooleanProperty lastName;
	private BooleanProperty email;
	private BooleanProperty birthday;
	private BooleanProperty phoneNumber;
	private BooleanProperty occupation;
	private BooleanProperty school;
	private BooleanProperty status;
	
	private BooleanProperty posts;
	private BooleanProperty friends;
	
	public ProfilePrivacy() {
		//set userID
		this.userID = new SimpleIntegerProperty();
		this.userID.set(Main.userID);

		this.firstName = new SimpleBooleanProperty();
		this.lastName = new SimpleBooleanProperty();
		this.email = new SimpleBooleanProperty();
		this.birthday = new SimpleBooleanProperty();
		this.phoneNumber = new SimpleBooleanProperty();
		this.occupation = new SimpleBooleanProperty();
		this.school = new SimpleBooleanProperty();
		this.status = new SimpleBooleanProperty();
		this.posts = new SimpleBooleanProperty();
		this.friends = new SimpleBooleanProperty();
	}
	
	//userID
	public int getUserID() {
		return userID.get();
	}
	
	public IntegerProperty userIDProperty() {
		return userID;
	}
	
	//first name
	public boolean getFirstName() {
		return firstName.get();
	}
	
	public void setFirstName(boolean bool) {
		firstName.set(bool);
	}
	
	public BooleanProperty firstNameProperty() {
		return firstName;
	}
	
	//last name
	public boolean getLastName() {
		return lastName.get();
	}
	
	public void setLastName(boolean bool) {
		lastName.set(bool);
	}
	
	public BooleanProperty lastNameProperty() {
		return lastName;
	}	
	
	//emal
	public boolean getEmail() {
		return email.get();
	}
	
	public void setEmail(boolean bool) {
		email.set(bool);
	}
	
	public BooleanProperty emailProperty() {
		return email;
	}
	
	//birthday
	public boolean getBirthday() {
		return birthday.get();
	}
	
	public void setBirthday(boolean bool) {
		birthday.set(bool);
	}
	
	public BooleanProperty birthdayProperty() {
		return birthday;
	}
	
	//phone boolber
	public boolean getPhoneNumber() {
		return phoneNumber.get();
	}
	
	public void setPhoneNumber(boolean bool) {
		phoneNumber.set(bool);
	}
	
	public BooleanProperty phoneNumberProperty() {
		return phoneNumber;
	}
	
	//occupation
	public boolean getOccupation() {
		return occupation.get();
	}
	
	public void setOccupation(boolean bool) {
		occupation.set(bool);
	}
	
	public BooleanProperty occupationProperty() {
		return occupation;
	}
	
	//school
	public boolean getSchool() {
		return school.get();
	}
	
	public void setSchool(boolean bool) {
		school.set(bool);
	}
	
	public BooleanProperty schoolProperty() {
		return school;
	}
	
	//status
	public boolean getStatus() {
		return status.get();
	}
	
	public void setStatus(boolean bool) {
		status.set(bool);
	}
	
	public BooleanProperty statusProperty() {
		return status;
	}
	
	//posts
	public boolean getPosts() {
		return posts.get();
	}
	
	public void setPosts(boolean bool) {
		posts.set(bool);
	}
	
	public BooleanProperty PostsProperty() {
		return posts;
	}
	
	//status
	public boolean getFriends() {
		return friends.get();
	}
	
	public void setFriends(boolean bool) {
		friends.set(bool);
	}
	
	public BooleanProperty friendsProperty() {
		return friends;
	}
}
