package com.android.demo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AndroidDemoActivity extends Activity implements OnItemClickListener{
    /** Called when the activity is first created. */
    private final String TAG = "AndroidDemoActivity";
    private final String NAME = "name";
    private final String INTENT = "intent";
    private final String ACTION = "com.android.demo.intent.action.DEMO";
    private final String CATEGORY = "com.android.demo.intent.category.DEMO";
    private ActivityManager mAm = null;
    private PackageManager mPm = null;
    private ImageView mExitImage = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        setTitle(getLocalClassName());
        mExitImage = (ImageView) findViewById(R.id.exit_image);
        mExitImage.setVisibility(View.INVISIBLE);
        mExitImage.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(AndroidDemoActivity.this, R.anim.slide_top);
                animation.setAnimationListener(new AnimationListener() {
                    
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mExitImage.setEnabled(false);
                        mExitImage.setVisibility(View.INVISIBLE);
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mExitImage.setVisibility(View.INVISIBLE);
                        mExitImage.setEnabled(true);
                    }
                });
                mExitImage.startAnimation(animation);
            }
        });
        ListView listView = (ListView)findViewById(R.id.activity_list);
        listView.setOnItemClickListener(this);
        mAm = (ActivityManager)getSystemService(Service.ACTIVITY_SERVICE);
        mPm = getPackageManager();
        List<NewHashMap<String, Object>> activitiesList = new ArrayList<NewHashMap<String,Object>>();
        if(mPm != null){
            queryActivities(activitiesList);
            Collections.sort(activitiesList);
            SimpleAdapter adapter = new ActivityAdapter(this, activitiesList, android.R.layout.simple_list_item_1, new String[]{NAME}, new int[] { android.R.id.text1 });
            listView.setAdapter(adapter);
        }
    }
    @SuppressWarnings({ "serial", "hiding" })
    private class NewHashMap<String, Object> extends HashMap<String, Object> implements Comparable<HashMap<String, Object>>{
        private final Collator   collator = Collator.getInstance();
        @Override
        public int compareTo(HashMap<String, Object> another) {
            return collator.compare(this.get(NAME), another.get(NAME));
        }
        
    }
    private void queryActivities(List<NewHashMap<String, Object>> list){
        Intent queryIntent = new Intent();
        queryIntent.setAction(ACTION);
        queryIntent.addCategory(CATEGORY);
//        queryIntent.setAction(Intent.ACTION_MAIN);
//        queryIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> lists = mPm.queryIntentActivities(queryIntent, 0);
        NewHashMap<String , Object> map = null;
//        Log.d(TAG, "lists = " + lists);
        if(lists != null){
            int size = lists.size();
//            Log.d(TAG, "size = " + size);
            int i =0;
            ResolveInfo info = null;
            while(i < size){
                map = new NewHashMap<String, Object>();
                info = lists.get(i);
                Intent intent = activityIntent(info.activityInfo.packageName, info.activityInfo.name);
                String name = info.activityInfo.name;
                if(name.startsWith(info.activityInfo.packageName)){
                    name = name.substring(info.activityInfo.packageName.length() + 1);
                }
                String label = info.activityInfo.loadLabel(mPm).toString();
                if(label != null && !label.equals("")){
//                    name = label;
                }
                map.put(NAME, name);
                map.put(INTENT, intent);
//                Log.d(TAG, "name = " + info.activityInfo.name);
                list.add(map);
                i++;
            }
        }
    }
    
    protected Intent activityIntent(String pkg, String className) {
//        Log.d(TAG, "pkg = " + pkg + " , className = " + className);
        Intent result = new Intent();
        result.setClassName(pkg, className);
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
        if(hashMap != null){
            Intent intent = (Intent) hashMap.get(INTENT);
            Log.d(TAG, "Name = " + hashMap.get(NAME));
            Log.d(TAG, "Intent = " + hashMap.get(INTENT));
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        int visible = mExitImage.getVisibility();
        if(visible == View.VISIBLE){
            super.onBackPressed();
        }else{
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_bottom);
            animation.setAnimationListener(new AnimationListener() {
                
                @Override
                public void onAnimationStart(Animation animation) {
                    mExitImage.setVisibility(View.INVISIBLE);
                }
                
                @Override
                public void onAnimationRepeat(Animation animation) {
                    
                }
                
                @Override
                public void onAnimationEnd(Animation animation) {
                    mExitImage.setVisibility(View.VISIBLE);
                }
            });
            if(mExitImage.isEnabled()){
                mExitImage.startAnimation(animation);
            }
        }
    }
    
    class ActivityAdapter extends SimpleAdapter{

        private Context mContext;
        private LayoutInflater mInflater;
        public ActivityAdapter(Context context,
                List<? extends Map<java.lang.String, ?>> data, int resource,
                java.lang.String[] from, int[] to) {
            super(context, data, 0, null, null);
            mContext = context;
            mInflater = (LayoutInflater)mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        }

        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = null;
            if (convertView == null) {
                view = (TextView) mInflater.inflate(
                        android.R.layout.simple_list_item_1, null);
            } else {
                view = (TextView) convertView;
            }
            view.setSingleLine();
            view.setEllipsize(TruncateAt.END);
            NewHashMap<String, Object> item = (NewHashMap<java.lang.String, java.lang.Object>) getItem(position);
            String text = null;
            if(item != null){
                text = (java.lang.String) item.get(NAME);
            }
            view.setText(text);
            return view;//super.getView(position, convertView, parent);
        }
        
    }
}