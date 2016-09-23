package tools;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

public class ImageTool { 
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	//压缩文件1
	public static Bitmap compressImage(String filepath,int size){
		Options options = new Options();
		options.inSampleSize = size;
		return BitmapFactory.decodeFile(filepath,options);
	}
	//修正图片角度
	public static Bitmap changeImage(Bitmap mp,int angle){
		Matrix matrix = new Matrix();
		matrix.setRotate(angle);
		return Bitmap.createBitmap(mp, 0, 0, mp.getWidth(), mp.getHeight(), matrix, true);
	}
	public static int[] calLayWidthHeight(int width,int height){
		int maxvalset = 500;
		int w = width;
		int h = height;
		int maxval = w > h ? w : h;
		boolean iswith = w > h;
		if(w<=10||h<=30){return new int[]{width,height};}
		if (maxval > maxvalset) {
			int toval = iswith ? h : w;
			int soufromval = maxvalset;
			int soutoval = (int) ((maxvalset * 1.0 / maxval) * toval);
			w = iswith ? soufromval : soutoval;
			h = iswith ? soutoval : soufromval;
		}
		return new int[]{w,h};
	}
	public static Bitmap changeImageToTalkingImage(Bitmap bitmap,boolean isleft){
		int maxvalset = 500;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int maxval = w > h ? w : h;
		boolean iswith = w > h;
		if(w<=20||h<=45){return bitmap;}
		Bitmap cover = null;
		if (maxval > maxvalset) {
			int toval = iswith ? h : w;
			int soufromval = maxvalset;
			int soutoval = (int) ((maxvalset * 1.0 / maxval) * toval);
			w = iswith ? soufromval : soutoval;
			h = iswith ? soutoval : soufromval;
		}
		cover = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas1 = new Canvas(cover);
		drawRoundRect(w, h, canvas1,isleft);
		drawSan(w, canvas1,isleft);
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		if (maxval > maxvalset) {
			Rect from = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			Rect to = new Rect(0, 0, w, h);
			canvas1.drawBitmap(bitmap, from, to, paint);
		} else {
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			canvas1.drawBitmap(bitmap, rect, rect, paint);
		}
		return cover;
	}

	public static int[] calImageRecent(int width,int height,int maxvalset){
		int w = width;
		int h = height;
		int maxval = w > h ? w : h;
		boolean iswith = w > h;
		if(w<=20||h<=45){return new int[]{w,h};}
		if (maxval > maxvalset) {
			int toval = iswith ? h : w;
			int soufromval = maxvalset;
			int soutoval = (int) ((maxvalset * 1.0 / maxval) * toval);
			w = iswith ? soufromval : soutoval;
			h = iswith ? soutoval : soufromval;
		}
		return new int[]{w,h};
	}

	static void drawSan(int with, Canvas pcanvas,boolean isleft) {
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		Path path = new Path();
        int basewidth = isleft ? 20 : with - 20;
		path.moveTo(basewidth, 20);// 此点为多边形的起点
        path.lineTo(isleft ? 0 : with, 30);
		path.lineTo(basewidth, 45);
		path.close(); // 使这些点构成封闭的多边形
		pcanvas.drawPath(path, paint);
	}

	static void drawRoundRect(int width, int height, Canvas pcanvas,boolean isleft) {
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		RectF oval3 = null;
        if (isleft) {
            oval3 = new RectF(20, 0, width, height);// 设置个新的长方形
        } else {
			oval3 = new RectF(0, 0, width - 20, height);// 设置个新的长方形
        }
		pcanvas.drawRoundRect(oval3, 10, 10, paint);//第二个参数是x半径，第三个参数是
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath,int width,int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);

	}


	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(InputStream stream,int width,int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(stream, null, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(stream, null, options);
	}

	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	//计算图片的缩放值
	public static BitmapFactory.Options calculateBitmapSize(String filepath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath,options);
		return options;
	}


	//计算图片的缩放值
	public static int calculateInSampleSizeBySize(int optionswith,int optionheight,int reqWidth, int reqHeight) {
		final int height = optionheight;
		final int width = optionswith;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}



}
