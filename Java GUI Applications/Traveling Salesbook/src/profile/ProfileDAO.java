package profile;
/*
 * Profile database access object
 * 
 */
import java.sql.ResultSet;
import java.sql.SQLException;

import main.Main;
import util.DBUtil;
import static util.DataUtil.boolToInt;

public class ProfileDAO {
	//select user profile
	public static Profile searchProfile(String userID){
		String selectStmt = "SELECT * FROM userProfiles WHERE userID=" + userID;
		try {
			ResultSet rsProfile;
			rsProfile = DBUtil.dbExecuteQuery(selectStmt);
			Profile profile = getProfile(rsProfile);
			return profile;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Store profile data from database in an Profile object
	private static Profile getProfile(ResultSet rs) {
		Profile profile = new Profile();
		try {
			if(rs.next()) {
				profile.setUsername(rs.getString("username"));
				profile.setFirstName(validateField(rs.getString("FirstName")));
				profile.setLastName(validateField(rs.getString("LastName")));
				profile.setEmail(rs.getString("email"));
				profile.setBirthday(validateField(rs.getString("DOB")));
				profile.setSecurityQuestion(rs.getString("SecurityQuestion"));
				profile.setSecurityAnswer(rs.getString("SecurityAnswer"));
				profile.setPhoneNumber(validateField(rs.getString("PhoneNumber")));
				profile.setOccupation(validateField(rs.getString("Occupation")));
				profile.setSchool(validateField(rs.getString("School")));
				profile.setStatus(validateField(rs.getString("Status")));
			}
			return profile;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	//select all privacy
	public static ProfilePrivacy searchProfilePrivacy(String userID){
		String selectStmt = "SELECT * FROM userPrivacyPreferences WHERE userID=" + userID;
		try {
			ResultSet rsPrivacy;
			rsPrivacy = DBUtil.dbExecuteQuery(selectStmt);
			ProfilePrivacy profilePrivacy = getProfilePrivacy(rsPrivacy);
			return profilePrivacy;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Store user privacy reference data from database in an ProfilePrivacy object
	private static ProfilePrivacy getProfilePrivacy(ResultSet rs) {
		ProfilePrivacy profilePrivacy = new ProfilePrivacy();
		try {
			if(rs.next()) {
				profilePrivacy.setFirstName(rs.getBoolean("showFirstName"));
				profilePrivacy.setLastName(rs.getBoolean("showLastName"));
				profilePrivacy.setEmail(rs.getBoolean("showEmail"));
				profilePrivacy.setBirthday(rs.getBoolean("showBirthday"));
				profilePrivacy.setPhoneNumber(rs.getBoolean("showPhoneNumber"));
				profilePrivacy.setOccupation(rs.getBoolean("showOccupation"));
				profilePrivacy.setSchool(rs.getBoolean("showSchool"));
				profilePrivacy.setStatus(rs.getBoolean("showStatus"));
				profilePrivacy.setPosts(rs.getBoolean("showPosts"));
				profilePrivacy.setFriends(rs.getBoolean("showFriends"));
			}
			return profilePrivacy;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	//edit user profile
	public static void editProfile(Profile profile) {
		String updateStmt = 
				"UPDATE userProfiles\n" +
						"SET username = '" + profile.getUsername() 
						+"', FirstName = '" + profile.getFirstName()
						+"', LastName = '" + profile.getLastName() 
						+"', Email = '" + profile.getEmail()
						+"', DOB = '" + profile.getBirthday()
						+"', PhoneNumber = '" + profile.getPhoneNumber()
						+"', Occupation = '" + profile.getOccupation()
						+"', School = '" + profile.getSchool()
						+"', Status = '" + profile.getStatus()
						+ "'\n"
						+ "WHERE userID = " + profile.getUserID() +";\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	//edit user privacy reference (hide or show)
	public static void editUserPrivacy(ProfilePrivacy profilePrivacy) {
		String updateStmt = 
				"UPDATE userPrivacyPreferences\n" +
						"SET showPosts = '" +  boolToInt(profilePrivacy.getPosts()) 
						+"', showFirstName = '" +  boolToInt(profilePrivacy.getFirstName())
						+"', showLastName = '" +  boolToInt(profilePrivacy.getLastName()) 
						+"', showEmail = '" +  boolToInt(profilePrivacy.getEmail())
						+"', showBirthday = '" +  boolToInt(profilePrivacy.getBirthday())
						+"', showPhoneNumber = '" +  boolToInt(profilePrivacy.getPhoneNumber())
						+"', showOccupation = '" +  boolToInt(profilePrivacy.getOccupation())
						+"', showSchool = '" +  boolToInt(profilePrivacy.getSchool())
						+"', showStatus = '" +  boolToInt(profilePrivacy.getStatus())
						+"', showFriends = '" +  boolToInt(profilePrivacy.getFriends())
						+ "'\n"
						+ "WHERE userID = " + profilePrivacy.getUserID() +";\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	//check the current password user input
	public static int checkUser(int userID, String hashedPass) {
		int res = 0;
		String Sql = "SELECT * FROM users WHERE userID = '" + Main.userID + "' AND password = '" + hashedPass +"'";
		ResultSet rs;
		try {
			rs = DBUtil.dbExecuteQuery(Sql);
			if(rs.next()) {
				res = rs.getInt("userID");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return res;		
	}
	
	//change password
	public static void updatePassword(int userID, String hashedPass) {
		String updateStmt = 
				"UPDATE users\n" +
						"SET password = '" + hashedPass + "'\n"
						+ "WHERE userID = " + userID +";\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	//change username if applicable
	public static void editUser(Profile profile) {
		String updateStmt = 
				"UPDATE users\n" +
						"SET username = '" + profile.getUsername() 
						+ "'\n"
						+ "WHERE userID = " + profile.getUserID() +";\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}

	//check if the username or email user input already exists in database
	public static boolean checkUsernameAndEmail(String str, String str2) {
		boolean res = false;
		String selectStmt = "SELECT * FROM userProfiles"
							+ " WHERE (username = '" + str + "' OR Email = '" + str2 + "') "
							+ "AND userID <> " + Main.userID + ";";
		try {
			ResultSet rs;
			rs = DBUtil.dbExecuteQuery(selectStmt);
			if(rs.next()) res = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//if some field in userProfile is null, return empty string
	private static String validateField(String str){
		String res = "";
		if(str != null) res = str;
		return res;
	}

}
