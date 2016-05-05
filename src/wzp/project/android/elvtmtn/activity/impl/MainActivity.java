package wzp.project.android.elvtmtn.activity.impl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.util.MyImageButton;

public class MainActivity extends BaseActivity {

	private MyImageButton mibtnWorkOrderSearch;
	private MyImageButton mibtnSignIn;
	private MyImageButton mibtnWorkOrderFeedback;
	private MyImageButton mibtnElevatorRecord;
	private MyImageButton mibtnUserInfo;
	private AlertDialog.Builder altDlgBuilder;
	
	private String[] items = new String[] {"保养工单", "故障工单"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initWidget();
	}
	
	private void initWidget() {
		mibtnWorkOrderSearch = (MyImageButton) findViewById(R.id.mibtn_workOrderSearch);
		mibtnSignIn = (MyImageButton) findViewById(R.id.mibtn_signIn);
		mibtnWorkOrderFeedback = (MyImageButton) findViewById(R.id.mibtn_workOrderFeedback);
		mibtnElevatorRecord = (MyImageButton) findViewById(R.id.mibtn_elevatorRecord);
		mibtnUserInfo = (MyImageButton) findViewById(R.id.mibtn_userInfo);
		altDlgBuilder = new AlertDialog.Builder(this);
		
		mibtnWorkOrderSearch.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				altDlgBuilder.setTitle("请选择工单类型 ");
				altDlgBuilder.setCancelable(true);
				altDlgBuilder.setItems(items, new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						WorkOrderSearchActivity.myStartActivity(MainActivity.this, which);
						if (WorkOrderType.FAULT_ORDER == which) {
							FaultOrderSearchActivity.myStartActivity(MainActivity.this);
						} else if (WorkOrderType.MAINTAIN_ORDER == which) {
							MaintainOrderSearchActivity.myStartActivity(MainActivity.this);
						}
					}
				});
				altDlgBuilder.show();
			}
		});
		
		mibtnSignIn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				altDlgBuilder.setTitle("请选择工单类型 ");
				altDlgBuilder.setCancelable(true);
				altDlgBuilder.setItems(items, new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EmployeeSignInActivity.myStartActivity(MainActivity.this, which);
					}
				});
				altDlgBuilder.show();
			}
		});
		
		mibtnWorkOrderFeedback.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				altDlgBuilder.setTitle("请选择工单类型 ");
				altDlgBuilder.setCancelable(true);
				altDlgBuilder.setItems(items, new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						WorkOrderFeedbackActivity.myStartActivity(MainActivity.this, which);
					}
				});
				altDlgBuilder.show();
			}
		});
		
		mibtnElevatorRecord.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent actIntent = new Intent(MainActivity.this, ElevatorRecordSearchActivity.class);
				startActivity(actIntent);
			}
		});
		
		mibtnUserInfo.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
//				Toast.makeText(MainActivity.this, "员工信息被点击", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this, EmployeeInfoActivity.class);
				startActivity(intent);
			}
		});
	}

}
