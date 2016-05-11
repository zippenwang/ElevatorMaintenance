package wzp.project.android.elvtmtn.biz.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Request;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.igexin.sdk.PushManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeLoginListener;
import wzp.project.android.elvtmtn.biz.IEmployeeSignInListener;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.util.MyApplication;

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
		
		String cid = MyApplication.getCid();
		if (cid.equals("")) {
			listener.onAfter();
			listener.onLoginFailure("还在初始化参数，请重试...");
			return;
		}
		
		String strUrl = ProjectContants.basePath + "/login";
		
		OkHttpUtils.post().url(strUrl)
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

}
