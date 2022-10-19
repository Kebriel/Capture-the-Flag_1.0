package sylaires.ctf.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class MainDB {
	
	private static Connection connection;
    private String host, database, username, password;
    private int port;
	
	public MainDB() {
		host = "localhost";
        port = 3306;
        database = "programzeta";
        username = "root";
        password = "root"; 
        
        try {
			openConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 private void openConnection() throws SQLException, ClassNotFoundException {
	        if (connection != null && !connection.isClosed()) {
				return;
			}
	        synchronized (this) {
	            if (connection != null && !connection.isClosed()) {
	                return;
	            }
	            connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
	        }
	    }
	 
	public Connection getDBConnection() {
		return connection;
	}

}
