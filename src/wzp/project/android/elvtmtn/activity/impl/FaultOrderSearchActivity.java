package wzp.project.android.elvtmtn.activity.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.fragment.IFinishedOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IUnfinishedOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.impl.FinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.fragment.impl.UnfinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.util.ActivityCollector;

/**
 * 主控界面
 * @author Zippen
 *
 */
public class FaultOrderSearchActivity extends FragmentActivity {

	private RelativeLayout relativeUnfinished;
	private RelativeLayout relativeFinished;
	private TextView tvUnfinishedHidden;
	private TextView tvFinishedHidden;
	private TextView tvUnfinished;
	private TextView tvFinished;
	private ViewPager vpFaultOrder;
	private Button btnBack;
	private ImageButton ibtnSort;
	private PopupMenu pmSort;
	private Menu menu;
	
	private int[] selectedStateArray;					// 记录选项卡是否被选中的状态
	private TextView[] tvHiddenArray;					// 用于标注当前所在的选项卡
	private TextView[] tvArray;							// 用于标注当前所在的选项卡的标题
	
	private int currentSelectedId = UNFINISHED;			// 0：未完成；1：已完成；
	private static final int UNFINISHED = 0x00;			// 未完成
	private static final int FINISHED = 0x01;			// 已完成
	
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	
	private static final String tag = "WorkOrderSearchActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fault_order_search);		
		
		initWidget();
		initParam();
		
		ActivityCollector.addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
	private void initWidget() {		
		relativeUnfinished = (RelativeLayout) findViewById(R.id.relative_unfinished);
		relativeFinished = (RelativeLayout) findViewById(R.id.relative_finished);
		
		tvUnfinishedHidden = (TextView) findViewById(R.id.tv_unfinished_hidden);
		tvFinishedHidden = (TextView) findViewById(R.id.tv_finished_hidden);
		tvUnfinished = (TextView) findViewById(R.id.tv_unfinished);
		tvFinished = (TextView) findViewById(R.id.tv_finished);
	
		vpFaultOrder = (ViewPager) findViewById(R.id.vp_faultOrder);
		// 设置ViewPager的预加载值，即让ViewPager维护以当前Fragment为中心，左右各1个Fragment
		vpFaultOrder.setOffscreenPageLimit(1);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ibtnSort = (ImageButton) findViewById(R.id.ibtn_sort);
		pmSort = new PopupMenu(this, ibtnSort);
		getMenuInflater().inflate(R.menu.fault_order_search_sort_menu, pmSort.getMenu());
		menu = pmSort.getMenu();
		
		pmSort.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem mi) {
				IUnfinishedOrderSortFragment unfinishedOrderSortFragment = null;
				IFinishedOrderSortFragment finishedOrderFragment = null;
				
				switch (currentSelectedId) {
					case UNFINISHED:
						unfinishedOrderSortFragment = (UnfinishedWorkOrderFragment) fragmentList.get(0);
						break;
					case FINISHED:
						finishedOrderFragment = (FinishedWorkOrderFragment) fragmentList.get(1);
						break;
					default:
						break;
				}
				
				switch (mi.getItemId()) {
					case R.id.item_occuredTimeIncrease:					
						if (unfinishedOrderSortFragment != null) {
							unfinishedOrderSortFragment.sortFaultOrderByOccurredTimeIncrease();
						}
						break;
					case R.id.item_occuredTimeDecrease:		
						if (unfinishedOrderSortFragment != null) {
							unfinishedOrderSortFragment.sortFaultOrderByOccurredTimeDecrease();
						}
						break;
					case R.id.item_sortByReceiveTime:	
						if (unfinishedOrderSortFragment != null) {
							unfinishedOrderSortFragment.sortFaultOrderByReceivingTime();
						}
						break;
					case R.id.item_fixedTimeIncrease:	
						if (finishedOrderFragment != null) {
							finishedOrderFragment.sortFaultOrderByFixedTimeIncrease();
						}
						break;
					case R.id.item_fixedTimeDecrease:	
						if (finishedOrderFragment != null) {
							finishedOrderFragment.sortFaultOrderByFixedTimeDecrease();
						}
						break;
				default:
					break;
				}
				
				
				return false;
			}
		});
		
		ibtnSort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				pmSort.show();
				switch (currentSelectedId) {
					case UNFINISHED:
						menu.findItem(R.id.item_sortByFaultOccuredTime).setVisible(true);
						menu.findItem(R.id.item_sortByReceiveTime).setVisible(true);
						menu.findItem(R.id.item_sortByFixedTime).setVisible(false);
						break;
					case FINISHED:
						menu.findItem(R.id.item_sortByFaultOccuredTime).setVisible(false);
						menu.findItem(R.id.item_sortByReceiveTime).setVisible(false);
						menu.findItem(R.id.item_sortByFixedTime).setVisible(true);
						break;
					default:
						break;
				}
			}
		});
	}
	
	private void initParam() {
		// 0表示选中，1表示未选中(默认第一个选中)
		selectedStateArray = new int[] {0, 1};
		tvHiddenArray = new TextView[] {tvUnfinishedHidden, tvFinishedHidden};
		tvArray = new TextView[] {tvUnfinished, tvFinished};
		
		relativeUnfinished.setOnClickListener(listener);
		relativeFinished.setOnClickListener(listener);
		
		vpFaultOrder.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {		
			@Override
			public int getCount() {
				return selectedStateArray.length;
			}
			
			@Override
			public Fragment getItem(int position) {
				Fragment fragment = null;
				switch (position) {
					case UNFINISHED:
						Log.d(tag, "case 0");
						fragment = new UnfinishedWorkOrderFragment();
						fragmentList.add(0, fragment);
						break;
					case FINISHED:
						Log.d(tag, "case 1");
						fragment = new FinishedWorkOrderFragment();
						fragmentList.add(1, fragment);
						break;
					default:
						break;
				}
				return fragment;
			}
		});
		
		vpFaultOrder.setOnPageChangeListener(new SimpleOnPageChangeListener(){
			public void onPageSelected(int position) {
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
					if (currentSelectedId == UNFINISHED) {
						return;
					} else {
						setSelectedTitle(UNFINISHED);
						vpFaultOrder.setCurrentItem(UNFINISHED);
					}
					break;
				case R.id.relative_finished:
					if (currentSelectedId == FINISHED) {
						return;
					} else {
						setSelectedTitle(FINISHED);
						vpFaultOrder.setCurrentItem(FINISHED);
					}
					break;
				default:
					break;
			}
		}
	};
	
	public static void myStartActivity(Context context) {
		Intent actIntent = new Intent(context, FaultOrderSearchActivity.class);
		context.startActivity(actIntent);
	}
}
