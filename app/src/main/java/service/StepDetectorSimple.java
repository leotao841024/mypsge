package service;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class StepDetectorSimple implements SensorEventListener {
	 private static final int ACCEL_RING_SIZE = 50;
	  private static final int VEL_RING_SIZE = 10;
	  private static final float STEP_THRESHOLD = 4f;
	  private static final int STEP_DELAY_NS = 250000000;
	 //private static final int STEP_DELAY_NS = 900;

	  private int accelRingCounter = 0;
	  private float[] accelRingX = new float[ACCEL_RING_SIZE];
	  private float[] accelRingY = new float[ACCEL_RING_SIZE];
	  private float[] accelRingZ = new float[ACCEL_RING_SIZE];
	  private int velRingCounter = 0;
	  private float[] velRing = new float[VEL_RING_SIZE];
	  private long lastStepTimeNs = 0;
	  private float oldVelocityEstimate = 0;
	  
	private ArrayList<IStepListener> mStepListeners = new ArrayList<IStepListener>(); 
	public void addStepListener(IStepListener sl) {
	    mStepListeners.add(sl);
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		     updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
		} 
		 /*
		FileUtils file=new FileUtils("demo");
		file.createSDDir();
		String path= file.getFilePath();
		try
		{
			File files= new File(path+"1.txt");
			if (!files.exists()) {
	           files.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(files, "rw");
	        raf.seek(files.length()); 
	        SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date1=new Date();
			String filenm = date.format(date1);
	        raf.write(filenm.getBytes());
	        raf.close();
		}catch(Exception ex){
			
		} */
	}
	
	void updateAccel(long timeNs, float x, float y, float z) {
		    float[] currentAccel = new float[3];
		    currentAccel[0] = x;
		    currentAccel[1] = y;
		    currentAccel[2] = z;

		    // First step is to update our guess of where the global z vector is.
		    accelRingCounter++;
		    accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0];
		    accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1];
		    accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2];

		    float[] worldZ = new float[3];
		    worldZ[0] = SensorFusionMath.sum(accelRingX) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
		    worldZ[1] = SensorFusionMath.sum(accelRingY) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
		    worldZ[2] = SensorFusionMath.sum(accelRingZ) / Math.min(accelRingCounter, ACCEL_RING_SIZE);

		    float normalization_factor = SensorFusionMath.norm(worldZ);

		    worldZ[0] = worldZ[0] / normalization_factor;
		    worldZ[1] = worldZ[1] / normalization_factor;
		    worldZ[2] = worldZ[2] / normalization_factor;

		    // Next step is to figure out the component of the current acceleration
		    // in the direction of world_z and subtract gravity's contribution
		    float currentZ = SensorFusionMath.dot(worldZ, currentAccel) - normalization_factor;
		    velRingCounter++;
		    velRing[velRingCounter % VEL_RING_SIZE] = currentZ;

		    float velocityEstimate = SensorFusionMath.sum(velRing);

		    if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD
		        && (timeNs - lastStepTimeNs > STEP_DELAY_NS)) {
		    	  for (IStepListener stepListener : mStepListeners) {
                      stepListener.handlerStep();
                  }
		    	  lastStepTimeNs = timeNs;
		    }
		    oldVelocityEstimate = velocityEstimate;
	}
}
