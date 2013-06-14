package com.agenda.utils;
import java.util.ArrayList;
import com.agenda.database.DatabaseConnector;
import com.agenda.layout.R;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class CustomAdapter  extends BaseAdapter
{
	public  ArrayList<EventsLists> dataEvents = new ArrayList<EventsLists>();
	private Context mContext;
	public CustomAdapter(Context c, DisplayMetrics metrics) 
    {
        mContext = c; 
        dataEvents.removeAll(dataEvents);
        UpdateDataList(dataEvents);
    }
	//day.set(Calendar.DAY_OF_MONTH, Integer.valueOf(strDay.split(" ")[1].split("/")[0]));	
	@Override
	public int getCount() {
		return dataEvents.size();
	}

	@Override
	public Object getItem(int position) {
		return dataEvents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder
	{
		
		public TextView dayNames;
		public TextView dayNumbers;
		public TextView eventTitles;
		public TextView hourStarts;
		public ImageView imgEvents;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder view;
		LayoutInflater inflator  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null)
		{
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.agenda_today_events_detail, null);	
		//	view.dayNames=(TextView)convertView.findViewById(R.id.daysName);
		//	view.imgEvents=(ImageView)convertView.findViewById(R.id.eventPriorite);
			//view.dayNumbers=(TextView)convertView.findViewById(R.id.dayNumber);
			//view.eventTitles=(TextView)convertView.findViewById(R.id.eventTitle);
			//view.hourStarts=(TextView)convertView.findViewById(R.id.hourStart);
			convertView.setTag(view);
			
		}
		else
		{
		view = (ViewHolder) convertView.getTag();
		}		
		
		view.dayNames.setText(dataEvents.get(position).name);
		view.imgEvents.setImageResource(R.drawable.green_dots);
		//view.dayNumbers.setText("ddd");
		//view.eventTitles.setText("dmfsd");
		//view.hourStarts.setText("dsda");
		return convertView;
	}
	

	public class EventsLists {
		String name;
		String numbers;
		String titles;
		String hour;

		public EventsLists(String nam, String num,String titl,String hours) {
			name = nam;
			numbers = num;
			titles=titl;
			hour=hours;
		}
	}

	public void UpdateDataList(ArrayList<EventsLists> sdata2) {
							        
	}
	 
	 
}

