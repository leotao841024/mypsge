package tools;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

public class Countdown {
	public interface CountdownBack{
		void process(int timer);
		void finish();
	}
	private CountdownBack callback;
	private Timer backTimer;
	private int total;
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.arg1!=0){
				callback.process(msg.arg1);	
			}else{
				callback.finish();	
			}
		};
	};
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			Message msg = new Message(); 
			msg.arg1 = total;
			if (total >0) {
				--total;
			}else{
				backTimer.cancel();
			}
			handler.sendMessage(msg);
		}
	};
	public Countdown(int total,CountdownBack callback) {
		this.total=total;
		this.callback=callback;
		backTimer=new Timer();
	}
	public void begin(){
		backTimer.schedule(task, 0, 1000);
	}
}
