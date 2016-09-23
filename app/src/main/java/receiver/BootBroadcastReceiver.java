package receiver;

import helper.SPHelper;
import service.MyService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast; 

public class BootBroadcastReceiver extends BroadcastReceiver {
	static final String action_boot="android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isrun= SPHelper.getDetailMsg(context, "isrun", true);
		if(isrun){
			SPHelper.setDetailMsg(context, "holdproc", true);
			Intent service = new Intent(context,MyService.class);  
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(service);
		}
	}
}
