package com.agenda.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.agenda.layout.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.sax.TextElementListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DayAdapter extends BaseAdapter {
	private GregorianCalendar mCalendar;
	private Calendar mCalendarToday;
	private Context mContext;
	private DisplayMetrics mDisplayMetrics;
	private List<String> mItems;
	private List<String>mDayName;
	private int mMonth;
	private int mYear;
	private MonthDisplayHelper mHelper;
	
	private final int[] mDaysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	
	public DayAdapter(Context c, int month, int year, DisplayMetrics metrics) {
		mContext = c;
		mMonth = month;
		mYear = year;
		mCalendar = new GregorianCalendar(mYear, mMonth, 1);
		mCalendarToday = Calendar.getInstance();
		mDisplayMetrics = metrics;
		populateMonth();
	}
	
	protected void onDate(int[] date, int position, View item) {
	}
	
	private void populateMonth() {
		mItems = new ArrayList<String>();	
		mDayName= new ArrayList<String>();
		mCalendarToday = Calendar.getInstance();
		int  firstDay=mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		for (int i=1;i<=firstDay;i++) {
			mItems.add(String.valueOf(i));
			mDayName.add(returnName(i));
		}
		
		Log.i(getClass().getSimpleName(), "TotalDayofWeek!!"+	firstDay);
	}
	
	
	private String returnName(int day) {
		switch (day) {
		case 0:
			return "MON";
		case 1:
			return "TUE";
		case 2:
			return "WED";
		case 3:
			return "THU";
		case 4:
			return "FRI";
		case 5:
			return "SAT";
		case 6:
			return "SUN";
		default:
			return "";
		}

	}
	
	
	private int getDay(int day) {
		switch (day) {
		case Calendar.MONDAY:
			return 0;
		case Calendar.TUESDAY:
			return 1;
		case Calendar.WEDNESDAY:
			return 2;
		case Calendar.THURSDAY:
			return 3;
		case Calendar.FRIDAY:
			return 4;
		case Calendar.SATURDAY:
			return 5;
		case Calendar.SUNDAY:
			return 6;
		default:
			return 0;
		}
	}


	public static class ViewHolder
	{
		public TextView   dayContent;
		public TextView  dayName;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
     	 ViewHolder view = null;
		 LayoutInflater inflater  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(convertView==null)
		{
			view = new ViewHolder();
			convertView = inflater.inflate(R.layout.agenda_today_events_detail, null);
		//	view.dayContent = (TextView) convertView.findViewById(R.id.dayDate);
		//	view.dayName=(TextView)convertView.findViewById(R.id.daysName);
			convertView.setTag(view);		
		}
		else
		{
			
		view = (ViewHolder) convertView.getTag();
		}	
		 view.dayContent.setText(mItems.get(position));
		 view.dayName.setText(mDayName.get(position));
		 
		return convertView;
	}
	
	
	
	
	
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
