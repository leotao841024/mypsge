package helper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import comm.HttpManager;

public abstract class BaseAsyncTask extends AsyncTask<String, String, Double> {
	public enum HttpType {
		Post,
		Get,
		JSON
	}
	private String url;
	private Map<String,Object> maps;
	private HttpType type;
//	private BaseHttpHelper httphelper=null;
	private HttpManager httphelper;
	private Context context;
	private Map<String,Object> param;
	private String model; 
	private JSONObject jsonObj;
	public BaseAsyncTask(String url,Map<String,Object> param,HttpType type,String model,Context context){
		this.url=url;
		this.maps=param;
		this.type=type;
		this.context = context;
		httphelper=new HttpManager();
		this.param=param;
		this.model=model; 
	}
	public BaseAsyncTask(String url,JSONObject jsonObj,HttpType type,String model,Context context){
		this.url=url;
		this.maps=param;
		this.type=type;
		this.context = context;
//		httphelper=new BaseHttpHelper(this.context);
		this.model=model;
		this.jsonObj=jsonObj;
	}

	@Override
	protected Double doInBackground(String... params) {
		String res="";
		if(type==HttpType.Get){
//			res=httphelper.GetDataByClient(url, param);
			httphelper.getNormalMsg(url,param,null,new HttpCallbackAsync());
		}else if(type==HttpType.Post){
			httphelper.postNormalMsg(url, param, null, new HttpCallbackAsync());
//			res=httphelper.PostDataByClient(url, param, model);
		}else if(type==HttpType.JSON){
			Map<String, String> headers=new HashMap<String,String>();
			String mtoken = SPHelper.getBaseMsg(context, "mtoken", "mtoken");
			headers.put("Accept", "application/json");
			headers.put("Content-type", "application/json");
			httphelper.postNormalMsg(url, param, headers, new HttpCallbackAsync());
//			res=httphelper.PostDataByClient(url, jsonObj, model,maps);
		}
//		publishProgress(res);
		return null;
	}
	class HttpCallbackAsync extends HttpCallback {
		@Override
		public void exception(Exception ex) {
			publishProgress(ex.getMessage());
		}

		@Override
		public void failed(String pmsg) {
			publishProgress(pmsg);
		}

		@Override
		public void success(String response) {
			publishProgress(response);
		}
	}
	
	@Override
	protected void onPostExecute(Double result) {
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		if(values!=null&&values.length>0){
			handler(values[0]);
		}else{
			handler("");
		} 
	}
	public abstract void handler(String param) throws RuntimeException;
}
