package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LineView extends View {

	private Paint mPaint;
	private int mColor = 0xff0000;
	public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public LineView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	} 
	@Override
	protected void onDraw(Canvas canvas) {
		mPaint = new Paint();
		mPaint.setColor(mColor);
		canvas.drawLine(0, getHeight()/2-20, getWidth(), getHeight()/2-20, mPaint);
		canvas.drawLine(0, getHeight()/2+20, getWidth(), getHeight()/2+20, mPaint);
	}

}
