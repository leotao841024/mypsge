package helper;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jam on 2016/4/6.
 */
public class HttpBase {

    public interface DownDataCallback{
        void exception(Exception ex);
        void success(HttpResponse response);
    }
    private HttpClient client;
    public HttpBase(){
        this.client=new DefaultHttpClient();
    }

    public void sendHttpGet(String url,Map<String,Object> param,Map<String,String> headers,DownDataCallback callback){
        String params = GetParams(param);
        HttpGet request = new HttpGet(url + (params == "" ? "" : "?" + params));
        if (headers!=null){
            for(Map.Entry<String,String> item:headers.entrySet()){
                request.addHeader(item.getKey(), item.getValue());
            }
        }
        try {
            HttpResponse response = client.execute(request);
            callback.success(response);
        } catch (IOException e) {
            callback.exception(e);
        }
    }

    String GetParams(Map<String, Object> param) {
        if (param==null) return "";
        String res = "";
        for (Iterator<String> i = param.keySet().iterator(); i.hasNext();) {
            String key = i.next();
            Object val = param.get(key);
            res += key + "=" + val.toString() + "&";
        }
        return res != "" ? res.substring(0, res.length() - 1) : "";
    }

    public void sendHttpPost(String url,Map<String,Object> params,Map<String,String> headers,Map<String,String> files,DownDataCallback callback){
        HttpPost post = new HttpPost(url);
        MultipartEntity entity = new MultipartEntity();
        try {
            if(params!=null){
                for (Map.Entry<String, Object> entry : params.entrySet()) {
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
            HttpResponse response= client.execute(post);
            callback.success(response);
        } catch (IOException e) {
            callback.exception(e);
        }
    }

}
