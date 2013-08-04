package com.android.demo.aidl.client;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.demo.R;
import com.android.demo.aidl.IRemoteService;
import com.android.demo.aidl.server.ServerService;

public class ClientActivity extends Activity {

	private static final String TAG = "ClientActivity";
	private TextView mAidlShow = null;
	private IRemoteService mService = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
		mAidlShow = (TextView) findViewById(R.id.aidl_show);
	}
	
	public void onClick(View view){
		Log.d(TAG, "onClick");
		int id = view.getId();
		switch(id){
		case R.id.aidl_start:
			Log.d(TAG, "aidl_start");			
			Intent service = new Intent(this, ServerService.class);
			boolean b = bindService(service, mServiceConnection, Service.BIND_AUTO_CREATE);
			Log.d(TAG, "b = " + b);
			break;
		case R.id.aidl_result:
			Log.d(TAG, "aidl_result");
			long result = 0;
			try {
				result = mService.getResult();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e1){
				e1.printStackTrace();
			}
			mAidlShow.setText("result : " + result);
			unbindService(mServiceConnection);
			break;
		default:
			break;
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(ClientActivity.this, name + " Service is disconnected !", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(ClientActivity.this, name + " Service is connected !", Toast.LENGTH_SHORT).show();
			mService = IRemoteService.Stub.asInterface(service);
			try {
				mService.start();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
