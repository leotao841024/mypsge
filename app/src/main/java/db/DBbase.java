package db;

import java.util.List;

import bean.DayStep;
import bean.HisSteps;
import bean.MinuteStep;
import bean.StepDatas;
 

public abstract class DBbase {
	public abstract void Insert(String sql);
	public abstract StepDatas UploadData();
	public abstract List<HisSteps> GetAllStep();
	public abstract DayStep GetOneDay(int year,int month,int day);
	public abstract List<MinuteStep> GetOneDaySteps(int year,int month,int day);
	public abstract void DelStepHisData(String sql); 
}
