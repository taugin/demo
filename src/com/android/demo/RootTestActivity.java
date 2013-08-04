package com.android.demo;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class RootTestActivity extends Activity implements OnClickListener{

	private ScrollView mScrollView;
	private TextView mShellResult;
	private Button mButtonGo;
	private EditText mShellCommand;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.root_test);
		mScrollView = (ScrollView) findViewById(R.id.shell_scroll);
		mShellResult = (TextView) findViewById(R.id.shell_result);
		mButtonGo = (Button) findViewById(R.id.start_execute);
		mShellCommand = (EditText) findViewById(R.id.command_params);
		
		mButtonGo.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		String shellCommand = mShellCommand.getEditableText().toString();
		if(shellCommand != null && !shellCommand.equals("")){
			executeCommand(shellCommand);
		}
//		mShellCommand.setText("");
	}
	
	private void executeCommand(String command){
		Process process = null;
		Runtime runtime = null;
		String result = "";
		runtime = Runtime.getRuntime();
		try {
			process = runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		OutputStream os = process.getOutputStream();
		
		if(process != null){
			InputStream is = process.getInputStream();
			byte []buffer = new byte[1024];
			try {
				while(is.read(buffer) > 0){
					String temp = new String(buffer);
					result += temp;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mShellResult.setText(result);
	}
}
