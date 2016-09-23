package groupview; 
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import tools.ToastManager;

import com.example.renrenstep.R;
import helper.BaseAsyncTask;

import constant.Cons;
import groupview.InfoViewBuilder.EventListener; 
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText; 
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PassWordViewHolder extends SlidingMenuViewHolder {
	private EditText et_old_pwd;
	private EditText et_first_npwd;
	private EditText et_last_npwd;
	private TextView tv_finish_pwd;
	private RelativeLayout layout;  
	public PassWordViewHolder(Activity context, EventListener listener) {
		super(context, listener);
	} 
	@Override
	protected int getLayoutID() { 
		return R.layout.sliding_secret;
	} 
	@Override
	protected int getCancelID() { 
		return R.id.ll_secret_exit;
	}
	
	public void setColor(int colval){
		layout.setBackgroundColor(colval);
	}
	private String getNewPassword(){
		return tv_finish_pwd.getText().toString();
	}
	
	@Override
	protected void findView(View view) { 
		et_old_pwd = (EditText) view.findViewById(R.id.et_old_pwd);
		et_first_npwd = (EditText) view.findViewById(R.id.et_first_npwd);
		et_last_npwd = (EditText) view.findViewById(R.id.et_last_npwd);
	    layout = (RelativeLayout) view.findViewById(R.id.layout_actionbar);
		tv_finish_pwd = (TextView) view.findViewById(R.id.tv_finish_pwd);
		tv_finish_pwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkPassword()){ 
//					passListner.onConfirm(et_old_pwd.getText().toString(), et_first_npwd.getText().toString());
					alterPassword(et_old_pwd.getText().toString(), et_first_npwd.getText().toString());
				}
			}
		});  
	}
	boolean checkPassword(){
		String firstPwd = et_first_npwd.getText().toString().trim();
		String lastPwd = et_last_npwd.getText().toString().trim();
		String oldpwd = et_old_pwd.getText().toString().trim();
		if (oldpwd.equals("")) {
			Toast.makeText(context,context.getResources().getString(R.string.oldpwdisnull), 2000).show();
			return false;
		}
		if (firstPwd.equals("") || lastPwd.equals("")) {
			Toast.makeText(context,context.getResources().getString(R.string.newpwdisnull), 2000).show();
			return false;
		}
		if (!firstPwd.equals(lastPwd)) {
			Toast.makeText(context,context.getResources().getString(R.string.confirm_secret), 2000).show();
			return false;
		}
		if (firstPwd.length() < 6) {
			ToastManager.show(context,context.getResources().getString(R.string.minpwdlen), 2000);
			return false;
		}
		return true;
	}
	void alterPassword(String oldpwd,String newpwd){
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("opwd", oldpwd);
		maps.put("npwd", newpwd);  
		new BaseAsyncTask(Cons.CONF_SECRET, maps, BaseAsyncTask.HttpType.Post, "",context) {
			@Override
			public void handler(String param) { 
				if (param != null) {
					try {
						JSONObject jsonObject = new JSONObject(param);
						int status = jsonObject.getInt("status");
						String description = jsonObject.getString("description");
						if (status == 0) {
							slidingMenu.toggle();
							listener.onConfirm(getNewPassword());
							Toast.makeText(context,description + "", Toast.LENGTH_LONG).show();
						} else {
						} 
						Toast.makeText(context, description + "",Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						Toast.makeText(context,context.getResources().getString(R.string.wangluoyic), 2000).show();
					}
				}
			}
		}.execute("");
	}
	public void hidden(){
		slidingMenu.toggle();
	}
}
