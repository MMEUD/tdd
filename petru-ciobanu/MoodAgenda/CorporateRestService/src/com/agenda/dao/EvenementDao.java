package com.agenda.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONObject;

import com.agenda.entity.EventEntity;
import com.agenda.entity.UsersEntity;
import com.agenda.utils.Utils;

public class EvenementDao {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
    Utils utils = new Utils();
    
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
	String dateAsString = simpleDateFormat.format(new Date());
	//GET  NEW EVENTS  FROM  DATABASE "get_new_events"
	public EventEntity actionGetEvents(String username, String sync) throws SQLException {
		connection = new OracleDbConn().getConnection();
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("ExceptionGetConn: " + e.toString());
		}
		String sqlStatement = "SELECT * FROM evenement WHERE  evenement_stputilcrea='"+username+" ' AND    evenement_datefinvalidite >= '"+ dateAsString +"'  ";
		rs = statement.executeQuery(sqlStatement);
		EventEntity eventEntity =null;
		eventEntity = new EventEntity();	
		boolean val = rs.next();
		if (val == false) {
			eventEntity.setId_evenement(0);		
			//System.out.println("NO DATA TO SYNC "+ dateAsString+ username);
		} else { 
			//System.out.println("FOUND RECORDS TO INSERT");
			eventEntity.setId_evenement(rs.getInt("id_evenement"));
			eventEntity.setEvenement_libelle(rs.getString("evenement_libelle"));
			eventEntity.setEvenement_datedebutvalidite(rs.getString("evenement_datedebutvalidite"));	
			eventEntity.setEvenement_datefinvalidite(rs.getString("evenement_datefinvalidite"));
			eventEntity.setEvenement_datedebut(rs.getString("evenement_datedebut"));
			eventEntity.setEvenement_datefin(rs.getString("evenement_datefin"));
			eventEntity.setEvenement_heuredebut(rs.getString("evenement_heuredebut"));
			eventEntity.setEvenement_heurefin(rs.getString("evenement_heurefin"));
			eventEntity.setEvenement_detail(rs.getString("evenement_detail"));
			eventEntity.setEvenement_nomfichier(rs.getString("evenement_nomfichier"));
			eventEntity.setEvenement_priorite(rs.getString("evenement_priorite"));
			eventEntity.setEvenement_stpdatemodif(rs.getString("evenement_stpdatemodif"));
			eventEntity.setEvenement_stpdatecrea(rs.getString("evenement_stpdatecrea"));
			eventEntity.setEvenement_stputilcrea(rs.getString("evenement_stputilcrea"));
			eventEntity.setEvenement_stpdatemodif(rs.getString("evenement_stpdatemodif"));
			eventEntity.setEvenement_stpstatut(rs.getString("evenement_stpstatut"));
			eventEntity.setEvenement_stpdatepubli(rs.getString("evenement_stpdatepubli"));
			eventEntity.setEvenement_stputilpubli(rs.getString("evenement_stputilpubli"));
			eventEntity.setEvenement_sync(rs.getString("evenement_sync"));
			eventEntity.setEvenement_deleted(rs.getString("evenement_deleted"));
			
		}
		statement.close();
		connection.close();
		return eventEntity;
	}

	// RECEIVE STATUS FROM SERVICE OPERATION    /sync_update_id
	public String actionReceiveId(String username, String id_evenement,String evenement_id_android,String sync,String where_sync)throws SQLException {
		connection = new OracleDbConn().getConnection();
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("ExceptionGetConn: " + e.toString());
		}		
		String sqlId="UPDATE evenement SET evenement_sync='" + sync + "' ,evenement_id_android='" + evenement_id_android + "' WHERE id_evenement='" + id_evenement+ "' AND evenement_sync='" + where_sync + "' ";
		int rows = statement.executeUpdate(sqlId);
		//SET SYNC STATUS TO 2 WHICH MEANS THE RECORDS FROM BOTH SIDE IS THE SAME
		String response =null;
		if (rows == 0){
			response="0";	
		}else{
			response="UPDATE COMPLETED";
		}
		statement.close();
		connection.close();
		return response;

	}

	//GET EVENTS  UPDATE
	public EventEntity actionUpdateEvents(String username, String sync) throws SQLException {
		connection = new OracleDbConn().getConnection();
		sync="0";
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("ExceptionGetConn: " + e.toString());
		}
		String sqlStatement = "SELECT * FROM evenement WHERE evenement_stputilcrea='" + username+ "' AND evenement_sync='3'   AND evenement_id_android IS NOT NULL ";
		rs = statement.executeQuery(sqlStatement);
		EventEntity eventEntity =null;
		eventEntity = new EventEntity();	
		boolean val = rs.next();
		if (val == false) {
			eventEntity.setId_evenement(0);
			//System.out.println("NO DATA TO FOUD  FOR UPDATE");
		} else { 
			eventEntity.setId_evenement(rs.getInt("id_evenement"));
			eventEntity.setEvenement_id_android(rs.getInt("evenement_id_android"));
			eventEntity.setEvenement_libelle(rs.getString("evenement_libelle"));
			eventEntity.setEvenement_datedebutvalidite(rs.getString("evenement_datedebutvalidite"));	
			eventEntity.setEvenement_datefinvalidite(rs.getString("evenement_datefinvalidite"));
			eventEntity.setEvenement_datedebut(rs.getString("evenement_datedebut"));
			eventEntity.setEvenement_datefin(rs.getString("evenement_datefin"));
			eventEntity.setEvenement_heuredebut(rs.getString("evenement_heuredebut"));
			eventEntity.setEvenement_heurefin(rs.getString("evenement_heurefin"));
			eventEntity.setEvenement_detail(rs.getString("evenement_detail"));
			eventEntity.setEvenement_nomfichier(rs.getString("evenement_nomfichier"));
			eventEntity.setEvenement_priorite(rs.getString("evenement_priorite"));
			eventEntity.setEvenement_stpdatemodif(rs.getString("evenement_stpdatemodif"));
			eventEntity.setEvenement_stpdatecrea(rs.getString("evenement_stpdatecrea"));
			eventEntity.setEvenement_stputilcrea(rs.getString("evenement_stputilcrea"));
			eventEntity.setEvenement_stpdatemodif(rs.getString("evenement_stpdatemodif"));
			eventEntity.setEvenement_stpstatut(rs.getString("evenement_stpstatut"));
			eventEntity.setEvenement_stpdatepubli(rs.getString("evenement_stpdatepubli"));
			eventEntity.setEvenement_stputilpubli(rs.getString("evenement_stputilpubli"));
			eventEntity.setEvenement_sync(rs.getString("evenement_sync"));
			eventEntity.setEvenement_deleted(rs.getString("evenement_deleted"));
		}
		statement.close();
		connection.close();
		return eventEntity;
	}

     //INSERT EVENTS  FROM ADROID
	public EventEntity actionInsertEvents(String username , String evenement_libelle,String evenement_id_android,String evenement_sync)throws SQLException {
		connection = new OracleDbConn().getConnection();	
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("ExceptionGetConn: " + e.toString());
		}
		String sqlmaxId = "SELECT MAX(id_evenement) AS newId FROM evenement";
		rs = statement.executeQuery(sqlmaxId);
		EventEntity eventEntity =null;
		eventEntity = new EventEntity();	
		boolean val = rs.next();
		if (val == false) {
			eventEntity.setId_evenement(0);
			statement.close();
		} else { 
			int newRow=rs.getInt(1)+1;
			String sqlInsert="INSERT INTO evenement (id_evenement,evenement_libelle,evenement_id_android,evenement_sync)" +" VALUES('" + newRow+ "','" + evenement_libelle+ "','" +newRow+ "'+1,evenement_sync)";
			int rows = statement.executeUpdate(sqlInsert);
			if(rows>0){
				eventEntity.setId_evenement(newRow);
				System.out.println("ID_EVENEMENT ORACLE: " + newRow);
			}else{
				eventEntity.setId_evenement(0);
			}
			statement.close();
		}	
		connection.close();
		return eventEntity;
	}
	
}
