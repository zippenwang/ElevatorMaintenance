package wzp.project.android.elvtmtn.activity.impl;

import java.util.ArrayList;
import java.util.List;

import com.baidu.platform.comapi.map.m;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.fragment.FinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.fragment.IFinishedOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IUnfOvdOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;
import wzp.project.android.elvtmtn.fragment.OverdueWorkOrderFragment;
import wzp.project.android.elvtmtn.fragment.UnfinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderSortPresenter;

/**
 * 主控界面
 * @author Zippen
 *
 */
public class MaintainOrderSearchActivity extends FragmentActivity {

	private RelativeLayout relativeUnfinished;
	private RelativeLayout relativeFinished;
	private RelativeLayout relativeOverdue;
	private TextView tvUnfinishedHidden;
	private TextView tvFinishedHidden;
	private TextView tvOverdueHidden;
	private TextView tvUnfinished;
	private TextView tvFinished;
	private TextView tvOverdue;
	private ViewPager vpMaintainOrder;
	private int[] selectedStateArray;
	private TextView[] tvHiddenArray;					// 用于标注当前所在的选项卡
	private TextView[] tvArray;							// 用于标注当前所在的选项卡的标题
	
//	private TextView tvWorkOrderType;
	private ImageButton ibtnSort;
	private PopupMenu pmSort;
	private Menu menu;
	
	private Button btnBack;
	
	private int currentSelectedId = 0;
	
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	
	private static final String tag = "WorkOrderSearchActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_maintain_order_search);		
		
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
		tvUnfinished = (TextView) findViewById(R.id.tv_unfinished);
		tvFinished = (TextView) findViewById(R.id.tv_finished);
		tvOverdue = (TextView) findViewById(R.id.tv_overdue);
		
//		tvWorkOrderType = (TextView) findViewById(R.id.tv_workOrderType);
		ibtnSort = (ImageButton) findViewById(R.id.ibtn_sort);
		pmSort = new PopupMenu(this, ibtnSort);
		getMenuInflater().inflate(R.menu.maintain_order_search_sort_menu, pmSort.getMenu());
		menu = pmSort.getMenu();
		
		pmSort.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem mi) {
				IUnfOvdOrderSortFragment unfOvdOrderFragment = null;
				IFinishedOrderSortFragment finishedOrderFragment = null;
				
				switch (currentSelectedId) {
					case 0:
						unfOvdOrderFragment = (UnfinishedWorkOrderFragment) fragmentList.get(0);
						break;
					case 1:
						finishedOrderFragment = (FinishedWorkOrderFragment) fragmentList.get(1);
						break;
					case 2:
						unfOvdOrderFragment = (OverdueWorkOrderFragment) fragmentList.get(2);
						break;
					default:
						break;
				}
				
				switch (mi.getItemId()) {
					case R.id.item_finalTimeIncrease:				
						if (unfOvdOrderFragment != null) {
							unfOvdOrderFragment.sortMaintainOrderByFinalTimeIncrease();
						}
						break;
					case R.id.item_finalTimeDecrease:			
						if (unfOvdOrderFragment != null) {
							unfOvdOrderFragment.sortMaintainOrderByFinalTimeDecrease();
						}
						break;
						
					case R.id.item_sortByReceiveTime:	
						if (unfOvdOrderFragment != null) {
							unfOvdOrderFragment.sortMaintainOrderByReceivingTime();
						}
						break;
						
					case R.id.item_finishTimeIncrease:	
						if (finishedOrderFragment != null) {
							finishedOrderFragment.sortMaintainOrderByFinishedTimeIncrease();
						}
						break;
						
					case R.id.item_finishTimeDecrease:	
						if (finishedOrderFragment != null) {
							finishedOrderFragment.sortMaintainOrderByFinishedTimeDecrease();
						}
						break;
						
					default:
						break;
				}
				return false;
			}
		});
	
		vpMaintainOrder = (ViewPager) findViewById(R.id.vp_maintainOrder);
		// 设置ViewPager的预加载值，即让ViewPager维护以当前Fragment为中心，左右各2个Fragment
		vpMaintainOrder.setOffscreenPageLimit(2);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ibtnSort.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pmSort.show();
				switch (currentSelectedId) {
					case 0:
						menu.findItem(R.id.item_sortByFinalTime).setVisible(true);
						menu.findItem(R.id.item_sortByReceiveTime).setVisible(true);
						menu.findItem(R.id.item_sortByFinishTime).setVisible(false);
						break;
					case 1:
						menu.findItem(R.id.item_sortByFinalTime).setVisible(false);
						menu.findItem(R.id.item_sortByReceiveTime).setVisible(false);
						menu.findItem(R.id.item_sortByFinishTime).setVisible(true);
						break;
					case 2:
						menu.findItem(R.id.item_sortByFinalTime).setVisible(true);
						menu.findItem(R.id.item_sortByReceiveTime).setVisible(true);
						menu.findItem(R.id.item_sortByFinishTime).setVisible(false);
						break;
					default:
						break;
				}
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
		
		vpMaintainOrder.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {		
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
						fragmentList.add(0, fragment);
						break;
					case 1:
						Log.d(tag, "case 1");
						fragment = new FinishedWorkOrderFragment();
						fragmentList.add(1, fragment);
						break;
					case 2:
						Log.d(tag, "case 2");
						fragment = new OverdueWorkOrderFragment();
						fragmentList.add(2, fragment);
						break;
					default:
						break;
				}
				return fragment;
			}
		});
		
		vpMaintainOrder.setOnPageChangeListener(new SimpleOnPageChangeListener(){
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
					if (currentSelectedId == 0) {
						return;
					} else {
						setSelectedTitle(0);
						vpMaintainOrder.setCurrentItem(0);
					}
					break;
				case R.id.relative_finished:
					if (currentSelectedId == 1) {
						return;
					} else {
						setSelectedTitle(1);
						vpMaintainOrder.setCurrentItem(1);
					}
					break;
				case R.id.relative_overdue:
					if (currentSelectedId == 2) {
						return;
					} else {
						setSelectedTitle(2);
						vpMaintainOrder.setCurrentItem(2);
					}
					break;
			}
		}
	};
	
	public static void myStartActivity(Context context) {
		Intent actIntent = new Intent(context, MaintainOrderSearchActivity.class);
		context.startActivity(actIntent);
	}
}
