package wzp.project.android.elvtmtn.activity.impl;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushManager;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IEmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.presenter.EmployeeLoginPresenter;
import wzp.project.android.elvtmtn.util.ActivityCollector;
import wzp.project.android.elvtmtn.util.ClearAllEditText;
import wzp.project.android.elvtmtn.util.DESUtil;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.MyProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

public class EmployeeLoginActivity extends BaseActivity implements IEmployeeLoginActivity {

	private ClearAllEditText caedtUsername;
	private ClearAllEditText caedtPassword;
	private Button btnLogin;
	private CheckBox cbIsRemember;
	private CheckBox cbIsAutoLogin;
	
	// 用于测试的跳转按钮
	private Button btnIntoNext;
	
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
		
		caedtUsername = (ClearAllEditText) findViewById(R.id.caedt_username);
		caedtPassword = (ClearAllEditText) findViewById(R.id.caedt_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		cbIsRemember = (CheckBox) findViewById(R.id.cb_isRemember);
		cbIsAutoLogin = (CheckBox) findViewById(R.id.cb_isAutoLogin);
		
		myProgressDialog = new MyProgressDialog(this);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
		boolean isRemember = preferences.getBoolean("isRemember", false);
		if (isRemember) {
			cbIsRemember.setChecked(true);
//			caedtUsername.setText(preferences.getString("username", ""));
			String username = "";
			try {
				username = ProjectContants.desUtil.decrypt(preferences.getString("username", ""));
			} catch (UnsupportedEncodingException e) {
				Log.e(tag, Log.getStackTraceString(e));
				throw new IllegalArgumentException("解密出现异常");
			}
			caedtUsername.setText(username);
			
//			caedtPassword.setText(preferences.getString("password", ""));
//			Drawable[] drawables = caedtPassword.getCompoundDrawables();
//			caedtPassword.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
		} else {
			cbIsRemember.setChecked(false);
		}
		boolean isAutoLogin = preferences.getBoolean("isAutoLogin", false);
		if (isAutoLogin) {
			cbIsAutoLogin.setChecked(true);
		} else {
			cbIsAutoLogin.setChecked(false);
		}
		
		btnLogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String username = caedtUsername.getText().toString();
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
//		editor.putString("username", caedtUsername.getEditableText().toString());
		String username = caedtUsername.getEditableText().toString();
		try {
			username = ProjectContants.desUtil.encrypt(username);
		} catch (UnsupportedEncodingException e) {
			Log.e(tag, Log.getStackTraceString(e));
			throw new IllegalArgumentException("加密出现异常");
		}
		editor.putString("username", username);
		editor.putBoolean("isRemember", cbIsRemember.isChecked());
		editor.putBoolean("isAutoLogin", cbIsAutoLogin.isChecked());
		editor.putString("employeeJson", JSON.toJSONString(employee));
		editor.putString("token", MyApplication.token);
		editor.commit();
		
		/*
		 * 若登录成功，则跳转至主界面
		 */
		finish();
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
	
	public static void myForceStartActivity(Context context) {
		ActivityCollector.finishAll();
		SharedPreferences.Editor editor =
				PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
//		editor.clear();
		editor.putString("token", "");
		editor.commit();
		MyApplication.token = null;
		myStartActivity(context);
	}

	@Override
	public void backToLoginInterface() {}
}
