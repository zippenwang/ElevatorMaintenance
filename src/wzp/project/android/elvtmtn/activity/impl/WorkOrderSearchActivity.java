package wzp.project.android.elvtmtn.activity.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.fragment.FinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.fragment.OverdueWorkOrderFragment;
import wzp.project.android.elvtmtn.fragment.UnfinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;

/**
 * 主控界面
 * @author Zippen
 *
 */
public class WorkOrderSearchActivity extends FragmentActivity {

	private TextView tvWorkOrderType;
	private RelativeLayout relativeUnfinished;
	private RelativeLayout relativeFinished;
	private RelativeLayout relativeOverdue;
	private TextView tvUnfinishedHidden;
	private TextView tvFinishedHidden;
	private TextView tvOverdueHidden;
	private TextView tvUnfinished;
	private TextView tvFinished;
	private TextView tvOverdue;
	private ViewPager vpWorkOrder;
	private int[] selectedStateArray;
	private TextView[] tvHiddenArray;					// 用于标注当前所在的选项卡
	private TextView[] tvArray;							// 用于标注当前所在的选项卡的标题
	
	private Button btnBack;
	
	private int currentSelectedId = 0;
	private int workOrderType = 0;						// 工单类型
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
		tvWorkOrderType = (TextView) findViewById(R.id.tv_workOrderType);
		// 保存工单类型
		workOrderType = getIntent().getIntExtra("workOrderType", WorkOrderType.MAINTAIN_ORDER);
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			tvWorkOrderType.setText("保养工单");
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			tvWorkOrderType.setText("故障工单");
		}
		
		relativeUnfinished = (RelativeLayout) findViewById(R.id.relative_unfinished);
		relativeFinished = (RelativeLayout) findViewById(R.id.relative_finished);
		relativeOverdue = (RelativeLayout) findViewById(R.id.relative_overdue);
		
		tvUnfinishedHidden = (TextView) findViewById(R.id.tv_unfinished_hidden);
		tvFinishedHidden = (TextView) findViewById(R.id.tv_finished_hidden);
		tvOverdueHidden = (TextView) findViewById(R.id.tv_overdue_hidden);
		tvUnfinished = (TextView) findViewById(R.id.tv_unfinished);
		tvFinished = (TextView) findViewById(R.id.tv_finished);
		tvOverdue = (TextView) findViewById(R.id.tv_overdue);
	
		vpWorkOrder = (ViewPager) findViewById(R.id.vp_workOrder);
		// 设置ViewPager的预加载值，即让ViewPager维护以当前Fragment为中心，左右各2个Fragment
		vpWorkOrder.setOffscreenPageLimit(2);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initParam() {
		// 0表示选中，1表示未选中(默认第一个选中)
		selectedStateArray = new int[] {0, 1, 1};
		tvHiddenArray = new TextView[] {tvUnfinishedHidden, tvFinishedHidden, tvOverdueHidden};
		tvArray = new TextView[] {tvUnfinished, tvFinished, tvOverdue};
		
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
//				Toast.makeText(WorkOrderSearchActivity.this, "页面更改", 0).show();
				setSelectedTitle(position);
			}
		});		
	}	
	
	private void setSelectedTitle(int position) {
		for (int i = 0; i < selectedStateArray.length; i++) {
			if (selectedStateArray[i] == 0) {
				selectedStateArray[i] = 1;
				tvHiddenArray[i].setVisibility(View.INVISIBLE);
			}

			// 被选中的标题为红色字体，未被选中的标题为黑色字体
			if (i == position) {
				tvArray[position].setTextColor(Color.RED);
			} else {
				tvArray[i].setTextColor(Color.BLACK);
			}
		}
		selectedStateArray[position] = 0;
		tvHiddenArray[position].setVisibility(View.VISIBLE);
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
	
	public static void myStartActivity(Context context, int workOrderType) {
		Intent actIntent = new Intent(context, WorkOrderSearchActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		context.startActivity(actIntent);
	}

	public int getWorkOrderType() {
		return workOrderType;
	}
}
