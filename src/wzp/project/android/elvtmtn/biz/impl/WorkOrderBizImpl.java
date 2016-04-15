package wzp.project.android.elvtmtn.biz.impl;

import java.io.IOException;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.util.OkHttpUtils;

public class WorkOrderBizImpl implements IWorkOrderBiz {
	
	private static final String tag = "WorkOrderBizImpl";
	

	@Override
	public <T> void getWorkOrdersByCondition(int workOrderType, int workOrderState, 
			final int pageNumber, int pageSize, final List<T> dataList, final IWorkOrderSearchListener listener) {
		StringBuilder strUrl = new StringBuilder(ProjectContants.basePath);

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
		
		// 创建请求体
		RequestBody requestBody = new FormEncodingBuilder()
			.add("pageNumber", String.valueOf(pageNumber))
			.add("pageSize", String.valueOf(pageSize))
			.add("type", String.valueOf(workOrderState))
			.build();
		Request request = OkHttpUtils.newRequestInstance(strUrl.toString(), null, requestBody);
		OkHttpUtils.enqueue(request, new Callback() {	
			@Override
			public void onResponse(Response response) throws IOException {
				if (response.isSuccessful()) {
					/*
					 * 响应成功
					 */
					String strResponse = response.body().string();
					
					if (!TextUtils.isEmpty(strResponse)) {
						List<T> respDataList = JSON.parseObject(strResponse, new TypeReference<List<T>>() {});
						if (respDataList != null && respDataList.size() > 0) {
							/*dataList.clear();
							for (int i=0; i<respDataList.size(); i++) {
								dataList.add(respDataList.get(i));
							}*/
							
							if (1 == pageNumber) {
								dataList.clear();
								for (int i=0; i<respDataList.size(); i++) {
									dataList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								dataList.addAll(respDataList);
							}
							
							listener.onSearchSuccess();
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
								listener.onSearchFailure(ProjectContants.ORDER_IS_NULL);
							} else if (pageNumber > 1) {
								listener.onSearchFailure(ProjectContants.ORDER_SHOW_COMPLETE);
							}
						}
					} else {
						listener.onServerException("服务器故障，响应数据有误！");
					}
				} else {
					// 响应失败
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
