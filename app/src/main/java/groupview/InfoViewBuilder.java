package groupview;

import com.example.renrenstep.R;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

public abstract class InfoViewBuilder {

	public interface EventListener<T>{
		void onConfirm(T result);
		void onCancel();
	}
	
	public abstract class DefaultListener implements EventListener{
		@Override
		public void onCancel() {	
		}
	}
	
    View view;
	Context context;
	View v_cancel;
	PopupWindow popupWindow;
	protected EventListener listener;
	
	public InfoViewBuilder(Context context, EventListener listener) {
		this.context = context;
		this.listener=listener;
	}
	
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
	    		popupWindow.dismiss();
	    	}
	    });
	    findView(view);
	}
	public void popup(int width,int height,int gravity, int x, int y){
		popupWindow = new PopupWindow(view,width,height, true);
		popupWindow.setFocusable(true); 
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		ColorDrawable dw = new ColorDrawable(-00000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAtLocation(view, gravity, x, y);
	}

}

