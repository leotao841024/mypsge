package helper;

 

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationHelper {
	private Intent messageIntent = null;
	private PendingIntent messagePendingIntent = null;
	// 通知栏消息
	private int messageNotificationID = 1000;
	private Notification messageNotification = null;
	private NotificationManager messageNotificatioManager = null;
	private int launcher;
	private Context context;
	public NotificationHelper(int launcher,Context context){
		this.launcher = launcher;
		this.context = context;
		initData();
	}
	
	void initData(){
		messageNotification = new Notification();
		messageNotification.icon = this.launcher;
		messageNotification.tickerText = "新消息";
		messageNotification.defaults = Notification.DEFAULT_SOUND;
		messageNotificatioManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
		messageIntent = new Intent();
		messagePendingIntent = PendingIntent.getActivity(context, 0,messageIntent, 0); 
	}
	
	public void sendMsgToSys(String title,String msg){
		messageNotification.setLatestEventInfo(context, title, msg, messagePendingIntent);
		messageNotificatioManager.notify(messageNotificationID,messageNotification);
		messageNotificationID++;
	}
	
}
