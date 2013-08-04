package com.android.demo.surface;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {

	private static final String TAG = "DrawThread";
	private Canvas mCanvas = null;
	private SurfaceHolder mSurfaceHolder = null;
	private volatile boolean done = false;
	public DrawThread(SurfaceHolder holder){
		mSurfaceHolder = holder;
		done = false;
	}
	@Override
	public void run() {
		textDraw();
	}
	public void stopDraw(){
		done = true;
	}
	
	private void textDraw(){
		Canvas c = null;
		String runText = "Hello,world !";
		boolean hpositive = true;
		boolean vpositive = true;
		int index = 0;
		int color[] = new int[]{Color.RED, Color.GREEN, 
				Color.BLUE, Color.CYAN, Color.DKGRAY, 
				Color.GRAY, Color.LTGRAY, Color.MAGENTA, 
				Color.TRANSPARENT, Color.YELLOW 
				};
		String []colorName = new String[]{"RED", "GREEN", "BLUE", "CYAN", "DKGRAY", "GRAY", "LTGRAY", "MAGENTA", "TRANSPANRET", "YELLOW"};
		Rect frame = mSurfaceHolder.getSurfaceFrame();
		int xStep = frame.width() / 50;
		int yStep = frame.height() / 50;
		Bitmap bitmap = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(bitmap);
		Rect vr = new Rect();
		Rect hr = new Rect();
		int rh = 40;
		Random random = null;
		random = new Random(System.currentTimeMillis());
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextSize(40.0f);
		
		TextPaint tp = new TextPaint(paint);
		float textLen = tp.measureText(runText);
		int i = 0;
		int j = 0;
		while (!done) {
			try {
				synchronized (mSurfaceHolder) {
					c = mSurfaceHolder.lockCanvas();
//					tempCanvas.drawRect(frame, bgPaint);
					tempCanvas.drawColor(Color.BLACK);
					Log.d(TAG, "running ... ");
					vr.left = 0;
					vr.top = (frame.height() - rh) / 2;
					vr.right = i;
					vr.bottom = vr.top + rh;

					hr.left = (frame.width() - rh) / 2;
					hr.top = j;
					hr.right = hr.left + rh;
					hr.bottom = 0;
					/*
					if(r.width() >= frame.width()){
						i = 0;
						r.right = i;
						index = random.nextInt(color.length);
						paint.setColor(color[index]);
						Log.d(TAG, "Color value = " + color[index]);
					}*/
					if(i < 0){
						hpositive = true;
						index = random.nextInt(color.length);
						paint.setColor(color[index]);
					}
					if(i > frame.width()){
						hpositive = false;
						index = random.nextInt(color.length);
						paint.setColor(color[index]);
					}
					
					if(j < 0){
						vpositive = true;
					}
					if(j > frame.height()){
						vpositive = false;
					}
					tempCanvas.drawText("Color : " + colorName[index], 0, vr.bottom + rh, paint);
					tempCanvas.drawRect(vr, paint);
					tempCanvas.drawRect(hr, paint);
//					tempCanvas.drawText(runText, i, r.top, paint);
					if(c != null){
						c.drawBitmap(bitmap, 0, 0, null);
					}
					if(hpositive){
						i = i + xStep;
					}else{
						i = i - xStep;
					}
					
					if(vpositive){
						j = j + yStep;
					}else{
						j = j - yStep;
					}
//					sleep(500);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
		bitmap.recycle();
		bitmap = null;
	}
	private void pointDraw(){
		Canvas c = null;
		String runText = "Hello,world !";
		boolean positive = true;
		float x = 0, y = 0;
		int index = 0;
		int color[] = new int[]{Color.RED, Color.GREEN, 
				Color.BLUE/*, Color.CYAN, Color.DKGRAY, 
				Color.GRAY, Color.LTGRAY, Color.MAGENTA, 
				Color.TRANSPARENT, Color.YELLOW */
				};
		String []colorName = new String[]{"RED", "GREEN", "BLUE", "CYAN", "DKGRAY", "GRAY", "LTGRAY", "MAGENTA", "TRANSPANRET", "YELLOW"};
		Rect frame = mSurfaceHolder.getSurfaceFrame();
		Rect r = new Rect();
		int rh = 40;
		Random random = null;
		random = new Random(System.currentTimeMillis());
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		int baseLine = frame.height() / 2;
		TextPaint tp = new TextPaint(paint);
		float textLen = tp.measureText(runText);
		float startX = 0f;
		float startY = baseLine;
		float stopX;
		float stopY;
		int ch = 1;
		int i = 0;
		while (!done) {
			try {
				synchronized (mSurfaceHolder) {
					c = mSurfaceHolder.lockCanvas();
					r.left = 0;
					r.top = (frame.height() - rh) / 2;
					r.right = i;
					r.bottom = r.top + rh;
//					index = random.nextInt(color.length);
//					paint.setColor(color[index]);
					y = random.nextInt(50);
//					y = random.nextFloat() * frame.height();
//					Log.d(TAG, "running ... x = " + x + " , y = " + y);
					if(startX > frame.width()){
						done = true;
					}
					stopX = startX + 10;
					stopY = ch * y + baseLine;
					if(c != null){
						Log.d(TAG, "running ... sx = " + startX + " , sy = " + startY + " , stopX = " + stopX + " , stopY = " + stopY);
						c.drawLine(startX, startY, stopX, stopY, paint);
					}
					startX = stopX;
					startY = stopY;
					ch = -ch;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
	
	private void waveDraw(){
		Canvas c = null;
		String runText = "Hello,world !";
		boolean hpositive = true;
		boolean vpositive = true;
		int index = 0;
		int color[] = new int[]{Color.RED, Color.GREEN, 
				Color.BLUE, Color.CYAN, Color.DKGRAY, 
				Color.GRAY, Color.LTGRAY, Color.MAGENTA, 
				Color.TRANSPARENT, Color.YELLOW 
				};
		String []colorName = new String[]{"RED", "GREEN", "BLUE", "CYAN", "DKGRAY", "GRAY", "LTGRAY", "MAGENTA", "TRANSPANRET", "YELLOW"};
		Rect frame = mSurfaceHolder.getSurfaceFrame();
		int xStep = 5;
		Bitmap bitmap = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(bitmap);
		Rect vr = new Rect();
		Rect hr = new Rect();
		int rh = 40;
		Random random = null;
		random = new Random(System.currentTimeMillis());
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextSize(40.0f);
		
		TextPaint tp = new TextPaint(paint);
		float textLen = tp.measureText(runText);
		int i = 0;
		int j = 0;
		while (!done) {
			try {
				synchronized (mSurfaceHolder) {
					c = mSurfaceHolder.lockCanvas();
//					tempCanvas.drawColor(Color.BLACK);
					Log.d(TAG, "running ... ");
//					tempCanvas.clipRect(rect);
					if(c != null){
						c.drawBitmap(bitmap, 0, 0, null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
		bitmap.recycle();
		bitmap = null;
	}
}
