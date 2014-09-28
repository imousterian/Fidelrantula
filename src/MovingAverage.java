package com.example.fidelrantula;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MovingAverage {
	private int _window;
	private Queue<Double> samples = new LinkedList<Double>();
	private double sum;

	public MovingAverage(int window){
		setWindow(window);
	}

	public void setWindow(int window){
		this._window = window;
	}

	public int getWindow(){
		return this._window;
	}

	public void addValue(double value){
		sum += value;
		samples.add(value);
		if(samples.size() > getWindow()){
			sum -= samples.remove();
		}
	}

	public double getAverage(){
		if(samples.size() == 0){
			return 0;
		}
		return sum/samples.size();
	}

	public double getAverageResults(double value){
		addValue(value);
		return getAverage();
	}

	public ArrayList<Double> calculateAveragesForDisplay(List<Measurement> values){
		ArrayList<Double> results = new ArrayList<Double>();
		for(Measurement m:values)
    	{
			double y = (double)m.getWeight();
			addValue(y);
			double x = getAverage();
			results.add(x);
    	}
		return results;
	}
}
