package view;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class IconBar extends View { 
	private int cav_height,cav_width;
	private float totalangle;
	private int piccode;
	public IconBar(Context context) {
		super(context);
		init(null, 0);
	}
	public IconBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public IconBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		/*canvas.drawArc(mColorWheelRectangle, 0, 359, false, mDefaultWheelPaint);
		canvas.drawArc(mColorWheelRectangle, 0, 359, false,
				mColorWheelPaintCentre);
		canvas.drawArc(mColorWheelRectangle, 90, mSweepAnglePer, false,
				mColorWheelPaint);
		canvas.drawText(mPercent + "%", mColorWheelRectangle.centerX()
				- (mTextP.measureText(String.valueOf(mPercent) + "%") / 2),
				mPercent_y, mTextP);
		canvas.drawText(stepnumbernow + "", mColorWheelRectangle.centerX()
				- (mTextnum.measureText(String.valueOf(stepnumbernow)) / 2),
				stepnumber_y, mTextnum);
		canvas.drawText(
				"步数",
				mColorWheelRectangle.centerX()
						- (mTextch.measureText(String.valueOf("步数")) / 2),
				Text_y, mTextch);
		*/
		
		Paint p = new Paint();
		p.setColor(Color.RED);// 设置红色 
		p.setStyle(Paint.Style.FILL);
		if(piccode!=0){
			canvas.drawBitmap(getPic(piccode), pic_x, pic_y,p);	
		}
		//p.setStrokeWidth(10);
		//canvas.drawPoint(pic_x,pic_y,p);
		//canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), p);
	}
	
	Bitmap getPic(int piccode){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),piccode); 
		Matrix matrix = new Matrix();
		matrix.postScale(0.6f,0.6f); //长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		return resizeBmp;
	}
	int[] getPicMsgArr(int piccode){
		Bitmap map=getPic(piccode);
		if(map==null){return new int[]{15,15};}
		return new int[]{map.getWidth(),map.getHeight()};
	}
	public int getheight(){
		return this.cav_height;
	}
	public int getwidth(){
		return this.cav_width;
	}
	public void update(float pic_x,float pic_y,float totalangle){
		this.pic_x=pic_x;
		this.pic_y=pic_y;
		this.totalangle=totalangle;
		filterPoint();
		invalidate();
	}
	
	void filterPoint(){
		float[][] angles={{0,45},{45,90},{90,135},{135,180},{180,225},{225,270},{270,315},{315,360},{360,720}};
		float[][] code={{0.1f,-1.1f},{0.5f,-0.2f},{0.3f,0},{0,0.2f},{-0.4f,0.2f},{-1.1f,0},{-1.3f,-0.2f},{-0.3f,-1.1f},{-0.1f,-1.1f}};	
		int[] pic_arr={com.example.renrenstep.R.drawable.icon_running_right,com.example.renrenstep.R.drawable.icon_running_right,
				com.example.renrenstep.R.drawable.icon_running_right,com.example.renrenstep.R.drawable.icon_running_right,
				com.example.renrenstep.R.drawable.icon_running_left,com.example.renrenstep.R.drawable.icon_running_left,
				com.example.renrenstep.R.drawable.icon_running_left,com.example.renrenstep.R.drawable.icon_running_left,
				com.example.renrenstep.R.drawable.icon_running_finish};
		float px=0;float py=0;
		int index=0;
		for(int i=0;i<angles.length;i++){
			if(totalangle>=angles[i][0]&&totalangle<angles[i][1]){
				px=code[i][0];
				py=code[i][1];
				index=i;
				break;
			}
		}
		this.piccode=pic_arr[index];
		int[] arr=getPicMsgArr(piccode);
		if(totalangle==0||totalangle==360){
			this.pic_x+=px*arr[0]!=0?px*arr[0]>0?px*arr[0]-arr[0]/2:px*arr[0]:0;
			this.pic_y-=arr[1]+arr[1]/2+20;
		}else{
			this.pic_x+=px*arr[0]!=0?px*arr[0]>0?px*arr[0]+33:px*arr[0]-33:0;
			this.pic_y+=py*arr[1]!=0?py*arr[1]>0?py*arr[1]+33:py*arr[1]-33:0;
		}
	}
	
	private float pic_x,pic_y;

	public float getPic_x() {
		return pic_x;
	}

	public void setPic_x(float pic_x) {
		this.pic_x = pic_x;
	}

	public float getPic_y() {
		return pic_y;
	}

	public void setPic_y(float pic_y) {
		this.pic_y = pic_y;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		cav_height = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		cav_width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		setMeasuredDimension(cav_width, cav_height);// 强制改View为以最短边为长度的正方形
		//int min = Math.min(width, height);// 获取View最短边的长度
	}
}
