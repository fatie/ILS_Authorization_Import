package org.centerlight.ils.authorizationImport;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AccessDBConnection {
	private static final Logger logger = LogManager.getLogger(AccessDBConnection.class);
	public Connection getDriverLoad() throws SQLException, ClassNotFoundException{
		logger.debug("Driver loading....");
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		logger.debug("Driver load----Done");
		String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\Users\\FGuo\\Documents\\work\\Transition\\ILS.accdb;";
		logger.debug("Connecting database...");
		Connection conn = DriverManager.getConnection(url);
		logger.debug("Connected");	
		return conn;
	}

}
