package service;

import helper.SPHelper;


import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
 




import bean.StepDatas;
 
import helper.BaseHttpHelper;
import helper.DatabaseHelper;
import db.DbSqlLite;
import comm.SqlLiteManager;
import manager.UploadDataManager;
import manager.UploadStepData;

 
import constant.Cons;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UploadDataService extends Service {
	private Timer timer;
	private int[] hour={6,9,10,13,14,15,20};
	private TimerTask task=new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(isvalid()){
				UploadData();
			}
		}
	};
	
	boolean isvalid(){
		Calendar rightNow = Calendar.getInstance();		
		int hournum=rightNow.get(Calendar.HOUR_OF_DAY);
		for(int item:hour){
			if(item==hournum){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		timer=new Timer();
		timer.schedule(task,1000,3000000);
	}
	
	void UploadData(){
		try
		{
			String mid=SPHelper.getBaseMsg(this, "mid", "0");
			DatabaseHelper base=new DatabaseHelper(this);
			SqlLiteManager sqlmana = new SqlLiteManager(new DbSqlLite(this, base));
			StepDatas datas = sqlmana.getUploadData(); 
			BaseHttpHelper helper=new BaseHttpHelper(this);
			UploadStepData uploadobj=new UploadStepData(helper,Cons.UPLOAD_STEPDATA_URL,Integer.parseInt(mid),this);
			UploadDataManager uploadMana=new UploadDataManager(uploadobj, datas.getBaseData(), datas.getMinStep(), this);
			uploadMana.justDoIt();
		}catch(Exception e){
			
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
