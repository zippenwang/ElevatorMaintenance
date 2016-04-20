package wzp.project.android.elvtmtn.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.impl.WorkOrderDetailActivity;
import wzp.project.android.elvtmtn.activity.impl.WorkOrderSearchActivity;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.adapter.FaultOrderAdapter;
import wzp.project.android.elvtmtn.helper.adapter.MaintainOrderAdapter;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderSearchPresenter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class FinishedWorkOrderFragment extends Fragment implements IWorkOrderSearchFragment {
	
	private PullToRefreshListView ptrlvFinished;			// 提供下拉刷新功能的ListView
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
	private WorkOrderSearchActivity workOrderSearchActivity;
	
	private volatile int curPage = 1;				// 当前需要访问的页码
	
	private boolean isPtrlvHidden = false;			// PullToRefreshListView控件是否被隐藏
	private String tipInfo;							// PullToRefreshListView控件被隐藏时的提示信息
	private boolean isFirstAccessServer = true;
	
	private static final String tag = "FinishedWorkOrderFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_finished_work_order, container, false);
		
		initWidget(view);
	
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		workOrderSearchActivity = (WorkOrderSearchActivity) activity;
		
		// 初始化ProgressDialog，必须在此处进行初始化，因为访问服务器时，需要调用ProgressDialog
		progressDialog = new ProgressDialog(workOrderSearchActivity);
		
		workOrderType = workOrderSearchActivity.getWorkOrderType();		// 获取工单类型
		
		/*
		 * 根据工单类型判断Adapter应该选用MaintainOrder还是FaultOrder
		 * 1、List中的泛型需要区分；
		 * 2、URL需要进行区分；
		 * 2、Adapter需要区分；
		 */		
		/*curPage = 1;
		if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
			mAdapter = new MaintainOrderAdapter(workOrderSearchActivity, 
					R.layout.listitem_maintain_order, maintainOrderList);
			workOrderSearchPresenter.searchMaintainOrder(WorkOrderState.FINISHED, curPage++, 
					ProjectContants.PAGE_SIZE, maintainOrderList);
		} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
			mAdapter = new FaultOrderAdapter(workOrderSearchActivity, 
					R.layout.listitem_fault_order, faultOrderList);
			workOrderSearchPresenter.searchFaultOrder(WorkOrderState.FINISHED, 
					curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
		}*/
		
		// 可以在此处进行第一次网络访问
//		initData();
	}
	

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && isFirstAccessServer) {
			curPage = 1;
			if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
				mAdapter = new MaintainOrderAdapter(workOrderSearchActivity, 
						R.layout.listitem_maintain_order, maintainOrderList);
				workOrderSearchPresenter.searchMaintainOrder(WorkOrderState.FINISHED, curPage++, 
						ProjectContants.PAGE_SIZE, maintainOrderList);
			} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
				mAdapter = new FaultOrderAdapter(workOrderSearchActivity, 
						R.layout.listitem_fault_order, faultOrderList);
				workOrderSearchPresenter.searchFaultOrder(WorkOrderState.FINISHED, 
						curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
			}
			isFirstAccessServer = false;
		}
		
		super.setUserVisibleHint(isVisibleToUser);
	}

	private void initWidget(View view) {
		ptrlvFinished = (PullToRefreshListView) view.findViewById(R.id.ptrlv_finished);
		linearTipInfo = (LinearLayout) view.findViewById(R.id.linear_tipInfo);
		tvTipInfo = (TextView) view.findViewById(R.id.tv_tipInfo);
		btnRefreshAgain = (Button) view.findViewById(R.id.btn_refreshAgain);
		
		btnRefreshAgain.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				new RefreshDataTask().execute();
			}
		});
		
//		ptrlvFinished.setAdapter(adapter);
		ptrlvFinished.setAdapter(mAdapter);
		
		ptrlvFinished.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		ptrlvFinished.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
		ptrlvFinished.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
		
		ptrlvFinished.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				ptrlvFinished.setMode(Mode.PULL_FROM_START);				
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new SearchMoreTask().execute();
			}
		});
		
		ptrlvFinished.setOnScrollListener(new OnScrollListener() {			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.i(tag, "onScroll#" + firstVisibleItem + "," + visibleItemCount + "," + totalItemCount);
				
				// 此处可以进行一下优化，没必要每次滑动时都执行如下操作
				if (0 == firstVisibleItem) {
					ptrlvFinished.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
					ptrlvFinished.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
					ptrlvFinished.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
				} else if ((totalItemCount - visibleItemCount) == firstVisibleItem) {
					ptrlvFinished.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
					ptrlvFinished.getLoadingLayoutProxy().setPullLabel("上拉加载更多...");
					ptrlvFinished.getLoadingLayoutProxy().setReleaseLabel("释放开始加载...");
				}
			}
		});
		
		ptrlvFinished.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent actIntent = new Intent(workOrderSearchActivity, WorkOrderDetailActivity.class);
				startActivity(actIntent);
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
        		workOrderSearchPresenter.searchMaintainOrder(WorkOrderState.FINISHED, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
			} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
				workOrderSearchPresenter.searchFaultOrder(WorkOrderState.FINISHED, curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
			}
        	
            // 返回执行的结果
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvFinished.onRefreshComplete();
        }	
	}
	
	private class SearchMoreTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
        		workOrderSearchPresenter.searchMaintainOrder(WorkOrderState.FINISHED, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
			} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
				workOrderSearchPresenter.searchFaultOrder(WorkOrderState.FINISHED, curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
			}
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvFinished.onRefreshComplete();
        }	
	}
	
	
	@Override
	public void showProgressDialog() {
		workOrderSearchActivity.runOnUiThread(new Runnable() {		
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
		/*workOrderSearchActivity.runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		});	*/
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
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
				ptrlvFinished.setVisibility(View.VISIBLE);
				linearTipInfo.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void closePullUpToRefresh() {
		if (ptrlvFinished.getMode() != Mode.PULL_FROM_START) {
			workOrderSearchActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvFinished.setMode(Mode.PULL_FROM_START);
				}
			});
		}
	}

	@Override
	public void openPullUpToRefresh() {
		if (ptrlvFinished.getMode() != Mode.BOTH) {	
			workOrderSearchActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvFinished.setMode(Mode.DISABLED);
					ptrlvFinished.setMode(Mode.BOTH);
				}
			});
		}
	}

	@Override
	public void hidePtrlvAndShowLinearLayout(final String info) {
		workOrderSearchActivity.runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				isPtrlvHidden = true;
				tipInfo = info;
				
				ptrlvFinished.setVisibility(View.GONE);
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
