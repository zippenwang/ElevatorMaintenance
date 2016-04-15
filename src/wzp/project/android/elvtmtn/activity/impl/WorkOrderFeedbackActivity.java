package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;

public class WorkOrderFeedbackActivity extends BaseActivity {

	private TextView tvWorkOrderType;
	private PullToRefreshListView ptrlvSignedIn;
	
	private ArrayAdapter<String> adapter;						// 先暂时简单地定义成String类型的适配器
	private List<String> dataList = new ArrayList<String>();	// 先暂时简单地定义成String类型的集合

	private int workOrderType;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
	private static final String tag = "WorkOrderFeedbackActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_order_feedback);
		
		initData();
		initWidget();
	}
	
	private void initData() {
		Intent intent = getIntent();
		workOrderType = intent.getIntExtra("workOrderType", 0);
		
		if (0 == workOrderType) {	
			dataList.addAll(Arrays.asList("保养工单1", "保养工单2", "保养工单3", "保养工单4", "保养工单5"));
		} else if (1 == workOrderType) {
			dataList.addAll(Arrays.asList("急修工单1", "急修工单2", "急修工单3", "急修工单4", "急修工单5"));
		}
	}
	
	private void initWidget() {
		tvWorkOrderType = (TextView) findViewById(R.id.tv_workOrderType);
		ptrlvSignedIn = (PullToRefreshListView) findViewById(R.id.ptrlv_signedIn);
			
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			tvWorkOrderType.setText("保养工单");
		} else {
			tvWorkOrderType.setText("故障工单");
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		ptrlvSignedIn.setAdapter(adapter);
		
		ptrlvSignedIn.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				/*
				 * 更新下拉列表时的提示信息 
				 */
				String label = "上次刷新时间：" + sdf.format(new Date());
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
				
                new GetDataTask().execute();
			}
		});
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
            ptrlvSignedIn.onRefreshComplete();
        }	
	}
	
	public static void myStartActivity(Context context, int workOrderType) {
		Intent actIntent = new Intent(context, WorkOrderFeedbackActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		context.startActivity(actIntent);
	}

}
