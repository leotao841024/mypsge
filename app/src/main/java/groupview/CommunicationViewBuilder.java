package groupview;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import tools.Countdown;
import tools.Countdown.CountdownBack;
import manager.MyLog;

import com.example.renrenstep.R;

import helper.BaseAsyncTask;
import groupview.InfoViewBuilder.EventListener;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class CommunicationViewBuilder extends SlidingMenuViewHolder implements OnClickListener { 
	private RelativeLayout layout_actionbar;
	protected EditText et_sendcode,et_entercode;
	private Button bt_send,bt_conf;
	private TextView tv_emailtitle;
	public CommunicationViewBuilder(Activity context, EventListener listener) {
		super(context, listener);
	}
	@Override
	protected int getLayoutID() {
		return R.layout.bind_phone;
	}
	@Override
	protected int getCancelID() {
		return R.id.ll_phone_exit;
	} 
	public void setPageDesc(String title,String sendcodebit,String et_sendcode_val){
		et_sendcode.setText(et_sendcode_val);
		et_sendcode.setHint(sendcodebit);
		tv_emailtitle.setText(title);
	}
	public void setColor(int colval){
		layout_actionbar.setBackgroundColor(colval);
		bt_send.setBackgroundColor(colval);
		bt_conf.setBackgroundColor(colval);
	}
	@Override
	protected void findView(View view) {
		layout_actionbar=(RelativeLayout)view.findViewById(R.id.layout_actionbar);
		et_sendcode=(EditText)view.findViewById(R.id.et_sendcode);
		et_entercode=(EditText)view.findViewById(R.id.et_entercode);
		bt_send = (Button)view.findViewById(R.id.bt_send);
		bt_conf = (Button)view.findViewById(R.id.bt_conf);
		tv_emailtitle= (TextView)view.findViewById(R.id.tv_emailtitle);
		bt_send.setOnClickListener(this);
		bt_conf.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
			case R.id.bt_send:
				if(isValidContent()){
					bt_send.setEnabled(false);
					sendCode();
				}
			break;
			case R.id.bt_conf:
				if(isValidContentAndCode()){
					sendConfirm();
				}
			break;
		}
	}
	void sendTimerBackCode(){
		Countdown countdown=new Countdown(60, new CountdownBack(){ 
			@Override
			public void process(int timer) {
				bt_send.setText(timer+"");
			}
			@Override
			public void finish() {
				bt_send.setEnabled(true);
				bt_send.setText(context.getResources().getString(R.string.send_code));
			}
		});
		countdown.begin();
	}
	protected void sendCode(){
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("uid", et_sendcode.getText().toString());
		new BaseAsyncTask(getSendCodeUrl(), maps, BaseAsyncTask.HttpType.Post, "",context) {
			@Override
			public void handler(String param) {  
				try {
					JSONObject jsonObject = new JSONObject(param);
					int status = jsonObject.getInt("status");
					if (status == 0) {
						sendTimerBackCode();// 每60秒发送
					} else {
						String description = jsonObject.getString("description");
						sendFail(description);
					}
				} catch (JSONException e) {
					MyLog.e("send_code_error", e.getMessage());
					sendFail(e.getMessage());
				}
			}
		}.execute("");
	}
	protected void sendConfirm(){
		Map<String, Object> maps = new HashMap<String, Object>();
		final String result = et_sendcode.getText().toString().trim();
		maps.put("uid",result);
		maps.put("code", et_entercode.getText().toString().trim()); 
		new BaseAsyncTask(getConfirmUrl(), maps, BaseAsyncTask.HttpType.Post, "",context) {
			@Override
			public void handler(String param) {
				try {
					JSONObject jsonObject = new JSONObject(param);
					int status = jsonObject.getInt("status");
					if(status==0){
						slidingMenu.toggle();
						listener.onConfirm(result);
					}else{
						String desc=jsonObject.getString("description");
						sendFail(desc);
					}
				}catch(Exception ex){
					sendFail(ex.getMessage());
				}
			}
		}.execute("");
	}
	protected abstract boolean isValidContent();
	protected abstract boolean isValidContentAndCode();
	protected abstract void sendFail(String msg); 
	protected abstract String getSendCodeUrl();
	protected abstract String getConfirmUrl(); 
	public void hidden(){
		slidingMenu.toggle();
	}
}
