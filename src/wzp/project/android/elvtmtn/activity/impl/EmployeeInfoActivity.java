package wzp.project.android.elvtmtn.activity.impl;

import java.util.List;

import com.alibaba.fastjson.JSON;

import android.app.ProgressDialog;
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
import wzp.project.android.elvtmtn.util.MyApplication;

public class EmployeeInfoActivity extends BaseActivity implements IEmployeeInfoActivity {

	private Button btnBack;
	private TextView tvUsername;
	private TextView tvName;
	private TextView tvPhone;
	private TextView tvFixGroup;
	private TextView tvGroupMember;
	private Button btnChangePassword;
	private ProgressDialog progressDialog;					// 进度对话框
	
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
		btnChangePassword = (Button) findViewById(R.id.btn_changePassword);
		progressDialog = new ProgressDialog(this);
		
		btnChangePassword.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				
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
				progressDialog.setTitle("正在访问服务器，请稍后...");
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(true);
				
				progressDialog.show();
			}
		});
	}

	@Override
	public void closeProgressDialog() {
		if (progressDialog != null
				&& progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void backToLoginInterface() {
		Intent intent = new Intent(this, EmployeeLoginActivity.class);
		startActivity(intent);
	}
}
