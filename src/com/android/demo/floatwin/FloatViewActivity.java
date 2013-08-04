package com.android.demo.floatwin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.demo.R;

public class FloatViewActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */

	
	private Button mOpenFloat = null;
	private Button mCloseFloat = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.float_setting);
        mOpenFloat = (Button) findViewById(R.id.open_float_win);
        mCloseFloat = (Button) findViewById(R.id.close_float_win);
        mOpenFloat.setOnClickListener(this);
        mCloseFloat.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = null;
		intent = new Intent(this, FloatService.class);
		switch(id){
		case R.id.open_float_win:
			startService(intent);
			break;
		case R.id.close_float_win:
			stopService(intent);
			break;
		default:
			break;
		}
	}
}