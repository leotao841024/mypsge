package comm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import db.DBhelper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;

public class GroupBitmap {
	private static DBhelper db=DBhelper.instance();
	public static synchronized void createGroupBitmap(String filenm,List<Long> pids){
		List<String> picnm = getPicNm(getMemIdStr(pids));
		Bitmap bmp = Bitmap.createBitmap(900, 900, Config.ARGB_8888); 
		bmp.eraseColor(Color.argb(0,0,0,0));
		Canvas canvas = new Canvas(bmp);
		int[][] bit_position=getBitmapArr();
		tools.FileUtils file = new tools.FileUtils("stepic");
		int index = 0;
		for (String item: picnm) {
			if(index<9){
				String dir = file.getFilePath();
				File files = new File(dir + item);
				if (files.exists()) {
					BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize=5;
					Bitmap mbmp = BitmapFactory.decodeFile(dir + item,options);
					if(mbmp==null) {continue;}
					canvas.drawBitmap(mbmp,null,new Rect(bit_position[index][0], bit_position[index][1],bit_position[index][2], bit_position[index][3]),new Paint());
					index++;
				}
			}
		}
		canvas.save(Canvas.ALL_SAVE_FLAG); 
		canvas.restore(); 
		file.saveMyBitmap(filenm + ".jpg", bmp);
	}
	
	private static String getMemIdStr(List<Long> plist) {
		StringBuilder sb = new StringBuilder();
		for (long item : plist) {
			sb.append(item + ",");
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	private static List<String> getPicNm(String param) {
		db.open();
		String sql = "select DISTINCT(acvtor) as picnm from apm_sys_friend where mid in(" + param + ")";// idcd="+ mmid + " and
		Cursor cursor = db.query(sql);
		List<String> mlist = new ArrayList<String>();
		while (cursor.moveToNext()) {
			mlist.add(cursor.getString(cursor.getColumnIndex("picnm")));
		}
		cursor.close();
		db.close();
		return mlist;
	}
	
	private static int[][] getBitmapArr(){
		int[][] arr={{0,0,300,300},{300,0,600,300},{600,0,900,300},{0,300,300,600},{300,300,600,600},{600,300,900,600},{0,600,300,900},{300,600,600,900},{600,600,900,900}};
		return arr;
	} 
	
}
