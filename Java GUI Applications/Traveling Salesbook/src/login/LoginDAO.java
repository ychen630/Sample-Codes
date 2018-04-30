package login;
/*
 * Login data access object
 * Process query to database for login, registration and forget password views
 */

import java.sql.ResultSet;
import java.sql.SQLException;

import profile.Profile;
import util.DBUtil;

public class LoginDAO {
	
	//process login
	public static int processLogin(String username, String password) {		
		int res = 0;
		String Sql = "SELECT * FROM users WHERE (email = '" + username
		+ "' OR username = '" + username + "') AND password = '" + password +"'";
			
		ResultSet rs;
		try {
			rs = DBUtil.dbExecuteQuery(Sql);
				
			//if rs has next, then there is a match, process login and store the userID of current user to Main.userID
			if (rs.next()) {
				//get userID
				res = rs.getInt("userID");
				System.out.println("Your query returned a match!");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//valid username
	public static int validUser(String username) {
		int res = 0;
		String Sql = "SELECT * FROM users WHERE (email = '" + username + "' OR username = '" + username + "') ";	
		ResultSet rs;
		try {
			rs = DBUtil.dbExecuteQuery(Sql);	
			//if rs has next, then there is a match, process login and store the userID of current user to Main.userID
			if (rs.next()) {
				//get userID
				res = rs.getInt("userID");
				System.out.println("Your query returned a match!");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//update password
	public static void updatePassword(int userID, String hashedPass) {
		String updateStmt = 
				"UPDATE users\n" +
						"SET password = '" + hashedPass + "'\n"
						+ "WHERE userID = " + userID +";\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//process registration
	public static int processRegistration(Profile profile, String hashedPass) {
		int res = 0;
		String sqlStatement = String.format("exec RegisterNewUser2 '%s','%s','%s','%s','%s','%s','%s','%s';",
				profile.getUsername(), hashedPass, profile.getFirstName(), profile.getLastName(), profile.getEmail(), 
				profile.getBirthday(), profile.getSecurityQuestion(), profile.getSecurityAnswer());
		ResultSet rs;
		try {
			rs = DBUtil.dbExecuteQuery(sqlStatement);

			if (rs.isBeforeFirst()) { // if the result set was not empty...
				rs.next();	// grab first row of set.
				if (rs.getString("Response").contains("Succ")) {
					res = rs.getInt("userID");
				}
				else if (rs.getString("Response").contains("Err")) {
					System.err.println("Proc returned duplication error in response! Registration failed.");
				}
				else {
					System.err.println("Unkown error read from proc response! ¯\\_(ツ)_/¯");
				}
			}
			else { // result set was empty! What happened???
				System.err.println("There was a problem registering! (no result returned from proc)");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
