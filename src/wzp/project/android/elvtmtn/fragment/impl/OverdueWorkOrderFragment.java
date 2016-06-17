package wzp.project.android.elvtmtn.fragment.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IWorkOrderSearchContainer;
import wzp.project.android.elvtmtn.activity.impl.EmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.impl.MaintainOrderDetailActivity;
import wzp.project.android.elvtmtn.activity.impl.MaintainOrderSearchActivity;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IOverdueOrderSortFragment;
import wzp.project.android.elvtmtn.helper.adapter.UnfOvdMaintainOrderAdapter;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.presenter.WorkOrderSearchPresenter;
import wzp.project.android.elvtmtn.presenter.WorkOrderSortPresenter;
import wzp.project.android.elvtmtn.util.MyProgressDialog;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 只有保养工单才有超期工单查询
 * @author Zippen
 *
 */
public class OverdueWorkOrderFragment extends Fragment 
		implements IWorkOrderSearchContainer, IOverdueOrderSortFragment {
	
	private PullToRefreshListView ptrlvOverdue;				// 提供下拉刷新功能的ListView
	private ILoadingLayout operateTip;						// 对PullToRefreshListView控件的操作提示
	private LinearLayout linearTipInfo;						// 提示网络异常、或当前工单不存在的LinearLayout控件
	private TextView tvTipInfo;								// 当ListView中传入的List为空，该控件用于提示数据为空
	private Button btnRefreshAgain;							// 重试按钮
	private MyProgressDialog myProgressDialog;				// 自定义进度对话框
	
	
	/*
	 * 由于需要在List集合中添加元素，因此不能直接定义一个List<?>
	 */
	private List<MaintainOrder> maintainOrderList = new ArrayList<MaintainOrder>();		// 保养工单集合
	private ArrayAdapter<MaintainOrder> mAdapter;
	
	private MaintainOrderSearchActivity workOrderSearchActivity;
	private int curPage = 1;						// 当前需要访问的页码
	private boolean isFirstAccessServer = true;		// 是否第一次访问服务器
	private SharedPreferences preferences 
		= ProjectContants.preferences;
	private long groupId;							// 小组id
	private int listIndex;							// item的序号对应的List集合序号
	private static final int REQUEST_REFRESH = 0x30;
	// 记录当前PullToRefreshListView控件显示的是否是下拉刷新的提示消息
	private boolean isShowPullDownInfo = true;		
	
	private WorkOrderSearchPresenter workOrderSearchPresenter = new WorkOrderSearchPresenter(this);
	private WorkOrderSortPresenter workOrderSortPresenter = new WorkOrderSortPresenter(this);
	
	private static final String tag = "OverdueWorkOrderFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_overdue_work_order, container, false);
		
		initWidget(view);
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Activity一定是MaintainOrderSearchActivity的实例
		workOrderSearchActivity = (MaintainOrderSearchActivity) activity;
		
		// 初始化ProgressDialog，必须在此处进行初始化，因为访问服务器时，需要调用ProgressDialog
		myProgressDialog = new MyProgressDialog(workOrderSearchActivity);
		myProgressDialog.setMessage("正在获取数据，请稍后...");
		myProgressDialog.setCancelable(true);
		
		groupId = preferences.getLong("groupId", -1);
		if (groupId == -1) {
			Log.e(tag, "缺失groupId");
			showToast("缺失重要数据，请重新登录");
			EmployeeLoginActivity.myForceStartActivity(workOrderSearchActivity);
			return;
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && isFirstAccessServer) {
			curPage = 1;
			mAdapter = new UnfOvdMaintainOrderAdapter(workOrderSearchActivity, 
					R.layout.listitem_unfinished_overdue_maintain_order, maintainOrderList);
			workOrderSearchPresenter.searchMaintainOrders(groupId, WorkOrderState.OVERDUE, curPage++, 
					ProjectContants.PAGE_SIZE, maintainOrderList);
			isFirstAccessServer = false;
			ptrlvOverdue.setAdapter(mAdapter);
		}
		
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_REFRESH:
				if (resultCode == Activity.RESULT_OK) {
					boolean isNeedRefresh = data.getBooleanExtra("isNeedRefresh", false);
					if (isNeedRefresh) {
						workOrderSearchPresenter.searchMaintainOrders(groupId, WorkOrderState.OVERDUE, 
								1, (curPage - 1) * ProjectContants.PAGE_SIZE, maintainOrderList);						
						ptrlvOverdue.getRefreshableView().setSelection(listIndex + 1);
					}
				}
				break;
	
			default:
				break;
		}
	}

	private void initWidget(View view) {
		ptrlvOverdue = (PullToRefreshListView) view.findViewById(R.id.ptrlv_overdue);
		ptrlvOverdue.getRefreshableView().setSelector(R.drawable.listview_bg);
		linearTipInfo = (LinearLayout) view.findViewById(R.id.linear_tipInfo);
		tvTipInfo = (TextView) view.findViewById(R.id.tv_tipInfo);
		btnRefreshAgain = (Button) view.findViewById(R.id.btn_refreshAgain);
		
		btnRefreshAgain.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				tvTipInfo.setText("尝试访问服务器");
				new RefreshDataTask().execute();
			}
		});
		
		operateTip = ptrlvOverdue.getLoadingLayoutProxy();		
		operateTip.setRefreshingLabel("正在刷新");
		operateTip.setPullLabel("下拉刷新...");
		operateTip.setReleaseLabel("释放开始刷新...");
		
		ptrlvOverdue.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				ptrlvOverdue.setMode(Mode.PULL_FROM_START);				
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new SearchMoreTask().execute();
			}
		});
		
		ptrlvOverdue.setOnScrollListener(new OnScrollListener() {			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (0 == firstVisibleItem
						&& !isShowPullDownInfo) {
					operateTip.setRefreshingLabel("正在刷新");
					operateTip.setPullLabel("下拉刷新...");
					operateTip.setReleaseLabel("释放开始刷新...");
					isShowPullDownInfo = true;
				} else if ((totalItemCount - visibleItemCount) == firstVisibleItem
						&& isShowPullDownInfo) {
					operateTip.setRefreshingLabel("正在加载");
					operateTip.setPullLabel("上拉加载更多...");
					operateTip.setReleaseLabel("释放开始加载...");
					isShowPullDownInfo = false;
				}
			}
		});
		
		ptrlvOverdue.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listIndex = position - 1;
				MaintainOrderDetailActivity.myStartActivityForResult(OverdueWorkOrderFragment.this, REQUEST_REFRESH, 
						WorkOrderState.OVERDUE, JSON.toJSONString(maintainOrderList.get(listIndex)));
			}
		});
	}
	
	private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	// pageNumber一定是为1，表示只加载第一页的内容
			curPage = 1;
        	workOrderSearchPresenter.searchMaintainOrders(groupId, WorkOrderState.OVERDUE, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvOverdue.onRefreshComplete();
        }	
	}
	
	private class SearchMoreTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	workOrderSearchPresenter.searchMaintainOrders(groupId, WorkOrderState.OVERDUE, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
        	ptrlvOverdue.onRefreshComplete();
        }	
	}

	@Override
	public void showProgressDialog() {
		workOrderSearchActivity.runOnUiThread(new Runnable() {		
			@Override
			public void run() {				
				myProgressDialog.show();
			}
		});			
	}
	
	@Override
	public void showProgressDialog(String tipInfo) {}

	@Override
	public void closeProgressDialog() {		
		if (myProgressDialog != null
				&& myProgressDialog.isShowing()) {
			myProgressDialog.dismiss();
		}
	}
	
	@Override
	public void backToLoginInterface() {
		EmployeeLoginActivity.myForceStartActivity(workOrderSearchActivity);
	}

	@Override
	public void showToast(final String text) {
		workOrderSearchActivity.runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(workOrderSearchActivity, text, Toast.LENGTH_SHORT).show();
			}
		});	
	}

	@Override
	public void updateInterface() {
		workOrderSearchActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ptrlvOverdue.setVisibility(View.VISIBLE);
				linearTipInfo.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
			}
		});
	}
	
	@Override
	public void locateToFirstItem() {
		ptrlvOverdue.getRefreshableView().setSelection(1);
	}

	@Override
	public void closePullUpToRefresh() {
		if (ptrlvOverdue.getMode() != Mode.PULL_FROM_START) {
			workOrderSearchActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvOverdue.setMode(Mode.PULL_FROM_START);
				}
			});
		}
	}

	@Override
	public void openPullUpToRefresh() {
		if (ptrlvOverdue.getMode() != Mode.BOTH) {	
			workOrderSearchActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvOverdue.setMode(Mode.DISABLED);
					ptrlvOverdue.setMode(Mode.BOTH);
				}
			});
		}
	}

	@Override
	public void hidePtrlvAndShowLinearLayout(final String info) {
		workOrderSearchActivity.runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				ptrlvOverdue.setVisibility(View.GONE);
				linearTipInfo.setVisibility(View.VISIBLE);
				tvTipInfo.setText(info);
			}
		});
	}

	@Override
	public void sortMaintainOrderByFinalTimeIncrease() {
		workOrderSortPresenter.sortMaintainOrderByFinalTimeIncrease(maintainOrderList);
	}

	@Override
	public void sortMaintainOrderByFinalTimeDecrease() {
		workOrderSortPresenter.sortMaintainOrderByFinalTimeDecrease(maintainOrderList);
	}

	@Override
	public void sortMaintainOrderByReceivingTime() {
		workOrderSortPresenter.sortMaintainOrderByReceivingTime(maintainOrderList);
	}
}
