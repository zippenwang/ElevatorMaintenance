package wzp.project.android.elvtmtn.biz.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import wzp.project.android.elvtmtn.activity.impl.FaultOrderDetailActivity;
import wzp.project.android.elvtmtn.biz.ISignleOrderSearchListener;
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.IWorkOrderCancelListener;
import wzp.project.android.elvtmtn.biz.IWorkOrderFeedbackListener;
import wzp.project.android.elvtmtn.biz.IWorkOrderReceiveListener;
import wzp.project.android.elvtmtn.biz.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;

public class WorkOrderBizImpl implements IWorkOrderBiz {
	
	private static final String tag = "WorkOrderBizImpl";
	

	@Override
	public <T> void getWorkOrdersByCondition(int workOrderType, int workOrderState, 
			final int pageNumber, int pageSize, final List<T> dataList, final IWorkOrderSearchListener listener) {
		/*StringBuilder strUrl = new StringBuilder(ProjectContants.basePath);

		switch (workOrderType) {
			case WorkOrderType.MAINTAIN_ORDER:
				strUrl.append("/maintainOrder/list");
				break;
			case WorkOrderType.FAULT_ORDER:
				strUrl.append("/faultOrder/list");
				break;
			default:
				break;
		}
		
		strUrl.append("?pageNumber=" + pageNumber + "&pageSize=" + pageSize + "&type=" + workOrderState);
		
		Request request = OkHttpUtils.newRequestInstance(strUrl.toString(), null, null);
		OkHttpUtils.enqueue(request, new Callback() {	
			@Override
			public void onResponse(Response response) throws IOException {
//				Log.i(tag, response.body().string());
				if (response.isSuccessful()) {
					
					 * 响应成功
					 
					String strResponse = response.body().string();
					
					Log.i(tag, strResponse);
					
					if (!TextUtils.isEmpty(strResponse)) {
						Log.i(tag, "JSON转换开始");
						List<T> respDataList = JSON.parseObject(strResponse, new TypeReference<List<T>>() {});
						Log.i(tag, "JSON转换结束");
						if (respDataList != null && respDataList.size() > 0) {
							dataList.clear();
							for (int i=0; i<respDataList.size(); i++) {
								dataList.add(respDataList.get(i));
							}
							
							if (1 == pageNumber) {
								dataList.clear();
								for (int i=0; i<respDataList.size(); i++) {
									 此处显示的类型为JSONObject，因此会导致Adapter中出现
									 * ClassCastException异常，无法将JSONObject转化为FaultOrder或MaintainOrder,
									 * 原因暂时不清楚。
									 
									Log.d(tag, respDataList.get(i).getClass().getSimpleName());
									dataList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								dataList.addAll(respDataList);
							}
							
//							listener.onSearchSuccess();
							if (respDataList.size() % 10 == 0) {
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
//						listener.onServerException("服务器故障，响应数据有误！");
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				} else {
					// 响应失败
//					listener.onServerException("服务器故障，响应失败！");
					listener.onSearchFailure("服务器故障，响应失败！");
				}
			}
			
			@Override
			public void onFailure(Request request, IOException exception) {
				Log.e(tag, Log.getStackTraceString(exception));
				listener.onSearchFailure("访问服务器失败\n请检查网络连接后重试");
			}
		});*/
	}

	/**
	 * 按条件查询保养工单
	 */
	@Override
	public void getMaintainOrdersByCondition(long groupId, int workOrderState, final int pageNumber, int pageSize,
			final List<MaintainOrder> dataList, final IWorkOrderSearchListener listener) {
		StringBuilder strUrl = new StringBuilder(ProjectContants.basePath);
		strUrl.append("/maintainOrder/list");
		
		OkHttpUtils.get().url(strUrl.toString())
			.addParams("groupId", String.valueOf(groupId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.addParams("type", String.valueOf(workOrderState))
			.build()
			.execute(new StringCallback() {
				@Override
				public void onAfter() {
					listener.onAfter();
				}

				@Override
				public void onResponse(String response) {					
					Log.i(tag, response);
					
					if (!TextUtils.isEmpty(response)) {
						List<MaintainOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<MaintainOrder>>() {});
						if (respDataList != null && respDataList.size() > 0) {							
							/*if (1 == pageNumber) {
								dataList.clear();
								for (int i=0; i<respDataList.size(); i++) {
									Log.d(tag, respDataList.get(i).getClass().getSimpleName());
									dataList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								dataList.addAll(respDataList);
							}*/
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
							/*
							 * 响应数据为空，表示当前请求的数据不存在，有两种可能的情况
							 * 1、压根就没有数据；
							 * 2、表示当前pageNumber下，没有数据，即1~pageNumber-1之间的页面，就已经把数据完全显示出来了；
							 * 
							 * 解决方案：
							 * 1、如果pageNumber等于1，属于上述第一种情况，此时应该提示“当前没有符合要求的工单”；
							 * 2、如果pageNumber大于1，属于上述第二种情况，此时应该利用Toast提示已经显示出所有工单，并关闭上拉加载的功能；
							 * 
							 * 提供一个标志位，根据不同的情况，执行不同的操作；
							 */
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");
				}
			});		
	}

	/**
	 * 按条件查询错误工单
	 */
	@Override
	public void getFaultOrdersByCondition(long groupId, int workOrderState, final int pageNumber, int pageSize,
			final List<FaultOrder> dataList, final IWorkOrderSearchListener listener) {
		StringBuilder strUrl = new StringBuilder(ProjectContants.basePath);
		strUrl.append("/faultOrder/list");
		
		OkHttpUtils.get().url(strUrl.toString())
			.addParams("groupId", String.valueOf(groupId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.addParams("type", String.valueOf(workOrderState))
			.build()
			.execute(new StringCallback() {
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
							/*
							 * 响应数据为空，表示当前请求的数据不存在，有两种可能的情况
							 * 1、压根就没有数据；
							 * 2、表示当前pageNumber下，没有数据，即1~pageNumber-1之间的页面，就已经把数据完全显示出来了；
							 * 
							 * 解决方案：
							 * 1、如果pageNumber等于1，属于上述第一种情况，此时应该提示“当前没有符合要求的工单”；
							 * 2、如果pageNumber大于1，属于上述第二种情况，此时应该利用Toast提示已经显示出所有工单，并关闭上拉加载的功能；
							 * 
							 * 提供一个标志位，根据不同的情况，执行不同的操作；
							 */
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");	
				}
			});
	}
	
	@Override
	public void getFaultOrderById(final String id, final ISignleOrderSearchListener listener) {
		StringBuilder strUrl = new StringBuilder(ProjectContants.basePath);
		strUrl.append("/faultOrder/detail");
		
		OkHttpUtils.get().url(strUrl.toString())
			.addParams("id", id)
			.build()
			.execute(new StringCallback() {
				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					
					if (!TextUtils.isEmpty(response)) {
						listener.onSearchSuccess(response);
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");	
				}
			});
	}
	
	
	@Override
	public void getReceivedMaintainOrdersByCondition(long employeeId,
			final int pageNumber, int pageSize, final List<MaintainOrder> dataList,
			final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/maintainOrder/accept/list";
		
		OkHttpUtils.get().url(url)
			.addParams("employeeId", String.valueOf(employeeId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.build()
			.execute(new StringCallback() {
				@Override
				public void onAfter() {
					listener.onAfter();
				}
				
				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					
					if (!TextUtils.isEmpty(response)) {
						List<MaintainOrder> respDataList = JSON.parseObject(response, 
								new TypeReference<List<MaintainOrder>>() {});
						if (respDataList != null && respDataList.size() > 0) {							
							/*if (1 == pageNumber) {
								dataList.clear();
								for (int i=0; i<respDataList.size(); i++) {
									Log.d(tag, respDataList.get(i).getClass().getSimpleName());
									dataList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								dataList.addAll(respDataList);
							}*/
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
							/*
							 * 响应数据为空，表示当前请求的数据不存在，有两种可能的情况
							 * 1、压根就没有数据；
							 * 2、表示当前pageNumber下，没有数据，即1~pageNumber-1之间的页面，就已经把数据完全显示出来了；
							 * 
							 * 解决方案：
							 * 1、如果pageNumber等于1，属于上述第一种情况，此时应该提示“当前没有符合要求的工单”；
							 * 2、如果pageNumber大于1，属于上述第二种情况，此时应该利用Toast提示已经显示出所有工单，并关闭上拉加载的功能；
							 * 
							 * 提供一个标志位，根据不同的情况，执行不同的操作；
							 */
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");	
				}
			});
		
	}
	
	@Override
	public void getReceivedFaultOrdersByCondition(long employeeId,
			final int pageNumber, int pageSize, final List<FaultOrder> dataList,
			final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/faultOrder/accept/list";
		
		OkHttpUtils.get().url(url)
			.addParams("employeeId", String.valueOf(employeeId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.build()
			.execute(new StringCallback() {
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
							/*if (1 == pageNumber) {
								dataList.clear();
								for (int i=0; i<respDataList.size(); i++) {
									Log.d(tag, respDataList.get(i).getClass().getSimpleName());
									dataList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								dataList.addAll(respDataList);
							}*/
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
							/*
							 * 响应数据为空，表示当前请求的数据不存在，有两种可能的情况
							 * 1、压根就没有数据；
							 * 2、表示当前pageNumber下，没有数据，即1~pageNumber-1之间的页面，就已经把数据完全显示出来了；
							 * 
							 * 解决方案：
							 * 1、如果pageNumber等于1，属于上述第一种情况，此时应该提示“当前没有符合要求的工单”；
							 * 2、如果pageNumber大于1，属于上述第二种情况，此时应该利用Toast提示已经显示出所有工单，并关闭上拉加载的功能；
							 * 
							 * 提供一个标志位，根据不同的情况，执行不同的操作；
							 */
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");	
				}
			});
	}
	
	@Override
	public void getSignedInFaultOrdersByCondition(long employeeId,
			final int pageNumber, int pageSize, final List<FaultOrder> faultOrderList,
			final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/faultOrder/feedback/list";
		
		OkHttpUtils.get().url(url)
			.addParams("employeeId", String.valueOf(employeeId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.build()
			.execute(new StringCallback() {
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
								faultOrderList.clear();
							}
							faultOrderList.addAll(respDataList);
							
							if (respDataList.size() % ProjectContants.PAGE_SIZE == 0) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_UNCOMPLETE);
							} else {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						} else {
							/*
							 * 响应数据为空，表示当前请求的数据不存在，有两种可能的情况
							 * 1、压根就没有数据；
							 * 2、表示当前pageNumber下，没有数据，即1~pageNumber-1之间的页面，就已经把数据完全显示出来了；
							 * 
							 * 解决方案：
							 * 1、如果pageNumber等于1，属于上述第一种情况，此时应该提示“当前没有符合要求的工单”；
							 * 2、如果pageNumber大于1，属于上述第二种情况，此时应该利用Toast提示已经显示出所有工单，并关闭上拉加载的功能；
							 * 
							 * 提供一个标志位，根据不同的情况，执行不同的操作；
							 */
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");	
				}
			});
		
	}
	
	@Override
	public void getSignedInMaintainOrdersByCondition(long employeeId,
			final int pageNumber, int pageSize, final List<MaintainOrder> maintainOrderList,
			final IWorkOrderSearchListener listener) {
		String url = ProjectContants.basePath + "/maintainOrder/feedback/list";
		
		OkHttpUtils.get().url(url)
			.addParams("employeeId", String.valueOf(employeeId))
			.addParams("pageNumber", String.valueOf(pageNumber))
			.addParams("pageSize", String.valueOf(pageSize))
			.build()
			.execute(new StringCallback() {
				@Override
				public void onAfter() {
					listener.onAfter();
				}
				
				@Override
				public void onResponse(String response) {
					Log.i(tag, response);
					
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
							/*
							 * 响应数据为空，表示当前请求的数据不存在，有两种可能的情况
							 * 1、压根就没有数据；
							 * 2、表示当前pageNumber下，没有数据，即1~pageNumber-1之间的页面，就已经把数据完全显示出来了；
							 * 
							 * 解决方案：
							 * 1、如果pageNumber等于1，属于上述第一种情况，此时应该提示“当前没有符合要求的工单”；
							 * 2、如果pageNumber大于1，属于上述第二种情况，此时应该利用Toast提示已经显示出所有工单，并关闭上拉加载的功能；
							 * 
							 * 提供一个标志位，根据不同的情况，执行不同的操作；
							 */
							if (1 == pageNumber) {
								listener.onSearchSuccess(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchSuccess(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onSearchFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onSearchFailure("服务器正在打盹，请\n检查网络连接后重试");	
				}
			});
	}

	@Override
	public void receiveOrder(int workOrderType, Long workOrderId, Long employeeId, 
			final IWorkOrderReceiveListener listener) {
		String url = null;
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			url = ProjectContants.basePath + "/maintainOrder/accept";
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			url = ProjectContants.basePath + "/faultOrder/accept";
		} else {
			throw new IllegalArgumentException("工单类型有误");
		}
		
		OkHttpUtils.post().url(url)
			.addParams("id", String.valueOf(workOrderId))
			.addParams("employeeId", String.valueOf(employeeId))
			.build()
			.execute(new StringCallback() {		
				@Override
				public void onResponse(String response) {
					Log.i(response, response);
					JSONObject jo = JSON.parseObject(response);
					String result = jo.getString("result");
					
					if (!TextUtils.isEmpty(result)) {
						if (result.equals("success")) {
							listener.onReceiveSuccess(jo.getDate("receivingTime"));
						} else if (result.equals("repeat")) {
							listener.onReceiveFailure("重复接单，接单失败！");
						} else if (result.equals("employeeNotExist")) {
							
						} else if (response.equals("faultOrderNotExist")) {
							
						} else if (response.equals("maintainOrderNotExist")) {
							
						}
					} else {
						listener.onReceiveFailure("服务器故障，响应数据有误！");
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onReceiveFailure("服务器正在打盹，请\n检查网络连接后重试");					
				}
			});
	}

	@Override
	public void cancelReceiveOrder(int workOrderType, Long workOrderId,
			Long employeeId, final IWorkOrderCancelListener listener) {
		String url = null;
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			url = ProjectContants.basePath + "/maintainOrder/cancel";
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			url = ProjectContants.basePath + "/faultOrder/cancel";
		} else {
			throw new IllegalArgumentException("工单类型有误");
		}
		
		OkHttpUtils.post().url(url)
			.addParams("id", String.valueOf(workOrderId))
			.addParams("employeeId", String.valueOf(employeeId))
			.build()
			.execute(new StringCallback() {				
				@Override
				public void onResponse(String response) {
					Log.i(response, response);
					JSONObject jo = JSON.parseObject(response);
					String result = jo.getString("result");
					
					if (!TextUtils.isEmpty(result)) {
						if (result.equals("success")) {
							listener.onCancelSuccess();
						} else if (result.equals("notMatch")) {
							
						} else if (result.equals("notAccept")) {
							
						} else if (result.equals("employeeNotExist")) {
							
						} else if (result.equals("maintainOrderNotExist")) {
							
						} else if (result.equals("faultOrderNotExist")) {
							
						}
					} else {
						listener.onCancelFailure("服务器故障，响应数据有误！");		
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e(tag, Log.getStackTraceString(e));
					listener.onCancelFailure("服务器正在打盹，请\n检查网络连接后重试");			
				}				
			});
	}

	@Override
	public void feedbackOrder(int workOrderType, Long workOrderId,
			Long employeeId, String faultReason, boolean isDone, String remark,
			String signOutAddress, String finishedItems, final IWorkOrderFeedbackListener listener) {
		String url = null;
		PostFormBuilder builder = OkHttpUtils.post()
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
		
		builder.build().execute(new StringCallback() {			
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
						
					} else if (result.equals("maintainOrderNotExist")) {
						
					} else if (result.equals("faultOrderNotExist")) {
						
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
		});
	}

	/*@Override
	public void sortMaintainOrderByFinalTimeIncrease(
			List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			return;
		}
		
		MaintainOrder[] maintainOrders = (MaintainOrder[]) maintainOrderList.toArray();
		long dateI;
		long dateJ;
		MaintainOrder temp;
		
		for (int i=0; i<maintainOrders.length-1; i++) {
			if (maintainOrders[i].getFinalTime() != null) {
				dateI = maintainOrders[i].getFinalTime().getTime();
			} else {
				dateI = 0;
			}
			
			for (int j=maintainOrders.length; j>i; j--) {				
				if (maintainOrders[j].getFinalTime() != null) {
					dateJ = maintainOrders[j].getFinalTime().getTime();
				} else {
					dateJ = 0;
				}
				
				if (dateI > dateJ) {
					temp = maintainOrders[i];
					maintainOrders[i] = maintainOrders[j];
					maintainOrders[j] = temp;
				}
			}
			
			maintainOrderList.add(maintainOrders[i]);
		}
	}*/

	
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
		 * 冒泡排序
		 */
		/*for (int i=0; i<length; i++) {			
			for (int j=length-1; j>i; j--) {
				maintainOrderI = maintainOrderList.get(i);
				idI =  maintainOrderI.getId();
				
				maintainOrderJ = maintainOrderList.get(j);
				idJ = maintainOrderJ.getId();
				
				if (idI < idJ) {
					temp = maintainOrderI;
					maintainOrderI = maintainOrderJ;
					maintainOrderJ = temp;
					
					maintainOrderList.remove(i);
					maintainOrderList.add(i, maintainOrderI);
					maintainOrderList.remove(j);
					maintainOrderList.add(j, maintainOrderJ);
				}
				
				maintainOrderJ = null;
				maintainOrderI = null;
			}
		}*/
		
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
					idA =  maintainOrderList.get(minElementIndex).getId();
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
					idA =  maintainOrderList.get(maxElementIndex).getId();
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
					idA =  faultOrderList.get(minElementIndex).getId();
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
					idA =  faultOrderList.get(minElementIndex).getId();
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
					tempDate = maintainOrderList.get(maxElementIndex).getReceivingTime();
					if (tempDate != null) {
						receivingTimeA = tempDate.getTime();
					} else {
						receivingTimeA = 0;
					}
					tempDate = null;
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
					tempDate = faultOrderList.get(maxElementIndex).getReceivingTime();
					if (tempDate != null) {
						receivingTimeA = tempDate.getTime();
					} else {
						receivingTimeA = 0;
					}
					tempDate = null;
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
					tempDate = maintainOrderList.get(minElementIndex).getSignOutTime();
					if (tempDate != null) {
						finishedTimeA = tempDate.getTime();
					} else {
						finishedTimeA = 0;
					}
					tempDate = null;
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
					tempDate = maintainOrderList.get(maxElementIndex).getSignOutTime();
					if (tempDate != null) {
						finishedTimeA = tempDate.getTime();
					} else {
						finishedTimeA = 0;
					}
					tempDate = null;
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
					tempDate = faultOrderList.get(minElementIndex).getSignOutTime();
					if (tempDate != null) {
						finishedTimeA = tempDate.getTime();
					} else {
						finishedTimeA = 0;
					}
					tempDate = null;
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
					tempDate = faultOrderList.get(maxElementIndex).getSignOutTime();
					if (tempDate != null) {
						finishedTimeA = tempDate.getTime();
					} else {
						finishedTimeA = 0;
					}
					tempDate = null;
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
					tempDate = maintainOrderList.get(maxElementIndex).getSignInTime();
					if (tempDate != null) {
						signInTimeA = tempDate.getTime();
					} else {
						signInTimeA = 0;
					}
					tempDate = null;
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
					tempDate = faultOrderList.get(maxElementIndex).getSignInTime();
					if (tempDate != null) {
						signInTimeA = tempDate.getTime();
					} else {
						signInTimeA = 0;
					}
					tempDate = null;
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
