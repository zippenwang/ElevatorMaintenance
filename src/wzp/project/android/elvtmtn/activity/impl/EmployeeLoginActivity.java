package wzp.project.android.elvtmtn.activity.impl;

import com.alibaba.fastjson.JSON;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IEmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.presenter.EmployeeLoginPresenter;
import wzp.project.android.elvtmtn.util.MyApplication;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class EmployeeLoginActivity extends BaseActivity implements IEmployeeLoginActivity {

	private EditText edtUsername;
	private EditText edtPassword;
	private Button btnLogin;
//	private Button btnExit;
	private CheckBox cbIsRemember;
	
	private Button btnIntoNext;
	
	private ProgressDialog progressDialog;
	
	private EmployeeLoginPresenter userLoginPresenter = new EmployeeLoginPresenter(this);
	
	private static final String tag = "EmployeeLoginActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initWidget();
	}

	/**
	 * UI控件初始化
	 */
	private void initWidget() {
		edtUsername = (EditText) findViewById(R.id.edt_username);
		edtPassword = (EditText) findViewById(R.id.edt_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
//		btnExit = (Button) findViewById(R.id.btn_exit);
		cbIsRemember = (CheckBox) findViewById(R.id.cb_isRemember);
		
		progressDialog = new ProgressDialog(this);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
		boolean isRemember = preferences.getBoolean("isRemember", false);
		if (isRemember) {
			cbIsRemember.setChecked(true);
			edtUsername.setText(preferences.getString("username", ""));
			edtPassword.setText(preferences.getString("password", ""));
		}
		
		btnLogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String username = edtUsername.getText().toString();
				String password = edtPassword.getText().toString();
				
				// 可以在此处对输入的内容进行正则表达式判断
				if (TextUtils.isEmpty(username.trim())
						|| TextUtils.isEmpty(password.trim())) {
					Toast.makeText(EmployeeLoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}		
				
				userLoginPresenter.login(username, password);
			}
		});
		
		/*btnExit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});*/
		
		btnIntoNext = (Button) findViewById(R.id.btn_intoNext);
		btnIntoNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EmployeeLoginActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void loginSuccess(Employee employee) {
		SharedPreferences.Editor editor 
			= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
		Log.i(tag, "employeeId:" + employee.getId() + ";groupId" + employee.getGroup().getId());
		editor.putLong("employeeId", employee.getId());
		editor.putLong("groupId", employee.getGroup().getId());
		editor.putString("username", edtUsername.getEditableText().toString());
		editor.putString("password", edtPassword.getEditableText().toString());
		editor.putBoolean("isRemember", cbIsRemember.isChecked());
		editor.putString("employeeJson", JSON.toJSONString(employee));
		editor.commit();
		
		/*
		 * 若登录成功，则跳转至主界面
		 */
		Intent intent = new Intent(EmployeeLoginActivity.this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void loginFail() {
		showToast("用户名或密码错误！");
	}

	@Override
	public void showProgressDialog() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				progressDialog.setTitle("正在验证用户名和密码，请稍后...");
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(true);
				
				progressDialog.show();
			}
		});
	}

	@Override
	public void closeProgressDialog() {
		/*runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		});*/
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void showToast(final String text) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(EmployeeLoginActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}
}
