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
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FinishedWorkOrderFragment extends Fragment {
	
	private PullToRefreshListView ptrlvFinished;
	private ArrayAdapter<String> adapter;						// 先暂时简单地定义成String类型的适配器
	private List<String> dataList = new ArrayList<String>();	// 先暂时简单地定义成String类型的集合
	
	private Activity activity;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
	
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
		this.activity = activity;
		// 可以在此处进行第一次网络访问
		initData();
	}

	private void initData() {
		dataList.addAll(Arrays.asList("刘备", "关羽", "张飞", "赵云", "诸葛孔明"));
		adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, dataList);
	}

	private void initWidget(View view) {
		ptrlvFinished = (PullToRefreshListView) view.findViewById(R.id.ptrlv_overdue);
		ptrlvFinished.setAdapter(adapter);
//		ptrlvOverdue.getLoadingLayoutProxy().setLastUpdatedLabel("你好");
		
		// 设置下拉刷新的具体操作
		ptrlvFinished.setOnRefreshListener(new OnRefreshListener<ListView>() {
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
            return Arrays.asList("AAAAAA", "BBBBBBB", "****CCCCCC****");
            
        }  
 
        @Override  
        protected void onPostExecute(List<String> result) {
        	dataList.addAll(result);
              
            //通知程序数据集已经改变，如果不做通知，那么将不会刷新ListView的集合  
            adapter.notifyDataSetChanged();
            
            // 当下拉更新完成后，一定要调用该方法，否则更新进度条会一直存在！！
            ptrlvFinished.onRefreshComplete();
        }	
	}
}
