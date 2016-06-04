package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.Group;

public class FaultHistoryDetailActivity extends BaseActivity {
	
	private Button btnBack;
	private TextView tvWorkOrderNo;
	private TextView tvFaultOccuredTime;
	private TextView tvFaultDescription;
	private TextView tvFixEmployee;
	private TextView tvFixGroup;
	private TextView tvSignOutTime;
	private TextView tvFaultReason;
	private TextView tvIsFixed;
	private TextView tvRemark;
	
	private FaultOrder faultOrder;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static String LOG_TAG = "FaultHistoryDetailActivity";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fault_history_detail);
		
		try {
			initData();
		} catch (IllegalArgumentException expection) {
			Log.e(LOG_TAG, Log.getStackTraceString(expection));
			Toast.makeText(this, "缺失重要数据，请重新登录", Toast.LENGTH_SHORT).show();
			EmployeeLoginActivity.myForceStartActivity(this);
			return;
		} catch (Exception exp2) {
			Log.e(LOG_TAG, Log.getStackTraceString(exp2));
			Toast.makeText(this, "程序异常，请重新登录", Toast.LENGTH_SHORT).show();
			EmployeeLoginActivity.myForceStartActivity(this);
			return;
		}
		
		initWidget();
	}
	
	private void initData() {
		String jsonFaultOrder = getIntent().getStringExtra("jsonFaultOrder");
		if (null == jsonFaultOrder) {
			throw new IllegalArgumentException("没有接收到工单json字符串");
		}
		
		faultOrder = JSON.parseObject(jsonFaultOrder, FaultOrder.class);
	}

	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		tvWorkOrderNo = (TextView) findViewById(R.id.tv_workOrderNo);
		tvFaultOccuredTime = (TextView) findViewById(R.id.tv_faultOccuredTime);
		tvFaultDescription = (TextView) findViewById(R.id.tv_faultDescription);
		tvFixEmployee = (TextView) findViewById(R.id.tv_fixEmployee);
		tvFixGroup = (TextView) findViewById(R.id.tv_fixGroup);
		tvSignOutTime = (TextView) findViewById(R.id.tv_signOutTime);
		tvFaultReason = (TextView) findViewById(R.id.tv_faultReason);
		tvIsFixed = (TextView) findViewById(R.id.tv_isFixed);
		tvRemark = (TextView) findViewById(R.id.tv_remark);
		
		btnBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		String no = faultOrder.getNo();
		if (!TextUtils.isEmpty(no)) {
			tvWorkOrderNo.setText(no);
		} else {
			tvWorkOrderNo.setText("暂无工单号");
			tvWorkOrderNo.setTextSize(18);
		}
		
		Date faultOccuredTime = faultOrder.getOccuredTime();
		if (faultOccuredTime != null) {
			tvFaultOccuredTime.setText(sdf.format(faultOccuredTime));
		} else {
			tvFaultOccuredTime.setText("暂无发生日期");
		}
		
		String faultDescription = faultOrder.getDescription();
		if (!TextUtils.isEmpty(faultDescription)) {
			tvFaultDescription.setText(faultDescription);
		} else {
			tvFaultDescription.setText("暂无故障描述");
		}
		
		Employee employee = faultOrder.getEmployee();
		if (employee != null) {
			String employeeName = employee.getName();
			if (!TextUtils.isEmpty(employeeName)) {
				tvFixEmployee.setText(employeeName.trim());
			} else {
				tvFixEmployee.setText("姓名未知");
			}
			
			Group group = employee.getGroup();
			if (group != null) {
				String groupName = group.getName();
				if (!TextUtils.isEmpty(groupName)) {
					tvFixGroup.setText(groupName.trim());
				} else {
					tvFixGroup.setText("小组名称未知");
				}
			} else {
				tvFixGroup.setText("小组未知");
			}
		} else {
			tvFixEmployee.setText("暂无员工信息");
			tvFixGroup.setText("暂无组信息");
		}
		
		Date signOutTime = faultOrder.getSignOutTime();
		if (signOutTime != null) {
			tvSignOutTime.setText(sdf.format(signOutTime));
		} else {
			tvSignOutTime.setText("暂无该时间");
		}
		
		String faultReason = faultOrder.getReason();
		if (!TextUtils.isEmpty(faultReason)) {
			tvFaultReason.setText(faultReason.trim());
		} else {
			tvFaultReason.setText("暂无该信息");
		}
		
		tvIsFixed.setText(faultOrder.getFixed() ? "已修好" : "未修好");
		
		String remark = faultOrder.getRemark();
		if (!TextUtils.isEmpty(remark)) {
			tvRemark.setText(faultOrder.getRemark());
		} else {
			tvRemark.setText("无");
		}
	}
	
	public static void myStartActivivty(Context context, String jsonFaultOrder) {
		Intent intent = new Intent(context, FaultHistoryDetailActivity.class);
		intent.putExtra("jsonFaultOrder", jsonFaultOrder);
		context.startActivity(intent);
	}
	
}
