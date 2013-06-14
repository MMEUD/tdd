package com.agenda.utils;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarDay {
    private List<String> mDay;
	public CalendarDay(){	
	}
	@SuppressLint("SimpleDateFormat")
	public String getDayName(String dates) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = fmt.parse(dates);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String dayOfTheWeek =null;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		    if(i == 2){
		        dayOfTheWeek = "Mon";           
		    } else if (i==3){
		        dayOfTheWeek = "Tue";
		    } else if (i==4){
		        dayOfTheWeek = "Wed";
		    } else if (i==5){
		        dayOfTheWeek = "Thu";
		    } else if (i==6){
		        dayOfTheWeek = "Fry";
		    } else if (i==7){
		        dayOfTheWeek = "Sat";
		    } else if (i==1){
		        dayOfTheWeek = "Sun";
		    }	
		return dayOfTheWeek ;
	}
    
	@SuppressWarnings("rawtypes")
	public List currentDay(){
		mDay = new ArrayList<String>();
		TimeZone MyTimezone = TimeZone.getDefault();
		Calendar calendar = new GregorianCalendar(MyTimezone);
		Date currentTime = new Date();
		calendar.setTime(currentTime);
		mDay.add(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase(Locale.getDefault()));
		mDay.add(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).toUpperCase(Locale.getDefault()));
		mDay.add(String.valueOf(calendar.get(Calendar.DATE)));
		return   mDay;
	}
	
	
	

}
