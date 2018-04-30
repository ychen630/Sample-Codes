package posts;

/* Post class (model)
 * Created by Yixin Chen on 4/17/2018
 * 
 * 
 */

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.Main;

public class Post {
	private IntegerProperty postID;
	private IntegerProperty userID;
	private StringProperty postContent;
	private StringProperty postTime;
	private StringProperty editTime;
	
	public Post() {
		this.postID = new SimpleIntegerProperty();
		this.userID = new SimpleIntegerProperty();
		this.userID.set(Main.userID);//need to be change according to the login information
		this.postContent = new SimpleStringProperty();
		this.postTime = new SimpleStringProperty();
		this.editTime = new SimpleStringProperty();	
	}
	
	public int getPostID() {
		return postID.get();
	}
	
	public void setPostID(int integer) {
		postID.set(integer);
	}
	
	public IntegerProperty postIDProperty() {
		return postID;
	}
	
	public int getUserID() {
		return userID.get();
	}
	
	public IntegerProperty userIDProperty() {
		return userID;
	}
	
	public String getPostContent() {
		return postContent.get();
	}
	
	public void setPostContent(String str) {
		postContent.set(str);
	}
	
	public StringProperty postContentProperty() {
		return postContent;
	}
	
	public String getPostTime() {
		return postTime.get();
	}
	
	public void setPostTime(String str) {
		postTime.set(str);
	}
	
	public StringProperty postTimeProperty() {
		return postTime;
	}
	
	public String getEditTime() {
		return editTime.get();
	}
	
	public void setEditTime(String str) {
		editTime.set(str);
	}
	
	public StringProperty editTimeProperty() {
		return editTime;
	}
	
}
