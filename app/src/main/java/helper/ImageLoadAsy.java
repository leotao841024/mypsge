package helper;

import manager.ImageCacheManger;
import manager.ImageCacheManger.IdownImageCallback;
import tools.FileUtils;
import tools.ImageTool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;

import java.io.File;

public class ImageLoadAsy extends AsyncTask<String, Void, Bitmap> {
	public enum LoadType{
		NORMAL,
		FILTER
	}
	public interface ImageLodeCallback{
		void handler(Bitmap map);
	}
	private ImageCacheManger imageCacheManger;
	private ImageLodeCallback callback;
	private String url="";
	private LoadType typ;
	private String key="";
	private boolean isleft;
	public ImageLoadAsy(ImageLodeCallback callback,String url,LoadType typ,String key){
		this.imageCacheManger = new ImageCacheManger("stepic");
		this.callback = callback;
		this.url = url;
		this.typ = typ;
		this.key=key;
	}
	public void setIsleft(boolean isleft){
		this.isleft=isleft;
	}
	@Override
	protected Bitmap doInBackground(String... arg0) {
		String[] filenms=url.split("/");
		if(filenms.length<=0){
			return null;
		}
		Bitmap bitmap = imageCacheManger.getCachedBitmap(key);
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		if(result==null){//Cons.DONW_PIC+filenm
			File file=new File(url);
			if(file.exists()&&LoadType.FILTER==typ){
				new ImageCoverCallback(key).finished(url);
			}else {
				IdownImageCallback call = filter();
				imageCacheManger.downData(url, call);
			}
		}else{
			callback.handler(result);
		}
	}
	IdownImageCallback filter(){
		LoadType[] num={LoadType.NORMAL,LoadType.FILTER};
		IdownImageCallback[] handlers={new ImageNormalCallback(key),new ImageCoverCallback(key)};
		for(int i=0;i<num.length;i++){
			if(num[i]==typ){
				return handlers[i];
			}
		}
		return handlers[0];
	}

	class BaseImageCallback implements IdownImageCallback{
		protected String key;
		public BaseImageCallback(String key){
			this.key=key;
		}
		@Override
		public void finished(String path) {}
		public void save(String key,Bitmap value){
			imageCacheManger.addBitmap(key,value);
		}
	}

	class ImageNormalCallback extends BaseImageCallback {
		public ImageNormalCallback(String key) {
			super(key);
		}
		@Override
		public void finished(String path) {
			Bitmap bitmap= BitmapFactory.decodeFile(path);
			callback.handler(bitmap);
			if(bitmap!=null) {
				save(key, bitmap);
			}
		}
	}
	class ImageCoverCallback extends BaseImageCallback{
		public ImageCoverCallback(String key) {
			super(key);
		}
		@Override
		public void finished(String path) {
			BitmapFactory.Options options = ImageTool.calculateBitmapSize(path);
			int[] arr = ImageTool.calImageRecent(options.outWidth, options.outHeight, 500);
			Bitmap bitmap = ImageTool.getSmallBitmap(path, arr[0], arr[1]);
			if(bitmap!=null) {
				bitmap = ImageTool.changeImageToTalkingImage(bitmap,isleft);
				save(key, bitmap);
				FileUtils filetool = new FileUtils("stepic");
				filetool.createSDDir();
				filetool.saveMyBitmap(key, bitmap);
			}
			callback.handler(bitmap);
		}
	}
}
