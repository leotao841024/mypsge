package view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
	 
    private boolean isPagingEnabled = true;
 
    public CustomViewPager(Context context) {
        super(context);
    }
 
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
 
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }
 
    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }}