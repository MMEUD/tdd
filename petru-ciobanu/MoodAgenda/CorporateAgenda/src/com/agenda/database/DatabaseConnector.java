package com.agenda.database;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
  
	public  void newEvents(String label,String datedebut,String heuredebut,String datefin,String heurefin,String detail,String priorite,String sync){
		ContentValues newCon = new ContentValues();
		newCon.put("evenement_libelle", label);
		newCon.put("evenement_datedebutvalidite", datedebut);
		newCon.put("evenement_datefinvalidite", datefin);
		newCon.put("evenement_datedebut", datedebut);
		newCon.put("evenement_datefin", datefin);
		newCon.put("evenement_heuredebut", heuredebut);
		newCon.put("evenement_heurefin", heurefin);
		newCon.put("evenement_detail", detail);
		newCon.put("evenement_priorite", priorite);
		newCon.put("evenement_sync", sync);
		open();
		database.insert("tbl_events", null, newCon);
		close();
	}
	
	//http://www.mysamplecode.com/2012/07/android-listview-cursoradapter-sqlite.html
	// EVENTS OPERATION
	public void insertEvents(String label, int datedebutvalidite, int datefinvalidite, int datedebut, int datefin, String heuredebut, String heurefin, String detail,
			String nomfichier, String priorite, int stpdatemodif, int stpdatecrea, String stputilcrea, String stputilmodif, int stpdatepubli, String stputilpubli) {
		ContentValues newCon = new ContentValues();
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
		newCon.put("evenement_stpdatepubli", stpdatepubli);
		newCon.put("evenement_stputilpubli", stputilpubli);
		open();
		database.insert("tbl_events", null, newCon);
		close();

	}	
	public Cursor todayEvents() {	
	String sql=	"SELECT DISTINCT _id,evenement_libelle,evenement_datedebut,evenement_priorite,evenement_heuredebut,evenement_heurefin FROM  tbl_events ORDER BY evenement_heuredebut ASC " ;
	Cursor mCursor = database.rawQuery(sql, null);	
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public void deleteEvents(int idEvents){
		
		open();
		database.delete("tbl_events", "_id" + "=" + idEvents, null) ;
		close();
	}
}
