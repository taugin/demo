package com.android.demo.fragmenttest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

import com.android.demo.R;
import com.android.demo.scrollview.OnViewChangeListener;
import com.android.demo.scrollview.ScrollLayout;

public class FragmentTest extends Fragment implements TabContentFactory, OnTabChangeListener,OnViewChangeListener{
    /** Called when the activity is first created. */
    private TabHost mTabHost;
    private View mRootView;
    private ScrollLayout mScrollLayout;    
    private ImageView[] mImageViews;    
    private int mViewCount;    
    private int mCurSel;
    private int mDefaultSelection;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDefaultSelection = 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment2, null);
        mTabHost = (TabHost) inflater.inflate(R.layout.manage_apps_tab_content, container, false);
        final TabHost tabHost = mTabHost;
        tabHost.setup();
        Log.d("weiliuzhao", "mTabHost = " + mTabHost);
        TabSpec tabSpec = null;
        init();
        for(int i=0;i<mViewCount;i++){
            tabSpec = tabHost.newTabSpec(String.valueOf(i));
            tabSpec = tabSpec.setIndicator("Picture " + i, getResources().getDrawable(R.drawable.ic_launcher));
            tabSpec = tabSpec.setContent(this);
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);
        tabHost.setCurrentTabByTag(String.valueOf(mDefaultSelection));
        return tabHost;
    }

    @Override
    public View createTabContent(String tag) {
        Log.d("weiliuzhao", "createTabContent");
        return mRootView;
    }

    @Override
    public void onTabChanged(String tabId) {
        String viewCurSel = String.valueOf(mCurSel);
        if(!viewCurSel.equals(tabId)){
            mScrollLayout.snapToScreen(Integer.parseInt(tabId));
        }
    }
    private void init(){
        mScrollLayout = (ScrollLayout) mRootView.findViewById(R.id.ScrollLayout);     
        mScrollLayout.setDefaultScreen(mDefaultSelection);
        LinearLayout linearLayout = (LinearLayout) mRootView.findViewById(R.id.llayout);       
        mViewCount = mScrollLayout.getChildCount();
        mImageViews = new ImageView[mViewCount];       
        for(int i = 0; i < mViewCount; i++)        {
            mImageViews[i] = (ImageView) linearLayout.getChildAt(i);
            mImageViews[i].setEnabled(true);
//            mImageViews[i].setOnClickListener(this);
            mImageViews[i].setTag(i);
        }  
        mCurSel = mDefaultSelection;
        mImageViews[mCurSel].setEnabled(false);        
        mScrollLayout.SetOnViewChangeListener(this);
    }

    @Override
    public void OnViewChange(int view) {
        setCurPoint(view);
        int curTab = mTabHost.getCurrentTab();
        if(curTab != view){
            mTabHost.setCurrentTab(view);
        }
    }
    private void setCurPoint(int index){
        if (index < 0 || index > mViewCount - 1 || mCurSel == index)        {
            return ;
        }        
        mImageViews[mCurSel].setEnabled(true);
        mImageViews[index].setEnabled(false);        
        mCurSel = index;
    }
}