package com.example.fidelrantula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.fidelrantula.Measurement.MeasurementComparator;


public class DBHandler extends SQLiteOpenHelper 
{
	protected static final int DATABASE_VERSION = 1;
	protected static final String DATABASE_NAME = "weightDB.db";
	protected static final String TABLE_MEASUREMENTS = "measurements";
	
	protected static final String COLUMN_ID = "_id";
	protected static final String COLUMN_TIMESTAMP = "timestamp";
	protected static final String COLUMN_M_ID = "id";
	
	protected static final String COLUMN_WEIGHT = "weight";
		
	protected SQLiteDatabase db;
	
	protected int daysToDisplay;
	protected int month = 30;
	
	protected static DateInputHandler dateinputhandler = new DateInputHandler();
	
	static MeasurementCollection measurements;
	
	public DBHandler(Context context, String name, CursorFactory factory, int version){
		super(context,DATABASE_NAME,factory,DATABASE_VERSION);
	}
	
	 public void open() throws SQLException {
		 db = this.getWritableDatabase();
	 }

	 public void close() {
		 db.close();
	 }
	
	@Override
	public void onCreate(SQLiteDatabase db){
		String CREATE_MEASUMERENTS_TABLE = "CREATE TABLE " +
				TABLE_MEASUREMENTS + "("
				+ COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TIMESTAMP
				+ " INTEGER," + COLUMN_WEIGHT + " INTEGER," + COLUMN_M_ID + " INTEGER" + ")";		
				db.execSQL(CREATE_MEASUMERENTS_TABLE);		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASUREMENTS);
	    onCreate(db);
	}
	
	public int checkDBexists(){
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEASUREMENTS, null);
		return cursor.getCount();		
	}

	//adding a new measurement
	public void addMeasurement(Measurement measurement) {
		
        ContentValues values = new ContentValues();        
        values.put(COLUMN_TIMESTAMP, measurement.getTimeStamp());        
        values.put(COLUMN_WEIGHT, measurement.getWeight());
        values.put(COLUMN_M_ID, measurement.getID());       
        db.insert(TABLE_MEASUREMENTS, null, values);  
        
	}
	
	public boolean deleteMeasurement(Measurement measurement) {

		boolean result = false;		
		long idDelete = measurement.getTimeStamp();		
		String query = "Select * FROM " + TABLE_MEASUREMENTS + " WHERE timestamp = " + idDelete;		
		Cursor cursor = db.rawQuery(query, null);		
		if (cursor.moveToFirst()) {
			db.delete(TABLE_MEASUREMENTS, COLUMN_TIMESTAMP + " = " + idDelete, null);
			cursor.close();
			result = true;
		}
		return result;
	}
		
	// Get All measurements, sorted by date
    public List<Measurement> getListOfMeasurements() {
    	
    	DateInputHandler dih = new DateInputHandler();
		ArrayList<Long> lst = dih.updateInputDates(this);
    	
    	long unixtime1 = lst.get(0);
    	long unixtime2 = lst.get(1);
        
    	measurements = new MeasurementCollection();
    	String query = "Select * FROM " + TABLE_MEASUREMENTS +
    			" WHERE timestamp >= " + unixtime1 + " AND timestamp <= " + unixtime2 + " ORDER BY " + COLUMN_TIMESTAMP;
    	
    	Cursor cursor = db.rawQuery(query, null);
    	int id = 0;
    	Measurement measurement = null;
        if (cursor.moveToFirst()) {
            do {
            		id ++;
                	measurement = new Measurement();
                	measurement.setID(id);
                	measurement.setTimeStamp(Integer.parseInt(cursor.getString(1)));
                	measurement.setWeight(Integer.parseInt(cursor.getString(2)));         
                	measurements.addToMeasurementCollection(measurement);            		
            } while (cursor.moveToNext());
      }   
        return measurements.getSortedMeasurementCollection();
    }
           	
	public int findMinValue(){
		String query = "Select MIN(weight) FROM " + TABLE_MEASUREMENTS;
		final SQLiteStatement stmt = db.compileStatement(query);
		return (int)stmt.simpleQueryForLong();
	}
	
	public int findMaxValue(){
		String query = "Select MAX(weight) FROM " + TABLE_MEASUREMENTS;
		final SQLiteStatement stmt = db.compileStatement(query);
		return (int)stmt.simpleQueryForLong();
	}
	public int findCountValue(){
		String query = "Select COUNT(*) FROM " + TABLE_MEASUREMENTS;
		final SQLiteStatement stmt = db.compileStatement(query);
		return (int)stmt.simpleQueryForLong();
	}
	
	public long findMinDateValue(){
		String query = "Select MIN(timestamp) FROM " + TABLE_MEASUREMENTS;
		final SQLiteStatement stmt = db.compileStatement(query);
		return (int)stmt.simpleQueryForLong();
	}
	
	public long findMaxDateValue(){
		String query = "Select MAX(timestamp) FROM " + TABLE_MEASUREMENTS;
		final SQLiteStatement stmt = db.compileStatement(query);
		return (int)stmt.simpleQueryForLong();
	}
	
	public long findValueMinMonth()
	{		
		String query = "Select * FROM " + TABLE_MEASUREMENTS + " ORDER BY " + COLUMN_TIMESTAMP + " DESC";		
		Cursor cursor = db.rawQuery(query, null);   	
    	int id = 0;   	
    	long empName = 0;    	
    	int i = findCountValue();   	
    	if(daysToDisplay >= i){
    		daysToDisplay = i;
    	}else{
    		daysToDisplay = month;
    	}
    	
        if (cursor.moveToFirst()) {
            do {
            	id++;           	
            	if (id == daysToDisplay){
            		empName = Long.parseLong(cursor.getString(cursor.getColumnIndex("timestamp")));
            	}
            	            	
            } while (cursor.moveToNext());
        }       
        return empName;
	}	
}
