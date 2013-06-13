package com.agenda.utils;

import java.util.Calendar;

import com.agenda.utils.CalendarInterface.TimeDialogFragmentListener;



import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimeDialogFragment extends DialogFragment {

	public static String TAG = "TimeDialogFragment";
	static Context mContext; // I guess hold the context that called it. Needed
	// when making a DatePickerDialog. I guess its
	// needed when conncting the fragment with the
	// context
	static int _Hour;
	static int _Minute;

	static TimeDialogFragmentListener mListener;

	public static TimeDialogFragment newInstance(Context context, TimeDialogFragmentListener listener, Calendar now) {
		TimeDialogFragment dialog = new TimeDialogFragment();
		mContext = context;
		mListener = listener;
		_Hour = now.get(Calendar.HOUR_OF_DAY);
		_Minute = now.get(Calendar.MINUTE);

		/* I dont really see the purpose of the below */
		Bundle args = new Bundle();
		args.putString("title", "Set Time");
		dialog.setArguments(args);// setArguments can only be called before
		// fragment is attached to an activity, so
		// right after the fragment is created

		return dialog;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new TimePickerDialog(mContext, mDateSetListener, _Hour, _Minute, true);
	}

	private TimePickerDialog.OnTimeSetListener mDateSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hour, int minute) {
			_Minute = minute;
			_Hour = hour;
			mListener.updateChangedTime(hour, minute);
		}
	};

}
