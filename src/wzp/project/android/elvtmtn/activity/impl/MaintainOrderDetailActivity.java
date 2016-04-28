package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainItem;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.UnfinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderReceivePresenter;
import wzp.project.android.elvtmtn.util.MyApplication;

public class MaintainOrderDetailActivity extends BaseActivity implements IWorkOrderDetailActivity {

	private Button btnBack;
	private Button btnQueryElevatorRecord;
	private Button btnReceiveOrder;
	private Button btnCancelReceiveOrder;
	private TextView tvWorkOrderId;
	private TextView tvElevatorAddress;
	private TextView tvMaintainType;
	private TextView tvMaintainItem;
	private TextView tvFinalTime;
	private TextView tvReceiveState;
	private LinearLayout linearReceiveTime;
	private TextView tvReceiveTime;
	
	private LinearLayout linearFinished;
	private TextView tvFixEmployee;
	private TextView tvFixGroup;
	private TextView tvSignInTime;
	private TextView tvSignOutTime;
	private TextView tvIsFinished;
	private TextView tvRemark;
	
	
	private WorkOrderReceivePresenter workOrderReceivePresenter 
		= new WorkOrderReceivePresenter(this);
	
	private SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private Long employeeId;
	
	private int workOrderState;
	private MaintainOrder maintainOrder;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private boolean isReceiveOrCancelOrder = false;
	
	private static final String tag = "WorkOrderDetailActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_order_detail);
		
		initData();
		initWidget();
	}
	
	private void initData() {
		Intent intent = getIntent();
		
		workOrderState = intent.getIntExtra("workOrderState", -1);		
		if (-1 == workOrderState) {
			throw new IllegalArgumentException("没有接收到工单状态的参数");
		}
		
		maintainOrder = JSON.parseObject(intent.getStringExtra("workOrder"), MaintainOrder.class);
		
		employeeId = preferences.getLong("employeeId", -1);
		if (-1 == employeeId) {
			throw new IllegalArgumentException("员工id有误");
		}
	}
	
	private void initWidget() {
		tvWorkOrderId = (TextView) findViewById(R.id.tv_workOrderId);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvMaintainType = (TextView) findViewById(R.id.tv_maintainType);
		tvMaintainItem = (TextView) findViewById(R.id.tv_maintainItem);
		tvFinalTime = (TextView) findViewById(R.id.tv_finalTime);
		tvReceiveState = (TextView) findViewById(R.id.tv_receiveState);
		linearReceiveTime = (LinearLayout) findViewById(R.id.linear_receiveTime);
		tvReceiveTime = (TextView) findViewById(R.id.tv_receiveTime);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnQueryElevatorRecord = (Button) findViewById(R.id.btn_queryElevatorRecord);
		btnReceiveOrder = (Button) findViewById(R.id.btn_receiveOrder);
		btnCancelReceiveOrder = (Button) findViewById(R.id.btn_cancelReceiveOrder);
		

		/*
		 * 为未完成、已完成、超期的保养工单均需要显示的控件设置数值
		 */
		tvWorkOrderId.setText(String.valueOf(maintainOrder.getId()));
		tvElevatorAddress.setText(maintainOrder.getElevatorRecord().getAddress().trim());
		tvMaintainType.setText(maintainOrder.getMaintainType().getName().trim());
		tvMaintainItem.setText("");
		List<MaintainItem> items = maintainOrder.getMaintainType().getMaintainItems();
		int itemsSize = items.size();
		for (int i=0; i<itemsSize; i++) {
			tvMaintainItem.append(items.get(i).getName().trim());
			if (i != (itemsSize - 1)) {
				tvMaintainItem.append("\n");
			}
		}
		
		tvFinalTime.setText(sdf.format(maintainOrder.getFinalTime()));
		
		/*if (maintainOrder.getEmployee() != null) {
			Log.i(tag, "Employee不为空");			
		} else {
			Log.i(tag, "Employee为空");
		}*/
		
		// 接单时间若不为空，对应的Employee实例也一定不为空
		if (maintainOrder.getReceivingTime() != null) {
			tvReceiveState.setText("已接单");
			tvReceiveState.setTextColor(Color.BLACK);
			linearReceiveTime.setVisibility(View.VISIBLE);
			tvReceiveTime.setText(sdf2.format(maintainOrder.getReceivingTime()));		
			if (maintainOrder.getEmployee() != null) {
				if (employeeId == maintainOrder.getEmployee().getId()) {
					btnReceiveOrder.setVisibility(View.GONE);
					btnCancelReceiveOrder.setVisibility(View.VISIBLE);
				} else {
					btnReceiveOrder.setEnabled(false);
				}
			}				
		} else {
			tvReceiveState.setText("未接单");
			tvReceiveState.setTextColor(Color.RED);
		}
				
		/*
		 * 已完成的保养工单需要显示的控件
		 */
		if (workOrderState == WorkOrderState.FINISHED) {
			linearFinished = (LinearLayout) findViewById(R.id.linear_finished);
			tvFixEmployee = (TextView) findViewById(R.id.tv_fixEmployee);
			tvFixGroup = (TextView) findViewById(R.id.tv_fixGroup);
			tvSignInTime = (TextView) findViewById(R.id.tv_signInTime);
			tvSignOutTime = (TextView) findViewById(R.id.tv_signOutTime);
			tvIsFinished = (TextView) findViewById(R.id.tv_isFinished);
			tvRemark = (TextView) findViewById(R.id.tv_remark);
			
			/*
			 * 为已完成的保养工单需要显示的控件设置数值
			 */
			linearFinished.setVisibility(View.VISIBLE);
			btnReceiveOrder.setVisibility(View.GONE);
			btnCancelReceiveOrder.setVisibility(View.GONE);
			
			if (maintainOrder.getEmployee() != null) {
				if (!TextUtils.isEmpty(maintainOrder.getEmployee().getName())) {
					tvFixEmployee.setText(maintainOrder.getEmployee().getName().trim());
				} else {
					tvFixEmployee.setText("姓名未知");
					tvFixEmployee.setTextColor(Color.RED);
				}
				if (maintainOrder.getEmployee().getGroup() != null) {
					tvFixGroup.setText(maintainOrder.getEmployee().getGroup().getName().trim());
				} else {
					tvFixGroup.setText("暂无组信息");
				}
			} else {
				tvFixEmployee.setText("暂无员工信息");
				tvFixEmployee.setTextColor(Color.RED);
				tvFixGroup.setText("暂无组信息");
			}
				
			if (maintainOrder.getSignInTime() != null) {
				tvSignInTime.setText(sdf2.format(maintainOrder.getSignInTime()));
			} else {
				tvSignInTime.setText("暂无该信息");
			}

			if (maintainOrder.getSignOutTime() != null) {
				tvSignOutTime.setText(sdf2.format(maintainOrder.getSignOutTime()));
			} else {
				tvSignOutTime.setText("暂无该信息");
			}

			tvIsFinished.setText(maintainOrder.getFinished() ? "已修好" : "未修好");
			tvRemark.setText(maintainOrder.getRemark());
		}
		
		
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (workOrderState != WorkOrderState.FINISHED) {
					Log.i(tag, "" + isReceiveOrCancelOrder);
					Intent fragIntent = new Intent(MaintainOrderDetailActivity.this, MaintainOrderSearchActivity.class);
					fragIntent.putExtra("isNeedRefresh", isReceiveOrCancelOrder);
					if (isReceiveOrCancelOrder) {
						fragIntent.putExtra("receivingTime", maintainOrder.getReceivingTime());
					}
					
					setResult(RESULT_OK, fragIntent);
				}
				finish();
			}
		});
		
		btnQueryElevatorRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ElevatorRecordDetailActivity.myStartActivity(MaintainOrderDetailActivity.this, 
						JSON.toJSONString(maintainOrder.getElevatorRecord()));
			}
		});
		
		btnReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				isReceiveOrCancelOrder = true;
				workOrderReceivePresenter.receiveOrder(WorkOrderType.MAINTAIN_ORDER, 
						maintainOrder.getId(), employeeId);
			}
		});
		
		btnCancelReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				isReceiveOrCancelOrder = true;

				workOrderReceivePresenter.cancelReceiveOrder(WorkOrderType.MAINTAIN_ORDER, 
						maintainOrder.getId(), employeeId);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (workOrderState != WorkOrderState.FINISHED) {
			Log.i(tag, "" + isReceiveOrCancelOrder);
			Intent fragIntent = new Intent(MaintainOrderDetailActivity.this, MaintainOrderSearchActivity.class);
			fragIntent.putExtra("isNeedRefresh", isReceiveOrCancelOrder);
			if (isReceiveOrCancelOrder) {
				fragIntent.putExtra("receivingTime", maintainOrder.getReceivingTime());
			}
			
			setResult(RESULT_OK, fragIntent);
		}
		finish();
	}

	@Override
	public void showToast(final String text) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(MaintainOrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}
	
	@Override
	public void receiveSuccess(final Date receivingTime) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MaintainOrderDetailActivity.this, "接单成功", Toast.LENGTH_SHORT).show();
				btnReceiveOrder.setVisibility(View.GONE);
				btnCancelReceiveOrder.setVisibility(View.VISIBLE);
				tvReceiveState.setText("已接单");
				tvReceiveState.setTextColor(Color.BLACK);
				linearReceiveTime.setVisibility(View.VISIBLE);
				maintainOrder.setReceivingTime(receivingTime);
				tvReceiveTime.setText(sdf2.format(receivingTime));
			}
		});
	}

	@Override
	public void cancelReceiveSuccess() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(MaintainOrderDetailActivity.this, "成功取消接单", Toast.LENGTH_SHORT).show();
				btnCancelReceiveOrder.setVisibility(View.GONE);
				btnReceiveOrder.setVisibility(View.VISIBLE);
				tvReceiveState.setText("未接单");
				tvReceiveState.setTextColor(Color.RED);
				linearReceiveTime.setVisibility(View.GONE);
				maintainOrder.setReceivingTime(null);
			}
		});
	}
	
	public static void myStartActivity(Context context, 
			int workOrderState, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, MaintainOrderDetailActivity.class);
		actIntent.putExtra("workOrderState", workOrderState);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivity(actIntent);
	}
	
	public static void myStartActivityForResult(Fragment fragment, int requestCode, 
			int workOrderState, String jsonWorkOrder) {
		Intent actIntent = new Intent(fragment.getActivity(), MaintainOrderDetailActivity.class);
		actIntent.putExtra("workOrderState", workOrderState);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		fragment.startActivityForResult(actIntent, requestCode);
	}

}
