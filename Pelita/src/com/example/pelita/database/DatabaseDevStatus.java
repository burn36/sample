package com.example.pelita.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.example.pelita.json.JSON;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseDevStatus extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "pelita.sqlite";
	private static String DB_PATH = "";
	private final Context myContext;
	private final String device_List = "device_list";

	public DatabaseDevStatus(Context context) {
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

	public void InsertDev(String id) {
		Toast.makeText(myContext, "insert to database", Toast.LENGTH_SHORT).show();
		createTable();
		Log.d("DatabaseList","insert");
		ContentValues cv = new ContentValues();
		SQLiteDatabase db = getWritableDatabase();
		cv.put("id_dev", id);
		cv.put("dev1", 0);
		cv.put("dev2", 0);
		cv.put("dev3", 0);
		cv.put("dev4", 0);
		cv.put("dev5", 0);
		cv.put("dev6", 0);
		db.insert(device_List, "id_dev", cv);

		db.close();
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
	public int getDev1(String id) {
		createTable();

		SQLiteDatabase db = getWritableDatabase();
		int deviceName=0;
		String query = "SELECT dev1 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getInt(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}

	public int getDev2(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		int deviceName=0;
		String query = "SELECT dev2 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getInt(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public int getDev3(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		int deviceName=0;
		String query = "SELECT dev3 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getInt(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public int getDev4(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		int deviceName=0;
		String query = "SELECT dev4 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getInt(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public int getDev5(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		int deviceName=0;
		String query = "SELECT dev5 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getInt(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public int getDev6(String id) {
		createTable();
		
		SQLiteDatabase db = getWritableDatabase();
		int deviceName=0;
		String query = "SELECT dev6 FROM " + device_List +" Where id_dev like '"+id+"'";
		Cursor kursor =db.rawQuery(query, null);
		if (kursor.moveToFirst()) {
			for (; !kursor.isAfterLast(); kursor.moveToNext()) {
				deviceName=(kursor.getInt(0));
			}
		}
		kursor.close();
		db.close();
		return deviceName;
	}
	public void UpdateDev1(String id, int status) {
		Log.d("updatedev1",id+" "+status);
		update("dev1",id, status);
	}
	public void UpdateDev2(String id, int status) {
		update("dev2",id, status);
	}
	public void UpdateDev3(String id, int status) {
		update("dev3",id, status);
	}
	public void UpdateDev4(String id, int status) {
		update("dev4",id, status);
	}
	public void UpdateDev5(String id, int status) {
		update("dev5",id, status);
	}
	public void UpdateDev12(String id, int status) {
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		String filter = "id_dev like '" + id + "'";
		cv.put("dev1", status);
		cv.put("dev2", status);
		db.update(device_List, cv, filter, null);
		db.close();
	}
	public void SynDev2(HashMap<String, Integer> x,String id){
		//Log.d("Syn",id);
		//JSON j = new JSON(myContext);
		//HashMap<String, Integer> x = new HashMap<String, Integer>();
		//x=j.getStatus(id);
	
		Iterator<String> iterator;
		iterator= x.keySet().iterator();
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		String filter = "id_dev like '" + id + "'";
		Log.d("size hash",x.size()+"");
		while (iterator.hasNext()) {
			String i=iterator.next();
			Log.d(i,x.get(i)+" sd");
			cv.put(i, x.get(i));
		}
		db.update(device_List, cv, filter, null);
		
		db.close();
		
	}
	public void SynDev(String id){
		JSON j = new JSON(myContext);
		HashMap<String, Integer> x = new HashMap<String, Integer>();
		x=j.getStatus(id);
		Iterator<String> iterator;
		iterator= x.keySet().iterator();
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		Log.d("SynDev",id);
		String filter = "id_dev like '"+id+"'";
		while (iterator.hasNext()) {
		
			String i=iterator.next();
		
				cv.put(i, x.get(i));
		}
		db.update(device_List, cv, filter, null);
		
		db.close();
	}
	private void update(String dev,String id, int status) {
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		String filter = "id_dev like '" + id + "'";
		cv.put(dev, status);
		db.update(device_List, cv, filter, null);
		db.close();
	}
	public void Delete(String id) {
		createTable();
		SQLiteDatabase db = getWritableDatabase();
		String filter = "id_dev like '" + id + "'";
		db.delete(device_List, filter, null);
		db.close();
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