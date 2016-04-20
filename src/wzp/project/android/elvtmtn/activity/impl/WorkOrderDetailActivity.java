package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IWorkOrderDetailActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.util.MyApplication;

public class WorkOrderDetailActivity extends BaseActivity implements IWorkOrderDetailActivity {

	private Button btnBack;
	private Button btnQueryElevatorRecord;
	private Button btnReceiveOrder;
	private Button btnCancelReceiveOrder;
	private TextView tvWorkOrderType;
	private TextView tvWorkOrderId;
	private TextView tvElevatorAddress;
	private LinearLayout linearFaultOccuredTime;
	private TextView tvFaultOccuredTime;
	private LinearLayout linearFaultDescription;
	private TextView tvFaultDescription;
	private TextView tvReceiveState;
	private LinearLayout linearReceiveTime;
	private TextView tvReceiveTime;
	
	private int workOrderType;
	private FaultOrder faultOrder;
	private MaintainOrder maintainOrder;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String tag = "WorkOrderDetailActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker_order_detail);
		
		initData();
		initWidget();
	}
	
	private void initData() {
		Intent intent = getIntent();
		workOrderType = intent.getIntExtra("workOrderType", -1);
		
		if (-1 == workOrderType) {
			throw new IllegalArgumentException("没有接收到工单类型的参数");
		} else {
			if (workOrderType == WorkOrderType.FAULT_ORDER) {
				faultOrder = JSON.parseObject(intent.getStringExtra("workOrder"), FaultOrder.class);
			} else if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
				maintainOrder = JSON.parseObject(intent.getStringExtra("workOrder"), MaintainOrder.class);
			}
		}
	}
	
	private void initWidget() {
		tvWorkOrderType = (TextView) findViewById(R.id.tv_workOrderType);
		tvWorkOrderId = (TextView) findViewById(R.id.tv_workOrderId);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		linearFaultOccuredTime = (LinearLayout) findViewById(R.id.linear_fault_occuredTime);
		tvFaultOccuredTime = (TextView) findViewById(R.id.tv_faultOccuredTime);
		linearFaultDescription = (LinearLayout) findViewById(R.id.linear_fault_description);
		tvFaultDescription = (TextView) findViewById(R.id.tv_faultDescription);
		tvReceiveState = (TextView) findViewById(R.id.tv_receiveState);
		linearReceiveTime = (LinearLayout) findViewById(R.id.linear_receiveTime);
		tvReceiveTime = (TextView) findViewById(R.id.tv_receiveTime);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnQueryElevatorRecord = (Button) findViewById(R.id.btn_queryElevatorRecord);
		btnReceiveOrder = (Button) findViewById(R.id.btn_receiveOrder);
		btnCancelReceiveOrder = (Button) findViewById(R.id.btn_cancelReceiveOrder);

		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			tvWorkOrderType.setText("保养工单");
			linearFaultDescription.setVisibility(View.GONE);
			linearFaultOccuredTime.setVisibility(View.GONE);
			
//			tvWorkOrderId.setText(text)
			
			
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			// 隐藏保养工单有关的控件
			// ...
			
			tvWorkOrderType.setText("故障工单");
			
			Log.i(tag, faultOrder.toString());
			tvWorkOrderId.setText(String.valueOf(faultOrder.getId()));
			tvElevatorAddress.setText(faultOrder.getElevatorRecord().getAddress());
			tvFaultOccuredTime.setText(sdf.format(faultOrder.getOccuredTime()));
			tvFaultDescription.setText(faultOrder.getDescription());
			
			if (faultOrder.getReceivingTime() != null) {
				tvReceiveState.setText("已接单");
				linearReceiveTime.setVisibility(View.VISIBLE);
				tvReceiveTime.setText(sdf2.format(faultOrder.getReceivingTime()));
			} else {
				tvReceiveState.setText("未接单");
				tvReceiveState.setTextColor(Color.RED);
			}
		}
		
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnQueryElevatorRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (WorkOrderType.MAINTAIN_ORDER == workOrderType) {
					ElevatorRecordDetailActivity.myStartActivity(WorkOrderDetailActivity.this, 
							JSON.toJSONString(maintainOrder.getElevatorRecord()));
				} else if (WorkOrderType.FAULT_ORDER == workOrderType) {
					ElevatorRecordDetailActivity.myStartActivity(WorkOrderDetailActivity.this, 
							JSON.toJSONString(faultOrder.getElevatorRecord()));
				}
			}
		});
		
		btnReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				/*
				 * 此处执行接单的相关操作，记录接单时间等。
				 */
				/*Log.d("WorkOrderDetailActivity", "接单按钮被按下");
				btnReceiveOrder.setVisibility(View.GONE);
				btnCancelReceiveOrder.setVisibility(View.VISIBLE);*/
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
				Long id = preferences.getLong("id", 0);
				
				
			}
		});
		
		btnCancelReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				/*
				 * 此处执行取消接单的相关操作，记录接单时间等。
				 */
				
				btnCancelReceiveOrder.setVisibility(View.GONE);
				btnReceiveOrder.setVisibility(View.VISIBLE);
			}
		});
	}
	
	@Override
	public void showToast(final String text) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(WorkOrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}
	
	@Override
	public void receiveSuccess() {
		showToast("接单成功");
		btnReceiveOrder.setVisibility(View.GONE);
		btnCancelReceiveOrder.setVisibility(View.VISIBLE);
		linearReceiveTime.setVisibility(View.VISIBLE);
		tvReceiveTime.setText(sdf2.format(new Date()));		
	}

	// 自定义一个startActivity()方法
	public static void myStartActivity(Context context, int workOrderType, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, WorkOrderDetailActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivity(actIntent);
	}
}
