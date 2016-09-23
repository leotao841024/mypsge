package service; 
import helper.SPHelper;

import java.util.Calendar;
import java.util.Date;

import bean.DayStep;
import android.app.Service; 
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class SimpleStepService extends Service {
	private SensorManager mSensorManager;
	private StepDetectorSimple detector;
	private StepStoreListener storelistener;
	private Sensor sensor;
	private static final String TAG = "AccleratePersist";
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return new StepBinder();
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate(); 
		storelistener = new StepStoreListener(this);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		detector=new StepDetectorSimple();
		detector.addStepListener(storelistener);	
	    sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) { 
		if(SPHelper.getBaseMsg(SimpleStepService.this, "bindkey", "3").equals("3")){
			initDevice();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void initDevice(){
		mSensorManager.unregisterListener(detector);
		mSensorManager.registerListener(detector, sensor, mSensorManager.SENSOR_DELAY_FASTEST);
	}
	
	public void stop() {
		mSensorManager.unregisterListener(detector);
	}
	
	public DayStep getNowTotalStep() {
		return storelistener.getNowSteps(); 
	}
	
	public void delData(long val){
		storelistener.delData(val);
	}
	
	public void initData(){
		storelistener.initTodayData();
	}
	
	public void SaveStepData(){
		Calendar rightNow = Calendar.getInstance();		
		int hour=rightNow.get(Calendar.HOUR_OF_DAY);
		int minute=rightNow.get(Calendar.MINUTE);
		Date d1=new Date();
		long _timer=d1.getTime();
		storelistener.SaveStepData(hour,minute,_timer,0);
	}
	
	public class StepBinder extends Binder{
		public SimpleStepService getStepBinder(){
			return  SimpleStepService.this;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy(); 
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		} 
	}
}
