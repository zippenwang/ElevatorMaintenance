package wzp.project.android.elvtmtn.activity.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IEmployeeInfoActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.presenter.EmployeeInfoSearchPresenter;
import wzp.project.android.elvtmtn.util.ActivityCollector;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.MyProgressDialog;

public class EmployeeInfoActivity extends BaseActivity implements IEmployeeInfoActivity {

	private Button btnBack;
	private TextView tvUsername;
	private TextView tvName;
	private TextView tvPhone;
	private TextView tvFixGroup;
//	private TextView tvGroupMember;
	private LinearLayout linearGroupMember;
	private Button btnExitCurLogin;
	private MyProgressDialog myProgressDialog; 
	private AlertDialog.Builder dialogBuilder;
	
	private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private long groupId;
	private long employeeId;
	private String[] operateItems = new String[] {"打电话", "发短信"};
	
	private static float density = 0f;
	
	private EmployeeInfoSearchPresenter employeeInfoSearchPresenter
		= new EmployeeInfoSearchPresenter(this);
	
	private static final String LOG_TAG = "EmployeeInfoActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_info);
		
		initWidget();
		
		try {
			initParams();
		} catch (IllegalArgumentException expection) {
			Log.e(LOG_TAG, Log.getStackTraceString(expection));
			showToast("缺失重要数据，请重新登录");
			EmployeeLoginActivity.myForceStartActivity(this);
			return;
		} catch (Exception exp2) {
			Log.e(LOG_TAG, Log.getStackTraceString(exp2));
			showToast("程序异常，请重新登录");
			EmployeeLoginActivity.myForceStartActivity(this);
			return;
		}
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		tvUsername = (TextView) findViewById(R.id.tv_username);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvFixGroup = (TextView) findViewById(R.id.tv_fixGroup);
//		tvGroupMember = (TextView) findViewById(R.id.tv_groupMember);
		linearGroupMember = (LinearLayout) findViewById(R.id.linear_groupMember);
		btnExitCurLogin = (Button) findViewById(R.id.btn_exitCurLogin);
		myProgressDialog = new MyProgressDialog(this);
		dialogBuilder = new AlertDialog.Builder(EmployeeInfoActivity.this)
			.setCancelable(true);
		
		btnExitCurLogin.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(EmployeeInfoActivity.this)
					.setMessage("您确定退出？")
					.setCancelable(true)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							EmployeeLoginActivity.myForceStartActivity(EmployeeInfoActivity.this);
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.create();
				dialog.show();
			}
		});
		
		btnBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}
	
	private void initParams() {
		density = getResources().getDisplayMetrics().density;
		
		groupId = preferences.getLong("groupId", -1);
		if (-1 == groupId) {
			throw new IllegalArgumentException("找不到groupId");
		}
		
		employeeId = preferences.getLong("employeeId", -1);
		if (-1 == employeeId) {
			throw new IllegalArgumentException("找不到employeeId");
		}

		employeeInfoSearchPresenter.searchEmployeeInfo(groupId);
	}

	@Override
	public void showToast(final String tipInfo) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(EmployeeInfoActivity.this, tipInfo, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void searchSuccess(final List<Employee> employeeList) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (employeeList != null
						&& employeeList.size()>0) {
					/*tvGroupMember.setText("");
					for (int i=0; i<employeeList.size(); i++) {
						if (employeeId == employeeList.get(i).getId()) {
							employee = employeeList.get(i);
							tvUsername.setText(employee.getUsername());
							tvName.setText(employee.getName());
							tvPhone.setText(employee.getPhone());
							tvFixGroup.setText(employee.getGroup().getName());
						}
						tvGroupMember.append(employeeList.get(i).getName() + "\n");
					}*/
					
					for (final Employee employee : employeeList) {
						if (employeeId == employee.getId()) {
							tvUsername.setText(employee.getUsername());
							tvName.setText(employee.getName());
							tvPhone.setText(employee.getPhone());
							tvFixGroup.setText(employee.getGroup().getName());
							continue;
						}
						
						RelativeLayout relativeSingleMember = new RelativeLayout(EmployeeInfoActivity.this);
						relativeSingleMember.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 
								LayoutParams.WRAP_CONTENT));
						relativeSingleMember.setPadding(0, 5, 0, 5);
						
						TextView tvName = new TextView(EmployeeInfoActivity.this);
						tvName.setWidth((int) (130 * density));
						tvName.setGravity(Gravity.LEFT);
						tvName.setText(employee.getName());
						tvName.setTextSize(18);
						
						TextView tvPhone = new TextView(EmployeeInfoActivity.this);
						tvPhone.setWidth((int) (130 * density));
						tvPhone.setGravity(Gravity.LEFT);
						tvPhone.setText(employee.getPhone());
						tvPhone.setTextSize(18);
						tvPhone.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG);
						tvPhone.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.setItems(operateItems, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if (0 == which) {
											// 打电话
											Intent intent = new Intent(Intent.ACTION_DIAL,
													Uri.parse("tel:" + employee.getPhone())); 
										    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
										    startActivity(intent);
										} else if (1 == which) {
											// 发短信
											Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + employee.getPhone()));
//											intent.putExtra("sms_body", "");
											startActivity(intent);  
										}
									}
								}).show();
							}
						});
						
						RelativeLayout.LayoutParams lpName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
								LayoutParams.WRAP_CONTENT);
						lpName.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						relativeSingleMember.addView(tvName, lpName);
						
						RelativeLayout.LayoutParams lpPhone = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
								LayoutParams.WRAP_CONTENT);
						lpPhone.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						relativeSingleMember.addView(tvPhone, lpPhone);
						
						linearGroupMember.addView(relativeSingleMember);
						
						relativeSingleMember = null;
						tvName = null;
						tvPhone = null;
						lpName = null;
						lpPhone = null;
					}
				}
			}
		});
	}

	@Override
	public void showProgressDialog() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				myProgressDialog.setMessage("正在获取数据，请稍后...");
				myProgressDialog.setCancelable(true);
				
				myProgressDialog.show();
			}
		});
	}
	
	@Override
	public void showProgressDialog(String tipInfo) {}

	@Override
	public void closeProgressDialog() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (myProgressDialog != null
						&& myProgressDialog.isShowing()) {
					myProgressDialog.dismiss();
				}
			}
		});
	}

	@Override
	public void backToLoginInterface() {
		EmployeeLoginActivity.myForceStartActivity(this);
	}

}
