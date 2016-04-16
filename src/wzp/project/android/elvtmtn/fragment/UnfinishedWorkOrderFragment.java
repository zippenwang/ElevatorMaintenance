package wzp.project.android.elvtmtn.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class UnfinishedWorkOrderFragment extends Fragment  implements IWorkOrderSearchFragment {

	private PullToRefreshListView ptrlvUnfinished;			// 提供下拉刷新功能的ListView
	private LinearLayout linearTipInfo;						// 提示网络异常、或当前工单不存在的LinearLayout控件
	private TextView tvTipInfo;								// 当ListView中传入的List为空，该控件用于提示数据为空
	private Button btnRefreshAgain;							// 重试按钮
	private ProgressDialog progressDialog;					// 进度对话框
	
	private ArrayAdapter<String> adapter;						// 先暂时简单地定义成String类型的适配器
	private List<String> dataList = new ArrayList<String>();	// 先暂时简单地定义成String类型的集合
	
	private int workOrderType;
	private ArrayAdapter<?> mAdapter;
//	private ArrayAdapter<MaintainOrder> maintainAdapter;
//	private ArrayAdapter<FaultOrder> faultAdapter;
	
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
	
	private static final String tag = "UnfinishedWorkOrderFragment";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_unfinished_work_order, container, false);
		
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
		curPage = 1;
		if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
			mAdapter = new MaintainOrderAdapter(workOrderSearchActivity, 
					R.layout.listitem_maintain_order, maintainOrderList);
//			maintainAdapter = new MaintainOrderAdapter(workOrderSearchActivity, 
//					R.layout.listitem_maintain_order, maintainOrderList);
			workOrderSearchPresenter.searchMaintainOrder(WorkOrderState.UNFINISHED, curPage++, 
					ProjectContants.PAGE_SIZE, maintainOrderList);
		} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
			mAdapter = new FaultOrderAdapter(workOrderSearchActivity, 
					R.layout.listitem_fault_order, faultOrderList);
//			faultAdapter = new FaultOrderAdapter(workOrderSearchActivity, 
//					R.layout.listitem_fault_order, faultOrderList);
			workOrderSearchPresenter.searchFaultOrder(WorkOrderState.UNFINISHED, 
					curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
		}		
		
		// 可以在此处进行第一次网络访问
//		initData();		
	}

	/**
	 * 模拟第一次访问服务器，获取数据
	 */
	private void initData() {
		dataList.addAll(Arrays.asList("Iverson", "Wade", "Paul", "James", "Bryant"));
		adapter = new ArrayAdapter<String>(workOrderSearchActivity, android.R.layout.simple_list_item_1, dataList);
	}

	/**
	 * 控件初始化
	 * @param view
	 */
	private void initWidget(View view) {
		ptrlvUnfinished = (PullToRefreshListView) view.findViewById(R.id.ptrlv_unfinished);
		linearTipInfo = (LinearLayout) view.findViewById(R.id.linear_tipInfo);
		tvTipInfo = (TextView) view.findViewById(R.id.tv_tipInfo);
		btnRefreshAgain = (Button) view.findViewById(R.id.btn_refreshAgain);
				
		btnRefreshAgain.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				new RefreshDataTask().execute();
			}
		});
		
//		ptrlvUnfinished.setAdapter(adapter);
		ptrlvUnfinished.setAdapter(mAdapter);
		/*if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			ptrlvUnfinished.setAdapter(maintainAdapter);
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			ptrlvUnfinished.setAdapter(faultAdapter);
		}*/
				
		ptrlvUnfinished.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		ptrlvUnfinished.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
		ptrlvUnfinished.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
		
		ptrlvUnfinished.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				ptrlvUnfinished.setMode(Mode.PULL_FROM_START);				
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new SearchMoreTask().execute();
			}
		});
		
		// 设置下拉刷新的具体操作
		/*ptrlvUnfinished.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = "上次刷新时间：" + sdf.format(new Date());
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
				
                new GetDataTask().execute();
			}
		});*/
		
		ptrlvUnfinished.setOnScrollListener(new OnScrollListener() {			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.i(tag, "onScroll#" + firstVisibleItem + "," + visibleItemCount + "," + totalItemCount);
				
				/*
				 * android4.4以上的版本，可以采用如下方法，实现对PullToRefreshListView模式（上拉刷新，还是下拉刷新）的切换
				 */				
				/*if (totalItemCount > visibleItemCount
						&& ptrlvUnfinished.getMode() != Mode.BOTH) {
					Log.i(tag, "pullToRefreshListView模式更改为BOTH");
//					ptrlvUnfinished.setMode(Mode.DISABLED);
					ptrlvUnfinished.setMode(Mode.BOTH);
				}*/
		
				/*
				 * andoird4.4以下的版本，需要利用如下比较损耗性能的方法，才能实现对PullToRefreshListView模式（上拉刷新，还是下拉刷新）的切换
				 */
				/*if ((totalItemCount != 2)
						&& (totalItemCount - 2) % 10 == 0) {
					Log.i(tag, "pullToRefreshListView模式更改为BOTH");
					ptrlvUnfinished.setMode(Mode.DISABLED);
					ptrlvUnfinished.setMode(Mode.BOTH);
				}*/
				
				// 此处可以进行一下优化，没必要每次滑动时都执行如下操作
				if (0 == firstVisibleItem) {
					ptrlvUnfinished.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
					ptrlvUnfinished.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
					ptrlvUnfinished.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
				} else if ((totalItemCount - visibleItemCount) == firstVisibleItem) {
					ptrlvUnfinished.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
					ptrlvUnfinished.getLoadingLayoutProxy().setPullLabel("上拉加载更多...");
					ptrlvUnfinished.getLoadingLayoutProxy().setReleaseLabel("释放开始加载...");
				}
			}
		});
		
		ptrlvUnfinished.setOnItemClickListener(new OnItemClickListener() {
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
	
	private class GetDataTask extends AsyncTask<Void, Void, List<String>> {
        @Override  
        protected List<String> doInBackground(Void... params) {
        	/*
        	 * 此处可执行一些费时的操作，例如网络访问
        	 */
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
            	Log.e(tag, Log.getStackTraceString(e));
            }

            // 返回执行的结果
            return Arrays.asList("武松", "李逵", "****鲁智深****");    
        }  
 
        @Override  
        protected void onPostExecute(List<String> result) {
        	dataList.addAll(result);
              
            // 通知程序数据集已经改变，如果不做通知，那么将不会刷新ListView的集合  
            adapter.notifyDataSetChanged();
            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvUnfinished.onRefreshComplete();
        }	
	}
	
	private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	// pageNumber一定是为1，表示只加载第一页的内容
			curPage = 1;
			
        	if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
        		workOrderSearchPresenter.searchMaintainOrder(WorkOrderState.UNFINISHED, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
			} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
				workOrderSearchPresenter.searchFaultOrder(WorkOrderState.UNFINISHED, curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
			}
        	
            // 返回执行的结果
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvUnfinished.onRefreshComplete();
        }	
	}
	
	private class SearchMoreTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
        		workOrderSearchPresenter.searchMaintainOrder(WorkOrderState.UNFINISHED, curPage++, ProjectContants.PAGE_SIZE, maintainOrderList);
			} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
				workOrderSearchPresenter.searchFaultOrder(WorkOrderState.UNFINISHED, curPage++, ProjectContants.PAGE_SIZE, faultOrderList);
			}
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvUnfinished.onRefreshComplete();
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
		workOrderSearchActivity.runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		});
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
				ptrlvUnfinished.setVisibility(View.VISIBLE);
				linearTipInfo.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
				/*if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
					maintainAdapter.notifyDataSetChanged();
				} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
					faultAdapter.notifyDataSetChanged();
				}*/
			}
		});
		
	}

	@Override
	public void closePullUpToRefresh() {
		if (ptrlvUnfinished.getMode() != Mode.PULL_FROM_START) {
			workOrderSearchActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvUnfinished.setMode(Mode.PULL_FROM_START);
				}
			});
		}
	}
	
	@Override
	public void openPullUpToRefresh() {
		if (ptrlvUnfinished.getMode() != Mode.BOTH) {	
			workOrderSearchActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvUnfinished.setMode(Mode.DISABLED);
					ptrlvUnfinished.setMode(Mode.BOTH);
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
				
				ptrlvUnfinished.setVisibility(View.GONE);
				linearTipInfo.setVisibility(View.VISIBLE);
				tvTipInfo.setText(info);
			}
		});
	}

/*	@Override
	public void hideLinearLayoutAndShowPtrlv() {
		ptrlvUnfinished.setVisibility(View.VISIBLE);
		linearTipInfo.setVisibility(View.GONE);
	}*/

	@Override
	public void setIsPtrlvHidden(boolean isPtrlvHidden) {
		this.isPtrlvHidden = isPtrlvHidden;
	}
}
