package com.example.fidelrantula;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class Measurement {

	private int _weight;
	private int _id;
	private long _timestamp;

	public Measurement(){
	}

	public Measurement(int id, long timestamp, int weight){
		this._id = id;
		this._timestamp = timestamp;
		this._weight = weight;
	}

	public Measurement(long timestamp,int weight){
		this._id = 0;
		this._timestamp = timestamp;
		this._weight = weight;
	}

	public void setID(int id){
		this._id = id;
	}

	public int getID(){
		return this._id;
	}

	public void setTimeStamp(long timestamp){
		this._timestamp = timestamp;
	}

	public long getTimeStamp(){
		return this._timestamp;
	}

	public void setWeight(int weight){
		this._weight = weight;
	}

	public int getWeight(){
		return this._weight;
	}

	public Calendar dateToCalendar(){
		final Calendar cal = Calendar.getInstance();
		Date date = new Date();
		date.setTime((long)this._timestamp*1000);
		cal.setTime(date);
		return cal;
	}

	public int getMonth(){
		Calendar cal = dateToCalendar();
		return cal.get(Calendar.MONTH);
	}

	public int getDay(){
		Calendar cal = dateToCalendar();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getYear(){
		Calendar cal = dateToCalendar();
		return cal.get(Calendar.YEAR);
	}

	public String getYearMonthAsString(){
		String year = String.valueOf(getYear());
		String month = String.valueOf(getMonth());
		String result = year + month;
		return result;
	}

	public String getMonthAsString(){
		int m = getMonth();
		String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11 ) {
            month = months[m];
        }
        return month;
	}

	public static class MeasurementComparator implements Comparator<Measurement>{

	    @Override
	    public int compare(Measurement m1, Measurement m2)  {
	    	if(m1.getTimeStamp() > m2.getTimeStamp()){
	            return 1;
	        } else {
	        	return -1;
	        }
	    }
	}

	public static class MeasurementComparatorByID implements Comparator<Measurement>{
	    @Override
	    public int compare(Measurement m1, Measurement m2){
	    	if(m1.getID() < m2.getID()){
	    		return 1;
	        } else {
	        	return -1;
	        }
	    }
	}
}
