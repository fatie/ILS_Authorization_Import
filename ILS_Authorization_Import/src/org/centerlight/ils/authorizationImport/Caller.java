package org.centerlight.ils.authorizationImport;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Caller {
	private static final Logger logger = LogManager.getLogger(Caller.class);

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Connection conn = (new AccessDBConnection()).getDriverLoad();
		
//		logger.debug("driver loading....");
//		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//		logger.debug("driver loading done");
	
//		Connection conn = AccessDBLogConnection.getDatabaseConnection();

//		String query = "Insert into log (Event) VALUES ('"+"aaaa"+"');";; 
		String query = "SELECT * FROM ILS_WS_Dx_WIP AS a;";
		
		Statement stmt = conn.createStatement();
		
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(query);
			int count = 0;
			int oldCount = 0;
			String previousRqstID= null;
			String currentRqstID = null;
			String icdCode = null;
			Hashtable<String, ArrayList<String>> rqst = new Hashtable<String, ArrayList<String>>();
			while(rs.next()){
				currentRqstID = rs.getString(1);
				if (currentRqstID.equals(previousRqstID)){
					count++;
					if (count <6){
						if(rs.getString(4).equals("1")){
							icdCode = rs.getString(3);
							rqst.get(currentRqstID).add(rs.getString(2));
						}
						else {
							if (rs.getString(3).equals(icdCode)){
								rqst.get(currentRqstID).add(rs.getString(2));
							}
							else {
								count--;
							}
						}
					}
					else {
						count--;
					}
					
					
				}
				else {
					oldCount = count;
					count = 1;
					
					icdCode = rs.getString(3);
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(rs.getString(2));
					rqst.put(currentRqstID, temp);
					
					
					if(previousRqstID != null){
						for (int j = 0; j < (5-oldCount); j++){
							rqst.get(previousRqstID).add(null);
						}
						
						logger.info(previousRqstID);
						for (int i = 0; i < rqst.get(previousRqstID).size(); i++){
							logger.info(rqst.get(previousRqstID).get(i));
						}
						logger.info("--------------------");
						
					}
				}
				
				previousRqstID = currentRqstID;
				

			}
			
			Set<String> keys = rqst.keySet();
			for (String key : keys){
				logger.debug(key);
				String queryUpdate = "Insert into ILS_WS_Dx_V ([Rqst ID], [Dx 1], [Dx 2], [Dx 3], [Dx 4], [Dx 5]) VALUES ('"+key+"', '"+rqst.get(key).get(0)+"', '"+rqst.get(key).get(1)+"', '"+rqst.get(key).get(2)+"', '"+rqst.get(key).get(3)+"', '"+rqst.get(key).get(4)+"');";  
				stmt.executeUpdate(queryUpdate);
				logger.debug("inserted");
				logger.debug("committed");
			}


			
			

		}
		catch(java.lang.Exception ex){
			ex.printStackTrace();
		}
		finally {
//			if(rs != null){
//				rs.close();
//			}
			if(stmt != null){
				stmt.close();
			}
			if (conn != null){
			    (new AccessDBClose(conn)).closeConnection();
			}
		}
	}
}
