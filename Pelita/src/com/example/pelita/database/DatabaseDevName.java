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

public class DatabaseDevName extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "pelita.sqlite";
	private static String DB_PATH = "";
	private final Context myContext;
	private final String device_List = "device_name";

	public DatabaseDevName(Context context) {
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
		
		Log.d("DatabaseList","insert");
		ContentValues cv = new ContentValues();
		SQLiteDatabase db = getWritableDatabase();
		cv.put("id_dev", id);
		cv.put("dev1", "Device 1");
		cv.put("dev2", "Device 2");
		cv.put("dev3", "Device 3");
		cv.put("dev4", "Device 4");
		cv.put("dev5", "Device 5");
		cv.put("dev6", "Device 6");
		cv.put("tipe", "intr");
		db.insert(device_List, "id_dev", cv);
		db.close();
		DatabaseDevStatus d= new DatabaseDevStatus(myContext);
		d.InsertDev(id);
	}
	public void InsertDev_blue(String id) {
		createTable();
		
		Log.d("DatabaseList","insert");
		ContentValues cv = new ContentValues();
		SQLiteDatabase db = getWritableDatabase();
		cv.put("id_dev", id);
		cv.put("dev1", "Device 1");
		cv.put("dev2", "Device 2");
		cv.put("dev3", "Device 3");
		cv.put("dev4", "Device 4");
		cv.put("dev5", "Device 5");
		cv.put("dev6", "Device 6");
		cv.put("tipe", "blue");
		db.insert(device_List, "id_dev", cv);
		db.close();
		DatabaseDevStatus d= new DatabaseDevStatus(myContext);
		d.InsertDev(id);
	}

	public void DeleteDev_list(String id) {
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		String filter = "id_dev like '" + id + "'";
		db.delete(device_List, filter, null);
		db.close();
	}

	public ArrayList<String> getDevice_List() {
		createTable();

		SQLiteDatabase db = getWritableDatabase();
		ArrayList<String> device_list = new ArrayList<String>();
		String query = "SELECT * FROM " + device_List;
		Cursor kursor = db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				Log.d(kursor.getString(0), kursor.getString(1));
				device_list
						.add(kursor.getString(0) + "@" + kursor.getString(1));

			}
		}
		kursor.close();
		db.close();
		return device_list;
	}
	public String getDevice() {
		createTable();

		SQLiteDatabase db = getWritableDatabase();
		String device_list = "";
		String query = "SELECT * FROM " + device_List;
		Cursor kursor = db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				device_list=(kursor.getString(0) + "@" + kursor.getString(1));
			}
		}
		kursor.close();
		db.close();
		return device_list;
	}
	public boolean cek_id(String id){
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		boolean ada= false;
		String query = "SELECT * FROM " + device_List + " Where id_dev like '"
				+ id+"'";
		Cursor kursor = db.rawQuery(query, null);
		if (kursor.getCount()>0) {
			ada=true;
		}
		Log.d("Count",kursor.getCount()+"");
		kursor.close();
		db.close();
		Log.d("ada",ada+"");
		return ada;
	}
	public String getDev1(String id) {
		createTable();

		SQLiteDatabase db = getWritableDatabase();
		String deviceName="";
		String query = "SELECT dev1 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getString(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}

	public String getDev2(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		String deviceName="";
		String query = "SELECT dev2 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getString(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public String getDev3(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		String deviceName="";
		String query = "SELECT dev3 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getString(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public String getDev4(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		String deviceName="";
		String query = "SELECT dev4 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getString(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public String getDev5(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		String deviceName="";
		String query = "SELECT dev5 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getString(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public String getDev6(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		String deviceName="";
		String query = "SELECT dev6 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getString(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public void UpdateDev1(String id, String nama) {
		update("dev1",id, nama);
	}
	public void UpdateDev2(String id, String nama) {
		update("dev2",id, nama);
	}
	public void UpdateDev3(String id, String nama) {
		update("dev3",id, nama);
	}
	public void UpdateDev4(String id, String nama) {
		update("dev4",id, nama);
	}
	public void UpdateDev5(String id, String nama) {
		update("dev5",id, nama);
	}

	private void update(String dev,String id, String nama) {
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		String filter = "id_dev like '" + id + "'";
		cv.put(dev, nama);
		db.update(device_List, cv, filter, null);
		db.close();
	}
	public void Delete(String id,String pass) {
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		String filter = "id_dev like '" + id + "' and pass like '"+pass+"'";
		db.delete(device_List, filter, null);
		db.close();
		DatabaseDevStatus d= new DatabaseDevStatus(myContext);
		d.Delete(id);
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