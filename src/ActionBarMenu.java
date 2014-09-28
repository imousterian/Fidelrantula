package com.example.fidelrantula;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

@SuppressLint("NewApi")
public class ActionBarMenu extends SherlockFragmentActivity{

	protected static OnItemSelectedListener listener;
	protected EditText userInput;
	private String value;

	private Fragment mFragment1;
	Calendar now;

	DatePicker dp;

	DialogFragment datepicker;

	private Fragment f;

	private int year, month,day;

	protected static boolean allClicked = true, monthClicked, rangeClicked;

	protected static int tapChecker2, tapCheckerAverage;

	protected static int choice = 2;

	public void setChoice(int c){
		ActionBarMenu.choice = c;
	}

	public int getChoice(){
		return choice;
	}

	public interface OnItemSelectedListener {
        public void onButtonLinearTrendSelected();
        public void onButtonValueAdded(String input, String txtDate);
        public void onClickChartDisplayUpdated();
        public void onButtonMovingAverageSelected();
        public void onButtonValueDeleted();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    FragmentManager fm = getSupportFragmentManager();
	    FragmentTransaction ft = fm.beginTransaction();
	    mFragment1 = fm.findFragmentByTag("f1");

	    if (mFragment1 == null) {
	    	mFragment1 = new MenuFragment();
	        ft.add(mFragment1, "f1");
	    }
	    ft.commit();
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId())
	    {
	    	case R.id.m_add:
	    		setInputValue();
	    		return true;
	    	case R.id.m_delete_last:
	    		listener.onButtonValueDeleted();
	    		return true;
	    	case R.id.m_delete_all:
	    		return true;
	    	case R.id.m_stats_trend:
	    		listener.onButtonLinearTrendSelected();
	    		if(tapChecker2 == 0){
	    			tapChecker2 = 1;
	    		}else{
	    			tapChecker2 = 0;
	    		}
	            return true;
	    	case R.id.m_stats_average:
	    		listener.onButtonMovingAverageSelected();
	    		if(tapCheckerAverage == 0){
	    			tapCheckerAverage = 1;
	    		}else{
	    			tapCheckerAverage = 0;
	    		}
	            return true;
	    	case R.id.m_dates_all:
	    		this.setChoice(1);
	    		listener.onClickChartDisplayUpdated();
	    		return true;
	    	case R.id.m_dates_month:
	    		this.setChoice(2);
	    		listener.onClickChartDisplayUpdated();
	    		return true;
	    	case R.id.m_dates_calendar:
	    		this.setChoice(3);
	    		showDatePickerDialogMyDate();
	    		return true;
	    	default:
	    		return super.onOptionsItemSelected(item);
	    }
	}

	public void showDatePickerDialogMyDate()
	{
		FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        f = fm1.findFragmentByTag("f1a");

        if (f == null) {
        	f = new MyDatePicker();
            ft1.add(f, "f1a");
        }
        ft1.commit();
	}

	public void setInputValue()
	{
		View promptsView = LayoutInflater.from(this).inflate(R.layout.edit_text, null);
		userInput = (EditText) promptsView.findViewById(R.id.input);

		final DatePicker datePicker3 = (DatePicker) promptsView.findViewById(R.id.d_picker3);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

        datePicker3.setCalendarViewShown(false);
        datePicker3.init(year, month, day, null);

		new AlertDialog.Builder(this)
			.setView(promptsView)
	    	.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {
	    			value = userInput.getText().toString();
	    			SimpleDateFormat dateViewFormatter = new SimpleDateFormat("yyyyMMdd");
	    			c.set(datePicker3.getYear(),datePicker3.getMonth(),datePicker3.getDayOfMonth());
	    			String txtDate = dateViewFormatter.format(c.getTime());
	    			listener.onButtonValueAdded(value,txtDate);
	    		}
	    	}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {
	    			// Do nothing.
	    		}
	    	}).show();

	}

	public static class MenuFragment extends SherlockFragment {
        @Override
	    public void onAttach(Activity activity) {
	      super.onAttach(activity);
	      if (activity instanceof OnItemSelectedListener) {
	        listener = (OnItemSelectedListener) activity;
	      } else {
	        throw new ClassCastException(activity.toString()
	            + " must implement OnItemSelectedListener");
	      }
	    }
    }

	public static class MyDatePicker extends DialogFragment{

		protected static int year;
		protected static int month;
		protected static int day;

		protected static ArrayList<String> dates;

		public ArrayList<String> getDatesFromPicker(){
			return dates;
		}

		public void setDatesFromPicker(String date){
			dates.add(date);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

		    // Get the layout inflater
		    LayoutInflater inflater = getActivity().getLayoutInflater();

		    final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

		    final AlertDialog.Builder dialogBuilder =  new AlertDialog.Builder(getActivity());

		    View customView = inflater.inflate(R.layout.date_picker, null);
		    dialogBuilder.setView(customView);

	        final DatePicker datePicker1 = (DatePicker) customView.findViewById(R.id.d_picker1);
	        final DatePicker datePicker2 = (DatePicker) customView.findViewById(R.id.d_picker2);

	        datePicker1.setCalendarViewShown(false);
	        datePicker1.init(year, month, day, null);

	        datePicker2.setCalendarViewShown(false);
	        datePicker2.init(year, month, day, null);

	        dialogBuilder.setNeutralButton("DONE", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					SimpleDateFormat dateViewFormatter = new SimpleDateFormat("yyyyMMdd");

					dates = new ArrayList<String>();

					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, datePicker1.getYear());
					c.set(Calendar.MONTH, datePicker1.getMonth());
					c.set(Calendar.DAY_OF_MONTH, datePicker1.getDayOfMonth());

					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

					String date1 = formatter.format(c.getTime());

					setDatesFromPicker(date1);

					c.set(Calendar.YEAR, datePicker2.getYear());
					c.set(Calendar.MONTH, datePicker2.getMonth());
					c.set(Calendar.DAY_OF_MONTH, datePicker2.getDayOfMonth());

					String date2 = formatter.format(c.getTime());
					setDatesFromPicker(date2);

					listener.onClickChartDisplayUpdated();
				}
			});

	        final AlertDialog dialog = dialogBuilder.create();

	        return dialog;
		}
	}
}
