package com.agenda.utils;
import com.agenda.layout.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CursorTodayEvents  extends CursorAdapter {
	@SuppressWarnings("deprecation")
	public CursorTodayEvents(Context context, Cursor c) {
		super(context, c);
	}
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView c_eventPriorite=(ImageView)view.findViewById(R.id.eventPriorite);
		TextView c_eventTitle = (TextView) view.findViewById(R.id.eventTitle);
		TextView c_hourStart = (TextView) view.findViewById(R.id.hourStart);
		TextView c_hourEnd = (TextView) view.findViewById(R.id.hourEnd);
		String s_libelle =cursor.getString(cursor.getColumnIndex("evenement_libelle"));
		String s_hourStart=cursor.getString(cursor.getColumnIndex("evenement_heuredebut"));
		String s_hourEnd=cursor.getString(cursor.getColumnIndex("evenement_heurefin"));
		c_eventTitle.setText(s_libelle);
		c_hourStart.setText(s_hourStart);
		c_hourEnd.setText(s_hourEnd);
		String imp=cursor.getString(cursor.getColumnIndex("evenement_priorite"));
		if (imp.toString().contentEquals("0")){
			c_eventPriorite.setImageResource(R.drawable.red_dots);
		}if (imp.toString().contentEquals("1")){
			c_eventPriorite.setImageResource(R.drawable.green_dots);
		}if (imp.toString().contentEquals("2")){
			c_eventPriorite.setImageResource(R.drawable.orange_dots);
		}
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {  
		LayoutInflater infl  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = infl.inflate(R.layout.agenda_today_events_detail, parent, false);
		bindView(v, context, cursor);
		return v;
	}


}

