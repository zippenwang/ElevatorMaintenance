package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSON;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.FaultOrder;

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
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fault_history_detail);
		
		initData();
		initWidget();
	}
	
	private void initData() {
		faultOrder = JSON.parseObject(getIntent().getStringExtra("jsonFaultOrder"), FaultOrder.class);
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
		
		tvWorkOrderNo.setText(faultOrder.getNo());
		tvFaultOccuredTime.setText(sdf.format(faultOrder.getOccuredTime()));
		tvFaultDescription.setText(faultOrder.getDescription());
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
		if (faultOrder.getSignOutTime() != null) {
			tvSignOutTime.setText(sdf.format(faultOrder.getSignOutTime()));
		} else {
			tvSignOutTime.setText("暂无该信息");
		}
		tvFaultReason.setText(faultOrder.getReason().trim());
		tvIsFixed.setText(faultOrder.getFixed() ? "已修好" : "未修好");
		String remark = faultOrder.getRemark();
		if (!TextUtils.isEmpty(remark)) {
			tvRemark.setText(faultOrder.getRemark());
		} else {
			tvRemark.setText("暂无");
		}
		
	}
	
	public static void myStartActivivty(Context context, String jsonFaultOrder) {
		Intent intent = new Intent(context, FaultHistoryDetailActivity.class);
		intent.putExtra("jsonFaultOrder", jsonFaultOrder);
		context.startActivity(intent);
	}
	
}
