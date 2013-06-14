package com.agenda.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public String getDate() {
		
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
		String result = simpleDateFormat.format(new Date());
		return result;

	}

}
