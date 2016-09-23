package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;
import bean.MinuteStep;
import bean.UploadDataBase;
import bean.UploadDataStail;
import helper.BaseHttpHelper;

public class UploadStepData extends UploadBase {
	private BaseHttpHelper httphelper;
	private Context context;
	private String url;
	private int key;
	public UploadStepData(BaseHttpHelper httphelper,String url, int key,Context context)
	{
		this.httphelper=httphelper;
		this.context=context;
		this.url=url;
		this.key=key;
	}
	@Override
	public boolean uploadBase(UploadDataBase base) {
		// TODO Auto-generated method stub
		Map<String, Object> maps = new HashMap<String, Object>();
		Gson gson = new Gson();
		maps.put("data", gson.toJson(base));
		maps.put("gid","");
		String res = httphelper.PostDataByClient(url, maps,"");
		return validResult(res);
	}

	@Override
	public boolean uploadDetail(List<MinuteStep> details) {
		List<String> mlist = new ArrayList<String>();
		List<Integer> slist = new ArrayList<Integer>();
		Map<String, Object> smaps = new HashMap<String, Object>();
		UploadDataStail stail = new UploadDataStail();
		for(MinuteStep step:details)
		{
			mlist.add(step.getYear()+"-"+step.getMonth()+"-"+step.getDay()+" "+step.getHour() + ":" + step.getMinute()+":00");
			slist.add(step.getSteps());	
		}
		String[] flist = mlist.toArray(new String[mlist.size()]);
		Integer[] dlist = slist.toArray(new Integer[slist.size()]);
		stail.setApptype("SMDDataV2");
		stail.setCollectdate(flist);
		stail.setSteps(dlist);
		stail.setDatakey(key);
		Gson gson1 = new Gson();
		smaps.put("data", gson1.toJson(stail));
		smaps.put("gid","");
		String sres = httphelper.PostDataByClient(url, smaps, ""); 
		return validResult(sres);
	}
	
	boolean validResult(String res)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(res);
			int valid= jsonObject.getInt("status");
			return valid==0;
		}catch(Exception ex)
		{
			return false;
		}
	}
}
