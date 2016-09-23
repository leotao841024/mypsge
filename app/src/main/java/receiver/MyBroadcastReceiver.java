package receiver;
 

import helper.SPHelper;
import service.SimpleStepService;
import service.UploadDataService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent; 

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean ishold= SPHelper.getDetailMsg(context, "holdproc", true);
		if(ishold){
			Intent service=new Intent(context,SimpleStepService.class);
			service.putExtra("istart", SPHelper.getBaseMsg(context, "bindkey", ""));
			context.startService(service);
			Intent uploadservice = new Intent(context, UploadDataService.class); 
			context.startService(uploadservice);
		}
	}
}
