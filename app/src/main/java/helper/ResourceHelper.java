package helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import app.MyApplication;

public class ResourceHelper {
	private static Context getContext(){
		return MyApplication.getInstance();
	}
	
	public static String getStringValue(int resId){
		return  getContext().getString(resId);
	}
	
	public static Drawable getDrawable(int resId){
		return getContext().getResources().getDrawable(resId);
	}
	
}

