package wzp.project.android.elvtmtn.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.impl.WorkOrderDetailActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UnfinishedWorkOrderFragment extends Fragment {

	private PullToRefreshListView ptrlvUnfinished;
	private ArrayAdapter<String> adapter;						// 先暂时简单地定义成String类型的适配器
	private List<String> dataList = new ArrayList<String>();	// 先暂时简单地定义成String类型的集合
	
	private Activity activity;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
	
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
		this.activity = activity;
		// 可以在此处进行第一次网络访问
		initData();
	}

	private void initData() {
		dataList.addAll(Arrays.asList("Iverson", "Wade", "Paul", "James", "Bryant"));
		adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, dataList);
	}

	private void initWidget(View view) {
		ptrlvUnfinished = (PullToRefreshListView) view.findViewById(R.id.ptrlv_overdue);
		ptrlvUnfinished.setAdapter(adapter);
//		ptrlvOverdue.getLoadingLayoutProxy().setLastUpdatedLabel("你好");
		
		// 设置下拉刷新的具体操作
		ptrlvUnfinished.setOnRefreshListener(new OnRefreshListener<ListView>() {
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
		
		ptrlvUnfinished.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent actIntent = new Intent(activity, WorkOrderDetailActivity.class);
				startActivity(actIntent);
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
            ptrlvUnfinished.onRefreshComplete();
        }	
	}
}
