package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.UnfinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderReceivePresenter;
import wzp.project.android.elvtmtn.util.MyApplication;

public class FaultOrderDetailActivity extends BaseActivity implements IWorkOrderDetailActivity {

	private Button btnBack;
	private Button btnQueryElevatorRecord;
	private Button btnReceiveOrder;
	private Button btnCancelReceiveOrder;
	private TextView tvWorkOrderId;
	private TextView tvElevatorAddress;
	private TextView tvFaultOccuredTime;
	private TextView tvFaultDescription;
	private TextView tvReceiveState;
	private LinearLayout linearReceiveTime;
	private TextView tvReceiveTime;
	
	private LinearLayout linearFinished;
	private TextView tvFixEmployee;
	private TextView tvFixGroup;
	private TextView tvSignInTime;
	private TextView tvSignInAddress;
	private TextView tvSignOutTime;
	private TextView tvSignOutAddress;
	private TextView tvFaultReason;
	private TextView tvIsFixed;
	private TextView tvRemark;
	
	
	private WorkOrderReceivePresenter workOrderReceivePresenter 
		= new WorkOrderReceivePresenter(this);
	
	private SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private Long employeeId;
	
	private int workOrderState;
	private FaultOrder faultOrder;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private boolean isReceiveOrCancelOrder = false;
	
	private static final String tag = "WorkOrderDetailActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fault_order_detail);
		
		initData();
		initWidget();
	}
	
	private void initData() {
		Intent intent = getIntent();
		workOrderState = intent.getIntExtra("workOrderState", -1);		
		
		if (-1 == workOrderState) {
			throw new IllegalArgumentException("没有接收到工单类型或工单状态的参数");
		} 

		faultOrder = JSON.parseObject(intent.getStringExtra("workOrder"), FaultOrder.class);
		
		employeeId = preferences.getLong("employeeId", -1);
		if (-1 == employeeId) {
			throw new IllegalArgumentException("员工id有误");
		}
	}
	
	private void initWidget() {
		tvWorkOrderId = (TextView) findViewById(R.id.tv_workOrderId);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvFaultOccuredTime = (TextView) findViewById(R.id.tv_faultOccuredTime);
		tvFaultDescription = (TextView) findViewById(R.id.tv_faultDescription);
		tvReceiveState = (TextView) findViewById(R.id.tv_receiveState);
		linearReceiveTime = (LinearLayout) findViewById(R.id.linear_receiveTime);
		tvReceiveTime = (TextView) findViewById(R.id.tv_receiveTime);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnQueryElevatorRecord = (Button) findViewById(R.id.btn_queryElevatorRecord);
		btnReceiveOrder = (Button) findViewById(R.id.btn_receiveOrder);
		btnCancelReceiveOrder = (Button) findViewById(R.id.btn_cancelReceiveOrder);
		
		/*
		 * 为未完成、已完成的故障工单均需要显示的控件设置数值
		 */
//		tvWorkOrderId.setText(String.valueOf(faultOrder.getId()));
		tvWorkOrderId.setText(String.valueOf(faultOrder.getNo()));
		tvElevatorAddress.setText(faultOrder.getElevatorRecord().getAddress());
		tvFaultOccuredTime.setText(sdf.format(faultOrder.getOccuredTime()));
		tvFaultDescription.setText(faultOrder.getDescription());
		
		/*if (faultOrder.getEmployee() != null) {
			Log.i(tag, "Employee不为空");			
		} else {
			Log.i(tag, "Employee为空");
		}*/
		
		if (faultOrder.getReceivingTime() != null) {
			tvReceiveState.setText("已接单");
			tvReceiveState.setTextColor(Color.BLACK);
			linearReceiveTime.setVisibility(View.VISIBLE);
			tvReceiveTime.setText(sdf2.format(faultOrder.getReceivingTime()));
			// 正常情况下，receivingTime为null，employee也一定为空
			if (faultOrder.getEmployee() != null) {
//				Log.i(tag, "进入if");
				if (employeeId == faultOrder.getEmployee().getId()) {
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
		 * 已完成的故障工单需要显示的控件
		 */
		if (workOrderState == WorkOrderState.FINISHED) {
			linearFinished = (LinearLayout) findViewById(R.id.linear_finished);
			tvFixEmployee = (TextView) findViewById(R.id.tv_fixEmployee);
			tvFixGroup = (TextView) findViewById(R.id.tv_fixGroup);
			tvSignInTime = (TextView) findViewById(R.id.tv_signInTime);
			tvSignInAddress = (TextView) findViewById(R.id.tv_signInAddress);
			tvSignOutTime = (TextView) findViewById(R.id.tv_signOutTime);
			tvSignOutAddress = (TextView) findViewById(R.id.tv_signOutAddress);
			tvFaultReason = (TextView) findViewById(R.id.tv_faultReason);
			tvIsFixed = (TextView) findViewById(R.id.tv_isFixed);
			tvRemark = (TextView) findViewById(R.id.tv_remark);
			
			/*
			 * 为已完成的故障工单需要显示的控件设置数值
			 */
			linearFinished.setVisibility(View.VISIBLE);
			btnReceiveOrder.setVisibility(View.GONE);
			btnCancelReceiveOrder.setVisibility(View.GONE);
			
			if (faultOrder.getEmployee() != null) {
				if (!TextUtils.isEmpty(faultOrder.getEmployee().getName())) {
					tvFixEmployee.setText(faultOrder.getEmployee().getName().trim());
				} else {
					tvFixEmployee.setText("姓名未知");
					tvFixEmployee.setTextColor(Color.RED);
				}
				if (faultOrder.getEmployee().getGroup() != null) {
					tvFixGroup.setText(faultOrder.getEmployee().getGroup().getName().trim());
				} else {
					tvFixGroup.setText("暂无组信息");
					tvFixGroup.setTextColor(Color.RED);
				}
			} else {
				tvFixEmployee.setText("暂无员工信息");
				tvFixEmployee.setTextColor(Color.RED);
				tvFixGroup.setText("暂无组信息");
				tvFixGroup.setTextColor(Color.RED);
			}
				
			if (faultOrder.getSignInTime() != null) {
				tvSignInTime.setText(sdf2.format(faultOrder.getSignInTime()));
			} else {
				tvSignInTime.setText("暂无该信息");
			}
			tvSignInAddress.setText(faultOrder.getSignInAddress());
			if (faultOrder.getSignOutTime() != null) {
				tvSignOutTime.setText(sdf2.format(faultOrder.getSignOutTime()));
			} else {
				tvSignOutTime.setText("暂无该信息");
			}
			tvSignOutAddress.setText(faultOrder.getSignOutAddress().trim());
			tvFaultReason.setText(faultOrder.getReason().trim());
			tvIsFixed.setText(faultOrder.getFixed() ? "已修好" : "未修好");
			tvRemark.setText(faultOrder.getRemark());
		}
		
		
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (workOrderState != WorkOrderState.FINISHED) {
					Log.i(tag, "" + isReceiveOrCancelOrder);
					Intent fragIntent = new Intent(FaultOrderDetailActivity.this, MaintainOrderSearchActivity.class);
					fragIntent.putExtra("isNeedRefresh", isReceiveOrCancelOrder);
					if (isReceiveOrCancelOrder) {
						fragIntent.putExtra("receivingTime", faultOrder.getReceivingTime());
					}
					
					setResult(RESULT_OK, fragIntent);
				}
				finish();
			}
		});
		
		btnQueryElevatorRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ElevatorRecordDetailActivity.myStartActivity(FaultOrderDetailActivity.this, 
						JSON.toJSONString(faultOrder.getElevatorRecord()));
			}
		});
		
		btnReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				isReceiveOrCancelOrder = true;
				/*
				 * 此处执行接单的相关操作，记录接单时间等。
				 */
				workOrderReceivePresenter.receiveOrder(WorkOrderType.FAULT_ORDER, faultOrder.getId(), employeeId);
			}
		});
		
		btnCancelReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				isReceiveOrCancelOrder = true;
				/*
				 * 此处执行取消接单的相关操作，记录接单时间等。
				 */
				workOrderReceivePresenter.cancelReceiveOrder(WorkOrderType.FAULT_ORDER, faultOrder.getId(), employeeId);

			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (workOrderState != WorkOrderState.FINISHED) {
			Log.i(tag, "" + isReceiveOrCancelOrder);
			Intent fragIntent = new Intent(FaultOrderDetailActivity.this, MaintainOrderSearchActivity.class);
			fragIntent.putExtra("isNeedRefresh", isReceiveOrCancelOrder);
			if (isReceiveOrCancelOrder) {
				fragIntent.putExtra("receivingTime", faultOrder.getReceivingTime());
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
				Toast.makeText(FaultOrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}
	
	@Override
	public void receiveSuccess(final Date receivingTime) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(FaultOrderDetailActivity.this, "接单成功", Toast.LENGTH_SHORT).show();
				btnReceiveOrder.setVisibility(View.GONE);
				btnCancelReceiveOrder.setVisibility(View.VISIBLE);
				tvReceiveState.setText("已接单");
				tvReceiveState.setTextColor(Color.BLACK);
				linearReceiveTime.setVisibility(View.VISIBLE);
				faultOrder.setReceivingTime(receivingTime);
				tvReceiveTime.setText(sdf2.format(receivingTime));
			}
		});
	}

	@Override
	public void cancelReceiveSuccess() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(FaultOrderDetailActivity.this, "成功取消接单", Toast.LENGTH_SHORT).show();
				btnCancelReceiveOrder.setVisibility(View.GONE);
				btnReceiveOrder.setVisibility(View.VISIBLE);
				tvReceiveState.setText("未接单");
				tvReceiveState.setTextColor(Color.RED);
				linearReceiveTime.setVisibility(View.GONE);
				faultOrder.setReceivingTime(null);
			}
		});
	}

	// 自定义一个startActivity()方法
	public static void myStartActivity(Context context,
			int workOrderState, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, FaultOrderDetailActivity.class);
		actIntent.putExtra("workOrderState", workOrderState);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivity(actIntent);
	}
	
	public static void myStartActivityForResult(Fragment fragment, int requestCode, 
			int workOrderState, String jsonWorkOrder) {
		Intent actIntent = new Intent(fragment.getActivity(), FaultOrderDetailActivity.class);
		actIntent.putExtra("workOrderState", workOrderState);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		fragment.startActivityForResult(actIntent, requestCode);
	}
}
