package manager;

import helper.SPHelper;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import bean.MinuteStep; 
import bean.UploadDataBase;

public class UploadDataManager { 
	private Context context; 
	private UploadBase uploadBase;
	private List<MinuteStep> minStep;
	private List<UploadDataBase> baseData;
	public UploadDataManager(UploadBase uploadBase,List<UploadDataBase> baseData,List<MinuteStep> minStep,
			Context context) {
		this.context = context; 
		this.uploadBase=uploadBase;
		this.baseData=baseData;
		this.minStep=minStep;
	}
	public boolean justDoIt()
	{
		boolean res=true;
		for (UploadDataBase base : baseData) {
			if(uploadBase.uploadBase(base))
			{
				if(uploadDetails(base.getYear(),base.getMonth(),base.getDay()))
				{
					SPHelper.setDetailMsg(context,"lastval", base.getTimer());
				}else
				{
					res= false;
					break;
				}
			}else
			{ 
				res= false;
				break;
			}
		}
		return res;
	}
	boolean uploadDetails(int year, int month, int day)
	{
		List<MinuteStep> list=new ArrayList<MinuteStep>();
		for (MinuteStep mstep : minStep) {
			if (year == mstep.getYear() && month == mstep.getMonth()
					&& day == mstep.getDay()) { 
					list.add(mstep); 
			}
		}
		return uploadBase.uploadDetail(list);
	}
}
