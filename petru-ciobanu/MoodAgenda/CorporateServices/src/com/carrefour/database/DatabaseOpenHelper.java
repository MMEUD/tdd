package com.carrefour.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public DatabaseOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String createEvents = "CREATE TABLE tbl_events(_id integer primary key autoincrement,id_evenement integer ,evenement_libelle,evenement_datedebutvalidite ,"
					+ "evenement_datefinvalidite ,evenement_datedebut ,evenement_datefin,evenement_heuredebut,evenement_heurefin,"
					+ "evenement_detail,evenement_nomfichier,evenement_priorite,evenement_stpdatemodif,evenement_stpdatecrea,evenement_stputilcrea,evenement_stputilmodif,"
					+ "evenement_stpstatut,evenement_stpdatepubli,evenement_stputilpubli, evenement_sync,evenement_deleted);";

			String createUsers = "CREATE TABLE tbl_user (_id integer primary key autoincrement ,ID_UTILISATEUR,UTILISATEUR_LIBELLE,UTILISATEUR_PASSWORD,UTILISATEUR_NOM,UTILISATEUR_PRENOM)";
			db.execSQL(createEvents);
			db.execSQL(createUsers);
		} catch (SQLiteException e) {
			Log.i(getClass().getSimpleName(), "DATABASE ERROR CREATION!!!" + e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
