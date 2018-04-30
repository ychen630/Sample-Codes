package posts;
/*
 * PostDAO class (Data Access Object)
 * Created by Yixin Chen on 4/17/2018
 * 
 */


import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

public class PostDAO {
	
	//select all posts
	public static ObservableList<Post> searchPosts(String userID){
		String selectStmt = "SELECT * FROM Post WHERE userID=" + userID;
		try {
			ResultSet rsPosts;
			rsPosts = DBUtil.dbExecuteQuery(selectStmt);
			ObservableList<Post> postList = getPostsList(rsPosts);
			return postList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Store post data from database in an ObservableList
	private static ObservableList<Post> getPostsList(ResultSet rs) {
		ObservableList<Post> postList = FXCollections.observableArrayList();
		try {
			while(rs.next()) {
				Post post = new Post();
				post.setPostID(rs.getInt("PostID"));
				post.setPostContent(rs.getString("Content"));
				post.setEditTime(rs.getString("EditTime"));
				post.setPostTime(rs.getString("PostTime"));
				postList.add(post);
			}
			return postList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//delete a selected post using postID
	public static void deletePost(int postID) {
		String updateStmt = "DELETE FROM Post\n" +
                        "WHERE postID ="+ postID +";\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	//edit a selected post using postID
	public static void editPost(int postID, String content, String editTime) {
		String updateStmt = 
				"UPDATE Post\n" +
						"SET Content = '" + content +"', EditTime = '" + editTime + "'\n" +
						"WHERE PostID = " + postID +";\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	//add a new post to database
	public static void addPost(String content, String postTime, int userID) {
		String updateStmt = 
				"INSERT INTO Post(userID, Content, PostTime)\n" +
						"VALUES\n" +
						"("+ userID +", '" + content + "', '" + postTime + "');\n";
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
