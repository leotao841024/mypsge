package helper;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import manager.ImageCacheManger;

/**
 * Created by jam on 2016/4/1.
 */

public class ImageLoadAsyImage extends AsyncTask<String, Void, Bitmap> {
    public interface ImageLodeCallback{
        void handler(Bitmap map);
    }
    private ImageCacheManger imageCacheManger;
    private ImageLodeCallback callback;
    private String url="";
    private boolean isleft;
    public ImageLoadAsyImage(ImageLodeCallback callback,String url,boolean isleft){
        this.imageCacheManger = new ImageCacheManger("stepic");
        this.callback = callback;
        this.url = url;
        this.isleft=isleft;
    }

    @Override
    protected Bitmap doInBackground(String... arg0) {
        String[] filenms=url.split("/");
        if(filenms.length<=0){
            return null;
        }
        Bitmap bitmap = imageCacheManger.getCachedBitmap(arg0[0]);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // TODO Auto-generated method stub
//        if(result==null){//Cons.DONW_PIC+filenm
//            imageCacheManger.downData1(url, new ImageCacheManger.IdownImageCallback() {
//                @Override
//                public void finished(Bitmap map) {
//                    callback.handler(map);
//                }
//            },isleft);
//        }else{
//            callback.handler(result);
//        }
    }

}
