package com.android.demo;


import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
public class FlashCameraTest extends Activity implements OnClickListener, OnCheckedChangeListener{

	private final String TAG = "FlashCameraTest";
	private Camera mCamera = null;
	private Parameters mParameters = null;
	private boolean mFlashLightState = false;
	private Button mSwitchButton = null;
//	private Switch mSwitch = null; 
	private SwitchHandler mHandler = null;
	private CheckedTextView mCheckedTextView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getLocalClassName());
		setContentView(R.layout.flash_camera);
		
		mCheckedTextView = (CheckedTextView)findViewById(R.id.checkedTextView);
		mCheckedTextView.setOnClickListener(this);
//		mSwitch = (Switch)findViewById(R.id.switch_button);
//		mSwitch.setOnCheckedChangeListener(this);
		mSwitchButton = (Button)findViewById(R.id.switch_flash_lamp);
		mSwitchButton.setOnClickListener(this);
		mHandler = new SwitchHandler(this);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.switch_flash_lamp:
			mHandler.postDelayed(new SwtichFlashState(), 1000);
			updateState();
			break;
		case R.id.checkedTextView:
			mHandler.postDelayed(new SwtichFlashState(), 1000);
			updateState();
			break;
		default:
			break;
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.d(TAG, "onCheckedChanged isChecked = " + isChecked);
		if(mFlashLightState != isChecked){
			mHandler.postDelayed(new SwtichFlashState(), 1000);
			updateState();
		}
	}
	private void updateState(){
		boolean buttonState = mSwitchButton.isEnabled();
		if(buttonState){
			mSwitchButton.setText(mFlashLightState ? R.string.switch_to_off : R.string.switch_to_on);
		}else{
			mSwitchButton.setText(mFlashLightState ? R.string.switch_flash_off : R.string.switch_flash_on);
		}
		mSwitchButton.setEnabled(!buttonState);
		
//		boolean switchState = mSwitch.isEnabled();
//		mSwitch.setChecked(mFlashLightState);
//		mSwitch.setEnabled(!switchState);
		
		
		boolean checkState = mCheckedTextView.isEnabled();
		mCheckedTextView.setChecked(mFlashLightState);
		mCheckedTextView.setEnabled(!checkState);
	}
	
	private void switchFlashLamp(){
		if(!mFlashLightState){
			mCamera = Camera.open();
		}
		mParameters = mCamera.getParameters();  
        mParameters.setFlashMode(!mFlashLightState ? Parameters.FLASH_MODE_TORCH : Parameters.FLASH_MODE_OFF);//¹Ø±Õ  
        mCamera.setParameters(mParameters);  
        if(mFlashLightState){
        	mCamera.stopPreview();
        	mCamera.release();
        }else{
        	mCamera.startPreview();
        }
	}
	
	class SwtichFlashState implements Runnable{
		
		@Override
		public void run() {
			switchFlashLamp();
			mFlashLightState = !mFlashLightState;
			updateState();
		}
		
	}
	class SwitchHandler extends Handler{
		private Context mContext;
		public SwitchHandler(Context context){
			mContext = context;
		}
	}
}
