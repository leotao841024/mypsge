package tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

public class PhotoTool {
	public static final int TAKE_PHOTO = 0;
	public static final int OPEN_PHOTOLIST = 1;
	public static Intent takePhotoIntent(Context context){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String filename = timeStampFormat.format(new Date());
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, filename);
		Uri photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); 
		return intent;
	}
	public static Intent openPhotoListIntent(){
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		return intent;
	}
	
	public static boolean isvalidPic(String pUri) {
		String[] kuozhan = { "jpg", "png", "jpeg" };
		String xuri = pUri.toLowerCase();
		for (String item : kuozhan) {
			if (xuri.contains(item)) {
				return true;
			}
		}
		return false;
	}

}
