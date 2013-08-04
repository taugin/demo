package com.android.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentDemoActivity extends Activity {

	public static String[] array = { "text1", "text2", "text3", "text4",
			"text5", "text6", "text7", "text8" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.fragment);
		
	}

	    
	public static class TitlesFragment extends ListFragment {

		boolean mDualPane;
		int mCurCheckPosition = 0;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState); 
			System.out.println("Fragment-->onCreate");
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			System.out.println("Fragment-->onCreateView");
			return super.onCreateView(inflater, container, savedInstanceState);
		}
		
		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			System.out.println("Fragment-->onPause");
		}
		
		
		@Override
		public void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			
			System.out.println("Fragment-->onStop");
		}
		
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			System.out.println("Fragment-->onAttach");
		}
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			System.out.println("Fragment-->onStart");
		}
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			System.out.println("Fragment-->onResume");
		}
		
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			System.out.println("Fragment-->onDestroy");
		}
		
		

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			System.out.println("Fragment-->onActivityCreted");
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, array));
			getListView().setSelector(R.drawable.list_selector);
			View detailsFrame = getActivity().findViewById(R.id.details);

			mDualPane = detailsFrame != null
					&& detailsFrame.getVisibility() == View.VISIBLE;

			if (savedInstanceState != null) {
				mCurCheckPosition = savedInstanceState.getInt("curChoice", 0); //娴犲簼绻氱�涙娈戦悩鑸碉拷娑擃厼褰囬崙鐑樻殶閹癸拷			
				}

			if (mDualPane) {
				getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

				showDetails(mCurCheckPosition);
			}
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);

			outState.putInt("curChoice", mCurCheckPosition);//娣囨繂鐡ㄨぐ鎾冲閻ㄥ嫪绗呴弽锟�		
			}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
			showDetails(position);
		}

		void showDetails(int index) {
			mCurCheckPosition = index; 
			if (mDualPane) {
				getListView().setItemChecked(index, true);
				DetailsFragment details = (DetailsFragment) getFragmentManager()
						.findFragmentById(R.id.details); 
				if (details == null || details.getShownIndex() != index) {
					details = DetailsFragment.newInstance(mCurCheckPosition); 

					//瀵版鍩屾稉锟介嚋fragment 娴滃濮熼敍鍫㈣娴肩磩qlite閻ㄥ嫭鎼锋担婊愮礆
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.replace(R.id.details, details);//鐏忓棗绶遍崚鎵畱fragment 閺囨寧宕茶ぐ鎾冲閻ㄥ墘iewGroup閸愬懎顔愰敍瀹巇d閸掓瑤绗夐弴鎸庡床娴兼矮绶峰▎锛勭柈閸旓拷					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//鐠佸墽鐤嗛崝銊ф暰閺佸牊鐏�
					ft.commit();//閹绘劒姘�
				}
			} else {
				new AlertDialog.Builder(getActivity()).setTitle(
						android.R.string.dialog_alert_title).setMessage(
						array[index]).setPositiveButton(android.R.string.ok,
						null).show();
			}
		}
	}

	/**
	 * 娴ｆ粈璐熼悾宀勬桨閻ㄥ嫪绔撮柈銊ュ瀻閿涘奔璐焒ragment 閹绘劒绶垫稉锟介嚋layout 
	 * @author terry
	 *
	 */
	public static class DetailsFragment extends Fragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		public static DetailsFragment newInstance(int index) { 
			DetailsFragment details = new DetailsFragment();
			Bundle args = new Bundle();
			args.putInt("index", index);
			details.setArguments(args);
			return details;
		}

		public int getShownIndex() {
			return getArguments().getInt("index", 0);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			if (container == null)
				return null;

			ScrollView scroller = new ScrollView(getActivity());
			TextView text = new TextView(getActivity());

			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 4, getActivity()
							.getResources().getDisplayMetrics());
			text.setPadding(padding, padding, padding, padding);
			scroller.addView(text);

			text.setText(array[getShownIndex()]);
			return scroller;
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// TODO Auto-generated method stub
			super.onCreateOptionsMenu(menu, inflater);
			 menu.add("Menu 1a");//.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	         menu.add("Menu 1b");//.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "index is"+getShownIndex()+" && menu text is "+item.getTitle(), 1000).show();
			return super.onOptionsItemSelected(item);
		}
	}
}