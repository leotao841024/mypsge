package service;


import bean.DayStep;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class NewStepService extends Service {
 
	private SensorManager mSensorManager;
	private NewStepDetector listener;
	private Sensor mStepDetector;
	private StepStoreListener storelistener;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new NewMsgBinder();
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		storelistener = new StepStoreListener(this);
		mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
		listener=new NewStepDetector();
		listener.addStepListener(storelistener);
		mStepDetector=mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mSensorManager.registerListener(listener, mStepDetector,SensorManager.SENSOR_DELAY_FASTEST);
		//mStepDetector=new NewStepDetector();
		//mStepDetector = mSensorManager.getDefaultSensor(sensorTypeD);  
	}
	
	public DayStep getNowTotalStep() {
		return storelistener.getNowSteps();
		// return new DayStep();
	}
	
	public class NewMsgBinder extends Binder {
		public NewStepService getService() {
			return NewStepService.this;
		}
	}
	
	private void RegisterSensor() { 
		/*
		 if (BuildConfig.DEBUG)Log.e("re-register sensor listener","re-register sensor listener");
		 SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		 try {
		 sm.unregisterListener(this);
		 } catch (Exception e) {
		 if (BuildConfig.DEBUG) Log.e("e", e+"");
		 e.printStackTrace();
		 }
		 if (BuildConfig.DEBUG) {
			 Log.e("step sensors: " ,""+ sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size());
			 if (sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size() < 1) return; // emulator
		 	Log.e("default: ", ""+ sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER).getName());
		 }
		 // enable batching with delay of max 5 min
		 sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
		 SensorManager.SENSOR_DELAY_NORMAL, 5 * MICROSECONDS_IN_ONE_MINUTE);
		 */
	}
}
