package wzp.project.android.elvtmtn.biz.impl;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeLoginListener;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.util.OkHttpUtils;

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
		
		// 创建请求体
		RequestBody requestBody = new FormEncodingBuilder()
			.add("username", employee.getUsername())
			.add("password", employee.getPassword())
			.build();
		Request request = OkHttpUtils.newRequestInstance(strUrl, null, requestBody);
		OkHttpUtils.enqueue(request, new Callback() {		
			@Override
			public void onResponse(Response response) throws IOException {
				if (response.isSuccessful()) {
					// 登录成功，解析服务器的响应体（json字符串），判断是否成功登录
					String strResponse = response.body().string();
					JSONObject jo = JSON.parseObject(strResponse);
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
				} else {
					listener.onServerException("服务器故障，响应失败！");
				}
			}
			
			@Override
			public void onFailure(Request request, IOException exception) {
				Log.e(tag, Log.getStackTraceString(exception));
				listener.onServerException("访问服务器失败\n请检查网络连接后重试");
			}
		});
	}

}
