package wzp.project.android.elvtmtn.activity.impl;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.util.MyApplication;

public class WelcomeActivity extends BaseActivity {
	
	private SharedPreferences preferences;
	private String token;
	private boolean isAutoLogin;
	private ConnectivityManager mConnectivityManager;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
		token = preferences.getString("token", "");
		isAutoLogin = preferences.getBoolean("isAutoLogin", false);
		
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		
		/*
		 * 此处可以访问一次服务器，确定服务器是否正常连接
		 */
		
		
		/*
		 * 定时任务，3s后执行
		 */
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				/*
				 * 检查网络连接
				 */
				if (null == mConnectivityManager.getActiveNetworkInfo()) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WelcomeActivity.this, "网络异常，检查网络后重试", Toast.LENGTH_LONG).show();
						}
					});
				}
				
				/*
				 * 判断接下来应该进入主界面还是登录界面
				 */
				if (isAutoLogin && !token.equals("")) {
					MyApplication.token = token;
					MainActivity.myStartActivity(WelcomeActivity.this);
				} else {
					EmployeeLoginActivity.myStartActivity(WelcomeActivity.this);
				}
				
				finish();			// 务必销毁当前Activity
			}
		}, 3000);
	}
}
