package groupview;

import tools.Toast;

import com.example.renrenstep.R;

import constant.Cons;
import groupview.InfoViewBuilder.EventListener;
import android.app.Activity;

public class Email2ViewHolder extends CommunicationViewBuilder{ 
	public Email2ViewHolder(Activity context, EventListener listener) {
		super(context, listener); 
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
		if(!et_sendcode.getText().toString().contains("@")&&et_entercode.getText().toString().length()!=6){
			Toast.makeText(context,context.getResources().getString(R.string.emailcdinvalid), 2000).show();
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
		return Cons.NEXT_CODE;
	}
	@Override
	protected String getConfirmUrl() { 
		return Cons.NEXT_EMAIL;
	} 
}
