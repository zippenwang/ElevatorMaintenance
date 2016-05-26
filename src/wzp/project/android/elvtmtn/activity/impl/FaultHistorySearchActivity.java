package wzp.project.android.elvtmtn.activity.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IWorkOrderSortContainer;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;
import wzp.project.android.elvtmtn.helper.adapter.FaultHistoryAdapter;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.presenter.WorkOrderSearchPresenter;
import wzp.project.android.elvtmtn.presenter.WorkOrderSortPresenter;
import wzp.project.android.elvtmtn.util.MyProgressDialog;

public class FaultHistorySearchActivity extends BaseActivity 
		implements IWorkOrderSearchFragment, IWorkOrderSortContainer {
	
	private Button btnBack;
	private ImageButton ibtnSort;
	private PullToRefreshListView ptrlvFaultHistory;
	private LinearLayout linearTipInfo;						// 提示网络异常、或当前工单不存在的LinearLayout控件
	private TextView tvTipInfo;								// 当ListView中传入的List为空，该控件用于提示数据为空
	private Button btnRefreshAgain;							// 重试按钮
	private MyProgressDialog myProgressDialog;
	private PopupMenu pmSort;
	
	private List<FaultOrder> faultOrderList = new ArrayList<FaultOrder>();
	private ArrayAdapter<FaultOrder> adapter;
	
	private WorkOrderSearchPresenter workOrderSearchPresenter = new WorkOrderSearchPresenter(this);
	private WorkOrderSortPresenter workOrderSortPresenter = new WorkOrderSortPresenter(this);
	
	private volatile int curPage = 1;				// 当前需要访问的页码
	
	private String tipInfo;							// PullToRefreshListView控件被隐藏时的提示信息
	private long elevatorRecordId;
	private int listIndex;
	// 记录当前PullToRefreshListView控件显示的是否是下拉刷新的提示消息
	private boolean isShowPullDownInfo = true;
	
	private static final String tag = "FaultHistorySearchActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fault_history);
		
		initData();
		initWidget();
	}
	
	private void initData() {
		Intent intent = getIntent();
		elevatorRecordId = intent.getLongExtra("elevatorRecordId", -1);
		Log.d(tag, "" + elevatorRecordId);
		
		if (elevatorRecordId == -1) {
			throw new IllegalArgumentException("电梯id有误！");
		}
		
		myProgressDialog = new MyProgressDialog(this);
		
		adapter = new FaultHistoryAdapter(this, R.layout.listitem_fault_history, faultOrderList);
		workOrderSearchPresenter.searchFaultOrdersByElevatorRecordId(elevatorRecordId, curPage++, 
				ProjectContants.PAGE_SIZE, faultOrderList);
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		ibtnSort = (ImageButton) findViewById(R.id.ibtn_sort);
		ptrlvFaultHistory = (PullToRefreshListView) findViewById(R.id.ptrlv_faultHistory);
		linearTipInfo = (LinearLayout) findViewById(R.id.linear_tipInfo);
		tvTipInfo = (TextView) findViewById(R.id.tv_tipInfo);
		btnRefreshAgain = (Button) findViewById(R.id.btn_refreshAgain);
				
		btnBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		btnRefreshAgain.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				tvTipInfo.setText("尝试访问服务器");
				new RefreshDataTask().execute();
			}
		});
		
		ptrlvFaultHistory.setAdapter(adapter);
				
		ptrlvFaultHistory.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		ptrlvFaultHistory.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
		ptrlvFaultHistory.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
		
		ptrlvFaultHistory.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				ptrlvFaultHistory.setMode(Mode.PULL_FROM_START);				
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new SearchMoreTask().execute();
			}
		});
		
		ptrlvFaultHistory.setOnScrollListener(new OnScrollListener() {			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (0 == firstVisibleItem
						&& !isShowPullDownInfo) {
					ptrlvFaultHistory.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
					ptrlvFaultHistory.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
					ptrlvFaultHistory.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
					isShowPullDownInfo = true;
				} else if ((totalItemCount - visibleItemCount) == firstVisibleItem
						&& isShowPullDownInfo) {
					ptrlvFaultHistory.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
					ptrlvFaultHistory.getLoadingLayoutProxy().setPullLabel("上拉加载更多...");
					ptrlvFaultHistory.getLoadingLayoutProxy().setReleaseLabel("释放开始加载...");
					isShowPullDownInfo = false;
				}
			}			
		});
		
		ptrlvFaultHistory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listIndex = position - 1;
				FaultHistoryDetailActivity.myStartActivivty(FaultHistorySearchActivity.this, 
						JSON.toJSONString(faultOrderList.get(listIndex)));
			}
		});
		
		pmSort = new PopupMenu(this, ibtnSort);
		getMenuInflater().inflate(R.menu.fault_history_search_sort_menu, pmSort.getMenu());
		pmSort.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.item_occuredTimeIncrease:
						workOrderSortPresenter.sortFaultOrderByOccurredTimeIncrease(faultOrderList);
						break;
					case R.id.item_occuredTimeDecrease:
						workOrderSortPresenter.sortFaultOrderByOccurredTimeDecrease(faultOrderList);
						break;
					case R.id.item_fixedTimeIncrease:
						workOrderSortPresenter.sortFaultOrderByFinishedTimeIncrease(faultOrderList);
						break;
					case R.id.item_fixedTimeDecrease:
						workOrderSortPresenter.sortFaultOrderByFinishedTimeDecrease(faultOrderList);
						break;
					default:
						break;
				}
				return false;
			}
		});
		
		ibtnSort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pmSort.show();
			}
		});
	}
	
	private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	// pageNumber一定是为1，表示只加载第一页的内容
			curPage = 1;
			workOrderSearchPresenter.searchFaultOrdersByElevatorRecordId(elevatorRecordId, curPage++, 
					ProjectContants.PAGE_SIZE, faultOrderList);
        	
            // 返回执行的结果
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
        	ptrlvFaultHistory.onRefreshComplete();
        }	
	}
	
	private class SearchMoreTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	workOrderSearchPresenter.searchFaultOrdersByElevatorRecordId(elevatorRecordId, curPage++, 
					ProjectContants.PAGE_SIZE, faultOrderList);
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
        	ptrlvFaultHistory.onRefreshComplete();
        }	
	}
	
	public static void myStartActivivty(Context context, long elevatorRecordId) {
		Intent intent = new Intent(context, FaultHistorySearchActivity.class);
		intent.putExtra("elevatorRecordId", elevatorRecordId);
		context.startActivity(intent);
	}

	@Override
	public void showProgressDialog() {
		runOnUiThread(new Runnable() {		
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
	public void showToast(final String text) {	
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(FaultHistorySearchActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}

	@Override
	public void backToLoginInterface() {
		EmployeeLoginActivity.myForceStartActivity(this);
	}

	@Override
	public void updateInterface() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ptrlvFaultHistory.setVisibility(View.VISIBLE);
				linearTipInfo.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void closePullUpToRefresh() {
		if (ptrlvFaultHistory.getMode() != Mode.PULL_FROM_START) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvFaultHistory.setMode(Mode.PULL_FROM_START);
				}
			});
		}
	}

	@Override
	public void openPullUpToRefresh() {
		if (ptrlvFaultHistory.getMode() != Mode.BOTH) {	
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvFaultHistory.setMode(Mode.DISABLED);
					ptrlvFaultHistory.setMode(Mode.BOTH);
				}
			});
		}
	}

	@Override
	public void hidePtrlvAndShowLinearLayout(final String info) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
//				isPtrlvHidden = true;
				tipInfo = info;
				
				ptrlvFaultHistory.setVisibility(View.GONE);
				linearTipInfo.setVisibility(View.VISIBLE);
				tvTipInfo.setText(info);
			}
		});
	}
}
