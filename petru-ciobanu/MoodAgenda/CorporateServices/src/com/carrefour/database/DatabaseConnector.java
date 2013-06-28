package com.carrefour.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class DatabaseConnector {

	private static final String DB_NAME = "carrefour_events";
	private SQLiteDatabase database;
	private DatabaseOpenHelper dbOpenHelper;
	public DatabaseConnector(Context context) {
		dbOpenHelper = new DatabaseOpenHelper(context, DB_NAME, null, 1);
	}
	public void open() throws SQLException {
		// open database in reading/writing mode
		database = dbOpenHelper.getWritableDatabase();
	}

	public void close() {
		if (database != null)
			database.close();
	}

	//CHECK  IF  RECORDS FROM CENTRAL DB EXISTS ON LOCAL  ANDROID DATABASE
	public Cursor checkIdEvenement(int id_evenement){
		Cursor mCursor = database.rawQuery("SELECT _id,id_evenement FROM tbl_events WHERE id_evenement ='"+ id_evenement + "' " , null);// 
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;	
	}

	//INSERT NEW  EVENTS  FROM  CENTRAL DB  TO LOCAL  DB 
	public  void  insertFromRest(int id_evenement,String label,String datedebutvalidite,String datefinvalidite,String datedebut,String datefin,String heuredebut,
			String heurefin,String detail,String nomfichier,String priorite,String stpdatemodif,String stpdatecrea,String stputilcrea,String stputilmodif,String stpstatus,
			String stpdatepubli,String stputilpubli,String sync ,String deleted){
		ContentValues newCon = new ContentValues();
		newCon.put("id_evenement", id_evenement);
		newCon.put("evenement_libelle", label);
		newCon.put("evenement_datedebutvalidite", datedebutvalidite);
		newCon.put("evenement_datefinvalidite", datefinvalidite);
		newCon.put("evenement_datedebut", datedebut);
		newCon.put("evenement_datefin", datefin);
		newCon.put("evenement_heuredebut", heuredebut);
		newCon.put("evenement_heurefin", heurefin);
		newCon.put("evenement_detail", detail);
		newCon.put("evenement_nomfichier", nomfichier);
		newCon.put("evenement_priorite", priorite);
		newCon.put("evenement_stpdatemodif", stpdatemodif);
		newCon.put("evenement_stpdatecrea", stpdatecrea);
		newCon.put("evenement_stputilcrea", stputilcrea);
		newCon.put("evenement_stputilmodif", stputilmodif);
		newCon.put("evenement_stpstatut", stpstatus);
		newCon.put("evenement_stpdatepubli", stpdatepubli);
		newCon.put("evenement_stputilpubli", stputilpubli);
		newCon.put("evenement_sync", sync);
		newCon.put("evenement_deleted", deleted);
		open();
		database.insert("tbl_events", null, newCon);
		database.
		close();
	}


	 //UPDATE  LOCAL  RECORDS  WITH  DATA  FROM CENTRAL DB
	public void updateSync(int id,int id_evenement,String label,String datedebutvalidite,String datefinvalidite,String datedebut,String datefin,String heuredebut,
				String heurefin,String detail,String nomfichier,String priorite,String stpdatemodif,String stpdatecrea,String stputilcrea,String stputilmodif,String stpstatus,
				String stpdatepubli,String stputilpubli,String sync ,String deleted){
			ContentValues newCon = new ContentValues();
			newCon.put("id_evenement", id_evenement);
			newCon.put("evenement_libelle", label);
			newCon.put("evenement_datedebutvalidite", datedebutvalidite);
			newCon.put("evenement_datefinvalidite", datefinvalidite);
			newCon.put("evenement_datedebut", datedebut);
			newCon.put("evenement_datefin", datefin);
			newCon.put("evenement_heuredebut", heuredebut);
			newCon.put("evenement_heurefin", heurefin);
			newCon.put("evenement_detail", detail);
			newCon.put("evenement_nomfichier", nomfichier);
			newCon.put("evenement_priorite", priorite);
			newCon.put("evenement_stpdatemodif", stpdatemodif);
			newCon.put("evenement_stpdatecrea", stpdatecrea);
			newCon.put("evenement_stputilcrea", stputilcrea);
			newCon.put("evenement_stputilmodif", stputilmodif);
			newCon.put("evenement_stpstatut", stpstatus);
			newCon.put("evenement_stpdatepubli", stpdatepubli);
			newCon.put("evenement_stputilpubli", stputilpubli);
			newCon.put("evenement_sync", "1");
			newCon.put("evenement_deleted", deleted);
			open();
			String where ="_id='"+ id +"' AND id_evenement='"+ id_evenement +"' ";
			database.update("tbl_events",newCon,where,null) ;
			close();
		}
	
	
	
	

	public Cursor getIdAndroid(){
		Cursor mCursor = database.rawQuery("SELECT id_evenement,_id,evenement_sync ,evenement_deleted FROM tbl_events WHERE id_evenement IS NOT NULL   AND evenement_sync='1' ", null);
		if (mCursor != null) {
			mCursor.moveToNext();
		}
		return mCursor;	
	}

	public void updateSyncStatus(int id ,String sync){
		ContentValues newCon = new ContentValues();
		newCon.put("evenement_sync",sync);
		open();
		database.update("tbl_events",newCon, "id_evenement" + "=" +id, null) ;
		close();
	}

  

	//EXPORT SERVICE TO REST  END POINT



}
