package com.android.demo.surface;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class DrawView extends SurfaceView implements Callback {

	private static final String TAG = "DrawView";
	private DrawThread mDrawThread = null;
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	private int mSuffaceFormat;
	public DrawView(Context context) {
		super(context);
	}
	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public DrawView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mSurfaceWidth = width;
		mSurfaceHeight = height;
		mSuffaceFormat = format;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG,"surfaceCreated");
		if(mDrawThread == null){
			mDrawThread = new DrawThread(holder);
			mDrawThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(mDrawThread != null){
			mDrawThread.stopDraw();
			try {
				mDrawThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getSurfaceWidth(){
		return mSurfaceWidth;
	}
	
	public int getSurfaceHeight(){
		return mSurfaceHeight;
	}
	
	public int getSurfaceFormat(){
		return mSuffaceFormat;
	}

}
