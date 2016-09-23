package groupview;

import java.util.List;

import com.example.renrenstep.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemInfoViewBuilder extends InfoViewBuilder {
	View v_confirm;
	view.PickerView pick_view;TextView tv_unit;RelativeLayout rel_age_pop;
	public MemInfoViewBuilder(Context context, EventListener listener) {
		super(context, listener);
	}
	public void setDataSource(List<String> source){
		pick_view.setData(source);
	}
	public void setSelectedVal(String pval){
		pick_view.setSelected(pval);
	}
	
	public String getSelectedValue(){
		return pick_view.getText();
	}
	
	public void setUnit(String pval){
		tv_unit.setText(pval);
	}
	
	@Override
	protected int getLayoutID() {
		return R.layout.popup_age;
	}
	
	@Override
	protected int getCancelID() {
		return R.id.tv_cancel;
	}
	public void setColor(int colval){
		rel_age_pop.setBackgroundColor(colval);
		tv_unit.setTextColor(colval);
		pick_view.setTextColor(colval);
		pick_view.initPaintStyle();
	}
	@Override
	protected void findView(View view) {
		v_confirm = view.findViewById(R.id.tv_confirm);
		pick_view=(view.PickerView)view.findViewById(R.id.pv_age);
		tv_unit=(TextView)view.findViewById(R.id.tv_unit);
		rel_age_pop= (RelativeLayout)view.findViewById(R.id.rel_age_pop);
		v_confirm.setOnClickListener(new OnClickListener(){
	    	@Override
			public void onClick(View v) {
	    		listener.onConfirm(getSelectedValue());
	    		popupWindow.dismiss();
	    	}
	    });
	} 
//	public void call(){
//	   final AgeInfoViewBuilder builder = new AgeInfoViewBuilder(this.context, new DefaultListener(){
//
//			@Override
//			public void onConfirm() {
//				builder.getSelectedValue();
//			} 
//		});
//		builder.setDataSource();
//		builder.popup(); 
//	}
}