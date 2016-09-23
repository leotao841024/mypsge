package comm;

import java.util.HashMap;
import java.util.Map;

import app.MyApplication;
import helper.FileHttp;
import helper.HttpCallback;
import helper.NormalHttp;
import helper.SPHelper;

/**
 * Created by jam on 2016/4/6.
 */
public class HttpManager {
    public void getNormalMsg(String url,Map<String,Object> params,Map<String,String> headers, final HttpCallback callback){
        headers = filterHeader(headers);
        new NormalHttp().httpGet(url,params,headers,callback);
    }

    public  void postNormalMsg(String url,Map<String,Object> params,Map<String,String> headers, final HttpCallback callback){
        headers = filterHeader(headers);
        new NormalHttp().httpPost(url, params, headers, callback);
    }

    public void getFileMsg(String url,Map<String,Object> params,Map<String,String> headers, final HttpCallback callback){
        headers = filterHeader(headers);
        new FileHttp().httpGet(url, params, headers, callback);
    }

    public void postFileMsg(String url,Map<String,Object> params,Map<String,String> headers,Map<String,String> files, final HttpCallback callback){
        headers = filterHeader(headers);
        new FileHttp().httpPost(url, params, headers, files, callback);
    }

    Map<String,String> filterHeader(Map<String,String> headers){
        if(headers==null){
            headers=new HashMap<String,String>();
        }
        String mtoken = SPHelper.getBaseMsg(MyApplication.getInstance(), "mtoken", "mtoken");
        headers.put("mtoken",mtoken);
        return headers;
    }
}
