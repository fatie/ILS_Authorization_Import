package org.centerlight.ils.authorizationImport;


import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccessDBClose {
	private static final Logger logger = LogManager.getLogger(AccessDBClose.class);
	private Connection conn;
	public AccessDBClose(Connection conn){
		this.conn = conn;
	}
	public void closeConnection() throws SQLException{
		logger.debug("Disconnecting....");
		this.conn.close();
		logger.debug("Disconnected");
	}

}
