package wzp.project.android.elvtmtn.fragment.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.impl.EmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.impl.MaintainOrderDetailActivity;
import wzp.project.android.elvtmtn.activity.impl.MaintainOrderSearchActivity;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IOverdueOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;
import wzp.project.android.elvtmtn.helper.adapter.UnfinishedFaultOrderAdapter;
import wzp.project.android.elvtmtn.helper.adapter.UnfOvdMaintainOrderAdapter;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderSearchPresenter;
import wzp.project.android.elvtmtn.presenter.WorkOrderSortPresenter;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.MyProgressDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
		implements IWorkOrderSearchFragment, IOverdueOrderSortFragment {
	
	private PullToRefreshListView ptrlvOverdue;			// 提供下拉刷新功能的ListView
	private LinearLayout linearTipInfo;						// 提示网络异常、或当前工单不存在的LinearLayout控件
	private TextView tvTipInfo;								// 当ListView中传入的List为空，该控件用于提示数据为空
	private Button btnRefreshAgain;							// 重试按钮
	private MyProgressDialog myProgressDialog;				// 自定义进度对话框
	
	private ArrayAdapter<MaintainOrder> mAdapter;
	
	/*
	 * 由于需要在List集合中添加元素，因此不能直接定义一个List<?>
	 */
	private List<MaintainOrder> maintainOrderList = new ArrayList<MaintainOrder>();		// 保养工单集合
	
	private WorkOrderSearchPresenter workOrderSearchPresenter = new WorkOrderSearchPresenter(this);
	private WorkOrderSortPresenter workOrderSortPresenter = new WorkOrderSortPresenter(this);
	private MaintainOrderSearchActivity workOrderSearchActivity;
	
	private volatile int curPage = 1;				// 当前需要访问的页码
	
	private boolean isFirstAccessServer = true;
	
	private SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private long groupId;
	
	private int listIndex;
	private static final int REQUEST_REFRESH = 0x30;
	
	// 记录当前PullToRefreshListView控件显示的是否是下拉刷新的提示消息
	private boolean isShowPullDownInfo = true;		
	
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
		
		groupId = preferences.getLong("groupId", -1);
		if (groupId == -1) {
			Log.e(tag, "缺失groupId");
			showToast("缺失重要数据，请重新登录");
			EmployeeLoginActivity.myForceStartActivity(workOrderSearchActivity);
			return;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_REFRESH:
				if (resultCode == Activity.RESULT_OK) {
					boolean isNeedRefresh = data.getBooleanExtra("isNeedRefresh", false);
					Log.i(tag, "" + isNeedRefresh);
					if (isNeedRefresh) {
						workOrderSearchPresenter.searchMaintainOrder(groupId, WorkOrderState.OVERDUE, 
								1, (curPage - 1) * ProjectContants.PAGE_SIZE, maintainOrderList);						
						ptrlvOverdue.getRefreshableView().setSelection(listIndex + 1);
					}
				}
				break;
	
			default:
				break;
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && isFirstAccessServer) {
			curPage = 1;
			mAdapter = new UnfOvdMaintainOrderAdapter(workOrderSearchActivity, 
					R.layout.listitem_unfinished_overdue_maintain_order, maintainOrderList);
			workOrderSearchPresenter.searchMaintainOrder(groupId, WorkOrderState.OVERDUE, curPage++, 
					ProjectContants.PAGE_SIZE, maintainOrderList);
			isFirstAccessServer = false;
			ptrlvOverdue.setAdapter(mAdapter);
		}
		
		super.setUserVisibleHint(isVisibleToUser);
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
		
		ptrlvOverdue.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		ptrlvOverdue.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
		ptrlvOverdue.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
		
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
				Log.i(tag, "onScroll#" + firstVisibleItem + "," + visibleItemCount + "," + totalItemCount);
				
				// 此处可以进行一下优化，没必要每次滑动时都执行如下操作
				if (0 == firstVisibleItem
						&& !isShowPullDownInfo) {
					ptrlvOverdue.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
					ptrlvOverdue.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
					ptrlvOverdue.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
					isShowPullDownInfo = true;
				} else if ((totalItemCount - visibleItemCount) == firstVisibleItem
						&& isShowPullDownInfo) {
					ptrlvOverdue.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
					ptrlvOverdue.getLoadingLayoutProxy().setPullLabel("上拉加载更多...");
					ptrlvOverdue.getLoadingLayoutProxy().setReleaseLabel("释放开始加载...");
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
        	workOrderSearchPresenter.searchMaintainOrder(groupId, WorkOrderState.OVERDUE, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
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
        	workOrderSearchPresenter.searchMaintainOrder(groupId, WorkOrderState.OVERDUE, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
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
				myProgressDialog.setMessage("正在获取数据，请稍后...");
				myProgressDialog.setCancelable(true);
				
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

	/*@Override
	public void setIsPtrlvHidden(boolean isPtrlvHidden) {
		this.isPtrlvHidden = isPtrlvHidden;
	}*/

	@Override
	public void sortMaintainOrderByFinalTimeIncrease() {
//		workOrderSortPresenter.sortMaintainOrderByFinalTimeIncrease(maintainOrderList, 
//				WorkOrderState.OVERDUE);
		workOrderSortPresenter.sortMaintainOrderByFinalTimeIncrease(maintainOrderList);
	}

	@Override
	public void sortMaintainOrderByFinalTimeDecrease() {
//		workOrderSortPresenter.sortMaintainOrderByFinalTimeDecrease(maintainOrderList, 
//				WorkOrderState.OVERDUE);		
		workOrderSortPresenter.sortMaintainOrderByFinalTimeDecrease(maintainOrderList);
	}

	@Override
	public void sortMaintainOrderByReceivingTime() {
//		workOrderSortPresenter.sortMaintainOrderByReceivingTime(maintainOrderList, 
//				WorkOrderState.OVERDUE);
		workOrderSortPresenter.sortMaintainOrderByReceivingTime(maintainOrderList);
	}

	/*
	 * 不存在超期的故障工单，因此不需要为如下方法编写方法体
	 */
	/*@Override
	public void sortFaultOrderByReceivingTime() {}

	@Override
	public void sortFaultOrderByOccurredTimeIncrease() {}

	@Override
	public void sortFaultOrderByOccurredTimeDecrease() {}*/
	
}
