package manager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class SlidingMenuManager { 
	public static SlidingMenu getFullRightSlidingMenu(View view,Activity activity){
		SlidingMenu	menu=new SlidingMenu(activity);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); 
		menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
		menu.setBehindWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setSecondaryMenu(view);
		return menu;
	}
}
