package helper;


import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by jam on 2016/4/6.
 */
public class NormalHttp extends HttpBase {
    public  void httpGet(String url,Map<String,Object> params,Map<String,String> headers, final HttpCallback callback){
        sendHttpGet(url,params,headers, new DownDataCallback() {
            @Override
            public void exception(Exception ex) {
                callback.exception(ex);
            }
            @Override
            public void success(HttpResponse response) {
                try {
                    String res= EntityUtils.toString(response.getEntity());
                    JSONObject obj=new JSONObject(res);
                    if(obj.getInt("status")==0){
                        callback.success(res);
                    }else{
                        callback.failed(obj.getString("description"));
                    }
                } catch (Exception e) {
                    callback.exception(e);
                }
            }
        });
    }
    public void httpPost(String url,Map<String,Object> params,Map<String,String> headers,final HttpCallback callback){
        sendHttpPost(url, params, headers, null, new DownDataCallback() {
            @Override
            public void exception(Exception ex) {
                callback.exception(ex);
            }
            @Override
            public void success(HttpResponse response) {
                try {
                    String res= EntityUtils.toString(response.getEntity());
                    JSONObject obj=new JSONObject(res);
                    if(obj.getInt("status")==0){
                        callback.success(res);
                    }else{
                        callback.failed(obj.getString("description"));
                    }
                } catch (Exception e) {
                    callback.exception(e);
                }
            }
        });
    }
}
