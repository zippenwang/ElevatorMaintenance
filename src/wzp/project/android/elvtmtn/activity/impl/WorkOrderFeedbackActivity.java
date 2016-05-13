package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;
import wzp.project.android.elvtmtn.helper.adapter.FaultOrderFeedbackAdapter;
import wzp.project.android.elvtmtn.helper.adapter.MaintainOrderFeedbackAdapter;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderSearchPresenter;
import wzp.project.android.elvtmtn.util.MyApplication;

public class WorkOrderFeedbackActivity extends BaseActivity implements IWorkOrderSearchFragment {

	private Button btnBack;
	private TextView tvWorkOrderType;
	private PullToRefreshListView ptrlvFeedback;
	private LinearLayout linearTipInfo;						// 提示网络异常、或当前工单不存在的LinearLayout控件
	private TextView tvTipInfo;								// 当ListView中传入的List为空，该控件用于提示数据为空
	private Button btnRefreshAgain;							// 重试按钮
	private ProgressDialog progressDialog;					// 进度对话框
	
	private int workOrderType;
	private ArrayAdapter<?> mAdapter;
	
	/*
	 * 由于需要在List集合中添加元素，因此不能直接定义一个List<?>
	 */
	private List<MaintainOrder> maintainOrderList = new ArrayList<MaintainOrder>();		// 保养工单集合
	private List<FaultOrder> faultOrderList = new ArrayList<FaultOrder>();				// 故障工单集合

	
	private WorkOrderSearchPresenter workOrderSearchPresenter = new WorkOrderSearchPresenter(this);
	
	private volatile int curPage = 1;				// 当前需要访问的页码
	
	private boolean isPtrlvHidden = false;			// PullToRefreshListView控件是否被隐藏
	private String tipInfo;							// PullToRefreshListView控件被隐藏时的提示信息
	
	private SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private long employeeId;	
	
	private int listIndex;
	private static final int REQUEST_REFRESH = 0x30;
	
	// 记录当前PullToRefreshListView控件显示的是否是下拉刷新的提示消息
	private boolean isShowPullDownInfo = true;
	
	private static final String tag = "WorkOrderFeedbackActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_order_feedback);
		
		initData();
		initWidget();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_REFRESH:
			if (resultCode == Activity.RESULT_OK) {
				boolean isNeedRefresh = data.getBooleanExtra("isNeedRefresh", false);
				if (isNeedRefresh) {
					if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
						workOrderSearchPresenter.searchSignedInMaintainOrders(employeeId, 1, 
								(curPage - 1) * ProjectContants.PAGE_SIZE, maintainOrderList);
					} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
						workOrderSearchPresenter.searchSignedInFaultOrders(employeeId, 1, 
								(curPage - 1) * ProjectContants.PAGE_SIZE, faultOrderList);
					}
				}
			}
			break;

		default:
			break;
		}
	}

	private void initData() {
		Intent intent = getIntent();
		workOrderType = intent.getIntExtra("workOrderType", -1);
		if (workOrderType == -1) {
			throw new IllegalArgumentException("工单类型错误");
		}

		employeeId = preferences.getLong("employeeId", -1);
		if (employeeId == -1) {
			throw new IllegalArgumentException("员工ID有误！");
		}
		
		progressDialog = new ProgressDialog(this);
		
		curPage = 1;
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			mAdapter = new MaintainOrderFeedbackAdapter(this, R.layout.listitem_feedback_order, 
					maintainOrderList);
			workOrderSearchPresenter.searchSignedInMaintainOrders(employeeId, curPage++, 
					ProjectContants.PAGE_SIZE, maintainOrderList);
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			mAdapter = new FaultOrderFeedbackAdapter(this, R.layout.listitem_feedback_order, 
					faultOrderList);
			workOrderSearchPresenter.searchSignedInFaultOrders(employeeId, curPage++, 
					ProjectContants.PAGE_SIZE, faultOrderList);
		}
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		tvWorkOrderType = (TextView) findViewById(R.id.tv_workOrderType);
		ptrlvFeedback = (PullToRefreshListView) findViewById(R.id.ptrlv_feedback);
		linearTipInfo = (LinearLayout) findViewById(R.id.linear_tipInfo);
		tvTipInfo = (TextView) findViewById(R.id.tv_tipInfo);
		btnRefreshAgain = (Button) findViewById(R.id.btn_refreshAgain);
		
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			tvWorkOrderType.setText("保养");
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			tvWorkOrderType.setText("故障");
		}
		
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
		
		ptrlvFeedback.setAdapter(mAdapter);
				
		ptrlvFeedback.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		ptrlvFeedback.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
		ptrlvFeedback.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
		
		ptrlvFeedback.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				ptrlvFeedback.setMode(Mode.PULL_FROM_START);				
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new SearchMoreTask().execute();
			}
		});
		
		ptrlvFeedback.setOnScrollListener(new OnScrollListener() {			
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
					ptrlvFeedback.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
					ptrlvFeedback.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
					ptrlvFeedback.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
					isShowPullDownInfo = true;
				} else if ((totalItemCount - visibleItemCount) == firstVisibleItem
						&& isShowPullDownInfo) {
					ptrlvFeedback.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
					ptrlvFeedback.getLoadingLayoutProxy().setPullLabel("上拉加载更多...");
					ptrlvFeedback.getLoadingLayoutProxy().setReleaseLabel("释放开始加载...");
					isShowPullDownInfo = false;
				}
			}			
		});
		
		ptrlvFeedback.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listIndex = position - 1;
				
				if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
					/*EmployeeSignInDetailActivity.myStartActivityForResult(EmployeeSignInActivity.this, REQUEST_REFRESH, 
							workOrderType, JSON.toJSONString(maintainOrderList.get(listIndex)));*/
					MaintainOrderFeedbackDetailActivity.myStartActivityForResult(WorkOrderFeedbackActivity.this, REQUEST_REFRESH, 
							JSON.toJSONString(maintainOrderList.get(listIndex)));
				} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
					FaultOrderFeedbackDetailActivity.myStartActivityForResult(WorkOrderFeedbackActivity.this, REQUEST_REFRESH,
							JSON.toJSONString(faultOrderList.get(listIndex)));
				}
			}
		});
		
		if (isPtrlvHidden) {
			hidePtrlvAndShowLinearLayout(tipInfo);
		}
	}
	
	private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	// pageNumber一定是为1，表示只加载第一页的内容
			curPage = 1;
			
        	if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
        		workOrderSearchPresenter.searchSignedInMaintainOrders(employeeId, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
        	} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
        		workOrderSearchPresenter.searchSignedInFaultOrders(employeeId, curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
        	}
        	
            // 返回执行的结果
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvFeedback.onRefreshComplete();
        }	
	}
	
	private class SearchMoreTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
        		workOrderSearchPresenter.searchSignedInMaintainOrders(employeeId, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
			} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
				workOrderSearchPresenter.searchSignedInFaultOrders(employeeId, curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
			}
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvFeedback.onRefreshComplete();
        }	
	}
	
	public static void myStartActivity(Context context, int workOrderType) {
		Intent actIntent = new Intent(context, WorkOrderFeedbackActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		context.startActivity(actIntent);
	}

	@Override
	public void showProgressDialog() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				progressDialog.setTitle("正在访问服务器，请稍后...");
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(true);
				
				progressDialog.show();
			}
		});
	}

	@Override
	public void closeProgressDialog() {
		if (progressDialog != null
				&& progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void showToast(final String text) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(WorkOrderFeedbackActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}
	
	@Override
	public void updateInterface() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ptrlvFeedback.setVisibility(View.VISIBLE);
				linearTipInfo.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
			}
		});
		
	}

	@Override
	public void closePullUpToRefresh() {
		if (ptrlvFeedback.getMode() != Mode.PULL_FROM_START) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvFeedback.setMode(Mode.PULL_FROM_START);
				}
			});
		}
	}
	
	@Override
	public void openPullUpToRefresh() {
		if (ptrlvFeedback.getMode() != Mode.BOTH) {	
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvFeedback.setMode(Mode.DISABLED);
					ptrlvFeedback.setMode(Mode.BOTH);
				}
			});
		}
	}

	@Override
	public void hidePtrlvAndShowLinearLayout(final String info) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				isPtrlvHidden = true;
				tipInfo = info;
				
				ptrlvFeedback.setVisibility(View.GONE);
				linearTipInfo.setVisibility(View.VISIBLE);
				tvTipInfo.setText(info);
			}
		});
	}

	@Override
	public void setIsPtrlvHidden(boolean isPtrlvHidden) {
		this.isPtrlvHidden = isPtrlvHidden;
	}


}
