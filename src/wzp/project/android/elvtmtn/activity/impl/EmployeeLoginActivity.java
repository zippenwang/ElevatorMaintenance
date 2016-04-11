package wzp.project.android.elvtmtn.activity.impl;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IEmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.presenter.UserLoginPresenter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmployeeLoginActivity extends BaseActivity implements IEmployeeLoginActivity {

	private EditText edtUsername;
	private EditText edtPassword;
	private Button btnLogin;
	private Button btnExit;
	
	private Button btnIntoNext;
	
	private ProgressDialog progressDialog;
	
	private UserLoginPresenter userLoginPresenter = new UserLoginPresenter(this);
	
	
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
		btnExit = (Button) findViewById(R.id.btn_exit);
		
		progressDialog = new ProgressDialog(this);
		
		btnLogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String username = edtUsername.getText().toString();
				String password = edtPassword.getText().toString();
				
				// 可以在此处对输入的内容进行正则表达式判断
				
				userLoginPresenter.login(username, password);
			}
		});
		
		btnExit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
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
	public void loginSuccess() {
		/*
		 * 若登录成功，则跳转至主界面
		 */
//		Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(EmployeeLoginActivity.this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void loginFail() {
//		Toast.makeText(this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
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
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		});
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