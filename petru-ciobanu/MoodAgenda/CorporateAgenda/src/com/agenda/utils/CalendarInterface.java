package com.agenda.utils;

public interface CalendarInterface {
	  public interface DateDialogFragmentListener{
	    	//this interface is a listener between the Date Dialog fragment and the activity to update the buttons date
	    	public void updateChangedDate(int year, int month, int day);
	    }
	    
	    
	    
	    public interface TimeDialogFragmentListener{
	    	//this interface is a listener between the Date Dialog fragment and the activity to update the buttons date
	    	public void updateChangedTime(int hour, int minute);
	    }
	    
	    
	  
}
