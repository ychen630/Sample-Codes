package friends;
/*
 * Controller for the main friends layout
 */
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import main.Main;
import profile.Profile;

public class FriendsLayoutController implements Initializable{
	
	public static Stage addFriendStage; //add friends popup window
	public static Stage showFriendDetails; // show friend details popup window
	
	@FXML
	private AnchorPane friendsListPane;
	
	//tableview and columns for listing friends
	private static TableView<Profile> friendsListTable;
	
	private static TableColumn<Profile, String> usernameCol;
	private static TableColumn<Profile, String> firstNameCol;
	private static TableColumn<Profile, String> lastNameCol;
	
	
	//index in the ListView of the selected item
	public static int selectedIdx = -1;
	
	//map the ListView index to postTD
	public static Map<Integer, Integer> indexIDMap;
	
	//go back to welcome page
	@FXML
	private void cancelListing(MouseEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("../root/RootLayout.fxml"));
		Main.stage.getScene().setRoot(fxml);
	}
	
	//load add friends layout
	@FXML
	private void addFriend(MouseEvent event) throws IOException {
		addFriendStage = new Stage();
		addFriendStage.initModality(Modality.APPLICATION_MODAL);
		
		AnchorPane addFriendFXML = (AnchorPane)FXMLLoader.load(getClass().getResource("AddFriendsLayout.fxml"));
		Scene scene = new Scene(addFriendFXML,500,350);
		addFriendStage.setScene(scene);
		addFriendStage.getIcons().add(new Image("file:icons/add.png"));
		addFriendStage.setResizable(false);
		addFriendStage.setTitle("Add A Friend");
		addFriendStage.show();
	}
	
	//load show friend detail layout
	@FXML
	private void showDetails(MouseEvent event) throws IOException {
		listFriendsDetails();
	}
	
	//delete a selected friend
	@FXML
	private void removeFriend(MouseEvent event) throws IOException {
		//get the index in the tableview
		selectedIdx = friendsListTable.getSelectionModel().getSelectedIndex();
		if(selectedIdx != -1) {			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Remove friend");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to delete this friend?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				int friendsID = indexIDMap.get(selectedIdx);
				FriendsDAO.deleteFriend(Main.userID, friendsID);
				showFriends();
			} else {
			    alert.close();
			}
		}
		//nothing selected
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No friend selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a friend to remove.");
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//list friends in tableview
		addFriendsTable();
		showFriends();
		//listener for click on a friend, popup the details window
		friendsListTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue) -> {
	            	if(newValue != null) {
	            		try {
							listFriendsDetails();
						} catch (IOException e) {
							e.printStackTrace();
						}
	            	}
	            });
	}

	//set the tableview and table columns and add them to main anchorpane
	private void addFriendsTable() {
		friendsListTable = new TableView<Profile>();
		friendsListTable.setStyle("-fx-font-size: 14px ;");
		usernameCol = new TableColumn<Profile, String>("Username");
		firstNameCol = new TableColumn<Profile, String>("First Name");
		lastNameCol = new TableColumn<Profile, String>("Last Name");
		
		friendsListTable.setLayoutX(115);
		friendsListTable.setLayoutY(150);
		friendsListTable.setPrefHeight(373);
		friendsListTable.setPrefWidth(408);
		//Columns take up all available width of the TableView
		friendsListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		usernameCol.setStyle("-fx-alignment: CENTER;");
		firstNameCol.setStyle("-fx-alignment: CENTER;");
		lastNameCol.setStyle("-fx-alignment: CENTER;");
		
		friendsListTable.getColumns().addAll(usernameCol, firstNameCol, lastNameCol);
		friendsListPane.getChildren().add(friendsListTable);
		AnchorPane.setTopAnchor(friendsListTable, 100.0);
		AnchorPane.setBottomAnchor(friendsListTable, 100.0);
		AnchorPane.setLeftAnchor(friendsListTable, 74.5);
		AnchorPane.setRightAnchor(friendsListTable, 74.5);
	}
	
	//get friends data from database, and display the data in tableview
	public static void showFriends() {
		ObservableList<Profile> friendsList = FriendsDAO.searchFriends(String.valueOf(Main.userID));
		usernameCol.setCellValueFactory(new PropertyValueFactory<Profile, String> ("username"));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Profile, String> ("firstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Profile, String> ("lastName"));
		friendsListTable.setItems(friendsList);
		
		//map tableview index to userID of friends
		indexIDMap = new HashMap<Integer, Integer>();
		int index = 0;
		for(Profile profile: friendsList ) {
			indexIDMap.put(index, profile.getUserID());
			index++;
		}
	}
	
	//show friend details
	private void listFriendsDetails() throws IOException {
		selectedIdx = friendsListTable.getSelectionModel().getSelectedIndex();
		if(selectedIdx != -1) {
			showFriendDetails = new Stage();
			showFriendDetails.initModality(Modality.APPLICATION_MODAL);
			
			AnchorPane friendsDetailsFXML = (AnchorPane)FXMLLoader.load(getClass().getResource("ShowFriendsDetailsLayout.fxml"));
			Scene scene = new Scene(friendsDetailsFXML,400,600);
			showFriendDetails.setScene(scene);
			showFriendDetails.getIcons().add(new Image("file:icons/friends.png"));
			showFriendDetails.setResizable(false);
			showFriendDetails.setTitle("Friend's Profile");
			showFriendDetails.show();
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No post selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a post to edit.");
			alert.showAndWait();
		}
	}
	
}
