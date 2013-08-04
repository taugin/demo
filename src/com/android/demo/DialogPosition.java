package com.android.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;

public class DialogPosition extends Activity implements OnClickListener{

	private final String TAG = "DialogPosition";
	private Button mButtonUp = null;
	private Button mButtonBottom = null;
	private Button mButtonLeft = null;
	private Button mButtonRight = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_position);
		mButtonUp = (Button)findViewById(R.id.dialog_up);
		mButtonBottom = (Button)findViewById(R.id.dialog_bottom);
		mButtonLeft = (Button)findViewById(R.id.dialog_left);
		mButtonRight = (Button)findViewById(R.id.dialog_right);
		
		mButtonUp.setOnClickListener(this);
		mButtonBottom.setOnClickListener(this);
		mButtonLeft.setOnClickListener(this);
		mButtonRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(this);
		AlertDialog dialog = null;
		Window window = null;
		WindowManager.LayoutParams params = null;
		switch(id){
		case R.id.dialog_up:
			builder = builder.setTitle(R.string.dialog_pos_up);
			builder = builder.setMessage(R.string.dialog_pos_up);
			builder = builder.setPositiveButton(android.R.string.ok, null);
			dialog = builder.create();
			window = dialog.getWindow();
//			params = window.getAttributes();
			window.setWindowAnimations(R.anim.shake);
			window.setGravity(Gravity.TOP);
			dialog.show();
			break;
		case R.id.dialog_bottom:
			builder = builder.setTitle(R.string.dialog_pos_bottom);
			builder = builder.setMessage(R.string.dialog_pos_bottom);
			builder = builder.setPositiveButton(android.R.string.ok, null);
			dialog = builder.create();
			window = dialog.getWindow();
//			params = window.getAttributes();
			window.setGravity(Gravity.BOTTOM);
			params = window.getAttributes();
			params.x = 100;
			params.y = -400;
			dialog.show();
			break;
		case R.id.dialog_left:
			builder = builder.setTitle(R.string.dialog_pos_left);
			builder = builder.setMessage(R.string.dialog_pos_left);
			builder = builder.setPositiveButton(android.R.string.ok, null);
			dialog = builder.create();
			window = dialog.getWindow();
//			params = window.getAttributes();
			window.setGravity(Gravity.LEFT);
			dialog.show();
			break;
		case R.id.dialog_right:
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.dialog_position, null);
			builder = builder.setMessage(R.string.dialog_pos_right);
			builder = builder.setPositiveButton(android.R.string.ok, null);
			builder = builder.setView(view);
			dialog = builder.create();
			window = dialog.getWindow();
			window.setWindowAnimations(R.anim.shake);
			params = window.getAttributes();
			params.x = 10;
			params.y = -200;
			window.setAttributes(params);
			window.setGravity(Gravity.RIGHT);
			dialog.show();
			break;
		default:
			break;
		}
		/*
		View view = window.getDecorView();
		float pivotX = view.getWidth() / 2;
		float pivotY = view.getHeight() / 2;
		RotateAnimation animation = new RotateAnimation(0, 360, pivotX, pivotY);
		animation.setDuration(1000);
		animation.setRepeatMode(Animation.INFINITE);
		view.setAnimation(animation);
		animation.start();*/
	}

}
