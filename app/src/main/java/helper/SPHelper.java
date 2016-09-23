package helper;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import constant.Cons;

public class SPHelper {
	
	static SharedPreferences getBasePre (Context context){
		String mid= getBaseMsg(context,"mid","mid");	
		SharedPreferences sp = context.getSharedPreferences(mid, Context.MODE_PRIVATE);
		return sp;
	}

	public static String getBaseMsg(Context context, String key,String defValue){
		SharedPreferences sp = context.getSharedPreferences(Cons.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	
	public static String getBaseMsg(Context context,String name, String key,String defValue){
		SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	public static int getBaseMsg(Context context, String key,int defValue){
		SharedPreferences sp = context.getSharedPreferences(Cons.USER_INFO, Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	
	public static void setBaseMsg(Context context, String key,String value){
		SharedPreferences sp = context.getSharedPreferences(Cons.USER_INFO, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static void setBaseMsg(Context context, String name, String key,String value){
		SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	public static void setBaseMsg(Context context, String key,int value){
		SharedPreferences sp = context.getSharedPreferences(Cons.USER_INFO, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	
	public static void setDetailMsg(Context context,String key,String value){
		Editor edit = getBasePre(context).edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	
	public static void setDetailMsg(Context context,String key,Boolean value){
		Editor edit = getBasePre(context).edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	
	public static void setDetailMsg(Context context,String key,int value){
		Editor edit = getBasePre(context).edit();
		edit.putInt(key, value);
		edit.commit();
	}
	
	public static void setDetailMsg(Context context,String key,Long value){
		Editor edit = getBasePre(context).edit();
		edit.putLong(key, value);
		edit.commit();
	}
	
	public static String getDetailMsg(Context context,String key,String defValue){
		return getBasePre(context).getString(key, defValue);
	}
	
	public static Boolean getDetailMsg(Context context,String key,Boolean defValue){
		return getBasePre(context).getBoolean(key, defValue);
	}
	
	public static int getDetailMsg(Context context,String key,int defValue){
		return getBasePre(context).getInt(key, defValue);
	}
	
	public static Long getDetailMsg(Context context,String key,Long defValue){
		return getBasePre(context).getLong(key, defValue);
	}
	
	public static Set<String> getDetailMsg(Context context,String key,Set<String> defValue){
		return getBasePre(context).getStringSet(key, defValue);
	}
	
	public static void setDetailMsg(Context context,String key,Set<String> value)
	{
		Editor edit = getBasePre(context).edit();
		edit.putStringSet(key, value); 
		edit.commit();
	}
	
	public static void delDetailMsg(Context context,String key)
	{
		Editor edit = getBasePre(context).edit();
		edit.remove(key);
		edit.commit();
	} 
	/*
	public static void setBoolean(Context context, String key, Boolean value){
		String mid = getString(context, Cons.USER_INFO, "mid", "mid");
		SharedPreferences sp = context.getSharedPreferences(mid, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	public static Boolean getBoolean(Context context, String key, Boolean defValue){
		String mid = getString(context, Cons.USER_INFO, "mid", "mid");
		SharedPreferences sp = context.getSharedPreferences(mid, Context.MODE_PRIVATE);
		Boolean str = sp.getBoolean(key, defValue);
		return str;
	}
	public static void setString(Context context, String key, String value){
		String mid = getString(context, Cons.USER_INFO, "mid", "mid");
		SharedPreferences sp = context.getSharedPreferences(mid, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	public static void setString(Context context, String spName, String key, String value){
		SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public static String getString(Context context, String key, String defValue){
		String mid = getString(context, Cons.USER_INFO, "mid", "mid");
		SharedPreferences sp = context.getSharedPreferences(mid, Context.MODE_PRIVATE);
		String str = sp.getString(key, defValue);
		return str;
	}
	public static String getString(Context context, String spName, String key, String defValue){
		SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		String str = sp.getString(key, defValue);
		return str;
	}
	
	public static void setInt(Context context, String key, Integer value){
		String mid = getString(context, Cons.USER_INFO, "mid", "mid");
		SharedPreferences sp = context.getSharedPreferences(mid, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	
	public static Integer getInt(Context context, String key, Integer defValue){
		String mid = getString(context, Cons.USER_INFO, "mid", "mid");
		SharedPreferences sp = context.getSharedPreferences(mid, Context.MODE_PRIVATE);
		int str = sp.getInt(key, defValue);
		return str;
	}
	*/
}



