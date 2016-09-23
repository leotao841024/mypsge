package helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream; 
import java.net.HttpURLConnection; 
import java.net.URL;  
import java.util.HashMap;
import java.util.Map; 
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject; 
import manager.MyLog;
import constant.Cons;
import android.content.Context; 
import android.os.AsyncTask;
 

public class HttpTool {
	public interface IDownCallbak{
		void success();
		void fail(String pmsg);
		void process(int total, int hasread);
		boolean isrun();
	}
	public static void downLoadData(String purl,OutputStream outstream,IDownCallbak callback){
		try {
			URL url = new URL(purl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			int total = conn.getContentLength();
			InputStream inputStream = conn.getInputStream();
			byte buf[] = new byte[1024];
			int hasread = 0;
			do {
				int numread = inputStream.read(buf);
				hasread+= numread;
				callback.process(total, hasread);
				if (numread <= 0) {
					break;
				}
				outstream.write(buf, 0, numread);
			} while (true&&callback.isrun());
			inputStream.close();
			conn.disconnect();
			callback.success();
		} catch (Exception ex) {
			callback.fail(ex.getMessage());
		}
	}

	public static String uploadFile(String url, String path, String key,Map<String,String> headers) throws ClientProtocolException, IOException {
		Map<String,String> files=new HashMap<String,String>();
		files.put(key, path);
		return uploadData(url,headers,null,files);
	}

	public static String uploadData(String url,Map<String,String> headers,Map<String,Object> body,Map<String,String> files) throws ClientProtocolException, IOException{
		HttpPost post = new HttpPost(url);
		MultipartEntity entity = new MultipartEntity();
		if(body!=null){
		for (Map.Entry<String, Object> entry : body.entrySet()) {
			StringBody par = new StringBody(entry.getValue().toString());
			entity.addPart(entry.getKey(), par);
		}}
		if(files!=null){
			for (Map.Entry<String, String> entry : files.entrySet()) {
				FileBody file = new FileBody(new File(entry.getValue()));
				entity.addPart(entry.getKey(), file);
			}
		}
		for(Map.Entry<String,String> item:headers.entrySet()){
			post.addHeader(item.getKey(), item.getValue());
		}
        post.setEntity(entity);

        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if(response.getStatusLine().getStatusCode()==200){
        	return EntityUtils.toString(response.getEntity());
        }
        return "";
	}
	public interface imageUploadCallback{
		String handler(String filepath);
		void excute(String result);
	}

	public static void  uploadLocalImage(String pfilepath,final Map<String,String> maps,final imageUploadCallback imageCallback){
		new AsyncTask<String, Void, String>() {
			protected void onPostExecute(String result) {
				imageCallback.excute(result);
			};
			@Override
			protected String doInBackground(String... params) {
				try {
					 	String filepath= imageCallback.handler(params[0]);
						String response =uploadFile(Cons.UPLOAD_HEAD, filepath,"filedata",maps);
						return response;
				} catch (Exception ex) {
					MyLog.e("imageupload", ex.getMessage());
				}
				return "";
			}
		}.execute(pfilepath);
	}
	public interface IHttpCallBackAsy{
		void exception(String pmsg);
		void failed(String pmsg);
		void success(String param);
	}
	public static void HttpRequestAsy(String url,Map<String,Object> param, BaseAsyncTask.HttpType type,String model,Context context,final IHttpCallBackAsy callback){
		new BaseAsyncTask(url,param,type,model,context){
			@Override
			public void handler(String param) {
				if(param!=null&&param.contains("status")){
					try {
						JSONObject obj=new JSONObject(param);
						if(obj.getInt("status")==0){
							callback.success(param);
						}else{
							String content =  obj.getString("description");
							callback.failed(content);
						}
					} catch (JSONException e) {
						callback.exception(e.getMessage());
					}
				}else{
					callback.exception(param);
				}
			}
		}.execute("");
	}
	public static void HttpRequestAsy(String url,JSONObject param, BaseAsyncTask.HttpType type,String model,Context context,final IHttpCallBackAsy callback){
		new BaseAsyncTask(url,param,type,model,context){
			@Override
			public void handler(String param) {
				if(param!=null&&param.contains("status")){
					try {
						JSONObject obj=new JSONObject(param);
						if(obj.getInt("status")==0){
							callback.success(param);
						}else{
							String content =  obj.getString("description");
							callback.failed(content);
						}
					} catch (JSONException e) {
						callback.exception(e.getMessage());
					}
				}else{
					callback.exception(param);
				}
			}
		}.execute("");
	}
}
