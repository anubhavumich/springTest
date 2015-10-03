package Functional;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.anubhavs.springtest.model.*;

import org.apache.log4j.Logger;

public class DBOperations {
	Connection connection;
	String dbPath;
	Exception e;
	final static Logger logger = Logger.getLogger(DBOperations.class);
	final static String LOG_TAG = "DBOperations";
	
	public DBOperations(){
		
	}
	
	public DBOperations(String root){
		try{
			DriverManager.registerDriver(new org.sqlite.JDBC());
			dbPath = "jdbc:sqlite:"+root;
		}catch(SQLException ex){
			logger.error(LOG_TAG + " " + ex.toString());
		}
	}
	
	public void Connect(){
		try{
			connection = DriverManager.getConnection(dbPath);
		}catch(SQLException ex){
			logger.error(LOG_TAG + " " + ex.toString());
		}
	}
	
	public ArrayList<TTRMarketTypeModel> GetTTRValues(){
		ArrayList<TTRMarketTypeModel> ttrList = new ArrayList<TTRMarketTypeModel>();
		try{
			if (connection ==null){
				Connect();
			}
			Statement stmt = connection.createStatement();
			String query = "select sum(minTimeDiff) as TTRSum, count(minTimeDiff) as TTRCount, min(minTimeDiff) as MinTTR, max(minTimeDiff) as MaxTTR, account_id, market_type from (SELECT sess_id, min(timeDiff) AS minTimeDiff, account_id, market_type FROM (SELECT session.sess_id, strftime('%s', retrieval.time_request) - strftime('%s', session.log_in_time) AS timeDiff, session.log_in_time, session.usage_group_id, cg.account_id, c.market_type FROM USER_SESSION session, USER_SESSION_RETRIEVAL retrieval, COMPANY_GROUP cg, COMPANY c WHERE session.sess_id = retrieval.sess_id and cg.usage_group_id = session.usage_group_id and c.account_id = cg.account_id) GROUP BY sess_id) GROUP BY market_type ORDER BY market_type";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				TTRMarketTypeModel ttr = new TTRMarketTypeModel();
				ttr.MarketType = rs.getString("market_type");
				ttr.TTRSumPerAccount = rs.getInt("TTRSum");
				ttr.Count = rs.getInt("TTRCount");
				ttr.AccountId = rs.getInt("account_id");
				ttr.MinTTR = rs.getInt("MinTTR");
				ttr.MaxTTR = rs.getInt("MaxTTR");
				ttrList.add(ttr);
			}
		}catch(SQLException ex){
			logger.error(LOG_TAG + " " + ex.toString());
		}
		return ttrList;
	}

}
