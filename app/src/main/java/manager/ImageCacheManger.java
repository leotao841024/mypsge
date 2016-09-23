package manager;

import app.MyApplication;
import comm.HttpManager;
import helper.HttpCallback;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import tools.FileUtils;
import tools.ImageCache;
import tools.ImageTool;

public class ImageCacheManger {
	private ImageCache cached;
	private FileUtils files;
    private HttpManager httpMana;
	private static ExecutorService  mDataThreadPool;
    public ImageCacheManger(String foldernm) {
		files = new FileUtils(foldernm);
		files.createSDDir();
		cached=ImageCache.instance();
        httpMana=new HttpManager();
	}

	static void initThreadPool(){
		if(mDataThreadPool==null){
			mDataThreadPool=Executors.newFixedThreadPool(2);
		}
	}
	public void clearBitmap(){
		cached.clearBitmap();
	}
	public Bitmap getCachedBitmap(String key){
		Bitmap map = cached.getBitmap(key);
		if(map==null){
			map=getBitmapFromSd(key);
		}
		return map;
	}
	public void addBitmap(String key,Bitmap value){
		 cached.addBitmap(key, value);
	}
	
	Bitmap getBitmapFromSd(String filenm){
        String filepath = files.getFilePath()+filenm;
        File file=new File(filepath);
        if(!file.exists())  return null;
        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        if (bitmap != null) {
            addBitmap(filenm, bitmap);
        }
		return bitmap;
	}

    public void uploadData(final String url, final Map<String,Object> params, final Map<String,String> headers,final  Map<String,String> files,final IdownImageCallback callback){
        final  Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                callback.finished(msg.obj.toString());
            }
        };
        final Message msg=new Message();
        mDataThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                httpMana.postFileMsg(url,params,headers,files,new HttpCallback(){
                    @Override
                    public void success(String response) {
                        msg.arg1=0;
                        msg.obj= response;
                        handler.sendMessage(msg);
                    }
                    @Override
                    public void exception(Exception ex) {
                        msg.arg1=0;
                        msg.obj= ex.getMessage();
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }


	public void downData(final String url,final IdownImageCallback downCallback){
		String[] filenms=url.split("/");
		if(filenms.length<=0){
			return;
		}
		final String filenm = filenms[filenms.length-1];
		initThreadPool();
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String path="";
                if(msg.arg1==0){
                    path = msg.obj.toString();
                }
                downCallback.finished(path);
            }
        };
		mDataThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                final Message msg = new Message();
                httpMana.getFileMsg(url,null,null,new HttpCallback(){
                    @Override
                    public void success(InputStream response) {
                        try {
                            File file=files.createSDFile(filenm);
                            FileOutputStream outputStream=new FileOutputStream(file);
                            CopyStream(response,outputStream);
                            msg.arg1=0;
                            msg.obj = file.getAbsolutePath();
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void failed(String pmsg) {
                        msg.arg1=1;
                        handler.sendMessage(msg);
                    }
                    @Override
                    public void exception(Exception ex) {
                        msg.arg1=2;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
	}
//    public void downData1(final String url,final IdownImageCallback downCallback,final boolean isleft){
//        String[] filenms=url.split("/");
//        if(filenms.length<=0){
//            return;
//        }
//        final String filenm = filenms[filenms.length-1];
//        initThreadPool();
//        final Handler handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                String path="";
//                if(msg.arg1==0){
//                   path = msg.obj.toString();
//                }
//                downCallback.finished(files.getFilePath());
//            }
//        };
//        File file=new File(url);
//        if(file.exists()){
//            Bitmap bitmap = decodeFile(file);
//            int angle = ImageTool.getBitmapDegree(file.getPath());
//            if(angle>0){
//                bitmap = ImageTool.changeImage(bitmap,angle);
//            }
//            bitmap = ImageTool.changeImageToTalkingImage(bitmap,isleft);
//            cached.addBitmap(filenm, bitmap);
//            files.saveMyBitmap(filenm, bitmap);
//            downCallback.finished(bitmap);
//        }else {
//            final Message msg= new Message();
//            mDataThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    httpMana.getFileMsg(url, null, null, new HttpCallback() {
//                        @Override
//                        public void success(InputStream response) {
//                            try {
//                                File file=files.createSDFile(filenm);
//                                FileOutputStream outputStream = new FileOutputStream(file);
//                                CopyStream(response, outputStream);
//                                msg.arg1 = 0;
//                                msg.obj = file.getAbsolutePath();
//                                handler.sendMessage(msg);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void failed(String pmsg) {
//                            msg.arg1 = 1;
//                            handler.sendMessage(msg);
//                        }
//
//                        @Override
//                        public void exception(Exception ex) {
//                            msg.arg1 = 2;
//                            handler.sendMessage(msg);
//                        }
//                    });
//                    }
//            });
//        }

//
//    private Bitmap getBitmap(String url,boolean isleft) {
//        File f = fileCache.getFile(url);
//        try {
//            Bitmap bitmap = null;
//            URL imageUrl = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
//            conn.setConnectTimeout(30000);
//            conn.setReadTimeout(30000);
//            conn.setInstanceFollowRedirects(true);
//            InputStream is = conn.getInputStream();
//            OutputStream os = new FileOutputStream(f);
//            CopyStream(is, os);
//            os.close();
//            bitmap = decodeFile(f);
//            int angle = ImageTool.getBitmapDegree(f.getPath());
//            if(angle>0){
//                bitmap = ImageTool.changeImage(bitmap,angle);
//            }
//            bitmap = ImageTool.changeImageToTalkingImage(bitmap,isleft);
//            return bitmap;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // Find the correct scale value. It should be the power of 2.
//          final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = ImageTool.calculateInSampleSizeBySize(width_tmp,height_tmp,480,800);
//            while (true) {
//                if (width_tmp / 2 < REQUIRED_SIZE|| height_tmp / 2 < REQUIRED_SIZE)
//                    break;
//                width_tmp /= 2;
//                height_tmp /= 2;
//                scale *= 2;
//            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

//    public static  Bitmap getBitmap(String urlStr) throws IOException{
//        Bitmap bitmap;
//        URL url = new URL(urlStr);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setReadTimeout(5 * 1000);
//        conn.setDoInput(true);
//        conn.connect();
//        InputStream is = conn.getInputStream();
//
////        int[] arr=ImageTool.calImageRecent(options.outWidth, options.outHeight, 500);
////        bitmap =ImageTool.getSmallBitmap(is,480,800); //BitmapFactory.decodeStream(is, null, options); //ImageTool.getSmallBitmap(is,480,800);//BitmapFactory.decodeStream(is);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(is,null,options);
//
//        // Calculate inSampleSize
//        int size = ImageTool.calculateInSampleSize(options, 480,800);
//        // Decode bitmap with inSampleSize set
//        BitmapFactory.Options options1 = new BitmapFactory.Options();
//        options1.inJustDecodeBounds = false;
//        options1.inSampleSize= size;
//        bitmap = BitmapFactory.decodeStream(is, null, options1);
//        is.close();
//        return bitmap;
//    }

	public interface IdownImageCallback{
		void finished(String path);
	}

}