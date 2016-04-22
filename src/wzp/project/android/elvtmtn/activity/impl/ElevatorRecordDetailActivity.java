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
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.helper.adapter.UnfinishedFaultOrderAdapter;

public class ElevatorRecordDetailActivity extends BaseActivity {

	private Button btnBack;
	private TextView tvElevatorNumber;
	private TextView tvModelNumber;
	private TextView tvElevatorType;
	private TextView tvMaxWeight;
	private TextView tvSpeed;
	private TextView tvManufacturingDate;
	private TextView tvLastMaintainTime;
	private TextView tvElevatorAddress;
	private TextView tvBuildingNumber;
	private TextView tvUserUnit;
	private TextView tvPhone;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elevator_record);
		
		initWidget();
		
		Intent intent = getIntent();
		String jsonElevatorRecord = intent.getStringExtra("elevatorRecord");
		showRecord(JSON.parseObject(jsonElevatorRecord, ElevatorRecord.class));
	}
	
	private void initWidget() {
		tvElevatorNumber = (TextView) findViewById(R.id.tv_elevatorNumber);
		tvModelNumber = (TextView) findViewById(R.id.tv_modelNumber);
		tvElevatorType = (TextView) findViewById(R.id.tv_elevatorType);
		tvMaxWeight = (TextView) findViewById(R.id.tv_maxWeight);
		tvSpeed = (TextView) findViewById(R.id.tv_speed);
		tvManufacturingDate = (TextView) findViewById(R.id.tv_manufacturingDate);
		tvLastMaintainTime = (TextView) findViewById(R.id.tv_lastMaintainTime);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvBuildingNumber = (TextView) findViewById(R.id.tv_buildingNumber);
		tvUserUnit = (TextView) findViewById(R.id.tv_userUnit);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void showRecord(ElevatorRecord elevatorRecord) {
		tvElevatorNumber.setText(String.valueOf(elevatorRecord.getId()));
		if (!TextUtils.isEmpty(elevatorRecord.getModelNumber())) {
			tvModelNumber.setText(elevatorRecord.getModelNumber());
		} else {
			tvModelNumber.setText("暂无该信息");
			tvModelNumber.setTextColor(Color.RED);
		}
		if (!TextUtils.isEmpty(elevatorRecord.getType())) {
			tvElevatorType.setText(elevatorRecord.getType());
		} else {
			tvElevatorType.setText("暂无该信息");
			tvElevatorType.setTextColor(Color.RED);
		}
		if (elevatorRecord.getMaxWeight() != null) {
			tvMaxWeight.setText(String.valueOf(elevatorRecord.getMaxWeight()));
		} else {
			tvMaxWeight.setText("暂无该信息");
			tvMaxWeight.setTextColor(Color.RED);
		}
		if (elevatorRecord.getSpeed() != null) {
			tvSpeed.setText(String.valueOf(elevatorRecord.getSpeed()));	
		} else {
			tvSpeed.setText("暂无该信息");	
			tvSpeed.setTextColor(Color.RED);
		}
		if (elevatorRecord.getManufacturingDate() != null) {
			tvManufacturingDate.setText(sdf.format(elevatorRecord.getManufacturingDate()));
		} else {
			tvManufacturingDate.setText("暂无该信息");
			tvManufacturingDate.setTextColor(Color.RED);
		}		
		if (elevatorRecord.getLastMaintainTime() != null) {
			tvLastMaintainTime.setText(sdf.format(elevatorRecord.getLastMaintainTime()));
		} else {
			tvLastMaintainTime.setText("暂无该信息");
			tvLastMaintainTime.setTextColor(Color.RED);
		}
		if (!TextUtils.isEmpty(elevatorRecord.getAddress())) {
			tvElevatorAddress.setText(elevatorRecord.getAddress());
		} else {
			tvElevatorAddress.setText("暂无该信息");
			tvElevatorAddress.setTextColor(Color.RED);
		}
		if (!TextUtils.isEmpty(elevatorRecord.getBuildingNumber())) {
			tvBuildingNumber.setText(elevatorRecord.getBuildingNumber());
		} else {
			tvBuildingNumber.setText("暂无该信息");
			tvBuildingNumber.setTextColor(Color.RED);
		}
		if (!TextUtils.isEmpty(elevatorRecord.getUnit())) {
			tvUserUnit.setText(elevatorRecord.getUnit());
		} else {
			tvUserUnit.setText("暂无该信息");
			tvUserUnit.setTextColor(Color.RED);
		}
		if (!TextUtils.isEmpty(elevatorRecord.getPhone())) {
			tvPhone.setText(elevatorRecord.getPhone());
		} else {
			tvPhone.setText("暂无该信息");
			tvPhone.setTextColor(Color.RED);
		}		
	}
	
	public static void myStartActivity(Context context, String jsonElevatorRecord) {
		Intent intent = new Intent(context, ElevatorRecordDetailActivity.class);
		intent.putExtra("elevatorRecord", jsonElevatorRecord);
		context.startActivity(intent);
	}
}
