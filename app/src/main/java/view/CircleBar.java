package view;

import java.text.DecimalFormat;
import java.util.HashMap;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class CircleBar extends View {

	private RectF mColorWheelRectangle = new RectF();
	private Paint mDefaultWheelPaint;//渐变
	private Paint mColorWheelPaint;//步数画笔
	private Paint mColorWheelPaintCentre;//背景圆环
	private Paint mTextP, mTextnum, mTextch;
	private Paint mFastStepPaint;//快走圆环
	private Typeface mFace;
	private float circleStrokeWidth;
	private float mTotalAnglePer, mFastAnglePer;
	private float mPercent;
	private int stepnumber, stepnumbernow, stepfastnumber;//目标步数，当前总步数，快走步数
	
	private float pressExtraStrokeWidth;
	//private BarAnimation anim;
	private int goalstepnum=10000;// 默认最大步数
	private float mPercent_y, stepnumber_y, Text_y;
	private DecimalFormat fnum = new DecimalFormat("#.0");// 格式为保留小数点后一位
	private int fastStartColor = 0xfffe5529
	, fastEndColor = 0xfffb2d5c, stepStartColor = 0xff3674E5, stepEndColor = 0xff324981;
	private float brecx,brecy,erecx,erecy;//矩形的四个坐标
	private int cav_height,cav_width;
	public CircleBar(Context context) {
		super(context);
		init(null, 0, context);
	}

	public CircleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0, context);
	}
	
	public CircleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle, context);
	}

	private void init(AttributeSet attrs, int defStyle, Context context) {
		mFace = Typeface.createFromAsset(context.getAssets(),"HELVETICANEUELTPRO-CN.OTF");
		LinearGradient stepShader = new LinearGradient(0, 0, 100, 100, new int[] {
				stepStartColor,stepEndColor
				}, null, Shader.TileMode.REPEAT);
		SweepGradient fastShader = new SweepGradient(300, 300, new int[]{fastStartColor, fastEndColor}, null);
		//渐变
		mColorWheelPaint = new Paint();
//		mColorWheelPaint.setShader(stepShader);
		mColorWheelPaint.setColor(Color.rgb(45, 86, 203));
//		Shader mShader2 = new LinearGradient(0, 0, 100, 100, new int[] {
//				Color.GREEN, Color.YELLOW, Color.GREEN,
//				}, null, Shader.TileMode.REPEAT);
//		Shader shader = new SweepGradient(getwidth()/2, getHeight()/2, new int[] { 0xff3D98FF,  
//             0xff2d56cb, 0xff32498a }, null);
//		mColorWheelPaint.setShader(shader);
		mColorWheelPaint.setStyle(Paint.Style.STROKE);// 空心
		mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔
		mColorWheelPaint.setAntiAlias(true);// 去锯齿

		mColorWheelPaintCentre = new Paint();
		mColorWheelPaintCentre.setColor(Color.rgb(215, 215, 215));
		mColorWheelPaintCentre.setStyle(Paint.Style.STROKE);
		mColorWheelPaintCentre.setStrokeCap(Paint.Cap.ROUND);
		mColorWheelPaintCentre.setAntiAlias(true);

		mDefaultWheelPaint = new Paint();
	//	mDefaultWheelPaint.setColor(Color.rgb(127, 127, 127));
		mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
		mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);
		mDefaultWheelPaint.setAntiAlias(true);

		mFastStepPaint = new Paint();// 快走
		mFastStepPaint.setTypeface(mFace);
//		Shader shader2 = new SweepGradient(getwidth()/2, getHeight()/2, new int[] { 0xfffe5529,  
//             0xfffb2d5c ,0xffFC384e}, null);
//		mFastStepPaint.setShader(shader2);
	//	mFastStepPaint.setShader(fastShader);
		mFastStepPaint.setColor(0xfffd2b5c);
		mFastStepPaint.setStyle(Paint.Style.STROKE);
		mFastStepPaint.setStrokeCap(Paint.Cap.ROUND);
		mFastStepPaint.setAntiAlias(true);

		mTextP = new Paint();//title
		mTextP.setTypeface(mFace);
		mTextP.setAntiAlias(true);
		mTextP.setColor(0xff4d4d4d);

		mTextnum = new Paint();//当前步数
		mTextnum.setTypeface(mFace);
		mTextnum.setAntiAlias(true);
		mTextnum.setColor(stepStartColor);
		mTextch = new Paint();
		mTextch.setAntiAlias(true);
		mTextch.setColor(0xff4d4d4d);
		//anim = new BarAnimation();
		invalidate();
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawArc(mColorWheelRectangle, 0, 359, false, mDefaultWheelPaint);//画灰色底圆
		canvas.drawArc(mColorWheelRectangle, 0, 359, false,
				mColorWheelPaintCentre);//画灰色小圆
		canvas.drawArc(mColorWheelRectangle, 270, mTotalAnglePer, false,
				mColorWheelPaint);//整体进程
		
		canvas.drawText("今日步数", mColorWheelRectangle.centerX()
				- (mTextP.measureText(String.valueOf("今日步数")) / 2),
				mPercent_y, mTextP);
		canvas.drawText(stepnumber + "", mColorWheelRectangle.centerX()
				- (mTextnum.measureText(String.valueOf(stepnumber)) / 2),
				stepnumber_y, mTextnum);
		canvas.drawText(
				"快走"+stepfastnumber+"步",
				mColorWheelRectangle.centerX()
						- (mTextch.measureText(String.valueOf("快走"+stepfastnumber+"步")) / 2),
				Text_y, mTextch);
		canvas.drawArc(mColorWheelRectangle, 270, mFastAnglePer, false,
				mFastStepPaint); //快走的
		//brecx,brecy,erecx,erecy; 
		//Paint p11 = new Paint();
		//p11.setColor(Color.RED);// 设置红色 
		//p11.setStyle(Paint.Style.STROKE);
		//p11.setStrokeWidth(40); 
		//Bitmap bitmap = BitmapFactory.decodeResource(getResources(), com.example.renrenstep.R.drawable.icon_running_finish);
		//canvas.drawPoint((float)x1+40,(float)y1,p11);
		//canvas.drawBitmap(bitmap, x1,y1-bitmap.getHeight(), p11);
		//Paint p = new Paint();
		//p.setColor(Color.GRAY);// 设置红色 
	   /// p.setStyle(Paint.Style.STROKE);
	   // p.setStrokeWidth(10);
	   // canvas.drawPoint(0,0,p);
		//canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), p);
	}
	public int getheight(){
		return this.cav_height; 
	}
	public int getwidth(){
		return this.cav_width;
	}
	public float getTotalAngle(){
		return this.mTotalAnglePer;
	}
	public HashMap<String,Float> getPoint(){
		float banjin=(erecy-brecy)/2;
		float yuandianx=banjin+brecx;
		float yuandiany=banjin+brecy;
		float x1   =  (float)(yuandianx   +   banjin   *   Math.cos((mTotalAnglePer+270)*3.14/180));
		float y1   =  (float)(yuandiany   +   banjin   *   Math.sin((mTotalAnglePer+270)*3.14/180));
		HashMap<String,Float> mp=new HashMap<String, Float>();
		mp.put("pointx", x1);
		mp.put("pointy", y1); 
		return mp;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int min = Math.min(width, height);// 获取View最短边的长度
		cav_height=min;
		cav_width=min;
		setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
		circleStrokeWidth = Textscale(25, min);// 圆弧的宽度
		pressExtraStrokeWidth = Textscale(2, min);// 圆弧离矩形的距离
		this.brecx=this.brecy=circleStrokeWidth + pressExtraStrokeWidth;
		this.erecx=this.erecy=min- circleStrokeWidth - pressExtraStrokeWidth;
		mColorWheelRectangle.set(brecx,brecy,erecx,erecy);// 设置矩形
		mTextP.setTextSize(Textscale(50, min));
		mTextnum.setTextSize(Textscale(150, min));
		mTextch.setTextSize(Textscale(45, min));
		mPercent_y = Textscale(160, min); 
		stepnumber_y = Textscale(300, min);
		Text_y = Textscale(380, min);
		mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
		mFastStepPaint.setStrokeWidth(circleStrokeWidth);
		mColorWheelPaintCentre.setStrokeWidth(circleStrokeWidth);
	}

	/**
	 * 进度条动画
	 * 
	 * @author Administrator
	 * 
	 */
	/*
	public class BarAnimation extends Animation {
		public BarAnimation() {

		}

		/**
		 * 每次系统调用这个方法时， 改变mSweepAnglePer，mPercent，stepnumbernow的值，
		 * 然后调用postInvalidate()不停的绘制view。
		 */
	/*
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f) {
				mPercent = Float.parseFloat(fnum.format(interpolatedTime
						* stepnumber * 100f / goalstepnum));// 将浮点值四舍五入保留一位小数
				mSweepAnglePer = interpolatedTime * stepnumber * 360
						/ goalstepnum;
				mFastAnglePer = interpolatedTime * stepfastnumber * 360
						/ goalstepnum;
				stepnumbernow = (int) (interpolatedTime * stepnumber);
			} else {
				mPercent = Float.parseFloat(fnum.format(stepnumber * 100f
						/ goalstepnum));// 将浮点值四舍五入保留一位小数
				mSweepAnglePer = stepnumber * 360 / goalstepnum;
				mFastAnglePer = stepfastnumber * 360 / goalstepnum;
				stepnumbernow = stepnumber;
			}
			postInvalidate();
		}
	}*/
	
	public void setStepColor(int color){
		mColorWheelPaint.setColor(color);
	}

	/**
	 * 根据控件的大小改变绝对位置的比例
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public float Textscale(float n, float m) {
		return n / 500 * m;
	}

	/**
	 * 更新步数和设置一圈动画时间
	 * 
	 * @param stepnumber
	 * @param time
	 */
	public void update(int stepnumber,int stepfastnumber) {
		this.stepnumber = stepnumber;
		this.stepfastnumber = stepfastnumber;
		calJiaoDu();
		invalidate(); 
	}
	private void calJiaoDu(){
		float per=stepnumber/(float)goalstepnum;
		per=per>1?1:per;
		float totaljd= 360*per;
		float fastper=stepnumber==0?0:stepfastnumber/(float)stepnumber;
		float fastjd=fastper*totaljd;
		this.mTotalAnglePer=totaljd;
		this.mFastAnglePer=fastjd;
	}
	

	/**
	 * 设置每天的最大步数
	 * 
	 * @param Maxstepnumber
	 */
	public void setMaxstepnumber(int Maxstepnumber) {
		this.goalstepnum = Maxstepnumber;
	}

	/**
	 * 设置进度条颜色
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int red, int green, int blue) {
		mColorWheelPaint.setColor(Color.rgb(red, green, blue));
	}

	/**
	 * 设置动画时间
	 * 
	 * @param time
	 */
	/*
	public void setAnimationTime(int time) {
		anim.setDuration(time * stepnumber / goalstepnum);// 按照比例设置动画执行时间
	}
	*/
}
