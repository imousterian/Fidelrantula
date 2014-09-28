package com.example.fidelrantula;

import java.util.ArrayList;
import java.util.List;

public class LinearRegression {
	
	public LinearRegression(){
	}
	
	public ArrayList<Double> calculateRegression(List<Measurement> values){
		
		ArrayList<Double> results = new ArrayList<Double>();		
		int N = values.size();		
		double xSum = 0.0, ySum = 0.0, xSquaredSum = 0.0, xySum = 0.0;   	
		for(Measurement m:values)
    	{
    		double x = (double)m.getID();
    		double y = (double)m.getWeight();
    		xSum += x;
    		ySum += y;
    		xSquaredSum += x * x;
    		xySum += x * y;  		    				
    	}
		
		double m = ((N * xySum) - (xSum * ySum)) / ((N * xSquaredSum) - (xSum * xSum));
		results.add(m);		
		double b = ((xSquaredSum * ySum) - (xSum * xySum)) / ((N * xSquaredSum) - (xSum * xSum));
		results.add(b);
		
		return results;
	}
}
