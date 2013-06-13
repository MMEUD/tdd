package com.agenda.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public DatabaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
		/*String createEvents = "CREATE TABLE tbl_events(evenement_adndroid_id integer primary key autoincrement,id_evenement,evenement_libelle,evenement_datedebutvalidite INTEGER," +
				"evenement_datefinvalidite INTEGER ,evenement_datedebut INTEGER ,evenement_datefin INTEGER ,evenement_heuredebut TEXT,evenement_heurefin TEXT,evenement_detail,evenement_nomfichier" +
				"evenement_priorite,evenement_stpdatemodif INTEGER ,evenement_stpdatecrea INTEGER,evenement_stputilmodif,evenement_stpstatut,evenement_stpdatepubli INTEGER,evenement_sync,evenement_sync_date);";                 
		
		String createUsers="CREATE TABLE tbl_user (android_id integer primary key autoincrement ,user_name,user_prenom,user_password)";
		db.execSQL(createEvents);	
		db.execSQL(createUsers);	*/
		} catch (SQLiteException e) {
		Log.i(getClass().getSimpleName(), "DATABASE ERROR CREATION!!!" + e);
		}  
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
	}

}
