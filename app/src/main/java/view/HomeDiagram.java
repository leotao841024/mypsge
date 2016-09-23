package view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.example.renrenstep.R;

/**
 * 线框图
 * 
 * @author Administrator
 * 
 */
public class HomeDiagram extends View {

	private List<Integer> milliliter;
	private float tb;
	private float interval_left_right;
	private float interval_left;
	private Paint paint_date, paint_brokenLine, paint_dottedline, framPanint;
	private Bitmap bitmap_point;
	private Path path;
	private float dotted_text;
	private float margin_bottom;
	private float mHeight ;
	private float mWidth;
	public float getDotted_text() {
		return dotted_text;
	} 
	public void setDotted_text(float dotted_text) {
		this.dotted_text = dotted_text;
	}

	public int fineLineColor = 0xff999999; // 灰色
	public int blueLineColor = 0xffFE522A; // 线的颜色
	public int point = R.drawable.icon_point_red;//点
	public int[] shaders = new int[] {
			Color.argb(100, 254, 82, 42), Color.argb(45, 254, 82, 42),
			Color.argb(10, 254, 82, 42) };
	private float base;
	
	public void setColor(int blueLineColor, int point, int[] shaders){
		this.blueLineColor = blueLineColor;
		this.point = point;
		this.shaders = shaders;
		init(milliliter);
		invalidate();
	}

	public HomeDiagram(Context context, List<Integer> milliliter,float width) {
		super(context);
		this.mWidth=width;
		init(milliliter);
	}
	
	public void init(List<Integer> milliliter) {
		if (null == milliliter || milliliter.size() == 0)
			return;
		//this.milliliter = delZero(milliliter);
		this.milliliter=milliliter;
		Resources res = getResources();
		tb = res.getDimension(R.dimen.historyscore_tb);
		//interval_left_right = tb * 3.0f-10;
		interval_left_right =mWidth-mWidth/24;
		interval_left = tb * 0.5f;

		paint_date = new Paint();
		paint_date.setStrokeWidth(tb * 0.1f);
		paint_date.setTextSize(tb * 1.2f);
		paint_date.setColor(fineLineColor);

		paint_brokenLine = new Paint();
		paint_brokenLine.setStrokeWidth(tb * 0.1f);
		paint_brokenLine.setColor(blueLineColor);
		paint_brokenLine.setAntiAlias(true);

		paint_dottedline = new Paint();
		paint_dottedline.setStyle(Paint.Style.STROKE);
		paint_dottedline.setColor(fineLineColor);

		framPanint = new Paint();
		framPanint.setAntiAlias(true);
		framPanint.setStrokeWidth(2f);

		path = new Path();
		bitmap_point = BitmapFactory.decodeResource(getResources(),
				point);
//		setLayoutParams(new LayoutParams(
	//		(int) ((this.milliliter.size()-1) * interval_left_right),
		//		LayoutParams.MATCH_PARENT));
		setLayoutParams(new LayoutParams(
				(int)(mWidth*12-mWidth/2),
							LayoutParams.MATCH_PARENT)); 
	}

	/**
	 * 移除左右为零的数据
	 * 
	 * @return
	 */
	public List<Integer> delZero(List<Integer> milliliter) {
		List<Integer> list = new ArrayList<Integer>();
		int sta = 0;
		int end = 0;
		for (int i = 0; i < milliliter.size(); i++) {
			if (milliliter.get(i) != 0) {
				sta = i;
				break;
			}
		}
		for (int i = milliliter.size() - 1; i >= 0; i--) {
			if (milliliter.get(i) != 0) {
				end = i;
				break;
			}
		}
		for (int i = 0; i < milliliter.size(); i++) {
			if (i >= sta && i <= end) {
				list.add(milliliter.get(i));
			}
		}
		dotted_text = ((Collections.max(milliliter) - Collections
				.min(milliliter)) / 12.0f * 5.0f);
		return list;
	}

	protected void onDraw(Canvas c) {
		mHeight = getHeight()-30 ;
		if (null == milliliter || milliliter.size() == 0)
			return;
		drawStraightLine(c);
		drawBrokenLine(c);
		drawDate(c);
		//mHeight - tb * 1.5f - (base * milliliter.get(i + 1))
		showDott(c, getHeight()-110 - (base * Collections.max(milliliter)));
	}

	/**
	 * 绘制竖线
	 * 
	 * @param c
	 */
	public void drawStraightLine(Canvas c) {
		float a=(mHeight - tb * 4)/ (Collections.max(milliliter) - Collections.min(milliliter));
		float y2 = mHeight - (a * Collections.max(milliliter))-tb+base;
		//float y2 = mHeight - (base * Collections.max(milliliter))-tb+base-20;
		int count_line = 0;
		//float y2 = mHeight - (base * Collections.max(milliliter))-tb+base;
		for (int i = 0; i < milliliter.size(); i++) {
			if (i==0) {//绘制Y轴
				c.drawLine(interval_left_right * i, 25, interval_left_right * i, mHeight - margin_bottom, paint_date);
				c.drawText(Collections.max(milliliter)+"步", tb/2, y2, paint_date);
			}
			//绘制竖线 连续四条为一组
			paint_dottedline.setColor(fineLineColor);
			if (count_line == 0) {
				c.drawLine(interval_left_right * i, mHeight - margin_bottom-20, interval_left_right * i, mHeight - margin_bottom, paint_date);
			}
			if (count_line == 2) {
				c.drawLine(interval_left_right * i, mHeight - margin_bottom-20, interval_left_right * i, mHeight - margin_bottom, paint_date);
			}
			if (count_line == 1 || count_line == 3) {
				c.drawLine(interval_left_right * i, mHeight - margin_bottom-10, interval_left_right * i, mHeight - margin_bottom, paint_date);
			}
			count_line++;
			if (count_line >= 4) {
				count_line = 0;
			}
		}
		//绘制X轴
		c.drawLine(0, mHeight - margin_bottom, mWidth*12-mWidth/2, mHeight - margin_bottom, paint_date);
	}

	/**
	 * 绘制折线
	 * 
	 * @param c
	 */
	public void drawBrokenLine(Canvas c) {
		base = (mHeight - tb * 4)
				/ (Collections.max(milliliter) - Collections.min(milliliter));

		Shader mShader = new LinearGradient(0, 0, 0, mHeight, shaders, null, Shader.TileMode.CLAMP);
		framPanint.setShader(mShader);
		c.drawBitmap(bitmap_point, 0-bitmap_point.getWidth()/2, mHeight-tb-bitmap_point.getHeight()/2, null);
		Date date=new Date();
		Calendar cander=Calendar.getInstance();
		int hour= cander.get(Calendar.HOUR_OF_DAY);
		//milliliter.size() - 1
		if(!isAllNone()){
		for (int i = 0; i <milliliter.size() - 1; i++) {
			float x1 = interval_left_right * i;
			float y1 = mHeight - (base * milliliter.get(i))-tb+base;
			float x2 = interval_left_right * (i + 1);
			float y2 = mHeight - (base * milliliter.get(i + 1))-tb+base;
			c.drawLine(x1, y1, x2, y2, paint_brokenLine);
			path.lineTo(x1, y1);
			if (i != 0){ 
				c.drawBitmap(bitmap_point, x1 - 15 / 2, y1- 15 / 2, null); 
			}
			if (i == milliliter.size() - 2) {
				path.lineTo(x2, y2);
				path.lineTo(x2, mHeight);
				path.lineTo(0, mHeight);
				path.close();
				c.drawPath(path, framPanint);
				c.drawBitmap(bitmap_point, x2 - bitmap_point.getWidth() / 2, y2
						- bitmap_point.getHeight() / 2, null);
			}
		}
		}
	}
	boolean isAllNone(){
		return Collections.max(milliliter)==1;
	}
	//画虚线
	public void showDott(Canvas c, float height){
		float y2 = mHeight - (base * Collections.max(milliliter))-tb+base;
		paint_dottedline.setColor(fineLineColor);
		Path path = new Path();
	//	path.moveTo(0, getHeight() - tb * 6.5f);
		path.moveTo(0,y2);
		path.lineTo(mWidth*12-mWidth/2,y2);
		PathEffect effects = new DashPathEffect(new float[] { tb * 0.3f,
				tb * 0.3f, tb * 0.3f, tb * 0.3f }, tb * 0.1f);
		paint_dottedline.setPathEffect(effects);
		c.drawPath(path, paint_dottedline);
	}

	/**
	 * 绘制时间
	 * 
	 * @param c
	 */
	public void drawDate(Canvas c) {
		int hour = 0;
		int space=15;
		for (int i = 0; i < milliliter.size(); i += 2) {
			paint_date.setStrokeWidth(tb * 2.8f);
			if(i==0){
				space=0;
			}else if(i==milliliter.size()-1){
				space=38;
			}else{
				space=15;
			}
			c.drawText(hour+"", interval_left_right * i -space,
					getHeight(), paint_date);
			hour += 4;
		} 
	}
}