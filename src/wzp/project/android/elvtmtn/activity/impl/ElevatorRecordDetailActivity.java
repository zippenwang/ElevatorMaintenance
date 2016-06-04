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
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.helper.adapter.UnfinishedFaultOrderAdapter;

public class ElevatorRecordDetailActivity extends BaseActivity {

	private Button btnBack;
	private TextView tvElevatorNumber;
	private TextView tvModelNumber;
	private TextView tvElevatorType;
	private TextView tvElevatorDescription;
	private TextView tvMaxWeight;
	private TextView tvSpeed;
	private TextView tvManufacturingDate;
	private TextView tvLastMaintainTime;
	private TextView tvElevatorAddress;
	private TextView tvBuildingNumber;
	private TextView tvUserUnit;
	private TextView tvPhone;
	private Button btnQueryFaultHistory;
	
	private ElevatorRecord elevatorRecord;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	
	private static String LOG_TAG = "ElevatorRecordDetailActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elevator_record_detail);
		
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
		showRecord(elevatorRecord);
	}
	
	private void initData() {		
		String jsonElevatorRecord = getIntent().getStringExtra("elevatorRecord");
		if (null == jsonElevatorRecord) {
			throw new IllegalArgumentException("没有接收到电梯档案json字符串");
		}
		
		elevatorRecord = JSON.parseObject(jsonElevatorRecord, ElevatorRecord.class);
	}
	
	private void initWidget() {
		tvElevatorNumber = (TextView) findViewById(R.id.tv_elevatorNumber);
		tvModelNumber = (TextView) findViewById(R.id.tv_modelNumber);
		tvElevatorType = (TextView) findViewById(R.id.tv_elevatorType);
		tvElevatorDescription = (TextView) findViewById(R.id.tv_elevatorDescription);
		tvMaxWeight = (TextView) findViewById(R.id.tv_maxWeight);
		tvSpeed = (TextView) findViewById(R.id.tv_speed);
		tvManufacturingDate = (TextView) findViewById(R.id.tv_manufacturingDate);
		tvLastMaintainTime = (TextView) findViewById(R.id.tv_lastMaintainTime);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvBuildingNumber = (TextView) findViewById(R.id.tv_buildingNumber);
		tvUserUnit = (TextView) findViewById(R.id.tv_userUnit);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnQueryFaultHistory = (Button) findViewById(R.id.btn_queryFaultHistory);
		
		
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnQueryFaultHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				FaultHistorySearchActivity.myStartActivivty(ElevatorRecordDetailActivity.this, 
						elevatorRecord.getId());
			}
		});
	}
	
	private void showRecord(ElevatorRecord elevatorRecord) {
		String no = elevatorRecord.getNo();
		if (!TextUtils.isEmpty(no)) {
			tvElevatorNumber.setText(no);
		} else {
			tvElevatorNumber.setText("暂无该信息");
			tvElevatorNumber.setTextColor(Color.RED);
		}
		
		String modelNumber = elevatorRecord.getModelNumber();
		if (!TextUtils.isEmpty(modelNumber)) {
			tvModelNumber.setText(modelNumber);
		} else {
			tvModelNumber.setText("暂无该信息");
			tvModelNumber.setTextColor(Color.RED);
		}
		
		String elevatorType = elevatorRecord.getType();
		if (!TextUtils.isEmpty(elevatorType)) {
			tvElevatorType.setText(elevatorType);
		} else {
			tvElevatorType.setText("暂无该信息");
			tvElevatorType.setTextColor(Color.RED);
		}
		
		Integer maxWeight = elevatorRecord.getMaxWeight();
		if (maxWeight != null) {
			tvMaxWeight.setText(String.valueOf(maxWeight));
		} else {
			tvMaxWeight.setText("暂无该信息");
			tvMaxWeight.setTextColor(Color.RED);
		}
		
		Float speed = elevatorRecord.getSpeed();
		if (speed != null) {
			tvSpeed.setText(String.valueOf(speed));	
		} else {
			tvSpeed.setText("暂无该信息");	
			tvSpeed.setTextColor(Color.RED);
		}
		
		Date manufacturingDate = elevatorRecord.getManufacturingDate();
		if (manufacturingDate != null) {
			tvManufacturingDate.setText(sdf.format(manufacturingDate));
		} else {
			tvManufacturingDate.setText("暂无该信息");
			tvManufacturingDate.setTextColor(Color.RED);
		}
		
		Date lastMaintainTime = elevatorRecord.getLastMaintainTime();
		if (lastMaintainTime != null) {
			tvLastMaintainTime.setText(sdf.format(lastMaintainTime));
		} else {
			tvLastMaintainTime.setText("暂无该信息");
			tvLastMaintainTime.setTextColor(Color.RED);
		}
		
		String address = elevatorRecord.getAddress();
		if (!TextUtils.isEmpty(address)) {
			tvElevatorAddress.setText(address);
		} else {
			tvElevatorAddress.setText("暂无该信息");
			tvElevatorAddress.setTextColor(Color.RED);
		}
		
		String buildingNumber = elevatorRecord.getBuildingNumber();
		if (!TextUtils.isEmpty(buildingNumber)) {
			tvBuildingNumber.setText(buildingNumber);
		} else {
			buildingNumber = "***";
			tvBuildingNumber.setText("暂无该信息");
			tvBuildingNumber.setTextColor(Color.RED);
		}
		
		String unint = elevatorRecord.getUnit();
		if (!TextUtils.isEmpty(unint)) {
			tvUserUnit.setText(unint);
		} else {
			unint = "******单位";
			tvUserUnit.setText("暂无该信息");
			tvUserUnit.setTextColor(Color.RED);
		}
		
		String phone = elevatorRecord.getPhone();
		if (!TextUtils.isEmpty(phone)) {
			tvPhone.setText(phone);
		} else {
			tvPhone.setText("暂无该信息");
			tvPhone.setTextColor(Color.RED);
		}
	
		String elevatorNumber = elevatorRecord.getElevatorNumber();
		if (TextUtils.isEmpty(elevatorNumber)) {
			elevatorNumber = "***";
		}

		tvElevatorDescription.setText(unint + buildingNumber + "号楼" + elevatorNumber + "号梯");
	}
	
	public static void myStartActivity(Context context, String jsonElevatorRecord) {
		Intent intent = new Intent(context, ElevatorRecordDetailActivity.class);
		intent.putExtra("elevatorRecord", jsonElevatorRecord);
		context.startActivity(intent);
	}
}
