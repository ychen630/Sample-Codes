package util;

/*
 * DBUtil Class, database utility
 * Created by Yixin Chen on 4/17/2018 
 *
 * Connect to database, disconnect from database
 * Execute query
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.rowset.CachedRowSetImpl;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class DBUtil {
	//database server
	private static final String DBURL = "jdbc:sqlserver://traveling-salesbook.chfgpq4clmvj.us-west-1.rds.amazonaws.com;user=travelingsalesman;password=namselasgnilevart;database=salesbook";
	//new connection
	private static Connection conn = null;
	
	//connect to database
	public static void dbConnect() {
		try {
			conn = DriverManager.getConnection(DBURL);		
			if (conn != null) {
				System.out.println("Connected to database!");
			}
		} catch (SQLException e) {
			System.out.println("Cannot connect to database, please try again later!");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Cannot connect to database.");
			alert.setContentText("Please try again later.");
			alert.showAndWait();
			e.printStackTrace();
		}
	}
	
	//disconnect from database
	public static void dbDisconnect() {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB
            dbConnect();
            System.out.println("Select statement: " + queryStmt + "\n");
 
            //Create statement
            stmt = conn.createStatement();
 
            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);
 
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }
 
    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB
            dbConnect();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            System.out.println("Select statement: " + sqlStmt + "\n");
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }
}
