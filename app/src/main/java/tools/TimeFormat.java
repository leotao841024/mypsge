package tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {
	public static String LongToDate(long timer,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
	    Date date= new Date(timer);
	    return sdf.format(date);
	} 
	public static String DateToString(Date date,String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
}
