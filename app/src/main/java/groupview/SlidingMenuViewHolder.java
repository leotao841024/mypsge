package groupview;

import groupview.InfoViewBuilder.EventListener;
 
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener; 

public abstract class SlidingMenuViewHolder  { 
	public SlidingMenuViewHolder(Activity context, EventListener listener) {
		this.context = context;
		this.listener=listener;
	}  
//	public abstract class DefaultListener implements EventListener{
//		@Override
//		public void onCancel() {	
//		}
//	} 
    View view;
	Activity context;
	View v_cancel;
	SlidingMenu slidingMenu;
	protected EventListener listener; 
	protected abstract int getLayoutID();
	protected abstract int getCancelID();
	
	protected abstract void findView(View view);
	
	public void setView(){		
		view = LayoutInflater.from(context).inflate(getLayoutID(), null);
		v_cancel = view.findViewById(getCancelID());
		v_cancel.setOnClickListener(new OnClickListener(){
	    	@Override
			public void onClick(View v) {
	    		listener.onCancel();
	    		slidingMenu.toggle();
	    	}
	    }); 
	    findView(view);
	}
	
	public void popup(){
		slidingMenu=new SlidingMenu(context);
		slidingMenu.setMode(SlidingMenu.RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.attachToActivity(context, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setBehindWidth(context.getWindowManager().getDefaultDisplay().getWidth()); 
		slidingMenu.setSecondaryMenu(view);
		slidingMenu.toggle();
	} 
}
