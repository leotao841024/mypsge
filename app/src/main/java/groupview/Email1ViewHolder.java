package groupview;

import com.example.renrenstep.R;

import constant.Cons;
import tools.Toast;
import groupview.InfoViewBuilder.EventListener;
import android.app.Activity;

public class Email1ViewHolder extends CommunicationViewBuilder {
	public Email1ViewHolder(Activity context, EventListener listener) {
		super(context, listener); 
	} 
	void initView(){
		et_sendcode.setEnabled(false);
	}
	@Override
	protected boolean isValidContent() {
		if(!et_sendcode.getText().toString().contains("@")){
			Toast.makeText(context,context.getResources().getString(R.string.email_error), 2000).show();
			return false;
		}
		return true;
	} 
	@Override
	protected boolean isValidContentAndCode() {
		if(et_entercode.getText().toString().length()!=6){
			Toast.makeText(context,context.getResources().getString(R.string.code_error), 2000).show();
			return false;
		}
		return true;
	}
	
	@Override
	protected void sendFail(String msg) {
		Toast.makeText(context,msg, 2000).show();
	}
	@Override
	protected String getSendCodeUrl() {
		return Cons.SEND_CODE;
	} 
	@Override
	protected String getConfirmUrl() { 
		return Cons.CHANGE_EMAILNO;
	}
	
}
