package posts;

/*
 *AddPostLayoutController Class, controller for add/edit Post view
 *Created by Yixin Chen on 4/12/2018  
 * 
 */


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import main.Main;

public class AddPostLayoutController implements Initializable{
	
	@FXML
	private TextArea postContent;
	
	private String newPost;
	
	//clear textfield
	@FXML
	private void clear() {
		postContent.clear();
	}
	
	//publish or edit a post
	@FXML
	private void post() {
		newPost = postContent.getText();
		String currentTime = getCurrentTime();
		//edit a post, and update the database
		if(PostLayoutController.selectedIdx != -1) {
			int postID = PostLayoutController.indexIDMap.get(PostLayoutController.selectedIdx);
			PostDAO.editPost(postID, newPost, currentTime);
		}
		//add a new post to database
		else {
			PostDAO.addPost(newPost, currentTime, Main.userID);
		}
		clear();
		//close the popup window
		PostLayoutController.addPostStage.hide();
		//list posts in main window
		PostLayoutController.showPosts();
	}
	
	//cancel publish or edit a post
	@FXML
	private void cancel() {
		clear();
		PostLayoutController.addPostStage.hide();
	}
	
	//initialize the popup window. 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		postContent.setWrapText(true);
		//in edit, display the original post in the popup window
		if(PostLayoutController.postToEdit != null) {
			String str = PostLayoutController.postToEdit.getText().trim();
			int index = str.indexOf("\n");
			if(index == -1) postContent.setText(str);
			else postContent.setText(str.substring(index + 1));
		}
	}
	
	//get current time in YYYY-MM-dd HH:mm:ss format
	public static String getCurrentTime() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
}
