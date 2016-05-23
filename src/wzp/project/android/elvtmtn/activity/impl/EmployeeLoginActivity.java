package wzp.project.android.elvtmtn.activity.impl;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushManager;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IEmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.presenter.EmployeeLoginPresenter;
import wzp.project.android.elvtmtn.util.ClearAllEditText;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.MyProgressDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

//	private EditText edtUsername;
	private ClearAllEditText caedtUsername;
//	private EditText edtPassword;
	private ClearAllEditText caedtPassword;
	private Button btnLogin;
//	private Button btnExit;
	private CheckBox cbIsRemember;
	
	// 用于测试的跳转按钮
	private Button btnIntoNext;
	
	private ProgressDialog progressDialog;
	private MyProgressDialog myProgressDialog;
	
	private EmployeeLoginPresenter userLoginPresenter = new EmployeeLoginPresenter(this);
	
	private PushManager pushManager = PushManager.getInstance();
	
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
		pushManager.initialize(this);
		
//		edtUsername = (EditText) findViewById(R.id.edt_username);
		caedtUsername = (ClearAllEditText) findViewById(R.id.caedt_username);
//		edtPassword = (EditText) findViewById(R.id.edt_password);
		caedtPassword = (ClearAllEditText) findViewById(R.id.caedt_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
//		btnExit = (Button) findViewById(R.id.btn_exit);
		cbIsRemember = (CheckBox) findViewById(R.id.cb_isRemember);
		
		progressDialog = new ProgressDialog(this);
		myProgressDialog = new MyProgressDialog(this);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
		boolean isRemember = preferences.getBoolean("isRemember", false);
		if (isRemember) {
			cbIsRemember.setChecked(true);
//			edtUsername.setText(preferences.getString("username", ""));
			caedtUsername.setText(preferences.getString("username", ""));
//			edtPassword.setText(preferences.getString("password", ""));
			caedtPassword.setText(preferences.getString("password", ""));
			Drawable[] drawables = caedtPassword.getCompoundDrawables();
			caedtPassword.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
		}
		
		btnLogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
//				String username = edtUsername.getText().toString();
				String username = caedtUsername.getText().toString();
//				String password = edtPassword.getText().toString();
				String password = caedtPassword.getText().toString();
				
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
//		editor.putString("username", edtUsername.getEditableText().toString());
		editor.putString("username", caedtUsername.getEditableText().toString());
//		editor.putString("password", edtPassword.getEditableText().toString());
		editor.putString("password", caedtPassword.getEditableText().toString());
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
				/*progressDialog.setTitle("正在验证用户名和密码，请稍后...");
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(true);
				
				progressDialog.show();*/
				
				myProgressDialog.setMessage("正在访问服务器，请稍后...");
				myProgressDialog.setCancelable(true);
				
				myProgressDialog.show();
			}
		});
	}
	
	@Override
	public void showProgressDialog(String tipInfo) {}

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
		/*if (progressDialog != null) {
			progressDialog.dismiss();
		}*/
		if (myProgressDialog != null
				&& myProgressDialog.isShowing()) {
			myProgressDialog.dismiss();
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
	
	public static void myStartActivity(Context context) {
		Intent actIntent = new Intent(context, EmployeeLoginActivity.class);
		context.startActivity(actIntent);
	}

	@Override
	public void backToLoginInterface() {}
}
