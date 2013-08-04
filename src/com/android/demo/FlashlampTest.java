package com.android.demo;

import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IHardwareService;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class FlashlampTest extends Activity implements OnClickListener{

	private final String TAG = "FlashlampTest";
	private MyFlashLight mMyFlashLight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getLocalClassName());
		setContentView(R.layout.flash_lamp);
		Button button = (Button)findViewById(R.id.switch_flash_lamp);
		button.setOnClickListener(this);
		try {
			mMyFlashLight = new MyFlashLight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		boolean enable = getFlashlighttEnabled();
		*/
		boolean enable = mMyFlashLight.isEnabled();
		button.setText(enable ? R.string.switch_flash_off : R.string.switch_flash_on);
	}

	private void setFlashlightEnabled(boolean isEnable) {
		try {
			Method method = Class.forName("android.os.ServiceManager")
					.getMethod("getService", String.class);
			IBinder binder = (IBinder) method.invoke(null,
					new Object[] { "hardware" });

			IHardwareService localhardwareservice = IHardwareService.Stub
					.asInterface(binder);
			localhardwareservice.setFlashlightEnabled(isEnable);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private boolean getFlashlighttEnabled(){
		boolean enable = false;
		try {
			Method method = Class.forName("android.os.ServiceManager")
					.getMethod("getService", String.class);
			IBinder binder = (IBinder) method.invoke(null,
					new Object[] { "hardware" });

			IHardwareService localhardwareservice = IHardwareService.Stub
					.asInterface(binder);
			enable = localhardwareservice.getFlashlightEnabled();
		} catch (Exception e) {
			e.printStackTrace();
			enable = false;
		}
		return enable;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.switch_flash_lamp:
			/**
			boolean enable = getFlashlighttEnabled();
			setFlashlightEnabled(!enable);
			enable = getFlashlighttEnabled();
			Button button = (Button)v;
			button.setText(enable ? R.string.switch_flash_off : R.string.switch_flash_on);
			Log.d(TAG, "enable = " + enable);
			*/
			
			boolean enable = mMyFlashLight.isEnabled();
			mMyFlashLight.enable(!enable);
			enable = mMyFlashLight.isEnabled();
			Button button = (Button)v;
			button.setText(enable ? R.string.switch_flash_off : R.string.switch_flash_on);
			Log.d(TAG, "enable = " + enable);
			break;
		default:
			break;
		}
	}
	
	
	
	
	public class MyFlashLight {
		private Object svc = null;
		private Method getFlashlightEnabled = null;
		private Method setFlashlightEnabled = null;

		@SuppressWarnings("unchecked")
		public MyFlashLight() throws Exception {
			Log.d(TAG, "MyFlashLight ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			try {
				// call ServiceManager.getService("hardware") to get an IBinder
				// for the service.
				// this appears to be totally undocumented and not exposed in
				// the SDK whatsoever.
				Class sm = Class.forName("android.os.ServiceManager");
				Object hwBinder = sm.getMethod("getService", String.class)
						.invoke(null, "hardware");

				// get the hardware service stub. this seems to just get us one
				// step closer to the proxy
				Class hwsstub = Class
						.forName("android.os.IHardwareService$Stub");
				Method asInterface = hwsstub.getMethod("asInterface",
						android.os.IBinder.class);
				svc = asInterface.invoke(null, (IBinder) hwBinder);

				// grab the class (android.os.IHardwareService$Stub$Proxy) so we
				// can reflect on its methods
				Class proxy = svc.getClass();

				// save methods
				getFlashlightEnabled = proxy.getMethod("getFlashlightEnabled");
				setFlashlightEnabled = proxy.getMethod("setFlashlightEnabled",
						boolean.class);
			} catch (Exception e) {
				Log.d(TAG, "MyFlashLight~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				throw new Exception("LED could not be initialized");
			}
		}

		public boolean isEnabled() {
			try {
				Log.d(TAG, "isEnable");
				return getFlashlightEnabled.invoke(svc).equals(true);
			} catch (Exception e) {
				Log.d(TAG, "isEnabled~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				return false;
			}
		}

		public void enable(boolean tf) {
			try {
				Log.d(TAG, "enable~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				setFlashlightEnabled.invoke(svc, tf);
			} catch (Exception e) {
//				Log.d(TAG, "enable~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			}
		}

	}
}
