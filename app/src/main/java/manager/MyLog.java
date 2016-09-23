package manager;

import java.util.Date;
import java.util.List;

import db.SysLog;
import android.util.Log;

public class MyLog { 
	public enum LogLevel{DEBUG,INFO,WARNING,ERROR}
	
	public static List<SysLog> getLogs(LogLevel level){
		return new SysLog().find(SysLog.class, "typ>=?","id desc","","",level.ordinal()+"");
	}
	
	public static void d(String key, String value) {  
		new SysLog(key,value,new Date().getTime(),LogLevel.DEBUG.ordinal()).save();
		Log.d(key, value);
	}
	
	public static void i(String key, String value) {
		new SysLog(key,value,new Date().getTime(),LogLevel.INFO.ordinal()).save();
		Log.i(key, value);
	} 
	
	public static void w(String key, String value) {
		new SysLog(key,value,new Date().getTime(),LogLevel.WARNING.ordinal()).save();
		Log.w(key, value);
	}
	
	public static void e(String key, String value) {
		new SysLog(key,value,new Date().getTime(),LogLevel.ERROR.ordinal()).save();
		Log.e(key, value);
	}
	
}
