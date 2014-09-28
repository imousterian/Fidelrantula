package com.example.fidelrantula;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;

public class DateInputHandler {
	
	protected static ArrayList<Long> datesHandler = new ArrayList<Long>();
	
	public void setDatesFromDateHandler(Long date){
		datesHandler.add(date);
	}
	
	public ArrayList<Long> updateInputDates(DBHandler dbhandler) 
	{
		long unixtime1 = 0;
    	long unixtime2 = 0;
    	
    	datesHandler = new ArrayList<Long>();
  		datesHandler.clear();
  		
  		int item = new ActionBarMenu().getChoice();  		
  		
  		if (item == 1){
  			unixtime1 = dbhandler.findMinDateValue();
    		unixtime2 = dbhandler.findMaxDateValue();	  
  		}
  		if(item == 2){
  			unixtime1 = dbhandler.findValueMinMonth();
    		unixtime2 = dbhandler.findMaxDateValue();
  		}
  		if(item == 3){
  			ActionBarMenu.MyDatePicker mdp = new ActionBarMenu.MyDatePicker();	    		
        	ArrayList<String> listOfRangeDates = mdp.getDatesFromPicker();   

        	if(listOfRangeDates != null){
        		unixtime1 = getRoundedDates(strDateToUnixTimestamp(listOfRangeDates.get(0)), dbhandler);
            	unixtime2 = getRoundedDates(strDateToUnixTimestamp(listOfRangeDates.get(1)), dbhandler);
        	}else{
        		unixtime1 = dbhandler.findMinDateValue();
        		unixtime2 = dbhandler.findMaxDateValue();
        	}	
  		}
	    	    
	    setDatesFromDateHandler(unixtime1);
    	setDatesFromDateHandler(unixtime2);    	
    	return datesHandler;	    
	}
	
	public long getRoundedDates(long date, DBHandler dbhandler){
				
		String query = "SELECT * FROM " + DBHandler.TABLE_MEASUREMENTS + " ORDER BY abs(timestamp - " + date + ")" + " LIMIT 1";
   	 	Cursor cursor = null;
        long empName = 0;       
        try{
       	 	cursor = dbhandler.db.rawQuery(query, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                empName = Long.parseLong(cursor.getString(cursor.getColumnIndex("timestamp")));
            }
            return empName;
        }finally {
            cursor.close();
        }
	}
		
	public long strDateToUnixTimestamp(String dt) {
       
        Date date = null;
        long unixtime;
        
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            date = formatter.parse(dt);
        } catch (ParseException ex) {
 
            ex.printStackTrace();
        }
        unixtime = date.getTime() / 1000L;
        return unixtime;
	}
}
