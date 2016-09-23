package helper;

import java.io.InputStream;

/**
 * Created by jam on 2016/4/6.
 */
public class HttpCallback {
    public void exception(Exception ex){}
    public void success(InputStream response){}
    public void success(String response){}
    public void failed(String pmsg){}
}
