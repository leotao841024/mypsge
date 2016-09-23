package service;

 
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock; 
import bean.DayStep; 
public class StepService extends Service {

	public static Boolean FLAG = false; 

	private SensorManager mSensorManager;
	private StepDetector detector;

	private PowerManager mPowerManager;
	private WakeLock mWakeLock;
	private Sensor mSensor;

	private StepStoreListener storelistener;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MsgBinder();
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		FLAG = true;
		detector = new StepDetector(this);
		storelistener = new StepStoreListener(this);
		detector.addStepListener(storelistener);
		mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "Jackie");
		mWakeLock.acquire();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensorManager.registerListener(detector,
				SensorManager.SENSOR_ACCELEROMETER
						| SensorManager.SENSOR_MAGNETIC_FIELD
						| SensorManager.SENSOR_ORIENTATION,
				SensorManager.SENSOR_DELAY_UI);
	}
	public void stop() {
		mSensorManager.unregisterListener(detector);
	} 
	/*
	public void start() {
		mSensorManager.registerListener(detector,
				SensorManager.SENSOR_ACCELEROMETER
						| SensorManager.SENSOR_MAGNETIC_FIELD
						| SensorManager.SENSOR_ORIENTATION,
				SensorManager.SENSOR_DELAY_UI);
	}
	*/
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FLAG = false;  
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		}
		if (mWakeLock != null) {
			mWakeLock.release();
		}
	}

	public DayStep getNowTotalStep() {
		return storelistener.getNowSteps();
		// return new DayStep();
	}

	public class MsgBinder extends Binder {
		public StepService getService() {
			return StepService.this;
		}
	}
}
