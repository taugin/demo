package com.android.demo.aidl.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.demo.aidl.IRemoteService;

public class ServerService extends Service {

	private final RemoteService mBinder = new RemoteService();
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	class RemoteService extends IRemoteService.Stub{

		private long mResult = 0;
		@Override
		public void start() throws RemoteException {
			mResult += 1000;
		}

		@Override
		public void stop() throws RemoteException {
			
		}

		@Override
		public void pause() throws RemoteException {
			
		}

		@Override
		public long getResult() throws RemoteException {
			return mResult;
		}
		
	}
}
