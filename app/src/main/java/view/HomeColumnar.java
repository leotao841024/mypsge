package view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import bean.StepHisData;
import bean.XYPoint;

import com.example.renrenstep.R;

public class HomeColumnar extends View {
	public interface OnItemClickListener {
		void OnItemClick(int position);
	}
	OnItemClickListener itemClickListener;
	private List<XYPoint> xyPoints = new ArrayList<XYPoint>();
	private int index;
	private List<StepHisData> hisdata;
	private float tb, xHeight, yWidth;
	private float interval_left_right;
	private float base;
	private Paint paint_Columnar, paint_xy;
	private int max_step,min_step;
	private int blueLineColor = 0xffafc7f5; // 蓝色
	private int redLineColor = 0xffFB2F5A; //红色
	private int xyColor = 0xff999999;
	
	public void setColor(int normalColor, int selectColor){
		this.blueLineColor = normalColor;
		this.redLineColor = selectColor;
		init();
		invalidate();
	}

	public HomeColumnar(Context context, List<StepHisData> hisdata,
			OnItemClickListener itemClickListener,int width) {
		super(context);
		this.hisdata =hisdata;
		this.itemClickListener = itemClickListener;
		setWillNotDraw(false);
		this.max_step = getMaxStep();
		this.min_step = getMinStep();
		this.yWidth=width;
		init();
	}

	public HomeColumnar(Context context, List<StepHisData> hisdata,int width) {
		super(context);
		this.hisdata =hisdata;
		setWillNotDraw(false);
		this.max_step = getMaxStep();
		this.min_step = getMinStep();
		this.yWidth=width;
		init();
	}

	private void init() {
		index = hisdata.size()-1;
		Resources res = getResources();//
		tb=res.getDimension(R.dimen.historyscore_tb);
		interval_left_right = this.yWidth/(hisdata.size()*2)-(this.yWidth/(hisdata.size()*2))/(hisdata.size()*2);
		//interval_left_right = tb * 3.0f; 
		paint_xy = new Paint();
		paint_xy.setStrokeWidth(tb * 0.1f);
		paint_xy.setTextSize(tb * 1.2f);
		paint_xy.setColor(xyColor);
		paint_xy.setTextAlign(Align.CENTER);

		paint_Columnar = new Paint();
		paint_Columnar.setStrokeWidth(tb * 0.1f);
		paint_Columnar.setStyle(Style.FILL);
		paint_Columnar.setAntiAlias(true);
		setLayoutParams(new LayoutParams((int) (hisdata.size()
				* interval_left_right * 2 + tb), LayoutParams.MATCH_PARENT)); 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		xHeight = getHeight() - tb / 2 * 3;
		//yWidth = getWidth();
		base =(max_step - min_step)==0?0:(xHeight - tb * 6)/ (max_step - min_step);
		drawXY(canvas);
		drawRectf(canvas, index);
		drawDate(canvas);
	}

	private void drawXY(Canvas canvas) {

		canvas.drawLine(0, tb, 0, xHeight, paint_xy);// y轴 
		canvas.drawLine(0, xHeight, yWidth, xHeight, paint_xy);// x轴 
		canvas.drawLine(0, tb * 3, 10, tb * 3, paint_xy); 
		canvas.drawText(String.valueOf(max_step)+"步", tb * 2, tb * 3,paint_xy); 
		canvas.drawText(this.getResources().getString(R.string.hismore),yWidth/2, tb * 3-10,paint_xy); 
		//canvas.drawText("步", tb, tb * 1, paint_xy);
	}

	/**
	 * 绘制矩形
	 * 
	 * @param c
	 */
	public void drawRectf(Canvas c, int index) {
		float totalheight= xHeight-tb * 3;
		float until= totalheight/max_step;
		for (int i = 0; i < hisdata.size(); i++) {
			if(index==i){
				paint_Columnar.setColor(redLineColor);
			}else{
				paint_Columnar.setColor(blueLineColor);
			}
			float x1 =  interval_left_right * ((i + 1) * 2 - 1);		
			//float y1 =hisdata.get(i).getSteps() - min_step==0? xHeight: xHeight- ((hisdata.get(i).getSteps() - min_step) * base + tb * 3);
			float y1=xHeight-(until*hisdata.get(i).getSteps());
			float x2 =  interval_left_right * ((i + 1) * 2)+30;
			float y2 = xHeight;
			c.drawRect(x1-15, y1, x2-15, y2, paint_Columnar);
			XYPoint point = new XYPoint(x1, y1, x2, y2, i);
			xyPoints.add(point);
		}
	}
	
	private int getMinStep(){
		int num=hisdata.size()>0?hisdata.get(0).getSteps():0;
		for(StepHisData stepdata:hisdata){
			if(num>stepdata.getSteps()){
				num=stepdata.getSteps();
			}
		}
		return num;
	}
	
	private int getMaxStep(){
		int num=0;
		for(StepHisData stepdata:hisdata){
			if(num<stepdata.getSteps()){
				num=stepdata.getSteps();
			}
		}
		return num;
	}

	/**
	 * 绘制日期
	 * 
	 * @param c
	 */
	@SuppressWarnings("deprecation")
	public void drawDate(Canvas c) {
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		int month = time.getMonth();
		int day = time.getDay();
		for (int i = 0; i < hisdata.size(); i++) {
			c.drawText(hisdata.get(i).getMonth() + "/" + hisdata.get(i).getDay(), interval_left_right * ((i + 1) * 2 - 1)
					+ interval_left_right / 2, getHeight(), paint_xy);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			for (int i = 0; i < xyPoints.size(); i++) {
				XYPoint point = xyPoints.get(i);
				if (x > point.getX1() && x < point.getX2()) {
					if (y > point.getY1() && y < point.getY2()) {
						itemClickListener.OnItemClick(point.getPosition());
						index = i;
						invalidate();
						break;
					}
				}
			}
		}
		return super.onTouchEvent(event);
	}

}
