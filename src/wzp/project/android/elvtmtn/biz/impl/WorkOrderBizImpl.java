package wzp.project.android.elvtmtn.biz.impl;

import java.util.Date;
import java.util.List;

import okhttp3.Call;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.listener.ISignleOrderSearchListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderCancelListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderFeedbackListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderReceiveListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.FailureTipMethod;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.myokhttp.MyOkHttpUtils;
import wzp.project.android.elvtmtn.util.myokhttp.MyStringCallback;

public class WorkOrderBizImpl implements IWorkOrderBiz {
	
	private static final String tag = "WorkOrderBizImpl";


	@Override
	public void getMaintainOrdersByCondition(long groupId, int workOrderState, final int pageNumber, int pageSize,
			final List<MaintainOrder> dataList, final IWorkOrderSearchListener listener) {
		StringBuilder strUrl = new StringBuilder(ProjectContants.basePath);
		strUrl.append("/maintainOrder/list");
		
		MyOkHttpUtils.get().url(strUrl.toString())
			.addHeader("token", MyApplication.token)
			.addParams("groupId", String.valueOf(groupId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.addParams("type", String.valueOf(workOrderState))
			.build()
			.execute(new MyStringCallback() {
				@Override
				public void onAfter() {
					listener.onAfter();
				}

				@Override
				public void onResponse(String response) {
					if (!TextUtils.isEmpty(response)) {
						List<MaintainOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<MaintainOrder>>() {});
						if (respDataList != null 
								&& respDataList.size() > 0) {
							if (1 == pageNumber) {
								dataList.clear();
							}
							dataList.addAll(respDataList);
							
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
						listener.onSearchFailure("服务器故障，请联系管理员！", FailureTipMethod.VIEW);
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请检查网络连接后重试", FailureTipMethod.VIEW);
				}

				@Override
				public void onError(Call call, Exception e, int respCode) {
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
	public void getFaultOrdersByCondition(long groupId, int workOrderState, final int pageNumber, int pageSize,
			final List<FaultOrder> dataList, final IWorkOrderSearchListener listener) {
		StringBuilder strUrl = new StringBuilder(ProjectContants.basePath);
		strUrl.append("/faultOrder/list");
		
		MyOkHttpUtils.get().url(strUrl.toString())
			.addHeader("token", MyApplication.token)
			.addParams("groupId", String.valueOf(groupId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.addParams("type", String.valueOf(workOrderState))
			.build()
			.execute(new MyStringCallback() {
				@Override
				public void onAfter() {
					listener.onAfter();
				}

				@Override
				public void onResponse(String response) {
					if (!TextUtils.isEmpty(response)) {
						List<FaultOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<FaultOrder>>() {});
						if (respDataList != null && respDataList.size() > 0) {
							if (1 == pageNumber) {
								dataList.clear();
							}
							dataList.addAll(respDataList);
							
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
					Log.e(tag, Log.getStackTraceString(e));
					if (respCode == 401) {
						listener.onSearchFailure("您的账号无效或已过期，请重新登录", FailureTipMethod.TOAST);
						listener.onBackToLoginInterface();
					} else {
						listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试", FailureTipMethod.VIEW);
					}
				}
			});
	}
	
	@Override
	public void getFaultOrderById(final String id, final ISignleOrderSearchListener listener) {
		StringBuilder strUrl = new StringBuilder(ProjectContants.basePath);
		strUrl.append("/faultOrder/detail");
		
		MyOkHttpUtils.get().url(strUrl.toString())
			.addHeader("token", MyApplication.token)
			.addParams("id", id)
			.build()
			.execute(new MyStringCallback() {
				@Override
				public void onResponse(String response) {
					if (!TextUtils.isEmpty(response)) {
						listener.onSearchSuccess(response);
					} else {
						listener.onSearchFailure("服务器故障，请联系管理员");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");	
				}

				@Override
				public void onError(Call call, Exception e, int respCode) {
					Log.e(tag, Log.getStackTraceString(e));
					if (respCode == 401) {
						listener.onSearchFailure("您的账号无效或已\n过期，请重新登录");
						listener.onBackToLoginInterface();
					} else {
						listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");
					}
				}
			});
	}
	
	
	@Override
	public void getReceivedMaintainOrdersByCondition(long employeeId,
			final int pageNumber, int pageSize, final List<MaintainOrder> dataList,
			final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/maintainOrder/accept/list";
		
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("employeeId", String.valueOf(employeeId))
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
					if (!TextUtils.isEmpty(response)) {
						List<MaintainOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<MaintainOrder>>() {});
						if (respDataList != null && respDataList.size() > 0) {
							if (1 == pageNumber) {
								dataList.clear();
							}
							dataList.addAll(respDataList);
							
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
	public void getReceivedFaultOrdersByCondition(long employeeId,
			final int pageNumber, int pageSize, final List<FaultOrder> dataList,
			final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/faultOrder/accept/list";
		
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("employeeId", String.valueOf(employeeId))
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
						List<FaultOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<FaultOrder>>() {});
						if (respDataList != null && respDataList.size() > 0) {
							if (1 == pageNumber) {
								dataList.clear();
							}
							dataList.addAll(respDataList);
							
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
	public void getSignedInFaultOrdersByCondition(long employeeId,
			final int pageNumber, int pageSize, final List<FaultOrder> faultOrderList,
			final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/faultOrder/feedback/list";
		
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("employeeId", String.valueOf(employeeId))
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
					if (!TextUtils.isEmpty(response)) {
						List<FaultOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<FaultOrder>>() {});
						if (respDataList != null && respDataList.size() > 0) {
							if (1 == pageNumber) {
								faultOrderList.clear();
							}
							faultOrderList.addAll(respDataList);
							
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
	public void getSignedInMaintainOrdersByCondition(long employeeId,
			final int pageNumber, int pageSize, final List<MaintainOrder> maintainOrderList,
			final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/maintainOrder/feedback/list";
		
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("employeeId", String.valueOf(employeeId))
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
					if (!TextUtils.isEmpty(response)) {
						List<MaintainOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<MaintainOrder>>() {});
						if (respDataList != null && respDataList.size() > 0) {
							if (1 == pageNumber) {
								maintainOrderList.clear();
							}
							maintainOrderList.addAll(respDataList);
							
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
	public void getFaultOrdersByElevatorId(long elevatorRecordId, final int pageNumber, int pageSize,
			final List<FaultOrder> faultOrderList, final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/faultOrder/history";
		
		MyOkHttpUtils.get().url(url)
			.addHeader("token", MyApplication.token)
			.addParams("elevatorRecordId", String.valueOf(elevatorRecordId))
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
					if (!TextUtils.isEmpty(response)) {
						List<FaultOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<FaultOrder>>() {});
						if (respDataList != null && respDataList.size() > 0) {
							if (1 == pageNumber) {
								faultOrderList.clear();
							}
							faultOrderList.addAll(respDataList);
							
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
	public void receiveOrder(int workOrderType, Long workOrderId, Long employeeId, 
			final IWorkOrderReceiveListener listener) {
		StringBuilder url = new StringBuilder(ProjectContants.basePath);
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			url.append("/maintainOrder/accept");
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			url.append("/faultOrder/accept");
		} else {
			throw new IllegalArgumentException("工单类型有误");
		}
		
		MyOkHttpUtils.post().url(url.toString())
			.addHeader("token", MyApplication.token)
			.addParams("id", String.valueOf(workOrderId))
			.addParams("employeeId", String.valueOf(employeeId))
			.build()
			.execute(new MyStringCallback() {		
				@Override
				public void onResponse(String response) {
					JSONObject jo = JSON.parseObject(response);
					String result = jo.getString("result");
					
					if (!TextUtils.isEmpty(result)) {
						if (result.equals("success")) {
							listener.onReceiveSuccess(jo.getDate("receivingTime"));
						} else if (result.equals("repeat")) {
							listener.onReceiveFailure("重复接单，接单失败！");
						} else if (result.equals("employeeNotExist")) {
							listener.onReceiveFailure("员工不存在，接单失败！");
						} else if (response.equals("faultOrderNotExist")) {
							listener.onReceiveFailure("工单不存在，接单失败！");
						} else if (response.equals("maintainOrderNotExist")) {
							listener.onReceiveFailure("工单不存在，接单失败！");
						}
					} else {
						listener.onReceiveFailure("服务器故障，请联系管理员");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onReceiveFailure("服务器正在打盹，请\n检查网络连接后重试");					
				}
				
				@Override
				public void onError(Call call, Exception e, int respCode) {
					Log.e(tag, Log.getStackTraceString(e));
					if (respCode == 401) {
						listener.onReceiveFailure("您的账号无效或已过期，请重新登录");
						listener.onBackToLoginInterface();
					} else {
						listener.onReceiveFailure("服务器正在打盹，请\n检查网络连接后重试");
					}
				}
			});
	}

	@Override
	public void cancelReceiveOrder(int workOrderType, Long workOrderId,
			Long employeeId, final IWorkOrderCancelListener listener) {
		StringBuilder url = new StringBuilder(ProjectContants.basePath);
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			url.append("/maintainOrder/cancel");
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			url.append("/faultOrder/cancel");
		} else {
			throw new IllegalArgumentException("工单类型有误");
		}
		
		MyOkHttpUtils.post().url(url.toString())
			.addHeader("token", MyApplication.token)
			.addParams("id", String.valueOf(workOrderId))
			.addParams("employeeId", String.valueOf(employeeId))
			.build()
			.execute(new MyStringCallback() {				
				@Override
				public void onResponse(String response) {
					Log.i(response, response);
					JSONObject jo = JSON.parseObject(response);
					String result = jo.getString("result");
					
					if (!TextUtils.isEmpty(result)) {
						if (result.equals("success")) {
							listener.onCancelSuccess();
						} else if (result.equals("notMatch")) {
							listener.onCancelFailure("工单和员工不匹配，取消接单失败！");
						} else if (result.equals("notAccept")) {
							listener.onCancelFailure("工单并未被接单，取消接单失败！");
						} else if (result.equals("employeeNotExist")) {
							listener.onCancelFailure("员工不存在，取消接单失败！");
						} else if (result.equals("maintainOrderNotExist")) {
							listener.onCancelFailure("工单不存在，取消接单失败！");
						} else if (result.equals("faultOrderNotExist")) {
							listener.onCancelFailure("工单不存在，取消接单失败！");
						}
					} else {
						listener.onCancelFailure("服务器故障，请联系管理员");		
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onCancelFailure("服务器正在打盹，请\n检查网络连接后重试");			
				}	
				
				@Override
				public void onError(Call call, Exception e, int respCode) {
					Log.e(tag, Log.getStackTraceString(e));
					if (respCode == 401) {
						listener.onCancelFailure("您的账号无效或已过期，请重新登录");
						listener.onBackToLoginInterface();
					} else {
						listener.onCancelFailure("服务器正在打盹，请\n检查网络连接后重试");
					}
				}
			});
	}

	@Override
	public void feedbackOrder(int workOrderType, Long workOrderId,
			Long employeeId, String faultReason, boolean isDone, String remark,
			String signOutAddress, String finishedItems, final IWorkOrderFeedbackListener listener) {
		String url = null;
		PostFormBuilder builder = MyOkHttpUtils.post()
			.addHeader("token", MyApplication.token)
			.addParams("id", String.valueOf(workOrderId))
			.addParams("employee.id", String.valueOf(employeeId))
			.addParams("remark", remark)
			.addParams("signOutAddress", signOutAddress);
		
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			url = ProjectContants.basePath + "/maintainOrder/feedback";
			builder.addParams("finished", String.valueOf(isDone))
				.addParams("finishedItems", finishedItems)
				.url(url);			
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			url = ProjectContants.basePath + "/faultOrder/feedback";
			builder.addParams("fixed", String.valueOf(isDone))
				.addParams("reason", faultReason)
				.url(url);
		} else {
			throw new IllegalArgumentException("工单类型有误");
		}
		
		builder.build().execute(new MyStringCallback() {			
			@Override
			public void onResponse(String response) {
				Log.i(response, response);
				JSONObject jo = JSON.parseObject(response);
				String result = jo.getString("result");
				
				if (!TextUtils.isEmpty(result)) {
					if (result.equals("success")) {
						listener.onFeedbackSuccess();
					} else if (result.equals("failed")) {
						listener.onFeedbackFailure("未知异常，工单反馈失败，请联系管理员");
					} else if (result.equals("employeeNotExist")) {
						listener.onFeedbackFailure("员工不存在，反馈失败！");	
					} else if (result.equals("maintainOrderNotExist")) {
						listener.onFeedbackFailure("工单不存在，反馈失败！");	
					} else if (result.equals("faultOrderNotExist")) {
						listener.onFeedbackFailure("工单不存在，反馈失败！");	
					}
				} else {
					listener.onFeedbackFailure("服务器故障，响应数据有误！");		
				}
			}
			
			@Override
			public void onError(Call call, Exception e) {
				Log.e(tag, Log.getStackTraceString(e));
				listener.onFeedbackFailure("服务器正在打盹，请\n检查网络连接后重试");
			}
			
			@Override
			public void onAfter() {
				listener.onAfter();
			}

			@Override
			public void onError(Call call, Exception e, int respCode) {
				Log.e(tag, "响应码为：" + respCode);
				Log.e(tag, Log.getStackTraceString(e));
				if (respCode == 401) {
					listener.onFeedbackFailure("您的账号无效或已过期，请重新登录");
					listener.onBackToLoginInterface();
				} else {
					listener.onFeedbackFailure("服务器正在打盹，请\n检查网络连接后重试");
				}
			}
		});
	}

	@Override
	public void sortMaintainOrderByFinalTimeIncrease(
			List<MaintainOrder> maintainOrderList) {
		int length = maintainOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		/*
		 * 按id号的顺序排列
		 */
		MaintainOrder maintainOrderA = null;
		MaintainOrder maintainOrderB = null;
		long idA;
		long idB;
		int minElementIndex;
		
		/*
		 * 选择排序
		 */
		for (int i=0; i<length; i++) {
			minElementIndex = i;
			idA =  maintainOrderList.get(minElementIndex).getId();
			for (int j=length-1; j>i; j--) {
				idB = maintainOrderList.get(j).getId();
				
				if (idA > idB) {
					minElementIndex = j;
					idA = idB;
				}
			}
			
			maintainOrderA = maintainOrderList.get(i);
			maintainOrderB = maintainOrderList.get(minElementIndex);
			
			maintainOrderList.remove(i);
			maintainOrderList.add(i, maintainOrderB);
			maintainOrderList.remove(minElementIndex);
			maintainOrderList.add(minElementIndex, maintainOrderA);
			
			maintainOrderA = null;
			maintainOrderB = null;
		}
	}
	
	@Override
	public void sortMaintainOrderByFinalTimeDecrease(
			List<MaintainOrder> maintainOrderList) {
		int length = maintainOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		/*
		 * 按id号的逆序排列
		 */
		MaintainOrder maintainOrderA = null;
		MaintainOrder maintainOrderB = null;
		long idA;
		long idB;
		int maxElementIndex;
		
		for (int i=0; i<length; i++) {
			maxElementIndex = i;
			idA =  maintainOrderList.get(maxElementIndex).getId();
			for (int j=length-1; j>i; j--) {
				idB = maintainOrderList.get(j).getId();
				
				if (idA < idB) {
					maxElementIndex = j;
					idA =  idB;
				}
			}
			
			maintainOrderA = maintainOrderList.get(i);
			maintainOrderB = maintainOrderList.get(maxElementIndex);
			
			maintainOrderList.remove(i);
			maintainOrderList.add(i, maintainOrderB);
			maintainOrderList.remove(maxElementIndex);
			maintainOrderList.add(maxElementIndex, maintainOrderA);
			
			maintainOrderA = null;
			maintainOrderB = null;
		}
	}

	@Override
	public void sortFaultOrderByOccurredTimeIncrease(
			List<FaultOrder> faultOrderList) {
		int length = faultOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		/*
		 * 按id号的顺序排列
		 */
		FaultOrder faultOrderA = null;
		FaultOrder faultOrderB = null;
		long idA;
		long idB;
		int minElementIndex;

		
		/*
		 * 选择排序
		 */
		for (int i=0; i<length; i++) {
			minElementIndex = i;
			idA =  faultOrderList.get(minElementIndex).getId();
			for (int j=length-1; j>i; j--) {
				idB = faultOrderList.get(j).getId();
				
				if (idA > idB) {
					minElementIndex = j;
					idA =  idB;
				}
			}
			
			faultOrderA = faultOrderList.get(i);
			faultOrderB = faultOrderList.get(minElementIndex);
			
			faultOrderList.remove(i);
			faultOrderList.add(i, faultOrderB);
			faultOrderList.remove(minElementIndex);
			faultOrderList.add(minElementIndex, faultOrderA);
			
			faultOrderA = null;
			faultOrderB = null;
		}
	}

	@Override
	public void sortFaultOrderByOccurredTimeDecrease(
			List<FaultOrder> faultOrderList) {
		int length = faultOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		/*
		 * 按id号的逆序排列
		 */
		FaultOrder faultOrderA = null;
		FaultOrder faultOrderB = null;
		long idA;
		long idB;
		int minElementIndex;

		
		/*
		 * 选择排序
		 */
		for (int i=0; i<length; i++) {
			minElementIndex = i;
			idA =  faultOrderList.get(minElementIndex).getId();
			for (int j=length-1; j>i; j--) {
				idB = faultOrderList.get(j).getId();
				
				if (idA < idB) {
					minElementIndex = j;
					idA =  idB;
				}
			}
			
			faultOrderA = faultOrderList.get(i);
			faultOrderB = faultOrderList.get(minElementIndex);
			
			faultOrderList.remove(i);
			faultOrderList.add(i, faultOrderB);
			faultOrderList.remove(minElementIndex);
			faultOrderList.add(minElementIndex, faultOrderA);
			
			faultOrderA = null;
			faultOrderB = null;
		}
	}

	@Override
	public void sortMaintainOrderByReceivingTime(
			List<MaintainOrder> maintainOrderList) {
		int length = maintainOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		MaintainOrder maintainOrderA = null;
		MaintainOrder maintainOrderB = null;
		long receivingTimeA;
		long receivingTimeB;
		Date tempDate;
		int maxElementIndex;
		
		for (int i=0; i<length; i++) {
			maxElementIndex = i;
			tempDate = maintainOrderList.get(maxElementIndex).getReceivingTime();
			if (tempDate != null) {
				receivingTimeA = tempDate.getTime();
			} else {
				receivingTimeA = 0;
			}
			tempDate = null;
			for (int j=length-1; j>i; j--) {
				tempDate = maintainOrderList.get(j).getReceivingTime();
				if (tempDate != null) {
					receivingTimeB = tempDate.getTime();
				} else {
					receivingTimeB = 0;
				}
				tempDate = null;
				
				if (receivingTimeA < receivingTimeB) {
					maxElementIndex = j;
					receivingTimeA = receivingTimeB;
				}
			}
			
			maintainOrderA = maintainOrderList.get(i);
			maintainOrderB = maintainOrderList.get(maxElementIndex);
			
			maintainOrderList.remove(i);
			maintainOrderList.add(i, maintainOrderB);
			maintainOrderList.remove(maxElementIndex);
			maintainOrderList.add(maxElementIndex, maintainOrderA);
			
			maintainOrderA = null;
			maintainOrderB = null;
		}
		
	}

	@Override
	public void sortFaultOrderByReceivingTime(List<FaultOrder> faultOrderList) {
		int length = faultOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		FaultOrder faultOrderA = null;
		FaultOrder faultOrderB = null;
		long receivingTimeA;
		long receivingTimeB;
		Date tempDate;
		int maxElementIndex;
		
		for (int i=0; i<length; i++) {
			maxElementIndex = i;
			tempDate = faultOrderList.get(maxElementIndex).getReceivingTime();
			if (tempDate != null) {
				receivingTimeA = tempDate.getTime();
			} else {
				receivingTimeA = 0;
			}
			tempDate = null;
			for (int j=length-1; j>i; j--) {
				tempDate = faultOrderList.get(j).getReceivingTime();
				if (tempDate != null) {
					receivingTimeB = tempDate.getTime();
				} else {
					receivingTimeB = 0;
				}
				tempDate = null;
				
				if (receivingTimeA < receivingTimeB) {
					maxElementIndex = j;
					receivingTimeA = receivingTimeB;
				}
			}
			
			faultOrderA = faultOrderList.get(i);
			faultOrderB = faultOrderList.get(maxElementIndex);
			
			faultOrderList.remove(i);
			faultOrderList.add(i, faultOrderB);
			faultOrderList.remove(maxElementIndex);
			faultOrderList.add(maxElementIndex, faultOrderA);
			
			faultOrderA = null;
			faultOrderB = null;
		}
	}

	@Override
	public void sortMaintainOrderByFinishedTimeIncrease(
			List<MaintainOrder> maintainOrderList) {
		int length = maintainOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		MaintainOrder maintainOrderA = null;
		MaintainOrder maintainOrderB = null;
		long finishedTimeA;
		long finishedTimeB;
		Date tempDate;
		int minElementIndex;
		
		for (int i=0; i<length; i++) {
			minElementIndex = i;
			tempDate = maintainOrderList.get(minElementIndex).getSignOutTime();
			if (tempDate != null) {
				finishedTimeA = tempDate.getTime();
			} else {
				finishedTimeA = 0;
			}
			tempDate = null;
			for (int j=length-1; j>i; j--) {
				tempDate = maintainOrderList.get(j).getSignOutTime();
				if (tempDate != null) {
					finishedTimeB = tempDate.getTime();
				} else {
					finishedTimeB = 0;
				}
				tempDate = null;
				
				if (finishedTimeA > finishedTimeB) {
					minElementIndex = j;
					finishedTimeA = finishedTimeB;
				}
			}
			
			maintainOrderA = maintainOrderList.get(i);
			maintainOrderB = maintainOrderList.get(minElementIndex);
			
			maintainOrderList.remove(i);
			maintainOrderList.add(i, maintainOrderB);
			maintainOrderList.remove(minElementIndex);
			maintainOrderList.add(minElementIndex, maintainOrderA);
			
			maintainOrderA = null;
			maintainOrderB = null;
		}
	}

	@Override
	public void sortMaintainOrderByFinishedTimeDecrease(
			List<MaintainOrder> maintainOrderList) {
		int length = maintainOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		MaintainOrder maintainOrderA = null;
		MaintainOrder maintainOrderB = null;
		long finishedTimeA;
		long finishedTimeB;
		Date tempDate;
		int maxElementIndex;
		
		for (int i=0; i<length; i++) {
			maxElementIndex = i;
			tempDate = maintainOrderList.get(maxElementIndex).getSignOutTime();
			if (tempDate != null) {
				finishedTimeA = tempDate.getTime();
			} else {
				finishedTimeA = 0;
			}
			tempDate = null;
			for (int j=length-1; j>i; j--) {
				tempDate = maintainOrderList.get(j).getSignOutTime();
				if (tempDate != null) {
					finishedTimeB = tempDate.getTime();
				} else {
					finishedTimeB = 0;
				}
				tempDate = null;
				
				if (finishedTimeA < finishedTimeB) {
					maxElementIndex = j;
					finishedTimeA = finishedTimeB;
				}
			}
			
			maintainOrderA = maintainOrderList.get(i);
			maintainOrderB = maintainOrderList.get(maxElementIndex);
			
			maintainOrderList.remove(i);
			maintainOrderList.add(i, maintainOrderB);
			maintainOrderList.remove(maxElementIndex);
			maintainOrderList.add(maxElementIndex, maintainOrderA);
			
			maintainOrderA = null;
			maintainOrderB = null;
		}
	}

	@Override
	public void sortFaultOrderByFinishedTimeIncrease(
			List<FaultOrder> faultOrderList) {
		int length = faultOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		FaultOrder faultOrderA = null;
		FaultOrder faultOrderB = null;
		long finishedTimeA;
		long finishedTimeB;
		Date tempDate;
		int minElementIndex;
		
		for (int i=0; i<length; i++) {
			minElementIndex = i;
			tempDate = faultOrderList.get(minElementIndex).getSignOutTime();
			if (tempDate != null) {
				finishedTimeA = tempDate.getTime();
			} else {
				finishedTimeA = 0;
			}
			tempDate = null;
			for (int j=length-1; j>i; j--) {
				tempDate = faultOrderList.get(j).getSignOutTime();
				if (tempDate != null) {
					finishedTimeB = tempDate.getTime();
				} else {
					finishedTimeB = 0;
				}
				tempDate = null;
				
				if (finishedTimeA > finishedTimeB) {
					minElementIndex = j;
					finishedTimeA = finishedTimeB;
				}
			}
			
			faultOrderA = faultOrderList.get(i);
			faultOrderB = faultOrderList.get(minElementIndex);
			
			faultOrderList.remove(i);
			faultOrderList.add(i, faultOrderB);
			faultOrderList.remove(minElementIndex);
			faultOrderList.add(minElementIndex, faultOrderA);
			
			faultOrderA = null;
			faultOrderB = null;
		}
		
	}

	@Override
	public void sortFaultOrderByFinishedTimeDecrease(
			List<FaultOrder> faultOrderList) {
		int length = faultOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		FaultOrder faultOrderA = null;
		FaultOrder faultOrderB = null;
		long finishedTimeA;
		long finishedTimeB;
		Date tempDate;
		int maxElementIndex;
		
		for (int i=0; i<length; i++) {
			maxElementIndex = i;
			tempDate = faultOrderList.get(maxElementIndex).getSignOutTime();
			if (tempDate != null) {
				finishedTimeA = tempDate.getTime();
			} else {
				finishedTimeA = 0;
			}
			tempDate = null;
			for (int j=length-1; j>i; j--) {
				tempDate = faultOrderList.get(j).getSignOutTime();
				if (tempDate != null) {
					finishedTimeB = tempDate.getTime();
				} else {
					finishedTimeB = 0;
				}
				tempDate = null;
				
				if (finishedTimeA < finishedTimeB) {
					maxElementIndex = j;
					finishedTimeA = finishedTimeB;
				}
			}
			
			faultOrderA = faultOrderList.get(i);
			faultOrderB = faultOrderList.get(maxElementIndex);
			
			faultOrderList.remove(i);
			faultOrderList.add(i, faultOrderB);
			faultOrderList.remove(maxElementIndex);
			faultOrderList.add(maxElementIndex, faultOrderA);
			
			faultOrderA = null;
			faultOrderB = null;
		}
	}

	@Override
	public void sortMaintainOrderBySignInTime(
			List<MaintainOrder> maintainOrderList) {
		int length = maintainOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		MaintainOrder maintainOrderA = null;
		MaintainOrder maintainOrderB = null;
		long signInTimeA;
		long signInTimeB;
		Date tempDate;
		int maxElementIndex;
		
		for (int i=0; i<length; i++) {
			maxElementIndex = i;
			tempDate = maintainOrderList.get(maxElementIndex).getSignInTime();
			if (tempDate != null) {
				signInTimeA = tempDate.getTime();
			} else {
				signInTimeA = 0;
			}
			tempDate = null;
			for (int j=length-1; j>i; j--) {
				tempDate = maintainOrderList.get(j).getSignInTime();
				if (tempDate != null) {
					signInTimeB = tempDate.getTime();
				} else {
					signInTimeB = 0;
				}
				tempDate = null;
				
				if (signInTimeA < signInTimeB) {
					maxElementIndex = j;
					signInTimeA = signInTimeB;
				}
			}
			
			maintainOrderA = maintainOrderList.get(i);
			maintainOrderB = maintainOrderList.get(maxElementIndex);
			
			maintainOrderList.remove(i);
			maintainOrderList.add(i, maintainOrderB);
			maintainOrderList.remove(maxElementIndex);
			maintainOrderList.add(maxElementIndex, maintainOrderA);
			
			maintainOrderA = null;
			maintainOrderB = null;
		}
	}

	@Override
	public void sortFaultOrderBySignInTime(List<FaultOrder> faultOrderList) {
		int length = faultOrderList.size();
		
		if (length == 0) {
			return;
		}
		
		FaultOrder faultOrderA = null;
		FaultOrder faultOrderB = null;
		long signInTimeA;
		long signInTimeB;
		Date tempDate;
		int maxElementIndex;
		
		for (int i=0; i<length; i++) {
			maxElementIndex = i;
			tempDate = faultOrderList.get(maxElementIndex).getSignInTime();
			if (tempDate != null) {
				signInTimeA = tempDate.getTime();
			} else {
				signInTimeA = 0;
			}
			tempDate = null;
			for (int j=length-1; j>i; j--) {
				tempDate = faultOrderList.get(j).getSignInTime();
				if (tempDate != null) {
					signInTimeB = tempDate.getTime();
				} else {
					signInTimeB = 0;
				}
				tempDate = null;
				
				if (signInTimeA < signInTimeB) {
					maxElementIndex = j;
					signInTimeA = signInTimeB;
				}
			}
			
			faultOrderA = faultOrderList.get(i);
			faultOrderB = faultOrderList.get(maxElementIndex);
			
			faultOrderList.remove(i);
			faultOrderList.add(i, faultOrderB);
			faultOrderList.remove(maxElementIndex);
			faultOrderList.add(maxElementIndex, faultOrderA);
			
			faultOrderA = null;
			faultOrderB = null;
		}
	}
}
