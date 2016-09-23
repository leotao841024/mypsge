package groupview;

import com.example.renrenstep.R;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NameViewBuilder extends InfoViewBuilder{
	private TextView tv_pop_finish;ImageView iv_clear;EditText et_mname;
	private RelativeLayout layout_actionbar;
	public NameViewBuilder(Context context, EventListener listener) {
		super(context, listener);
	}
	@Override
	protected int getLayoutID() { 
		return R.layout.popoup_name;
	} 
	@Override
	protected int getCancelID() { 
		return R.id.ll_name_exit;
	} 
	public void setSource(String source){
		et_mname.setText(source);
	}
	private String getName(){
		return et_mname.getText().toString();
	}
	public void setColor(int colval){
		layout_actionbar.setBackgroundColor(colval);
	}
	@Override
	protected void findView(View view) {
		layout_actionbar=(RelativeLayout)view.findViewById(R.id.layout_actionbar);
		tv_pop_finish=(TextView)view.findViewById(R.id.tv_pop_finish);
		iv_clear=(ImageView)view.findViewById(R.id.iv_clear);
		et_mname=(EditText)view.findViewById(R.id.et_mname);
		iv_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				et_mname.setText("");
				
			}
		});
		tv_pop_finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listener.onConfirm(getName());
				popupWindow.dismiss();
			}
		});


	}
}
