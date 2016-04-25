package wzp.project.android.elvtmtn.activity.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.ProgressDialog;
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
import wzp.project.android.elvtmtn.activity.IElevatorRecordSearchActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.helper.adapter.ElevatorRecordAdapter;
import wzp.project.android.elvtmtn.util.MyApplication;

public class ElevatorRecordSearchActivity extends BaseActivity implements IElevatorRecordSearchActivity {

	private Button btnBack;
	private Button btnSearchRecord;
	private PullToRefreshListView ptrlvElevatorRecord;
	private LinearLayout linearTipInfo;						// 提示网络异常、或当前工单不存在的LinearLayout控件
	private TextView tvTipInfo;								// 当ListView中传入的List为空，该控件用于提示数据为空
	private Button btnRefreshAgain;							// 重试按钮
	private ProgressDialog progressDialog;					// 进度对话框
	
	private ArrayAdapter<ElevatorRecord> mAdapter;
	
	private List<ElevatorRecord> elevatorRecordList = new ArrayList<ElevatorRecord>();
		
	private volatile int curPage = 1;				// 当前需要访问的页码
	
	private boolean isPtrlvHidden = false;			// PullToRefreshListView控件是否被隐藏
	private String tipInfo;							// PullToRefreshListView控件被隐藏时的提示信息
	
	private SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private long employeeId;
	private long groupId;
	
	private int listIndex;
	
	private static final String tag = "ElevatorRecordSearchActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elevator_record);
		
		initData();
		initWidget();
	}

	private void initData() {
		employeeId = preferences.getLong("employeeId", -1);
		if (employeeId == -1) {
			throw new IllegalArgumentException("员工ID有误！");
		}
		
		groupId = preferences.getLong("groupId", -1);
		if (groupId == -1) {
			throw new IllegalArgumentException("员工所在小组ID有误！");
		}
		
		progressDialog = new ProgressDialog(this);
		
		curPage = 1;
		mAdapter = new ElevatorRecordAdapter(this, R.layout.listitem_elevator_record, elevatorRecordList);
		// 访问一次网络
		// ...
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		btnSearchRecord = (Button) findViewById(R.id.btn_searchElevatorRecord);
		ptrlvElevatorRecord = (PullToRefreshListView) findViewById(R.id.ptrlv_elevatorRecord);
		linearTipInfo = (LinearLayout) findViewById(R.id.linear_tipInfo);
		tvTipInfo = (TextView) findViewById(R.id.tv_tipInfo);
		btnRefreshAgain = (Button) findViewById(R.id.btn_refreshAgain);
		
		btnBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnSearchRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		btnRefreshAgain.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				tvTipInfo.setText("尝试访问服务器");
				new RefreshDataTask().execute();
			}
		});
		
		ptrlvElevatorRecord.setAdapter(mAdapter);
				
		ptrlvElevatorRecord.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		ptrlvElevatorRecord.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
		ptrlvElevatorRecord.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
		
		ptrlvElevatorRecord.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				ptrlvElevatorRecord.setMode(Mode.PULL_FROM_START);				
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new SearchMoreTask().execute();
			}
		});
		
		ptrlvElevatorRecord.setOnScrollListener(new OnScrollListener() {			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.i(tag, "onScroll#" + firstVisibleItem + "," + visibleItemCount + "," + totalItemCount);
				
				// 此处可以进行一下优化，没必要每次滑动时都执行如下操作
				if (0 == firstVisibleItem) {
					ptrlvElevatorRecord.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
					ptrlvElevatorRecord.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
					ptrlvElevatorRecord.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
				} else if ((totalItemCount - visibleItemCount) == firstVisibleItem) {
					ptrlvElevatorRecord.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
					ptrlvElevatorRecord.getLoadingLayoutProxy().setPullLabel("上拉加载更多...");
					ptrlvElevatorRecord.getLoadingLayoutProxy().setReleaseLabel("释放开始加载...");
				}
			}			
		});
		
		ptrlvElevatorRecord.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listIndex = position - 1;
				ElevatorRecordDetailActivity.myStartActivity(ElevatorRecordSearchActivity.this, 
						JSON.toJSONString(elevatorRecordList.get(listIndex)));				
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
			
			// 访问网络
        	
            // 返回执行的结果
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
        	ptrlvElevatorRecord.onRefreshComplete();
        }	
	}
	
	private class SearchMoreTask extends AsyncTask<Void, Void, Void> {
        @Override  
        protected Void doInBackground(Void... params) {
        	// 访问网络
        	
            return null;    
        }  
 
        @Override  
        protected void onPostExecute(Void result) {            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
        	ptrlvElevatorRecord.onRefreshComplete();
        }	
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
				Toast.makeText(ElevatorRecordSearchActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}
	
	@Override
	public void updateInterface() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ptrlvElevatorRecord.setVisibility(View.VISIBLE);
				linearTipInfo.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
			}
		});
		
	}

	@Override
	public void closePullUpToRefresh() {
		if (ptrlvElevatorRecord.getMode() != Mode.PULL_FROM_START) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvElevatorRecord.setMode(Mode.PULL_FROM_START);
				}
			});
		}
	}
	
	@Override
	public void openPullUpToRefresh() {
		if (ptrlvElevatorRecord.getMode() != Mode.BOTH) {	
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ptrlvElevatorRecord.setMode(Mode.DISABLED);
					ptrlvElevatorRecord.setMode(Mode.BOTH);
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
				
				ptrlvElevatorRecord.setVisibility(View.GONE);
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
