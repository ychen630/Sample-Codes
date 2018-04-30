package friends;
/*
 * 
 * Controller for add friend layout
 * 
 */
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.Main;
import profile.Profile;

public class AddFriendsLayoutController implements Initializable{

	@FXML
	TextField firstName;
	@FXML
	TextField lastName;
	@FXML
	TextField username;
	@FXML
	AnchorPane addFriendsSearchArea;
	
	@FXML
	TextArea searchResultLog;  
	
	@FXML
	private TableView<Profile> friendsListTable;
	
	@FXML
	private TableColumn<Profile, String> usernameCol;
	@FXML
	private TableColumn<Profile, String> firstNameCol;
	@FXML
	private TableColumn<Profile, String> lastNameCol;
	
	//index in the ListView of the selected item
	public static int selectedIdx = -1;
	
	//map the ListView index to friend's userTD
	public static Map<Integer, Integer> indexIDMap;
	
	//change focus on start
	final BooleanProperty firstTime = new SimpleBooleanProperty(true); 
	
	
	//clear search fields
	@FXML
	private void clearSearchFields() {
		firstName.clear();
		lastName.clear();
		username.clear();
	}
	
	//search for friends
	@FXML 
	private void searchForFriends() {
		showFriends();
	}
	
	//add the selected friend to friend list and store the data to database
	@FXML
	private void addFriendsToList() {
		//index of the selected friend in the table view
		selectedIdx = friendsListTable.getSelectionModel().getSelectedIndex();
		//if a row is selected in the table view
		if(selectedIdx != -1) {	
			//get the userID of the selected friend
			int friendsID = indexIDMap.get(selectedIdx);
			//check if the selected friend is already in the list
			if(FriendsDAO.checkDuplicate(Main.userID, friendsID)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Add Friend");
				alert.setHeaderText(null);
				alert.setContentText("Friend already in list.");
				alert.showAndWait();				
			}
			//add friend to list (database)
			else {
				FriendsDAO.addFriend(Main.userID, friendsID);
				FriendsLayoutController.showFriends();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Add Friend");
				alert.setHeaderText(null);
				alert.setContentText("Successfully add selected friend.");
				alert.showAndWait();
			}
			
		}
		//nothing selected, popup an information window
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No friend selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a friend to remove.");
			alert.showAndWait();
		}
	}
	
	//cancel adding friend, close the window
	@FXML
	private void cancel() {
		clearSearchFields();
		//clear table view
		FriendsLayoutController.addFriendStage.hide();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//not focus on the username textField on start
		username.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
            	addFriendsSearchArea.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
		//set text wrap for the search result log
		searchResultLog.setWrapText(true);
		//set alignment to Center for the table view columns
		usernameCol.setStyle("-fx-alignment: CENTER;");
		firstNameCol.setStyle("-fx-alignment: CENTER;");
		lastNameCol.setStyle("-fx-alignment: CENTER;");
		
	}
	
	//search friends in database based on user input and display search result
	private void showFriends() {
		ObservableList<Profile> friendsList = FriendsDAO.searchFriendsToAdd(firstName.getText(), lastName.getText(), username.getText());
		if(friendsList.size() == 0){
			searchResultLog.setText("No record found. Please try again.");
		}
		else {
			usernameCol.setCellValueFactory(new PropertyValueFactory<Profile, String> ("username"));
			firstNameCol.setCellValueFactory(new PropertyValueFactory<Profile, String> ("firstName"));
			lastNameCol.setCellValueFactory(new PropertyValueFactory<Profile, String> ("lastName"));
			friendsListTable.setItems(friendsList);
			if(friendsList.size() == 1) searchResultLog.setText(friendsList.size() + " record found."); 
			else searchResultLog.setText(friendsList.size() + " records found."); 
			
			//map tableview index to userID of friends
			indexIDMap = new HashMap<Integer, Integer>();
			int index = 0;
			for(Profile profile: friendsList ) {
				indexIDMap.put(index, profile.getUserID());
				index++;
			}
		}
	}
	
}
