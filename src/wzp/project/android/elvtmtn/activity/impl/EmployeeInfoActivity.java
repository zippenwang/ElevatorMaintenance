package wzp.project.android.elvtmtn.activity.impl;

import java.util.List;

import com.alibaba.fastjson.JSON;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	private TextView tvGroupMember;
	private Button btnExitCurLogin;
	private ProgressDialog progressDialog;					// 进度对话框
	private MyProgressDialog myProgressDialog; 
	
	private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private Employee employee;
	private long groupId;
	
	private EmployeeInfoSearchPresenter employeeInfoSearchPresenter
		= new EmployeeInfoSearchPresenter(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_info);
		
		initWidget();
		initData();
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		tvUsername = (TextView) findViewById(R.id.tv_username);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvFixGroup = (TextView) findViewById(R.id.tv_fixGroup);
		tvGroupMember = (TextView) findViewById(R.id.tv_groupMember);
		btnExitCurLogin = (Button) findViewById(R.id.btn_exitCurLogin);
		progressDialog = new ProgressDialog(this);
		myProgressDialog = new MyProgressDialog(this);
		
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
	
	private void initData() {
		String employeeJson = preferences.getString("employeeJson", "");
		if (employeeJson.equals("")) {
			throw new IllegalArgumentException("找不到Employee实例的JSON字符串");
		}
		
		employee = JSON.parseObject(employeeJson, Employee.class);
		groupId = employee.getGroup().getId();
		
		tvUsername.setText(employee.getUsername());
		tvName.setText(employee.getName());
		tvPhone.setText(employee.getPhone());
		tvFixGroup.setText(employee.getGroup().getName());
		tvGroupMember.setText("");

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
	public void searchSuccess(List<Employee> employeeList) {
		tvGroupMember.setText("");
		for (int i=0; i<employeeList.size(); i++) {
			tvGroupMember.append(employeeList.get(i).getName() + "\n");
		}
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
		if (myProgressDialog != null
				&& myProgressDialog.isShowing()) {
			myProgressDialog.dismiss();
		}
	}

	@Override
	public void backToLoginInterface() {
		EmployeeLoginActivity.myForceStartActivity(this);
	}

}
