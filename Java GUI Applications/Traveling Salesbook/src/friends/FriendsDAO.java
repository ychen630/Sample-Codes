package friends;
/*
 * friends data access object
 * communicate to database
 * 
 */
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;
import profile.Profile;
import util.DBUtil;

public class FriendsDAO {
	//select all visible friends
	public static ObservableList<Profile> searchFriends(String userID){
		String selectStmt = "SELECT * FROM userProfiles \n"
				+ "INNER JOIN friendsRelation ON userProfiles.userID = friendsRelation.FriendsID \n"
				+ "INNER JOIN userPrivacyPreferences ON friendsRelation.FriendsID = userPrivacyPreferences.userID \n"
				+ "WHERE friendsRelation.userID = " + userID
				+ " AND userPrivacyPreferences.showFriends = 1; \n"; 
		try {
			ResultSet rsPosts;
			rsPosts = DBUtil.dbExecuteQuery(selectStmt);
			ObservableList<Profile> friendsList = getFriendsList(rsPosts);
			return friendsList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Store profile data from database in an ObservableList
	private static ObservableList<Profile> getFriendsList(ResultSet rs) {
		ObservableList<Profile> friendsList = FXCollections.observableArrayList();
		try {
			while(rs.next()) {
				Profile profile = new Profile();
				profile.setUserID(rs.getInt("friendsID"));
				profile.setUsername(rs.getString("username"));
				profile.setFirstName(rs.getString("FirstName"));
				profile.setLastName(rs.getString("LastName"));
				profile.setEmail(rs.getString("email"));
				profile.setBirthday(rs.getString("DOB"));
				profile.setPhoneNumber(rs.getString("PhoneNumber"));
				profile.setOccupation(rs.getString("Occupation"));
				profile.setSchool(rs.getString("School"));
				profile.setStatus(rs.getString("Status"));
				friendsList.add(profile);
			}
			return friendsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//delete a selected friend using current userID and friendID
	public static void deleteFriend(int userID, int friendID) {
		String updateStmt = "DELETE FROM friendsRelation\n" +
                        "WHERE userID ="+ userID + " AND friendsID ="+ friendID + ";\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	//search users based on user input firstName or lastName or username 
	public static ObservableList<Profile> searchFriendsToAdd(String firstName,String lastName, String username){
		String selectStmt;
		//no input, list all visible user records except the current user
		if(firstName.length() == 0 && lastName.length() == 0 && username.length() == 0) {
			selectStmt = "SELECT * FROM userProfiles \n"
					+ "INNER JOIN userPrivacyPreferences ON userProfiles.userID = userPrivacyPreferences.userID "
					+ "WHERE userProfiles.userID <> " + Main.userID 
					+ " AND userPrivacyPreferences.showFriends = 1\n"; 
		}
		//list user records which has at least one match field as user input
		else{
			selectStmt = "SELECT * FROM userProfiles \n"
					+ "INNER JOIN userPrivacyPreferences ON userProfiles.userID = userPrivacyPreferences.userID "
					+ "WHERE userProfiles.firstName = '" + firstName 
					+ "' OR userProfiles.lastName = '" + lastName
					+ "' OR userProfiles.username = '" + username
					+ "' AND userProfiles.userID <> " + Main.userID 
					+ " AND userPrivacyPreferences.showFriends = 1\n"; 
		}
		try {
			ResultSet rsPosts;
			rsPosts = DBUtil.dbExecuteQuery(selectStmt);
			ObservableList<Profile> friendsList = getFriendsListToAdd(rsPosts);
			return friendsList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Store profile data from database in an ObservableList
	private static ObservableList<Profile> getFriendsListToAdd(ResultSet rs) {
		ObservableList<Profile> friendsList = FXCollections.observableArrayList();
		try {
			while(rs.next()) {
				Profile profile = new Profile();
				profile.setUserID(rs.getInt("userID"));
				profile.setUsername(rs.getString("username"));
				profile.setFirstName(rs.getString("FirstName"));
				profile.setLastName(rs.getString("LastName"));
				profile.setEmail(rs.getString("email"));
				profile.setBirthday(rs.getString("DOB"));
				profile.setPhoneNumber(rs.getString("PhoneNumber"));
				profile.setOccupation(rs.getString("Occupation"));
				profile.setSchool(rs.getString("School"));
				profile.setStatus(rs.getString("Status"));
				friendsList.add(profile);
			}
			return friendsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//add the selected friend to list (database)
	public static void addFriend(int userID, int friendID) {
		String updateStmt = "INSERT INTO friendsRelation (userID, friendsID)\n" +
                        "VALUES\n"
                        + "(" + userID + ", " + friendID + ")\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	//check if the selected friend is already in the friend list
	public static boolean checkDuplicate(int userID, int friendID) {
		String selectStmt = "SELECT * FROM friendsRelation\n" +
                "WHERE userID ="+ userID + " AND friendsID ="+ friendID + ";\n";
		try {
			ResultSet rsPosts;
			rsPosts = DBUtil.dbExecuteQuery(selectStmt);
			if(rsPosts.next()) return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
