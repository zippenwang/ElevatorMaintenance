package wzp.project.android.elvtmtn.activity.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.util.WorkOrderType;

public class EmployeeSignInActivity extends BaseActivity {
	
	private TextView tvWorkOrderType;
	private ListView lvReceived;
	
	private ArrayAdapter<String> adapter;						// 先暂时简单地定义成String类型的适配器
	private List<String> dataList = new ArrayList<String>();	// 先暂时简单地定义成String类型的集合

	private int workOrderType;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
	
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
		lvReceived = (ListView) findViewById(R.id.lv_received);
			
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			tvWorkOrderType.setText("保养工单");
		} else {
			tvWorkOrderType.setText("故障工单");
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		lvReceived.setAdapter(adapter);
	}

	public static void myStartActivity(Context context, int workOrderType) {
		Intent actIntent = new Intent(context, EmployeeSignInActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		context.startActivity(actIntent);
	}
}
