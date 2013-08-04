package com.android.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AntialiasTest extends Activity {

	private final String TAG = "AntialiasTest";
	private final int DELAY = 100;
	private final int MARGIN = 10;
	private LinearLayout mLayout = null;
	private CustomView mCustomView;
	private Handler mHandler;
	private boolean mStopAnimation = true;
	private boolean mAntialias = false;
	private NotifyImageViewRedraw mNotifyImageViewRedraw;
	private float mRotateDegrees = 0;
	private final float DELTA_DEGREES = 1; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.antialias);
		mLayout = (LinearLayout)findViewById(R.id.graph);
		mHandler = new Handler();
		mNotifyImageViewRedraw = new NotifyImageViewRedraw();
		CustomView view = new CustomView(this);
		view.setBackgroundColor(Color.GREEN);
		mCustomView = view;
		view.setImageResource(R.drawable.fengjie);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.leftMargin = MARGIN;
		params.rightMargin = MARGIN;
		params.topMargin = MARGIN;
		params.bottomMargin = MARGIN;
		mLayout.setGravity(Gravity.CENTER);
		mLayout.addView(view, params);
		
		
	}
	public void onClick(View view){
		int id = view.getId();
		switch(id){
		case R.id.state_image:
			mStopAnimation = !mStopAnimation;
			mHandler.post(mNotifyImageViewRedraw);
			Button button = (Button)view;
			button.setText(mStopAnimation ? R.string.state_start : R.string.state_stop);
			break;
		case R.id.state_antialias:
			mAntialias = !mAntialias;
			button = (Button)view;
			button.setText(mAntialias ? R.string.state_sawtooth : R.string.state_antialias);
			break;
		default:
			break;
		}
	}
	
	class CustomView extends ImageView{

		private int mCurW = 0;
		private int mCurH = 0;
		public CustomView(Context context) {
			super(context);
		}
		public CustomView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		public CustomView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}
		
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			Log.d(TAG, "onSizeChanged");
			mCurW = w;
			mCurH = h;
			super.onSizeChanged(w, h, oldw, oldh);
		}
		
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			Log.d(TAG, "onMeasure widthMeasureSpec = " + widthMeasureSpec + " , heightMeasureSpec = " + heightMeasureSpec);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			Log.d(TAG, "onDraw mAntialias = "  +mAntialias);
			float px = (float)mCurW / 2;
			float py = (float)mCurH / 2;
			float sx = 0.5f;
			float sy = 0.5f;
//			int w = getDrawable().getIntrinsicWidth();
//			int h = getDrawable().getIntrinsicHeight();
			RectF rect = new RectF(0, 0, mCurW ,mCurH);
			Path path = new Path();
			path.addRoundRect(rect, 20.0f, 20.0f, Direction.CW);
//			canvas.clipPath(path);
			
			canvas.scale(sx, sy, px, py);
			canvas.rotate(mRotateDegrees, px, py);
			if(mAntialias){
				PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
				canvas.setDrawFilter(filter);
			}
			
			
//			Matrix src = getImageMatrix();
//			src.postSkew(0.0f, 0.0f);
//			src.postTranslate(10.0f, 10.0f);
//			setImageMatrix(src);
			super.onDraw(canvas);
		}
		public void resetPicture(){
			mRotateDegrees = 0;
			invalidate();
		}
	}
	
	class NotifyImageViewRedraw implements Runnable{
		@Override
		public void run() {
			if(mStopAnimation){
				mCustomView.resetPicture();
				return ;
			}
			mRotateDegrees += DELTA_DEGREES;
			mCustomView.postInvalidate();
			mHandler.postDelayed(mNotifyImageViewRedraw, DELAY);
		}
		
	}
	
	
}
