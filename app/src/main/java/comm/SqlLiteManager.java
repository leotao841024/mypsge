package comm;

import java.util.List;

import bean.DayStep;
import bean.HisSteps;
import bean.StepDatas;
import db.DBbase;

public class SqlLiteManager {
	private DBbase base;
	
	public SqlLiteManager(DBbase base){
		this.base=base;
	}
	public void Insert(int year,int month,int day,int hour,int min,int stepnumber,long timer,String mid){
		String sql="insert into apm_steps(year,month,day,hour,minute,steps,mtoken,timer) values ("+year+","+month+","+day+","+hour+","+min+","+stepnumber+",'"+mid+"',"+timer+")";
		base.Insert(sql);
	}
	
	public StepDatas getUploadData(){
		return base.UploadData(); 
	}
	
	public List<HisSteps> GetHisStep()
	{
		return base.GetAllStep();
	}
	public DayStep GetOneDayStepData(int year,int month,int day)
	{
		return base.GetOneDay(year, month, day);
	}
	public void DelStepData(long val){
		String sql="delete from  apm_steps where  timer<"+val+"";
		base.DelStepHisData(sql);
	}
	
	public void DelStepHisData(long timer) {
		// TODO Auto-generated method stub
		String sql="delete from apm_steps where timer>="+timer+"";
		base.DelStepHisData(sql);
	}
}
