package com.android.demo.floatwin;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.demo.floatwin.FloatView.OnLongPressListener;
import com.android.demo.floatwin.FloatView.OnPressListener;

public class FloatService extends Service implements OnClickListener{
	private static final String TAG = "FloatService";
	
	private WindowManager mWindowManager=null;
	private WindowManager.LayoutParams mLayoutParams=null;
	
	private View mFloatView = null;
	
	private Handler mHandler;
	
	private boolean mStopped = false;
	private DetectTopActivity mDetectTopActivity = null;
	private ActivityManager mActivityManager = null;
	
	private TextView mTextView;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		initLayoutParams();
		
		mTextView = new TextView(this);
		mTextView.setTextColor(Color.RED);
		mTextView.setOnClickListener(this);
		createView();
		mHandler = new Handler();
		mActivityManager = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
		mDetectTopActivity = new DetectTopActivity();
		mHandler.postDelayed(mDetectTopActivity, 1000);
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		mWindowManager.removeView(mFloatView);
		mStopped = true;
		mDetectTopActivity = null;
	}
	private void initLayoutParams(){
		mWindowManager = (WindowManager)getSystemService(Service.WINDOW_SERVICE);
    	mLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams.type = LayoutParams.TYPE_PHONE; // 设置window type
		mLayoutParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        mLayoutParams.alpha = 1.0f;
        //设置Window flag
		mLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         mLayoutParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL 
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
        */
//        mLayoutParams.gravity=Gravity.LEFT|Gravity.TOP;   //调整悬浮窗口至左上角
        mLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
        //以屏幕左上角为原点，设置x、y初始值
		mLayoutParams.x = 0;
		mLayoutParams.y = 0;

		// 设置悬浮窗口长宽数据
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		mLayoutParams.width = LayoutParams.WRAP_CONTENT;
		mLayoutParams.height = LayoutParams.WRAP_CONTENT;
	}
	private void createView(){
    	//获取WindowManager
		FloatView floatView = null;
		floatView = new FloatView(getApplicationContext(), mWindowManager, mLayoutParams);  
//		floatView.setBackgroundColor(Color.RED);
        
		floatView.setOnPressListener(new OnPressListener() {
			
			@Override
			public void onShortPress() {
				Log.d(TAG, "HaHa, onShortPress");
				ActivityManager am = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
				String packageName = am.getRunningTasks(1).get(0).topActivity.getClassName();
				Log.d(TAG, "packageName = " + packageName);
				if(!"com.android.demo.floatwin.FloatViewActivity".equals(packageName)){
					Intent intent = new Intent(FloatService.this, FloatViewActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});
		floatView.setOnLongPressListener(new OnLongPressListener() {
			
			@Override
			public void onLongPress() {
				Log.d(TAG, "HaHa, onLongPress");
			}
		});
        //显示myFloatView图像
		mFloatView = floatView;
//		ImageView imageView = new ImageView(this);
//		imageView.setImageResource(R.drawable.fengjie);
		mFloatView = mTextView;
        mWindowManager.addView(mFloatView, mLayoutParams);
        /*
        float fromX = 1.0f;
		float toX = 0.5f;
		float fromY = 1.0f;
		float toY = 0.5f;
		float pivotX = mFloatView.getWidth() / 2;
		float pivotY = mFloatView.getHeight() / 2;
		ScaleAnimation animation = new ScaleAnimation(fromX, toX, fromY, toY, pivotX, pivotY);
		animation.setDuration(500);
		animation.setRepeatMode(Animation.REVERSE);
		animation.setRepeatCount(Animation.INFINITE);
		mFloatView.setAnimation(animation);
		animation.start();	
		*/
    }
	
	class DetectTopActivity implements Runnable{
		@Override
		public void run() {
			if(mStopped){
				return ;
			}
			String className = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
			MemoryInfo info = new MemoryInfo();
			mActivityManager.getMemoryInfo(info);
			long cur = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String time = sdf.format(new Date(cur));
			mTextView.setText(time);
			Log.d(TAG, "TopClassName = " + className);
			
			mHandler.postDelayed(mDetectTopActivity, 1000);
		}
		
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "HaHa, onShortPress");
		ActivityManager am = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
		String packageName = am.getRunningTasks(1).get(0).topActivity.getClassName();
		Log.d(TAG, "packageName = " + packageName);
		if(!"com.android.demo.floatwin.FloatViewActivity".equals(packageName)){
			Intent intent = new Intent(FloatService.this, FloatViewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

}
