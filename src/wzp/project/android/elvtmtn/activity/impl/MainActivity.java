package wzp.project.android.elvtmtn.activity.impl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;

public class MainActivity extends BaseActivity {

	private Button btnWorkOrderSearch;
	private Button btnSignIn;
	private Button btnWorkOrderFeedback;
//	private Button btnTakePhotos;
	private Button btnElevatorRecord;
	private Button btnUserInfo;
	private AlertDialog.Builder altDlgBuilder;
	
	private String[] items = new String[] {"保养工单", "故障工单"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initWidget();
	}
	
	private void initWidget() {
		btnWorkOrderSearch = (Button) findViewById(R.id.btn_workOrderSearch);
		btnSignIn = (Button) findViewById(R.id.btn_signIn);
		btnWorkOrderFeedback = (Button) findViewById(R.id.btn_workOrderFeedback);
//		btnTakePhotos = (Button) findViewById(R.id.btn_takePhotos);
		btnElevatorRecord = (Button) findViewById(R.id.btn_elevatorRecord);
		btnUserInfo = (Button) findViewById(R.id.btn_userInfo);
		altDlgBuilder = new AlertDialog.Builder(this);
		
		btnWorkOrderSearch.setOnClickListener(new OnClickListener() {			
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
		
		btnSignIn.setOnClickListener(new OnClickListener() {			
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
		
		btnWorkOrderFeedback.setOnClickListener(new OnClickListener() {			
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
		
		/*btnTakePhotos.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "拍照上传被点击", Toast.LENGTH_SHORT).show();
			}
		});*/
		
		btnElevatorRecord.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent actIntent = new Intent(MainActivity.this, ElevatorRecordSearchActivity.class);
				startActivity(actIntent);
			}
		});
		
		btnUserInfo.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
//				Toast.makeText(MainActivity.this, "员工信息被点击", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this, EmployeeInfoActivity.class);
				startActivity(intent);
			}
		});
	}

}
