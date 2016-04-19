package wzp.project.android.elvtmtn.biz.impl;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Request;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeLoginListener;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;

public class EmployeeBizImpl implements IEmployeeBiz {
	
	private static final String tag = "UserBizImpl";

	@Override
	public void login(final Employee employee, final IEmployeeLoginListener listener) {
		/*
		 * 用于测试的代码，模拟访问服务器费时的操作
		 */
		/*new Timer().schedule(new TimerTask() {		
			@Override
			public void run() {
				if (employee.getUsername().equals("wzp") && employee.getPassword().equals("1234")) {
					listener.onLoginSuccess();
				} else {
					listener.onLoginFailure();
				}
			}
		}, 3000);*/

		
		String strUrl = ProjectContants.basePath + "/login";
		
		OkHttpUtils.post().url(strUrl)
			.addParams("username", employee.getUsername())
			.addParams("password", employee.getPassword())
			.build()
			.execute(new StringCallback() {
				
				@Override
				public void onBefore(Request request) {
					listener.onBefore();
				}

				@Override
				public void onAfter() {
					listener.onAfter();
				}

				@Override
				public void onResponse(String response) {
					JSONObject jo = JSON.parseObject(response);
					String result = jo.getString("result");
					
					if (!TextUtils.isEmpty(result)) {
						if (result.equals("success")) {
							listener.onLoginSuccess();
						} else if (result.equals("failed")) {
							listener.onLoginFailure();
						}
					} else {
						listener.onServerException("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onServerException("访问服务器失败\n请检查网络连接后重试");					
				}
			});
	}

}
