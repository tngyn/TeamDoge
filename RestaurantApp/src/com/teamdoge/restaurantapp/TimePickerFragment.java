package com.teamdoge.restaurantapp;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {

	private TimePickedListener mListener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Use the current time as the default values for the picker
	final Calendar c = Calendar.getInstance();
	int hour = c.get(Calendar.HOUR_OF_DAY);
	int minute = c.get(Calendar.MINUTE);
	
	// Create a new instance of TimePickerDialog and return it
	return new TimePickerDialog(getActivity(), this, hour, minute,
	DateFormat.is24HourFormat(getActivity()));
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// set time on view
		Calendar c = Calendar.getInstance();
	    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
	    c.set(Calendar.MINUTE, minute);
	    
	    Log.d("Debugging", this.getArguments().toString());
		mListener.onTimePicked(c, this.getArguments().getInt("sourceId"));
		
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	    try
	    {
	        mListener = (TimePickedListener) activity;
	    }
	    catch (ClassCastException e)
	    {
	        throw new ClassCastException(activity.toString() + " must implement " + TimePickedListener.class.getName());
	    }
	}


}
