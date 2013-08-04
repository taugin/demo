package com.android.demo.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup{

    private static final String TAG = "ScrollLayout";      
    private VelocityTracker mVelocityTracker;  			    
    private static final int SNAP_VELOCITY = 600;        
    private Scroller  mScroller;
    private int mCurScreen;
	private int mDefaultScreen = 0;
    private int mLastMotionX;
    private int mLastMotionY;
    private int mTouchSlop;						
    
//    private static final int TOUCH_STATE_REST = 0;
//    private static final int TOUCH_STATE_SCROLLING = 1;
//    private int mTouchState = TOUCH_STATE_REST;
    
    private OnViewChangeListener mOnViewChangeListener;	 
    
    
	public ScrollLayout(Context context) {
		super(context);
		init(context);
	}	
	public ScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		mCurScreen = mDefaultScreen;    	  
	    mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();  
	    mScroller = new Scroller(context); 
	    setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	}

	public void setDefaultScreen(int screen){
		mCurScreen = screen; 
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG, "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);
	}

	 public void snapToDestination() {    
		 final int screenWidth = getWidth();    
	     final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;    
	     snapToScreen(destScreen);    
	 }  
	
	 public void snapToScreen(int whichScreen) {
		// get the valid layout page
		 whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		 if (getScrollX() != (whichScreen * getWidth())) {
			 final int delta = whichScreen * getWidth() - getScrollX();
			 mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);
			 Log.d(TAG, "snapToScreen whichScreen = " + whichScreen);
			 mCurScreen = whichScreen;
			 invalidate(); // Redraw the layout
			 if (mOnViewChangeListener != null) {
				 mOnViewChangeListener.OnViewChange(mCurScreen);
			 }
		 }
	 }

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) { 
//			Log.d(TAG, "curX = " + mScroller.getCurrX() + " , curY = " + mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
            postInvalidate();    
        }   
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int event = ev.getAction();
		int x,y;
		switch(event){
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = (int) ev.getRawX();
			mLastMotionY = (int) ev.getRawY();
			return false;
		case MotionEvent.ACTION_MOVE:
			x = (int) ev.getRawX();
			y = (int) ev.getRawY();
			int scaleX = x - mLastMotionX;
			int scaleY = y - mLastMotionY;
			if(Math.abs(scaleX) > mTouchSlop && Math.abs(scaleY) < mTouchSlop){
				return true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final int x = (int) event.getX();
		final int y = (int) event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;

		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			boolean canMove = isCanMove(deltaX);
//			Log.d(TAG, "onTouchEvent :MotionEvent.ACTION_MOVE canMove = "
//					+ canMove);
			if (canMove) {
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
				}
				mLastMotionX = x;
				scrollBy(deltaX, 0);
			}

			break;
		case MotionEvent.ACTION_UP:
			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
			} else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			// mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	private boolean isCanMove(int deltaX){
		int scroll = getScrollX();
//		Log.d(TAG, "scroll = " + scroll + " , deltaX = " + deltaX);
		if (getScrollX() <= 0 && deltaX < 0 ){
			return false;
		}	
		if  (getScrollX() >=  (getChildCount() - 1) * getWidth() && deltaX > 0){
			return false;
		}		
		return true;
	}
	
	public void SetOnViewChangeListener(OnViewChangeListener listener){
		mOnViewChangeListener = listener;
	}
	
}