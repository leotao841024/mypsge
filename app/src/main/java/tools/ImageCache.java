package tools;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageCache extends LruCache<String,Bitmap>  { 
	private static ImageCache cached = null;
	private Map<String,SoftReference<Bitmap>> maps;
	public ImageCache(int maxSize) {
		super(maxSize); 
		maps=new HashMap<String, SoftReference<Bitmap>>(); 
	}
	
	// 获取系统内存大小
	private static int getSysCachedSize() {
		int maxmemory = (int) (Runtime.getRuntime().maxMemory());
		return maxmemory;
	}

	public static ImageCache instance(){
		if (cached == null) {
			cached = new ImageCache(getSysCachedSize()/8);
		}
		return cached;
	}
	
	public Map<String,SoftReference<Bitmap>> getSoftReference(){
		return maps;
	}
	
	public void addBitmap(String key, Bitmap value) {
		cached.put(key, value);
	}

	public void removeBitmapFromCache(String key) {
		cached.remove(key);
	}
	
	public void removeAlldBitmapFromCache1() {
		for(int i=0;i<cached.size();i++){
			
		}
	}

	public int getCachedBimapCount() {
		return cached.size();
	}

	public void replaceBitMap(String key, Bitmap value) {
		cached.remove(key);
		cached.put(key, value);
	}
	
	public boolean hasBitmap(String key){
		return cached.get(key)!=null||cached.getSoftReference().containsKey(key);
	}
	
	public Bitmap getBitmap(String key){
		Bitmap bitmap = cached.get(key);
		if(bitmap==null){
			SoftReference<Bitmap> srmap= cached.getSoftReference().get(key);
			if(srmap!=null){
				bitmap=srmap.get();
			}
		}
		return bitmap;
	}
	
	public void clearBitmap(){
		cached.evictAll();
		maps.clear();
	}
	
    @Override  
    protected int sizeOf(String key, Bitmap value) {
		return value.getByteCount();
//      return value.getRowBytes() * value.getHeight();
    }

	@Override
	protected void entryRemoved(boolean evicted, String key, Bitmap oldValue,Bitmap newValue) { 
		super.entryRemoved(evicted, key, oldValue, newValue);
		if(evicted){
			SoftReference<Bitmap> bitmap=new SoftReference<Bitmap>(oldValue);
			maps.put(key, bitmap);
		}
	}


}
