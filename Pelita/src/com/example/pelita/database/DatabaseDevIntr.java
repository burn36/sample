package com.example.pelita.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseDevIntr extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "pelita.sqlite";
	private static String DB_PATH = "";
	private final Context myContext;
	private final String device_wifii = "device_wifii";

	public DatabaseDevIntr(Context context) {
		super(context, DATABASE_NAME, null, 1);
		this.myContext = context;
		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";
	}

	public void createTable() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
		String outFileName = DB_PATH + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void InsertDev_wifii(String id, String pass) {
		createTable();

		ContentValues cv = new ContentValues();
		SQLiteDatabase db = getWritableDatabase();
		cv.put("id_dev", id);
		cv.put("pass", pass);
		db.insert(device_wifii, "id_dev", cv);

		db.close();
	}
	public void DeleteDev_wifii(String id) {
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		String filter = "id_dev like '" + id + "'";
		db.delete(device_wifii, filter, null);
		db.close();
	}
	public ArrayList<String> getDevice_wifii() {
		createTable();
		Log.d("database","start");
		SQLiteDatabase db = getWritableDatabase();
		ArrayList<String> device_list = new ArrayList<String>();
		String query = "SELECT * FROM " + device_wifii;
		Cursor kursor = db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				Log.d(kursor.getString(0),kursor.getString(1));
				device_list.add(kursor.getString(0)+"@"+kursor.getString(1));

			}
		}
		kursor.close();
		db.close();
		return device_list;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}
}