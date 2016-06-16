package wzp.project.android.elvtmtn.activity.impl;

import java.io.UnsupportedEncodingException;

import com.igexin.sdk.PushManager;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IEmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.presenter.EmployeeLoginPresenter;
import wzp.project.android.elvtmtn.util.ActivityCollector;
import wzp.project.android.elvtmtn.util.ClearAllEditText;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.MyProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class EmployeeLoginActivity extends BaseActivity implements IEmployeeLoginActivity {

	/*
	 * 控件定义
	 */
	private ClearAllEditText caedtUsername;
	private ClearAllEditText caedtPassword;
	private Button btnLogin;
	private CheckBox cbIsRemember;
	private CheckBox cbIsAutoLogin;
	private MyProgressDialog myProgressDialog;
	
	private EmployeeLoginPresenter userLoginPresenter = new EmployeeLoginPresenter(this);
	
	private PushManager pushManager = PushManager.getInstance();		// 个推核心类
	
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
		myProgressDialog.setMessage("正在访问服务器，请稍后...");
		myProgressDialog.setCancelable(true);
		
		SharedPreferences preferences = ProjectContants.preferences;
		boolean isRemember = preferences.getBoolean("isRemember", false);
		if (isRemember) {
			cbIsRemember.setChecked(true);
			
			/*
			 * 获取username，解密后显示在输入框中
			 */
			String username = "";
			try {
				username = ProjectContants.desUtil.decrypt(preferences.getString("username", ""));
			} catch (UnsupportedEncodingException e) {
				Log.e(tag, Log.getStackTraceString(e));
//				throw new IllegalArgumentException("解密出现异常");
			}
			caedtUsername.setText(username);
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
				/*
				 * 暂时单纯判断输入是否为空
				 */
				if (TextUtils.isEmpty(username.trim())
						|| TextUtils.isEmpty(password.trim())) {
					Toast.makeText(EmployeeLoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}		
				
				userLoginPresenter.login(username, password);
			}
		});
	}

	@Override
	public void loginSuccess(Employee employee) {
		SharedPreferences.Editor editor 
			= ProjectContants.preferences.edit();
		editor.putLong("employeeId", employee.getId());
		editor.putLong("groupId", employee.getGroup().getId());
		String username = caedtUsername.getEditableText().toString();
		try {
			username = ProjectContants.desUtil.encrypt(username);
		} catch (UnsupportedEncodingException e) {
			Log.e(tag, Log.getStackTraceString(e));
//			throw new IllegalArgumentException("加密出现异常");
			username = "";
		}
		editor.putString("username", username);
		editor.putBoolean("isRemember", cbIsRemember.isChecked());
		editor.putBoolean("isAutoLogin", cbIsAutoLogin.isChecked());
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
	
	/**
	 * 正常启动登录界面的方法
	 * @param context 当前所在的Context实例
	 */
	public static void myStartActivity(Context context) {
		Intent actIntent = new Intent(context, EmployeeLoginActivity.class);
		context.startActivity(actIntent);
	}
	
	/**
	 * 强制跳转登陆界面的方法
	 * @param context 当前所在的Context实例
	 */
	public static void myForceStartActivity(Context context) {
		// 强制跳转至登录界面，需要销毁所有还未销毁的Activity
		ActivityCollector.finishAll();
		
		SharedPreferences.Editor editor = ProjectContants.editor;
//		editor.clear();
		editor.putString("token", "");
		editor.putString("employeeId", "");
		editor.putString("groupId", "");
		editor.commit();
		MyApplication.token = null;
		myStartActivity(context);
	}

	@Override
	public void backToLoginInterface() {}
}
