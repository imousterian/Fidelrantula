package com.example.fidelrantula;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.example.fidelrantula.Measurement.MeasurementComparator;
import com.example.fidelrantula.Measurement.MeasurementComparatorByID;

public class MeasurementCollection {
	
	Measurement measurement;
	List<Measurement> measurements;

	public MeasurementCollection(){
		measurements = new LinkedList<Measurement>();
	}
	
	public void addToMeasurementCollection(Measurement measurement){
		measurements.add(measurement);
	}
	public List<Measurement> getMeasurementCollection(){
		return measurements;
	}
	public List<Measurement> getSortedMeasurementCollection(){
		Collections.sort(measurements, new MeasurementComparator());  
		return measurements;
	}
	
	public List<Measurement> getSortedMeasurementCollectionByID(){
		Collections.sort(measurements, new MeasurementComparatorByID());  
		return measurements;
	}
	
	public Measurement findMin()
	{
		int lowerValueIndex = 0;
		try{
			int nbElements = measurements.size();			   
			for (int i = 0; i < nbElements; i++)
			{
				if(measurements.get(i).getWeight() < measurements.get(lowerValueIndex).getWeight()){
					lowerValueIndex = i;
			    }
			}
			 
		}catch(NullPointerException e){	}
		
		return measurements.get(lowerValueIndex);   
	}
	
	public Measurement findMax()
	{
		int maxValueIndex = 0;
		int nbElements = measurements.size();
		try{
			for (int i = 0; i < nbElements; i++){
				if(measurements.get(i).getWeight() > measurements.get(maxValueIndex).getWeight()){
					maxValueIndex = i;
					}
				}
			}catch(NullPointerException e){ }
	   
	   return measurements.get(maxValueIndex);
	}
	
	public Measurement findMinDate()
	{
		int nbElements = measurements.size();
		int lowerValueIndex = 0;
		for (int i = 0; i < nbElements; i++) {
			if((measurements.get(i).getYearMonthAsString()).compareTo(measurements.get(lowerValueIndex).getYearMonthAsString()) < 0) {
				lowerValueIndex = i;
			}
		}
	   return measurements.get(lowerValueIndex);
	}
	
	public Measurement findMaxDate()
	{
		int nbElements = measurements.size();
		int maxValueIndex = 0;
		for (int i = 0; i < nbElements; i++) {
			if((measurements.get(i).getYearMonthAsString()).compareTo(measurements.get(maxValueIndex).getYearMonthAsString()) > 0) {
				maxValueIndex = i;
			}
		}
	   return measurements.get(maxValueIndex);
	}
	
	public String determineTitle(Measurement measurement1, Measurement measurement2){
		String values = null;
		
		if(findMinDate().getTimeStamp() == findMaxDate().getTimeStamp()){
			String year = String.valueOf(measurement1.getYear());
			String month = measurement1.getMonthAsString();
			String result = month + " " + year;
			values = result;
		}else{
			int year1 = measurement1.getYear();
			int year2 = measurement2.getYear();
			if (year1 == year2){
				String result = measurement1.getMonthAsString() + " - " + measurement2.getMonthAsString() + " " + String.valueOf(year1);
				values = result;
			}else{
				String result = measurement1.getMonthAsString() + " " + String.valueOf(year1) + " - " 
							+ measurement2.getMonthAsString() + " " + String.valueOf(year2);
				values = result;	
			}
		}
		return values;
	}
}
