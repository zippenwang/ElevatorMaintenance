package wzp.project.android.elvtmtn.activity.impl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.fragment.FinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.fragment.OverdueWorkOrderFragment;
import wzp.project.android.elvtmtn.fragment.UnfinishedWorkOrderFragment;

public class WorkOrderSearchActivity extends FragmentActivity {

	private RelativeLayout relativeUnfinished;
	private RelativeLayout relativeFinished;
	private RelativeLayout relativeOverdue;
	private TextView tvUnfinishedHidden;
	private TextView tvFinishedHidden;
	private TextView tvOverdueHidden;
	private ViewPager vpWorkOrder;
	private int[] selectedStateArray;
	private TextView[] tvArray;
	private int currentSelectedId = 0;
	
	private static final String tag = "WorkOrderSearchActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_work_order_search);		
		
		initWidget();
		initParam();
	}
	
	private void initWidget() {
		relativeUnfinished = (RelativeLayout) findViewById(R.id.relative_unfinished);
		relativeFinished = (RelativeLayout) findViewById(R.id.relative_finished);
		relativeOverdue = (RelativeLayout) findViewById(R.id.relative_overdue);
		
		tvUnfinishedHidden = (TextView) findViewById(R.id.tv_unfinished_hidden);
		tvFinishedHidden = (TextView) findViewById(R.id.tv_finished_hidden);
		tvOverdueHidden = (TextView) findViewById(R.id.tv_overdue_hidden);
	
		vpWorkOrder = (ViewPager) findViewById(R.id.vp_workOrder);
	}
	
	private void initParam() {
		// 0表示选中，1表示未选中(默认第一个选中)
		selectedStateArray = new int[] {0, 1, 1};
		tvArray = new TextView[] {tvUnfinishedHidden, tvFinishedHidden, tvOverdueHidden};

		relativeUnfinished.setOnClickListener(listener);	
		relativeOverdue.setOnClickListener(listener);
		relativeFinished.setOnClickListener(listener);
		
		vpWorkOrder.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {		
			@Override
			public int getCount() {
				return selectedStateArray.length;
			}
			
			@Override
			public Fragment getItem(int position) {
				Fragment fragment = null;
				switch (position) {
					case 0:
						Log.d(tag, "case 0");
						fragment = new UnfinishedWorkOrderFragment();
						break;
					case 1:
						Log.d(tag, "case 1");
						fragment = new FinishedWorkOrderFragment();
						break;
					case 2:
						Log.d(tag, "case 2");
						fragment = new OverdueWorkOrderFragment();
						break;
				}
				return fragment;
			}
		});
		
		vpWorkOrder.setOnPageChangeListener(new SimpleOnPageChangeListener(){
			public void onPageSelected(int position) {
				Toast.makeText(WorkOrderSearchActivity.this, "页面更改", 0).show();
				setSelectedTitle(position);
			}
		});		
	}	
	
	private void setSelectedTitle(int position) {
		for (int i = 0; i < selectedStateArray.length; i++) {
			if (selectedStateArray[i] == 0) {
				selectedStateArray[i] = 1;
				tvArray[i].setVisibility(View.INVISIBLE);
			}
		}
		selectedStateArray[position] = 0;
		tvArray[position].setVisibility(View.VISIBLE);
		currentSelectedId = position;
	}
	
	// 定义监听器，用于监听直接点击某一项内容
	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.relative_unfinished:
					if (currentSelectedId == 0) {
						return;
					} else {
						setSelectedTitle(0);
						vpWorkOrder.setCurrentItem(0);
					}
					break;
				case R.id.relative_finished:
					if (currentSelectedId == 1) {
						return;
					} else {
						setSelectedTitle(1);
						vpWorkOrder.setCurrentItem(1);
					}
					break;
				case R.id.relative_overdue:
					if (currentSelectedId == 2) {
						return;
					} else {
						setSelectedTitle(2);
						vpWorkOrder.setCurrentItem(2);
					}
					break;
			}
		}
	};
}
