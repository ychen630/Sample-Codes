package friends;
/*
 * Controller for the show friend detail layout
 */
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import posts.Post;
import posts.PostDAO;
import profile.Profile;
import profile.ProfileDAO;
import profile.ProfilePrivacy;

public class ShowFriendsDetailsController implements Initializable{

	@FXML
	private Label username;	
	@FXML
	private Label firstName;
	@FXML
	private Label lastName;
	@FXML
	private Label birthday;
	@FXML
	private Label email;
	@FXML
	private Label phone;
	@FXML
	private Label occupation;
	@FXML
	private Label school;
	@FXML
	private Label status;
	@FXML
	private ListView<Text> postsList;
	
	int friendsID = FriendsLayoutController.indexIDMap.get(FriendsLayoutController.selectedIdx);
	
	//display the profile of the selected friend based on his/her user privacy reference settings 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//get profile data from database
		Profile profile = ProfileDAO.searchProfile(String.valueOf(friendsID));
		//get privacy reference data from database
		ProfilePrivacy profilePrivacy = ProfileDAO.searchProfilePrivacy(String.valueOf(friendsID));
		//display username
		username.setText(profile.getUsername());
		
		//other data from friend's profile
		if(profilePrivacy.getFirstName()) firstName.setText(profile.getFirstName());
		else firstName.setText("Hidded by user");
		if(profilePrivacy.getLastName()) lastName.setText(profile.getLastName());
		else lastName.setText("Hidded by user");
		if(profilePrivacy.getEmail()) email.setText(profile.getEmail());
		else email.setText("Hidded by user");
		if(profilePrivacy.getBirthday()) birthday.setText(profile.getBirthday());
		else birthday.setText("Hidded by user");
		if(profilePrivacy.getPhoneNumber()) phone.setText(profile.getPhoneNumber());
		else phone.setText("Hidded by user");
		if(profilePrivacy.getOccupation()) occupation.setText(profile.getOccupation());
		else occupation.setText("Hidded by user");
		if(profilePrivacy.getSchool()) school.setText(profile.getSchool());
		else school.setText("Hidded by user");
		if(profilePrivacy.getStatus()) status.setText(profile.getStatus());
		else status.setText("Hidded by user");
		if(profilePrivacy.getPosts()) showPosts();
		else postsList.getItems().add(new Text("User chose not to display posts."));
	}
	
	//get all posts from database
	private List<Text> getPosts() {
		List<Text> result = new ArrayList<>();
		ObservableList<Post> postList = PostDAO.searchPosts(String.valueOf(friendsID));
		for(Post post : postList) {
			String postTime = post.getPostTime();
			String content = post.getPostContent();
			String editTime = post.getEditTime();
			String toPost;
			if(editTime != null) {
				toPost = "Post on " + postTime + "  Last edit on " + editTime + "\n" + content + "\n";
			}
			else {
				toPost = "Post on " + postTime +  "\n" + content + "\n";
			}
			Text text = new Text(toPost);
			result.add(text);
		}
		return result;
	}
	
	//display all posts in ListView
	public void showPosts() {
		postsList.getItems().clear();
		List<Text> toPost = getPosts();
		for(Text text : toPost) {
			System.out.println(text);
			text.wrappingWidthProperty().bind(postsList.widthProperty());
			postsList.getItems().add(text);
		}
	}

}
