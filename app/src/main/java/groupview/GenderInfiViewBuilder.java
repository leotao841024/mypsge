package groupview;

import com.example.renrenstep.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GenderInfiViewBuilder extends InfoViewBuilder implements OnClickListener {
	private LinearLayout linear_woman,linear_man;
	private TextView tv_man,tv_woman;
	private String[] res;
	public GenderInfiViewBuilder(Context context, EventListener listener) {
		super(context, listener); 
	}
	@Override
	protected int getLayoutID() { 
		return R.layout.popup_sex;
	}
	@Override
	protected int getCancelID() { 
		return R.id.tv_cancel;
	}
	@Override
	protected void findView(View view) {
		linear_woman=(LinearLayout)view.findViewById(R.id.linear_woman);
		linear_man=(LinearLayout)view.findViewById(R.id.linear_man);
		tv_man=(TextView)view.findViewById(R.id.tv_man);
		tv_woman=(TextView)view.findViewById(R.id.tv_woman);
		linear_woman.setOnClickListener(this);
		linear_man.setOnClickListener(this);
	}
	public void setColor(int colval){ 
		tv_man.setTextColor(colval);
		tv_woman.setTextColor(colval); 
	}
	
	public void setResource(String pitem1,String pitem2){
		tv_man.setText(pitem1);
		tv_woman.setText(pitem2);
	}
	public void setResult(String[] res){
		this.res = res;
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
			case R.id.linear_man:
				listener.onConfirm(res[0]);
				popupWindow.dismiss();
				break;
			case R.id.linear_woman:
				listener.onConfirm(res[1]);
				popupWindow.dismiss();
				break;
			default:
				popupWindow.dismiss();
				break;
		}
	} 
}
