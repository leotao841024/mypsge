package service;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class NewStepDetector implements SensorEventListener{
	private ArrayList<IStepListener> mStepListeners = new ArrayList<IStepListener>(); 
    public void addStepListener(IStepListener sl) {
        mStepListeners.add(sl);
    }
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		 for (IStepListener stepListener : mStepListeners) {
             stepListener.handlerStep();
         }
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
}
