package wzp.project.android.elvtmtn.activity.impl;

import com.alibaba.fastjson.JSON;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.util.MyApplication;

public class EmployeeInfoActivity extends BaseActivity {

	private TextView tvUsername;
	private TextView tvName;
	private TextView tvFixGroup;
	private TextView tvGroupMember;
	private Button btnChangePassword;
	
	private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private Employee employee;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_info);
		
		initWidget();
		initData();
	}
	
	private void initWidget() {
		tvUsername = (TextView) findViewById(R.id.tv_username);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvFixGroup = (TextView) findViewById(R.id.tv_fixGroup);
		tvGroupMember = (TextView) findViewById(R.id.tv_groupMember);
		btnChangePassword = (Button) findViewById(R.id.btn_changePassword);
		btnChangePassword.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				
			}
		});
	}
	
	private void initData() {
		String employeeJson = preferences.getString("employeeJson", "");
		if (employeeJson.equals("")) {
			throw new IllegalArgumentException("找不到Employee实例的JSON字符串");
		}
		
		employee = JSON.parseObject(employeeJson, Employee.class);
		
		tvUsername.setText(employee.getUsername());
		tvName.setText(employee.getName());
		tvFixGroup.setText(employee.getGroup().getName());
		tvGroupMember.setText("");
		/*for (Employee ele : employee.getGroup().getEmployees()) {
			tvGroupMember.append(ele.getName() + ",");
		}*/
	}
}
