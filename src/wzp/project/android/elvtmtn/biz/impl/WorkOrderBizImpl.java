package wzp.project.android.elvtmtn.biz.impl;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.IWorkOrderCancelListener;
import wzp.project.android.elvtmtn.biz.IWorkOrderReceiveListener;
import wzp.project.android.elvtmtn.biz.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
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
							if (1 == pageNumber) {
								dataList.clear();
								for (int i=0; i<respDataList.size(); i++) {
									Log.d(tag, respDataList.get(i).getClass().getSimpleName());
									dataList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								dataList.addAll(respDataList);
							}
							
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
					listener.onSearchFailure("服务器正在打盹\n请检查网络连接后重试");
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
								for (int i=0; i<respDataList.size(); i++) {
									Log.d(tag, respDataList.get(i).getClass().getSimpleName());
									dataList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								dataList.addAll(respDataList);
							}
							
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
					listener.onSearchFailure("服务器正在打盹\n请检查网络连接后重试");	
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
							if (1 == pageNumber) {
								dataList.clear();
								for (int i=0; i<respDataList.size(); i++) {
									Log.d(tag, respDataList.get(i).getClass().getSimpleName());
									dataList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								dataList.addAll(respDataList);
							}
							
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
					listener.onSearchFailure("服务器正在打盹\n请检查网络连接后重试");	
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
					listener.onReceiveFailure("服务器正在打盹\n请检查网络连接后重试");					
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
					listener.onCancelFailure("服务器正在打盹\n请检查网络连接后重试");			
				}
			});
	}


	
	

}
