package view;
 


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TalkingContentImageView extends ImageView {
	private static final int COLORDRAWABLE_DIMENSION = 1;

	private static final int DEFAULT_BORDER_WIDTH = 0;
	private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

	public boolean isleft() {
		return isleft;
	}

	public void setIsleft(boolean isleft) {
		this.isleft = isleft;
	}
	private boolean isleft;
	public TalkingContentImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		Drawable drwable= getDrawable();
//		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.aa);//((BitmapDrawable)drwable).getBitmap();
////		Bitmap tobitmap = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
//		Paint paint = new Paint();
//////		RectF rect=new RectF(0, 0, 200, 200);
////		paint.setAntiAlias(true);
//////		canvas.drawRoundRect(rect,0, 0, paint);
////		int hk=w>h?w:h;
////		Rect rect=new Rect(0, 0,hk, hk);
////		Bitmap tobitmap = Bitmap.createBitmap(hk, hk, Config.ARGB_8888);
////		Canvas cans=new Canvas(tobitmap);
////		Bitmap bg = ((BitmapDrawable)getResources().getDrawable(R.drawable.aa)).getBitmap();
////		Bitmap front = ((BitmapDrawable)getResources().getDrawable(R.drawable.bb)).getBitmap();
////		canvas.drawBitmap(bg, 0, 0, paint);
////		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
//		Bitmap bitmap_bg = BitmapFactory.decodeResource(getResources(), R.drawable.chat_adapter_to_bg);
//		Bitmap bitmap = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
//		Canvas cans=new Canvas(bitmap);
//		cans.drawBitmap(bitmap_bg, 0, 0, paint);
//		//canvas.drawBitmap(getRoundCornerImage(bitmap_bg,b),0, 0, paint);
//		Rect from=new Rect(0,0,bitmap_bg.getWidth(),bitmap_bg.getHeight());
//		Rect to=new Rect(0,0,500,500);
//		canvas.drawBitmap(bitmap, from, to, paint);
		//super.onDraw(canvas);
//		
//		Bitmap aa = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
//		Bitmap bb = BitmapFactory.decodeResource(getResources(),R.drawable.chat_adapter_to_bg);
//		Bitmap cc = BitmapFactory.decodeResource(getResources(),R.drawable.bb);
//		
//		Bitmap bg = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
//		Canvas canvas1=new Canvas(bg);
//		Rect rect=new Rect(0, 0, 500, 500);
//		
//		canvas1.drawBitmap(aa, new Rect(0, 0, aa.getWidth(),aa.getHeight()), rect, null);
//		
//		Paint paint = new Paint();
//		paint.setAntiAlias(true);
//		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
//		canvas1.drawBitmap(bb,  new Rect(0, 0, bb.getWidth(),bb.getHeight()), rect, paint);
//		canvas1.drawBitmap(cc,300, 100,paint);
//		
//		canvas.drawBitmap(bg, 0, 0, null);
		//super.onDraw(canvas);
			///	canvas.drawBitmap(bitmap, 0, 0, null);
		//paint.setFilterBitmap(true);
//      paint.setDither(true);  
//      canvas1.drawARGB(0, 0, 0, 0);  
        //paint.setColor(Color.parseColor("#BAB399"));  
        //paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        //canvas1.drawCircle(28, 28, 30, paint);
		Drawable sourcedrawable= getDrawable();
		Bitmap bitmap= getBitmapFromDrawable(sourcedrawable); //((BitmapDrawable)sourcedrawable).getBitmap();
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int maxval = w>h?w:h;
		boolean iswith = w>h;
		Bitmap cover = null;
		//int with,height=0;
		if(maxval>300){
			int toval = iswith ? h : w;
			int soufromval= 300;
			int soutoval= (int)((300*1.0/maxval)*toval);
			w=iswith?soufromval:soutoval;
			h=iswith?soutoval:soufromval;
			//cover = Bitmap.createBitmap(iswith?soufromval:soutoval, iswith?soutoval:soufromval,Config.ARGB_8888);
		}
		cover = Bitmap.createBitmap(w,h,Config.ARGB_8888);
		Canvas canvas1=new Canvas(cover); 
		drawRoundRect(w, h, canvas1);
        drawSan(w,canvas1);
        Paint paint=new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        if(maxval>300){
        	Rect from=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        	Rect to=new Rect(0,0,w,h);
        	canvas1.drawBitmap(bitmap,from,to, paint);
        }else{
        	canvas1.drawBitmap(bitmap, 0, 0, paint);
        }
		canvas.drawBitmap(cover, 0, 0, null); 
	}


	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		try {
			Bitmap bitmap;
			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	void drawSan(int with,Canvas pcanvas){
		Paint paint=new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
	    Path path = new Path();
		int basewidth=isleft?10:with-10;
        path.moveTo(basewidth, 10);// 此点为多边形的起点
        path.lineTo(isleft ? 0 : with, 20);
        path.lineTo(basewidth, 30);
        path.close(); // 使这些点构成封闭的多边形  
        pcanvas.drawPath(path, paint);	
	}
	
	void drawRoundRect(int width,int height,Canvas pcanvas){
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
		RectF oval3 =null;
		if(isleft) {
			oval3 = new RectF(10, 0, width, height);// 设置个新的长方形
		}else{
			oval3 = new RectF(0, 0, width - 10, height);// 设置个新的长方形
		}
        pcanvas.drawRoundRect(oval3, 10, 10, paint);//第二个参数是x半径，第三个参数是
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		invalidate();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		invalidate();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		invalidate();
	}
	//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
////		Drawable sourcedrawable= getDrawable();
////		Bitmap bitmap= getBitmapFromDrawable(sourcedrawable); //((BitmapDrawable)sourcedrawable).getBitmap();
////		int w = bitmap.getWidth();
////		int h = bitmap.getHeight();
////		boolean iswith = w>h;
////		int maxval = w>h?w:h;
////		Bitmap cover = null;
////		//int with,height=0;
////		if(maxval>300){
////			int toval = iswith ? h : w;
////			int soufromval= 300;
////			int soutoval= (int)((300*1.0/maxval)*toval);
////			w=iswith?soufromval:soutoval;
////			h=iswith?soutoval:soufromval;
////		}
////		setMeasuredDimension(w, h);//
//	}



	public static Bitmap getRoundCornerImage(Bitmap bitmap_bg,Bitmap bitmap_in)//#閬浘 鏄剧ず鐨勫唴瀹�
	{
		Bitmap roundConcerImage = Bitmap.createBitmap(500,500, Config.ARGB_8888);
		Canvas canvas = new Canvas(roundConcerImage);
		Paint paint = new Paint();
		Rect rect = new Rect(0,0,500,500);
		Rect rectF = new Rect(0, 0, 500, 500);
		paint.setAntiAlias(true);
		NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
		patch.draw(canvas, rect);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap_in, rectF, rect, paint);
		return roundConcerImage;
	}
	
	
}
