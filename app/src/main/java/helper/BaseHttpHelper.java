package helper;

import helper.SPHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import constant.Cons;
import android.content.Context;
import android.util.Log;

public class BaseHttpHelper {
	private Context context;
	private HttpClient client = null;

	public BaseHttpHelper(Context context) {
		// this.type = type;
		this.context = context;
		this.client = new DefaultHttpClient();
	}
	public String GetDataByClient(String url, Map<String, Object> param) {
		String lines = "";
		try {
			String params = GetParams(param);
			HttpGet request = new HttpGet(url + (params == "" ? "" : "?" + params));
			String mtoken = SPHelper.getBaseMsg(context, "mtoken", "mtoken"); 
			request.setHeader("mtoken",mtoken);
			HttpResponse response = client.execute(request);
			lines = HandlerStream(response.getEntity().getContent());
		} catch (Exception ex) {
			lines = ex.getMessage();
		}
		return lines;
	}

	public String PostDataByClient(String url, Map<String, Object> param,
			String model) {
		String lines = "";
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(getPostParams(param, model), HTTP.UTF_8));
			request.setParams(PostParams(param, model));
			String mtoken = SPHelper.getBaseMsg(context, "mtoken", "mtoken");
			request.setHeader("mtoken", mtoken);
			HttpResponse response = client.execute(request);
			lines = HandlerStream(response.getEntity().getContent());
		} catch (Exception ex) {
			lines = ex.getMessage();
		}
		return lines;
	}

	public String PostDataByClient(String url, JSONObject param,String model,Map<String,String> header) {
		String lines = "";
		try {
			HttpPost request = new HttpPost(url);
			StringEntity en=new StringEntity(param.toString(),HTTP.UTF_8);
			request.setEntity(en); 
			for(Entry<String,String> item: header.entrySet()){
				request.setHeader(item.getKey(), item.getValue());	
			}
			HttpResponse response = client.execute(request);

			lines = HandlerStream(response.getEntity().getContent());
		} catch (Exception ex) {
			lines = ex.getMessage();
		}
		return lines;
	}
	

	String HandlerStream(InputStream stream) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(stream));
			String line;
			while ((line = rd.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception ex) {
			builder.append(ex.getMessage());
		}
		return builder.toString();
	}

	String GetParams(Map<String, Object> param) {
		String res = "";
		for (Iterator<String> i = param.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			Object val = param.get(key);
			res += key + "=" + val.toString() + "&";
		}
		return res != "" ? res.substring(0, res.length() - 1) : "";
	}

	List<NameValuePair> getPostParams(Map<String, Object> param, String model) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		HttpParams httpParameters = new BasicHttpParams();
		if (param != null) {
			for (Iterator<String> i = param.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				Object val = param.get(key); 
				if (model != "") {
					key = model + "[" + key + "]";
				}
				list.add(new BasicNameValuePair(key, val.toString()));
			}
		}
		return list;
	}

	HttpParams PostParams(Map<String, Object> param, String model) {
		HttpParams httpParameters = new BasicHttpParams();
		if (param != null) {
			for (Iterator<String> i = param.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				Object val = param.get(key);
				if (model != "") {
					key = model + "[" + key + "]";
				}
				httpParameters.setParameter(key, val);
			}
		}
		return httpParameters;
	}
}
