package service;

 
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import helper.SPHelper;
import receiver.MyBroadcastReceiver;

public class MyService extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	private WakeLock mWakelock;
	private PowerManager mPwrMgr; //电源管理
	private WakeLock mTurnBackOn = null;
	private int mSavedTimeout;
	private Handler handler = new Handler(); 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);    
	    MyBroadcastReceiver receiver = new MyBroadcastReceiver();   
	    registerReceiver(receiver, filter);
	    boolean ishold= SPHelper.getDetailMsg(MyService.this, "holdproc", true);
		if(ishold){
			Intent service=new Intent(MyService.this,SimpleStepService.class);
			MyService.this.startService(service);
			Intent uploadservice = new Intent(MyService.this, UploadDataService.class); 
			MyService.this.startService(uploadservice);
		}
	}
	
	@Override  
	public int onStartCommand(Intent intent, int flags, int startId) {  
	    flags = START_STICKY;  
	    return super.onStartCommand(intent, flags, startId); 
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent my_service = new Intent(this,MyService.class);
		startService(my_service);
	}
}
