package wzp.project.android.elvtmtn.activity.impl;

import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.util.MyApplication;

public class WelcomeActivity extends BaseActivity {
	
	private SharedPreferences preferences;
	private String token;
	private boolean isAutoLogin;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
		token = preferences.getString("token", "");
		isAutoLogin = preferences.getBoolean("isAutoLogin", false);
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				/*if (token.equals("")) {
					EmployeeLoginActivity.myStartActivity(WelcomeActivity.this);
				} else {
					MyApplication.token = token;
					MainActivity.myStartActivity(WelcomeActivity.this);
				}*/
				if (isAutoLogin && !token.equals("")) {
					MyApplication.token = token;
					MainActivity.myStartActivity(WelcomeActivity.this);
				} else {
					EmployeeLoginActivity.myStartActivity(WelcomeActivity.this);
				}
				
				finish();
			}
		}, 3000);
	}
}
