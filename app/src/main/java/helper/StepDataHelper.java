package helper;

import manager.MyLog;
import android.content.Context;
import bean.StepDatas;
import db.DbSqlLite;
import comm.SqlLiteManager;
import manager.UploadDataManager;
import manager.UploadStepData;
import constant.Cons;

public class StepDataHelper {
	private Context context;
	public StepDataHelper(Context context){
		this.context=context;
	}
	
	public void UploadData(){
		try
		{
			String mid=SPHelper.getBaseMsg(context, "mid", "0");
			DatabaseHelper base=new DatabaseHelper(context);
			SqlLiteManager sqlmana = new SqlLiteManager(new DbSqlLite(context, base));
			StepDatas datas = sqlmana.getUploadData(); 
			BaseHttpHelper helper=new BaseHttpHelper(context);
			UploadStepData uploadobj=new UploadStepData(helper,Cons.UPLOAD_STEPDATA_URL,Integer.parseInt(mid),context);
			UploadDataManager uploadMana=new UploadDataManager(uploadobj, datas.getBaseData(), datas.getMinStep(), context);
			uploadMana.justDoIt();
		}catch(Exception e){
			MyLog.e("updataerror",e.getMessage());
		}
	}
	
}
