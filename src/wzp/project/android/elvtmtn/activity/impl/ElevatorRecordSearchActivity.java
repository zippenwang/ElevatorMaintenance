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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IElevatorRecordSearchActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.helper.adapter.ElevatorRecordAdapter;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.presenter.ElevatorRecordSearchPresenter;
import wzp.project.android.elvtmtn.util.ClearAllEditText;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.MyProgressDialog;

public class ElevatorRecordSearchActivity extends BaseActivity 
		implements IElevatorRecordSearchActivity {

	private RelativeLayout relativeBase;
	private Button btnBack;
	private ImageButton ibtnSearchRecord;
	private RelativeLayout relativeSearch;
	private ImageButton ibtnBackToPrevious;
	private ClearAllEditText caedtCondition;
	private ImageButton ibtnSearchByCondition;
	private PullToRefreshListView ptrlvElevatorRecord;
	private LinearLayout linearTipInfo;						// 提示网络异常、或当前工单不存在的LinearLayout控件
	private TextView tvTipInfo;								// 当ListView中传入的List为空，该控件用于提示数据为空
	private Button btnRefreshAgain;							// 重试按钮
	private MyProgressDialog myProgressDialog;
	
	private ArrayAdapter<ElevatorRecord> mAdapter;
	
	private List<ElevatorRecord> elevatorRecordList = new ArrayList<ElevatorRecord>();
	private ElevatorRecordSearchPresenter presenter = new ElevatorRecordSearchPresenter(this);
	
	private volatile int curPage = 1;				// 当前需要访问的页码
	
	private boolean isPtrlvHidden = false;			// PullToRefreshListView控件是否被隐藏
	private String tipInfo;							// PullToRefreshListView控件被隐藏时的提示信息
	
	private SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private long employeeId;
	private long groupId;
	
	private int listIndex;
	// 记录当前PullToRefreshListView控件显示的是否是下拉刷新的提示消息
	private boolean isShowPullDownInfo = true;
	
	private static final int ALL_ELEVATOR_RECORDS = 0x50;
	private static final int ELEVATOR_RECORDS_BY_CONDITION = 0x55;
	private int currentSearchMode = ALL_ELEVATOR_RECORDS;
	private String searchCondition = null;
	
	private static final String tag = "ElevatorRecordSearchActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elevator_record);
		
		try {
			initData();
		} catch (IllegalArgumentException expection) {
			Log.e(tag, Log.getStackTraceString(expection));
			showToast("缺失重要数据，请重新登录");
			EmployeeLoginActivity.myForceStartActivity(this);
			return;
		} catch (Exception exp2) {
			Log.e(tag, Log.getStackTraceString(exp2));
			showToast("程序异常，请重新登录");
			EmployeeLoginActivity.myForceStartActivity(this);
			return;
		}
		
		initWidget();
	}

	private void initData() {
		employeeId = preferences.getLong("employeeId", -1);
		if (employeeId == -1) {
			throw new IllegalArgumentException("缺失employeeId");
		}
		
		groupId = preferences.getLong("groupId", -1);
		if (groupId == -1) {
			throw new IllegalArgumentException("缺失groupId");
		}
		
		myProgressDialog = new MyProgressDialog(this);
		
		curPage = 1;
		mAdapter = new ElevatorRecordAdapter(this, R.layout.listitem_elevator_record, elevatorRecordList);
		presenter.searchAllElevatorRecords(curPage++, ProjectContants.PAGE_SIZE, elevatorRecordList);
	}
	
	private void initWidget() {
		relativeBase = (RelativeLayout) findViewById(R.id.relative_base);
		btnBack = (Button) findViewById(R.id.btn_back);
		ibtnSearchRecord = (ImageButton) findViewById(R.id.ibtn_searchElevatorRecord);
		relativeSearch = (RelativeLayout) findViewById(R.id.relative_search);
		ibtnBackToPrevious = (ImageButton) findViewById(R.id.ibtn_backToPrevious);
		caedtCondition = (ClearAllEditText) findViewById(R.id.caedt_condition);
		ibtnSearchByCondition = (ImageButton) findViewById(R.id.ibtn_searchByCondition);
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
		
		ibtnSearchRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				relativeBase.setVisibility(View.GONE);
				relativeSearch.setVisibility(View.VISIBLE);
				// 设置edtCondition没用，原因暂不明
				caedtCondition.setFocusable(true);
				caedtCondition.setSelectAllOnFocus(true);
			}
		});
		
		ibtnBackToPrevious.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				relativeBase.setVisibility(View.VISIBLE);
				relativeSearch.setVisibility(View.GONE);
				currentSearchMode = ALL_ELEVATOR_RECORDS;
			}
		});
		
		ibtnSearchByCondition.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				searchCondition = caedtCondition.getText().toString();
				currentSearchMode = ELEVATOR_RECORDS_BY_CONDITION;
				curPage = 1;
				presenter.searchElevatorRecordsByCondition(curPage++, ProjectContants.PAGE_SIZE, 
						searchCondition, elevatorRecordList);
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
				if (0 == firstVisibleItem
						&& !isShowPullDownInfo) {
					ptrlvElevatorRecord.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
					ptrlvElevatorRecord.getLoadingLayoutProxy().setPullLabel("下拉刷新...");
					ptrlvElevatorRecord.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新...");
					isShowPullDownInfo = true;
				} else if ((totalItemCount - visibleItemCount) == firstVisibleItem
						&& isShowPullDownInfo) {
					ptrlvElevatorRecord.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
					ptrlvElevatorRecord.getLoadingLayoutProxy().setPullLabel("上拉加载更多...");
					ptrlvElevatorRecord.getLoadingLayoutProxy().setReleaseLabel("释放开始加载...");
					isShowPullDownInfo = false;
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
			if (currentSearchMode == ALL_ELEVATOR_RECORDS) {
				presenter.searchAllElevatorRecords(curPage++, ProjectContants.PAGE_SIZE, 
						elevatorRecordList);
			} else if (currentSearchMode == ELEVATOR_RECORDS_BY_CONDITION) {
				presenter.searchElevatorRecordsByCondition(curPage++, ProjectContants.PAGE_SIZE, 
						searchCondition, elevatorRecordList);
			}
        	
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
        	if (currentSearchMode == ALL_ELEVATOR_RECORDS) {
				presenter.searchAllElevatorRecords(curPage++, ProjectContants.PAGE_SIZE, 
						elevatorRecordList);
			} else if (currentSearchMode == ELEVATOR_RECORDS_BY_CONDITION) {
				presenter.searchElevatorRecordsByCondition(curPage++, ProjectContants.PAGE_SIZE, 
						searchCondition, elevatorRecordList);
			}
        	
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
		EmployeeLoginActivity.myForceStartActivity(this);
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

	@Override
	public void hideBtnRefresh() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				btnRefreshAgain.setVisibility(View.GONE);
			}
		});
	}
}
