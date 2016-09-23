package helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import constant.Cons;

public class HttpHelper {
	public static String requestGet(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse execute = client.execute(get);
			if (execute.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(execute.getEntity());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static Bitmap donwload(String url, Context context) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		get.setHeader("mtoken", SPHelper.getBaseMsg(context, "mtoken", "mtoken"));
		try {
			HttpResponse execute = client.execute(get);
			if (execute.getStatusLine().getStatusCode() == 200) {
				byte[] byteArray = EntityUtils.toByteArray(execute.getEntity());
				Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
						byteArray.length);
				return bitmap;
			}
		} catch (Exception e) {

		}
		return null;
	}

	public static String postString(String url, List<NameValuePair> params) {
		// params.add(new BasicNameValuePair("username","beijibear"));
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String login() {
		HttpPost post = new HttpPost(Cons.LOGIN_URL);
		try {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("uid", "13263135241"));
			list.add(new BasicNameValuePair("pwd", "123456"));
			post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
//上传文件

	public static void uploadInfo(Context context, Map<String, Object> maps, final Handler handler, final int statusCode){
		new BaseAsyncTask(Cons.UPDATE_INFO, maps, BaseAsyncTask.HttpType.Post, "",context) {
			@Override
			public void handler(String param) {
				Log.i("httphelper   上传info", param);
				if (param != null) {
					try {
						JSONObject jsonObject = new JSONObject(
								param);
						int status = jsonObject.getInt("status");
						Message msg = new Message();
						msg.arg1 = status;
						msg.what = statusCode;
						handler.sendMessage(msg);
						return ;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}.execute("");
	}
}










