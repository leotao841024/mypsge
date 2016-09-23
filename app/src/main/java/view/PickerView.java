package view;

import helper.BGHelper;
import helper.SPHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.renrenstep.InfoActivity;

import constant.Cons;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import app.MyApplication;

public class PickerView extends View {
	
	public int unitColor; 
	private String text;
	public String unit = "";

	public static final String TAG = "PickerView";

	public static final float MARGIN_ALPHA = 2.8f;

	public static final float SPEED = 2;

	private List<String> mDataList;

	private int mCurrentSelected;
	private Paint mPaint, unitPaint;

	private float mMaxTextSize = 30;
	private float mMinTextSize = 15;

	private float mMaxTextAlpha = 255;
	private float mMinTextAlpha = 120;
	//int textcolor=SPHelper.getDetailMsg(InfoActivity.this, "gender", "M").equals("M")?0xff3D98FF:0xffff0000;
	//private int mColorText = getResources().getColor(MyApplication.getInstance().getBackground());
	private int mColorText = 0 ;//SPHelper.getDetailMsg(getContext(), Cons.APP_SEX, "M").equals("F")?0xffff0000:0xff3D98FF;

	private int mViewHeight;
	private int mViewWidth;
	
	private float mLastDownY;

	private float mMoveLen = 0;
	private boolean isInit = false;
	private onSelectListener mSelectListener;
	private Timer timer;
	private MyTimerTask mTask;

	Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (Math.abs(mMoveLen) < SPEED) {
				mMoveLen = 0;
				if (mTask != null) {
					mTask.cancel();
					mTask = null;
					performSelect();
				}
			} else
				mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
			invalidate();
		}

	};

	public PickerView(Context context) {
		super(context);
		init();
	}

	public PickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setOnSelectListener(onSelectListener listener) {
		mSelectListener = listener;
	}

	private void performSelect() {
//		if (mSelectListener != null) {
			text = mDataList.get(mCurrentSelected);
//			mSelectListener.onSelect(text);
//		}
	}

	public String getText() {
		return text;
	}
	
	public void setData(List<String> datas) {
		mDataList = datas;
		mCurrentSelected = datas.size() / 2;
		invalidate();
	}

	public void setSelected(int selected) {
		mCurrentSelected = selected;
		int distance = mDataList.size() / 2 - mCurrentSelected;
		if (distance < 0)
			for (int i = 0; i < -distance; i++) {
				moveHeadToTail();
				mCurrentSelected--;
			}
		else if (distance > 0)
			for (int i = 0; i < distance; i++) {
				moveTailToHead();
				mCurrentSelected++;
			}
		invalidate();
	}

	public void setSelected(String mSelectItem) {
		for (int i = 0; i < mDataList.size(); i++)
			if (mDataList.get(i).equals(mSelectItem)) {
				setSelected(i);
				break;
		}
		text = mSelectItem;
	}
	
	private void moveHeadToTail() {
		String head = mDataList.get(0);
		mDataList.remove(0);
		mDataList.add(head);
	}

	private void moveTailToHead() {
		String tail = mDataList.get(mDataList.size() - 1);
		mDataList.remove(mDataList.size() - 1);
		mDataList.add(0, tail);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewHeight = getMeasuredHeight();
		mViewWidth = getMeasuredWidth();

		mMaxTextSize = mViewHeight / 4.0f;
		mMinTextSize = mMaxTextSize / 2f;
		isInit = true;
		invalidate();
	}

	private void init() {
//		unitColor= getResources().getColor(MyApplication.getInstance().getBackground());
		unitColor = BGHelper.setBackground(getContext(), SPHelper.getDetailMsg(getContext(), Cons.APP_SEX, "M"));
		timer = new Timer();
		mDataList = new ArrayList<String>();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		unitPaint = new Paint();
	}
	
	public void initPaintStyle(){
		mPaint.setStyle(Style.FILL);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(mColorText);
		unitPaint.setColor(mColorText);
		unitPaint.setTextSize(40);
		unitPaint.setTextAlign(Align.CENTER);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isInit)
			drawData(canvas);
		
		canvas.drawLine(0, getHeight()/3, getWidth(), getHeight()/3, unitPaint);
		canvas.drawLine(0, getHeight()/3*2, getWidth(), getHeight()/3*2, unitPaint);
		canvas.drawText(unit, getWidth()-50, getHeight()/2+20, unitPaint);
	}

	private void drawData(Canvas canvas) {

		float scale = parabola(mViewHeight / 4.0f, mMoveLen);
		float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
		mPaint.setTextSize(size);
		mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));

		float x = (float) (mViewWidth / 2.0);
		float y = (float) (mViewHeight / 2.0 + mMoveLen);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
		if (mDataList != null && mDataList.size() != 0) {
			canvas.drawText(mDataList.get(mCurrentSelected), x, baseline,
					mPaint);
		}
		for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
			drawOtherText(canvas, i, -1);
		}

		for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
			drawOtherText(canvas, i, 1);
		}

	}
	
	public void setTextColor(int color){
		this.mColorText = color;
	}


	private void drawOtherText(Canvas canvas, int position, int type) {
		float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type
				* mMoveLen);
		float scale = parabola(mViewHeight / 4.0f, d);
		float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
		mPaint.setTextSize(size);
		mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
		float y = (float) (mViewHeight / 2.0 + type * d);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
		canvas.drawText(mDataList.get(mCurrentSelected + type * position),
				(float) (mViewWidth / 2.0), baseline, mPaint);
	}

	private float parabola(float zero, float x) {
		float f = (float) (1 - Math.pow(x / zero, 2));
		return f < 0 ? 0 : f;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			doDown(event);
			break;
		case MotionEvent.ACTION_MOVE:
			doMove(event);
			break;
		case MotionEvent.ACTION_UP:
			doUp(event);
			break;
		}
		return true;
	}

	private void doDown(MotionEvent event) {
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mLastDownY = event.getY();
	}

	private void doMove(MotionEvent event) {

		mMoveLen += (event.getY() - mLastDownY);

		if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {

			moveTailToHead();
			mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
		} else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {

			moveHeadToTail();
			mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
		}

		mLastDownY = event.getY();
		invalidate();
	}

	private void doUp(MotionEvent event) {

		if (Math.abs(mMoveLen) < 0.0001) {
			mMoveLen = 0;
			return;
		}
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mTask = new MyTimerTask(updateHandler);
		timer.schedule(mTask, 0, 10);
	}
	public void setUnitText(String unit){
		this.unit = unit;
	}
	public void setUnitColor(int unitColor){
		this.unitColor = unitColor;
	}
	public void setmColor(int mColorText){
		this.mColorText = mColorText;
	}

	class MyTimerTask extends TimerTask {
		Handler handler;

		public MyTimerTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendMessage(handler.obtainMessage());
		}

	}

	public interface onSelectListener {
		void onSelect(String text);
	}
}
