package groupview;

import tools.Toast;

import com.example.renrenstep.R;

import constant.Cons;
import groupview.InfoViewBuilder.EventListener;
import android.app.Activity;

public class Phone2ViewHolder extends CommunicationViewBuilder{ 
	public Phone2ViewHolder(Activity context, EventListener listener) {
		super(context, listener); 
	}
	@Override
	protected boolean isValidContent() {
		if(et_sendcode.getText().toString().length()!=11){
			Toast.makeText(context,context.getResources().getString(R.string.phone_error), 2000).show();
			return false;
		}
		return true;
	}
	@Override
	protected boolean isValidContentAndCode() {
		if(et_sendcode.getText().toString().length()!=11&&et_entercode.getText().toString().length()!=6){
			Toast.makeText(context,context.getResources().getString(R.string.phoneinvalid), 2000).show();
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
		// TODO Auto-generated method stub
		return Cons.BIND_PHONE;
	} 
	@Override
	protected String getConfirmUrl() { 
		return Cons.CHECK_PHONE;
	}
 
}
