package wzp.project.android.elvtmtn.biz.impl;

import java.util.List;

import okhttp3.Call;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import wzp.project.android.elvtmtn.activity.impl.EmployeeLoginActivity;
import wzp.project.android.elvtmtn.biz.IElevatorRecordBiz;
import wzp.project.android.elvtmtn.biz.listener.IElevatorRecordSearchListener;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.helper.contant.FailureTipMethod;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.myokhttp.MyOkHttpUtils;
import wzp.project.android.elvtmtn.util.myokhttp.MyStringCallback;

public class ElevatorRecordBizImpl implements IElevatorRecordBiz {

	private static final String tag = "ElevatorRecordBizImpl";
	
	@Override
	public void getAllElevatorRecords(final int pageNumber, int pageSize, 
			final List<ElevatorRecord> elevatorRecordList, final IElevatorRecordSearchListener listener) {
		String url = ProjectContants.basePath + "/elevatorRecord/list";
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.build()
			.execute(new MyStringCallback() {				
				@Override
				public void onAfter() {
					listener.onAfter();
				}

				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					
					if (!TextUtils.isEmpty(response)) {
						List<ElevatorRecord> respDataList = JSON.parseObject(response, 
								new TypeReference<List<ElevatorRecord>>() {});
						if (respDataList != null && respDataList.size() > 0) {							
							if (1 == pageNumber) {
								elevatorRecordList.clear();
							}
							elevatorRecordList.addAll(respDataList);
							
							if (respDataList.size() % ProjectContants.PAGE_SIZE == 0) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_UNCOMPLETE);
							} else {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						} else {
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，请联系管理员", FailureTipMethod.VIEW);
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请检查网络连接后重试", FailureTipMethod.VIEW);
				}

				@Override
				public void onError(Call call, Exception e, int respCode) {
					Log.e(tag, "响应码为：" + respCode);
					Log.e(tag, Log.getStackTraceString(e));
					if (respCode == 401) {
						listener.onSearchFailure("您的账号无效或已\n过期，请重新登录", FailureTipMethod.TOAST);
						listener.onBackToLoginInterface();
					} else {
						listener.onSearchFailure("服务器正在打盹，请检查网络连接后重试", FailureTipMethod.VIEW);
					}
				}
			});
	}
	
	@Override
	public void getAllElevatorRecords(final long groupId, final int pageNumber, int pageSize, 
			final List<ElevatorRecord> elevatorRecordList, final IElevatorRecordSearchListener listener) {
		String url = ProjectContants.basePath + "/elevatorRecord/list";
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("groupId", String.valueOf(groupId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.build()
			.execute(new MyStringCallback() {				
				@Override
				public void onAfter() {
					listener.onAfter();
				}

				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					
					if (!TextUtils.isEmpty(response)) {
						List<ElevatorRecord> respDataList = JSON.parseObject(response, 
								new TypeReference<List<ElevatorRecord>>() {});
						if (respDataList != null && respDataList.size() > 0) {							
							if (1 == pageNumber) {
								elevatorRecordList.clear();
							}
							elevatorRecordList.addAll(respDataList);
							
							if (respDataList.size() % ProjectContants.PAGE_SIZE == 0) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_UNCOMPLETE);
							} else {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						} else {
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，请联系管理员", FailureTipMethod.VIEW);
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请检查网络连接后重试", FailureTipMethod.VIEW);
				}
				
				@Override
				public void onError(Call call, Exception e, int respCode) {
					Log.e(tag, "响应码为：" + respCode);
					Log.e(tag, Log.getStackTraceString(e));
					if (respCode == 401) {
						listener.onSearchFailure("您的账号无效或已\n过期，请重新登录", FailureTipMethod.TOAST);
						listener.onBackToLoginInterface();
					} else {
						listener.onSearchFailure("服务器正在打盹，请检查网络连接后重试", FailureTipMethod.VIEW);
					}
				}
			});
	}

	@Override
	public void getElevatorRecordsByCondition(final int pageNumber, 
			int pageSize, String searchParam, 
			final List<ElevatorRecord> elevatorRecordList, 
			final IElevatorRecordSearchListener listener) {
		String url = ProjectContants.basePath + "/elevatorRecord/search";
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.addParams("searchParam", searchParam)
			.build()
			.execute(new MyStringCallback() {				
				@Override
				public void onAfter() {
					listener.onAfter();
				}

				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					
					if (!TextUtils.isEmpty(response)) {
						List<ElevatorRecord> respDataList = JSON.parseObject(response, 
								new TypeReference<List<ElevatorRecord>>() {});
						if (respDataList != null && respDataList.size() > 0) {							
							if (1 == pageNumber) {
								elevatorRecordList.clear();
							}
							elevatorRecordList.addAll(respDataList);
							
							if (respDataList.size() % ProjectContants.PAGE_SIZE == 0) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_UNCOMPLETE);
							} else {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						} else {
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，请联系管理员", FailureTipMethod.VIEW);
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请检查网络连接后重试", FailureTipMethod.VIEW);
				}

				@Override
				public void onError(Call call, Exception e, int respCode) {
					Log.e(tag, "响应码为：" + respCode);
					Log.e(tag, Log.getStackTraceString(e));
					if (respCode == 401) {
						listener.onSearchFailure("您的账号无效或已\n过期，请重新登录", FailureTipMethod.TOAST);
						listener.onBackToLoginInterface();
					} else {
						listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试", FailureTipMethod.VIEW);
					}
				}
			});
	}

}
