package service;

import helper.SPHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
 

import java.util.List;

import constant.Cons;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import db.DBbase;
import helper.DatabaseHelper;
import db.DbSqlLite;
import comm.SqlLiteManager;
import bean.DayStep;
import bean.MinuteStep;

public class StepStoreListener implements IStepListener {
	private DatabaseHelper database;
	private MinuteStep step;
	private SqlLiteManager manager;
//	private Setting  setting; 
	private DayStep dayStep; 
	private int totalStep;
	private Context context;
	private Intent intent ; 
	//private String mid;
	private List<Long> lsteps;
	public StepStoreListener(Context context) {
		this.context=context;
		//this.mid=;
		this.database=new DatabaseHelper(context);
		DBbase sqllite=new DbSqlLite(context,database);
		this.manager=new SqlLiteManager(sqllite);
		this.step=new MinuteStep();
		this.dayStep=new DayStep();
		initTodayData();
		intent= new Intent(Cons.PROGRESS_MUSIC_ACTION); 
		lsteps=new ArrayList<Long>();
	}
	
	int filterStep(long time){
		int _step=0;
		long timer=lsteps.size()>0?lsteps.get(lsteps.size()-1):0;
		if(time-timer>1000||time-timer<250){
			lsteps.clear();
		}else if(lsteps.size()==9){
			_step=10;
		}else if(lsteps.size()>9){
			_step=1;
		}
		lsteps.add(time);
		return _step;
	}
	
	@Override
	public void handlerStep() {
		// TODO Auto-generated method stub
		Calendar rightNow = Calendar.getInstance();		
		int hour=rightNow.get(Calendar.HOUR_OF_DAY);
		int minute=rightNow.get(Calendar.MINUTE);
		Date d1=new Date();
		long _timer=d1.getTime();
		int _addnum=filterStep(_timer);
		if(step.getHour()==hour&&step.getMinute()==minute&&_addnum!=0){
			step.setSteps(step.getSteps()+_addnum);
			sendBroadbost();
		}
		else if(_addnum!=0){
			SaveStepData(hour,minute,_timer,_addnum);
		}
	}
	public void initTodayData()
	{
		Calendar rightNow = Calendar.getInstance();
		dayStep=  manager.GetOneDayStepData(rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH)+1, rightNow.get(Calendar.DAY_OF_MONTH));
		totalStep=dayStep.getTotal_step(); 
	}
	public void delData(long val){
		manager.DelStepData(val);
	}
	void sendBroadbost()
	{ 
		//initTodayData(); 
		dayStep.setTotal_step(totalStep +step.getSteps()); 
		Bundle buldle=new Bundle();
		buldle.putSerializable("key",dayStep);
		intent.putExtras(buldle);
		context.sendOrderedBroadcast(intent,null);
	}
	public void SaveStepData(int hour,int minute,long timer,int addnum){
		try
		{
			MinuteStep lstep= (MinuteStep)step.clone();
			AnsyTry ansytry=new AnsyTry(lstep);
			ansytry.execute("");
			step.setHour(hour);
			step.setMinute(minute);
			step.setSteps(addnum);
			step.setTimer(timer);
		}catch(Exception ex){
		}
	}
	void setStepDate(){
		
	}
	
	public DayStep getNowSteps()
	{
		//steps.setTotal_step(steps.getTotal_step()+step.getSteps());
		dayStep.setTotal_step(totalStep +step.getSteps());
		return dayStep;
	}
	
    class AnsyTry extends AsyncTask<String, Integer, Double> {
		private MinuteStep pstep;
		private Calendar rightNow;
		public AnsyTry(MinuteStep pstep) {
			if(pstep.getSteps()!=0){
				rightNow= Calendar.getInstance();
				this.pstep=pstep;
				this.pstep.setYear(rightNow.get(Calendar.YEAR));
				this.pstep.setMonth(rightNow.get(Calendar.MONTH)+1);
				this.pstep.setDay(rightNow.get(Calendar.DAY_OF_MONTH));
			}
		}
		@Override
		protected Double doInBackground(String... params) { 
			Double du = (double)0;
			if(pstep!=null){
				manager.Insert(pstep.getYear(),pstep.getMonth(),pstep.getDay(),pstep.getHour(),pstep.getMinute(),pstep.getSteps(),pstep.getTimer(),SPHelper.getBaseMsg(context, "mid","0"));
				initTodayData();
				sendBroadbost(); 
			}
			return du;
		}
		
	} 
}
