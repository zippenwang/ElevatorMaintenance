package wzp.project.android.elvtmtn.activity.impl;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.util.MyImageButton;

public class MainActivity extends BaseActivity {

	/*
	 * 控件定义
	 */
	private MyImageButton mibtnWorkOrderSearch;
	private MyImageButton mibtnSignIn;
	private MyImageButton mibtnWorkOrderFeedback;
	private MyImageButton mibtnElevatorRecord;
	private MyImageButton mibtnUserInfo;
	private AlertDialog.Builder altDlgBuilder;
	
	private int pressBackTimes;				// Back键按下的次数
	
	private String[] items = new String[] {"保养工单", "故障工单"};		// 工单类型数组
	
	
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
		altDlgBuilder.setTitle("请选择工单类型 ");
		altDlgBuilder.setCancelable(true);
		
		mibtnWorkOrderSearch.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				altDlgBuilder.setItems(items, new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
				Intent intent = new Intent(MainActivity.this, EmployeeInfoActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public static void myStartActivity(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onBackPressed() {
//		moveTaskToBack(true);		// 退出后并没有销毁Activity，只是将应用程序的back stack保存至后台中
		/*
		 * 按一次退出，提示“再按一次退出系统”，1.5秒内再按一次退出，则真正退出应用程序
		 */
		pressBackTimes++;
		if (pressBackTimes == 2) {
			finish();
		} else if (pressBackTimes < 2) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					pressBackTimes = 0;
				}
			}, 1500);
			Toast.makeText(this, "再按一次退出系统", Toast.LENGTH_SHORT).show();
		}
		
	}
}
