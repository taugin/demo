package com.android.demo.surface;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class SurfaceViewTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DrawView drawView = new DrawView(this);
		SurfaceHolder holder = drawView.getHolder();
		holder.addCallback(drawView);
		WindowManager.LayoutParams p = getWindow().getAttributes();
		p.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		setContentView(drawView);
	}
	
}
