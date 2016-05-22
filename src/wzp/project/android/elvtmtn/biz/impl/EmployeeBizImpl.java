package wzp.project.android.elvtmtn.biz.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Request;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.igexin.sdk.PushManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeInfoSearchListener;
import wzp.project.android.elvtmtn.biz.IEmployeeLoginListener;
import wzp.project.android.elvtmtn.biz.IEmployeeSignInListener;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.myokhttp.MyOkHttpUtils;
import wzp.project.android.elvtmtn.util.myokhttp.MyStringCallback;

public class EmployeeBizImpl implements IEmployeeBiz {
	
	private static final String tag = "EmployeeBizImpl";

	@Override
	public void login(final Employee employee, final IEmployeeLoginListener listener) {
		String cid = MyApplication.getCid();
		if (cid.equals("")) {
			listener.onAfter();
			listener.onLoginFailure("还在初始化参数，请重试...");
			return;
		}
		
		String strUrl = ProjectContants.basePath + "/login";
		
		/*OkHttpUtils.post().url(strUrl)
			.addParams("username", employee.getUsername())
			.addParams("password", employee.getPassword())
			.addParams("cid", MyApplication.getCid())
			.build()
			.execute(new StringCallback() {
				@Override
				public void onAfter() {
					listener.onAfter();
				}

				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					JSONObject jo = JSON.parseObject(response);
					String result = jo.getString("result");
					
					if (!TextUtils.isEmpty(result)) {
						if (result.equals("success")) {
							listener.onLoginSuccess((JSON.toJavaObject((JSONObject) jo.get("employee"), Employee.class)));
						} else if (result.equals("failed")) {
							listener.onLoginFailure("用户名或密码错误！");
						}
					} else {
//						listener.onServerException("服务器故障，响应数据有误！");
						listener.onLoginFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
//					listener.onServerException("访问服务器失败，请\n检查网络连接后重试");
					listener.onLoginFailure("访问服务器失败，请\n检查网络连接后重试");
				}
			});*/
		
		MyOkHttpUtils.post().url(strUrl)
			.addParams("username", employee.getUsername())
			.addParams("password", employee.getPassword())
			.addParams("cid", MyApplication.getCid())
			.build()
			.execute(new MyStringCallback() {
				@Override
				public void onAfter() {
					listener.onAfter();
				}
	
				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					JSONObject jo = JSON.parseObject(response);
					String result = jo.getString("result");
					
					if (!TextUtils.isEmpty(result)) {
						if (result.equals("success")) {
							MyApplication.token = jo.getString("token");
							Log.d(tag, MyApplication.token);
							listener.onLoginSuccess((JSON.toJavaObject((JSONObject) jo.get("employee"), Employee.class)));
						} else if (result.equals("failed")) {
							listener.onLoginFailure("用户名或密码错误！");
						}
					} else {
						listener.onLoginFailure("服务器故障，响应数据有误！");
					}
				}

				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onLoginFailure("访问服务器失败，请\n检查网络连接后重试");
				}
				
				@Override
				public void onError(Call call, Exception e, int respCode) {
					Log.e(tag, "响应码为：" + respCode);
					Log.e(tag, Log.getStackTraceString(e));
					listener.onLoginFailure("访问服务器失败，请\n检查网络连接后重试");
				}
			});
	}

	@Override
	public void signIn(int workOrderType, Long workOrderId,
			String signInAddress, final IEmployeeSignInListener listener) {
		String url = null;
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			url = ProjectContants.basePath + "/maintainOrder/signIn";
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			url = ProjectContants.basePath + "/faultOrder/signIn";
		} else {
			throw new IllegalArgumentException("工单类型有误");
		}
		
		OkHttpUtils.post().url(url)
			.addParams("id", String.valueOf(workOrderId))
			.addParams("signInAddress", signInAddress)
			.build()
			.execute(new StringCallback() {
				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					JSONObject jo = JSON.parseObject(response);
					String result = jo.getString("result");
					
					if (!TextUtils.isEmpty(result)) {
						if (result.equals("success")) {
							listener.onSignInSuccess();
						} else if (result.endsWith("OrderNotExist")) {
							// 表名工单不存在
							listener.onSignInFailure("签到失败！工单不存在！");
						}
					} else {
						listener.onSignInFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSignInFailure("服务器正在打盹\n请检查网络连接后重试");		
				}
			});
	}

	@Override
	public void getInfo(Long groupId, final IEmployeeInfoSearchListener listener) {
		String url = ProjectContants.basePath + "/group/employees";
		
		/*OkHttpUtils.get().url(url)
			.addParams("id", String.valueOf(groupId))
			.build()
			.execute(new StringCallback() {
				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					
					if (!TextUtils.isEmpty(response)) {
						List<Employee> employeeList = JSON.parseObject(response, 
								new TypeReference<List<Employee>>() {});
						listener.onSearchSuccess(employeeList);						
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");		
				}

				@Override
				public void onAfter() {
					listener.onAfter();
				}
				
				
			});*/
		
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("id", String.valueOf(groupId))
			.build()
			.execute(new MyStringCallback() {
				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					
					if (!TextUtils.isEmpty(response)) {
						List<Employee> employeeList = JSON.parseObject(response, 
								new TypeReference<List<Employee>>() {});
						listener.onSearchSuccess(employeeList);						
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");		
				}
				
				@Override
				public void onError(Call call, Exception e, int respCode) {
					Log.e(tag, "响应码为：" + respCode);
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");
					if (respCode == 401) {
						listener.onBackToLoginInterface();
					}
				}
	
				@Override
				public void onAfter() {
					listener.onAfter();
				}
			});
	}

}
