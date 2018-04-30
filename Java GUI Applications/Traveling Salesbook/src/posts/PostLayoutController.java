package posts;

/*
 *PostLayoutController Class, controller for Post main view
 *Created by Yixin Chen on 4/12/2018  
 * 
 */

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;

public class PostLayoutController implements Initializable{
	//popup window
	public static Stage addPostStage;
	
	//post content for editing
	public static Text postToEdit;
	
	//index in the ListView of the selected item
	public static int selectedIdx = -1;
	
	//map the ListView index to postTD
	public static Map<Integer, Integer> indexIDMap;
	
	//ListView to display posts
	private static ListView<Text> postList;
	
	@FXML
	private AnchorPane postViewMain;
	
	//go back to main page
	@FXML
	private void cancelListing(MouseEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("../root/RootLayout.fxml"));
		Main.stage.getScene().setRoot(fxml);
	}
	
	//open add post popup window
	@FXML
	private void addPost(MouseEvent event) throws IOException {
		postToEdit = new Text();
		selectedIdx = -1;
		showPopupWindow("file:icons/add.png", "Add A Post");
	}
	
	//open edit post popup window
	@FXML
	private void editPost(MouseEvent event) throws IOException {
		selectedIdx = postList.getSelectionModel().getSelectedIndex();
		if(selectedIdx != -1) {
			postToEdit = postList.getSelectionModel().getSelectedItem();
			showPopupWindow("file:icons/edit.png", "Edit A Post");
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No post selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a post to edit.");
			alert.showAndWait();
		}
		
	}
	
	//delete a post from database and from ListView
	@FXML
	private void removePost(MouseEvent event) throws IOException {
		selectedIdx = postList.getSelectionModel().getSelectedIndex();
		if(selectedIdx != -1) {			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Remove post");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to delete this post?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				int postID = indexIDMap.get(selectedIdx);
				PostDAO.deletePost(postID);
				showPosts();
			} else {
			    alert.close();
			}
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No post selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a post to remove.");
			alert.showAndWait();
		}
	}

	//populate the ListView with all posts
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		postList = new ListView<Text>();
		postList.setLayoutX(19);
		postList.setLayoutY(82);
		postList.setPrefWidth(520);
		postList.setPrefHeight(422);
		postList.setStyle("-fx-font-size: 11pt ;");
		postViewMain.getChildren().add(postList);		
		showPosts();
	}

	//get all posts from database
	private static List<Text> getPosts() {
		List<Text> result = new ArrayList<>();
		ObservableList<Post> postList = PostDAO.searchPosts(String.valueOf(Main.userID));
		indexIDMap = new HashMap<Integer, Integer>();
		int index = 0;
		for(Post post : postList) {
			String postTime = post.getPostTime().substring(0, post.getPostTime().length()-2);
			String content = post.getPostContent();
			String toPost;
			if(post.getEditTime() != null) {
				String editTime = post.getEditTime().substring(0, post.getEditTime().length()-2);
				toPost = "Post on " + postTime + "  Last edit on " + editTime + "\n" + content + "\n";
			}
			else {
				toPost = "Post on " + postTime +  "\n" + content + "\n";
			}
			Text text = new Text(toPost);
			result.add(text);
			indexIDMap.put(index, post.getPostID());
			index++;
		}
		return result;
	}
	
	//display all posts in ListView
	public static void showPosts() {
		postList.getItems().clear();
		List<Text> toPost = getPosts();
		for(Text text : toPost) {
			text.wrappingWidthProperty().bind(postList.widthProperty());
			postList.getItems().add(text);
		}
	}
	
	//show post/edit popup window
	private void showPopupWindow(String icon, String title) {
		addPostStage = new Stage();
		addPostStage.initModality(Modality.APPLICATION_MODAL);		
		try {
			AnchorPane postFXML;
			postFXML = (AnchorPane)FXMLLoader.load(getClass().getResource("AddPostLayout.fxml"));
			Scene scene = new Scene(postFXML,500,350);
			addPostStage.setScene(scene);
			addPostStage.getIcons().add(new Image(icon));
			addPostStage.setResizable(false);
			addPostStage.setTitle(title);
			addPostStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
