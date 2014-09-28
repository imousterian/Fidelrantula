package com.example.fidelrantula;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FidelrantulaMain extends ActionBarMenu implements ActionBarMenu.OnItemSelectedListener
{
	// The main renderer that includes all the renderers customizing a chart.
    private XYMultipleSeriesRenderer mMainRenderer = new XYMultipleSeriesRenderer(2);
    // The chart view that displays the data.
    private GraphicalView mChartView;
    // The main dataset that includes all the series that go into a chart.
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    // colors for the graph
    private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.RED };
    // series for linear trend
    private XYSeries mLinearTrendSeries;
    // The most recently added series.
    private XYSeries mDataSeries;
    // series for moving average
    private XYSeries mMovingAverageSeries;
    // xy-renderers for types of graphs
	private XYSeriesRenderer linearTrendRenderer = new XYSeriesRenderer();
	private XYSeriesRenderer mDataRenderer = new XYSeriesRenderer();
	private XYSeriesRenderer movingAverageRenderer = new XYSeriesRenderer();

	protected DBHandler dbhandler;

	private TextView title;
	private static Measurement lastMeasurement;

	public FidelrantulaMain(){
	   super();
	}

	@Override
	protected void onResume() {
	   super.onResume();
	   dbhandler.open();
	}

	@Override
	protected void onPause() {
	   super.onPause();
	   dbhandler.close();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fidelrantula_main);

        title = (TextView) findViewById(R.id.id_title);

        dbhandler = new DBHandler(this,null,null,1);
        dbhandler.open();

        mMainRenderer.setApplyBackgroundColor(true);
        mMainRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));

        mMainRenderer.setChartTitleTextSize(35);
        mMainRenderer.setLabelsTextSize(25);
        mMainRenderer.setLegendTextSize(25);
        mMainRenderer.setMargins(new int[] { 30, 30, 100, 30 });
        mMainRenderer.setXTitle("\n\n\n\n\n Days");
        mMainRenderer.setAxisTitleTextSize(25);
        mMainRenderer.setXLabels(0);

	    if (dbhandler.checkDBexists() == 0){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	    	alertDialog.setTitle("Welcome to Fidelrantula Measurerer!");
	    	alertDialog.setMessage("Please start by entering new data");
	    	alertDialog.setPositiveButton("Ok", null);
	    	AlertDialog dialog = alertDialog.show();
	    	TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
	    	messageText.setGravity(Gravity.CENTER);
	    }

	    // create a chart if it doesn't exist or refresh it
	    if (mChartView == null){
  	      	LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
  	      	mChartView = ChartFactory.getLineChartView(this, mDataset, mMainRenderer);
  	      	layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
  	    }else {
            mChartView.repaint();
  	    }

	    XYSeries series = new XYSeries("Little Fatso's weight \n\n\n\n");
    	mDataRenderer.setLineWidth(2f);
    	mDataRenderer.setColor(COLORS[4]);
        mDataset.addSeries(series);
        mDataSeries = series;
        // create a renderer for the data series
    	mMainRenderer.addSeriesRenderer(mDataRenderer);

    	// rendering linear trend series
    	XYSeries series2 = new XYSeries("Linear trend \n\n\n\n");
	    mDataset.addSeries(series2);
	    mLinearTrendSeries = series2;
    	mMainRenderer.addSeriesRenderer(linearTrendRenderer);
    	linearTrendRenderer.setLineWidth(1f);
	    linearTrendRenderer.setColor(COLORS[0]);

	    // rendering moving average series
    	XYSeries maverage = new XYSeries("Moving average \n\n\n\n");
	    mDataset.addSeries(maverage);
	    mMovingAverageSeries = maverage;
    	mMainRenderer.addSeriesRenderer(movingAverageRenderer);
    	movingAverageRenderer.setLineWidth(1f);
    	movingAverageRenderer.setColor(COLORS[1]);

	    this.updateChart();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
	}

	@Override
	public void onButtonValueAdded(String input, String txtDate) {

		if (!input.matches("")){
			int weight = Integer.parseInt(input);
			DateInputHandler dih = new DateInputHandler();
			long unixtime = dih.strDateToUnixTimestamp(txtDate);
			Measurement measurement = new Measurement(unixtime,weight);
			lastMeasurement = measurement;
			dbhandler.addMeasurement(measurement);
			this.updateChart();
		}else{
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setMessage("Please enter a valid number");
			alertDialog.setPositiveButton("Ok", null);
			AlertDialog dialog = alertDialog.show();
			TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
		}
	}

	@Override
	public void onButtonValueDeleted(){
		boolean result = dbhandler.deleteMeasurement(lastMeasurement);
		if(result){
			this.updateChart();
		}
	}

	@Override
	public void onButtonLinearTrendSelected() {
		mLinearTrendSeries.clear();
		linearTrendRenderer.setShowLegendItem(false);

		if(tapChecker2 == 0){
			List<Measurement> measurements = dbhandler.getListOfMeasurements();
			this.createLinearTrend(measurements);
			linearTrendRenderer.setShowLegendItem(true);
		}
		mChartView.repaint();
	}

	@Override
	public void onButtonMovingAverageSelected() {
		mMovingAverageSeries.clear();
		movingAverageRenderer.setShowLegendItem(false);

		if(tapCheckerAverage == 0){
			List<Measurement> measurements = dbhandler.getListOfMeasurements();
			this.createMovingAverage(measurements);
			movingAverageRenderer.setShowLegendItem(true);
		}
		mChartView.repaint();
	}

	@Override
	public void onClickChartDisplayUpdated() {
		this.updateChart();
	}

	public void createLinearTrend(List<Measurement> measurements){
		ArrayList<Double> regression = new LinearRegression().calculateRegression(measurements);

		for (Measurement m: measurements){
			double x = (double)m.getID();
  	  		double y = regression.get(0) * x + regression.get(1);
  	  		mLinearTrendSeries.add(x,y);
		}
	}

	public void createMovingAverage(List<Measurement> measurements){
		MovingAverage ma = new MovingAverage(3);
		ArrayList<Double> averages = ma.calculateAveragesForDisplay(measurements);
		int b = 0;
		for(double m: averages){
			b++;
			mMovingAverageSeries.add(b,m);
		}
	}

    public void updateChart(){

    	mDataSeries.clear();
    	mMainRenderer.clearXTextLabels();
    	mLinearTrendSeries.clear();
    	mMovingAverageSeries.clear();

    	List<Measurement> measurements = dbhandler.getListOfMeasurements();

    	for (Measurement m: measurements)
    	{
			double x = (double)m.getID();
  	  		double y = (double)m.getWeight();
  	  		String day = String.valueOf(m.getDay());
  	  		mDataSeries.add(x,y);
  	  		mMainRenderer.addXTextLabel(x,day);
		}

    	Measurement measurement1 = new Measurement();
    	Measurement measurement2 = new Measurement();

    	if(measurements.size() != 0)
    	{
    		measurement1 = DBHandler.measurements.findMin();

        	int minYRange = measurement1.getWeight();
        	measurement2 = DBHandler.measurements.findMax();
        	int maxYRange = measurement2.getWeight();
        	int countRows = measurements.size();

        	mMainRenderer.setRange(new double[] { 0, countRows + 1, minYRange - (int)(0.01 * minYRange), maxYRange + (int)(0.01*maxYRange) });

        	measurement1 = DBHandler.measurements.findMinDate();
        	measurement2 = DBHandler.measurements.findMaxDate();

        	String titleString = DBHandler.measurements.determineTitle(measurement1,measurement2);
        	title.setText("\n" + titleString);
    	}

    	if(tapChecker2 == 1)
    	{
    		this.createLinearTrend(measurements);

    	}else{
    		linearTrendRenderer.setShowLegendItem(false);
    	}

    	if(tapCheckerAverage == 1){
    		this.createMovingAverage(measurements);
    	}else{
    		movingAverageRenderer.setShowLegendItem(false);
    	}

    	mChartView.repaint();
    }
}
