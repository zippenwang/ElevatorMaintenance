package wzp.project.android.elvtmtn.biz.impl;

import java.util.List;

import okhttp3.Call;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import wzp.project.android.elvtmtn.biz.IElevatorRecordBiz;
import wzp.project.android.elvtmtn.biz.IElevatorRecordSearchListener;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;

public class ElevatorRecordBizImpl implements IElevatorRecordBiz {

	private static final String tag = "ElevatorRecordBizImpl";
	
	@Override
	public void getAllElevatorRecords(long groupId, final int pageNumber, int pageSize, 
			final List<ElevatorRecord> elevatorRecordList, final IElevatorRecordSearchListener listener) {
		String url = ProjectContants.basePath + "/elevatorRecord/list";
		OkHttpUtils.get().url(url)
			.addParams("groupId", String.valueOf(groupId))
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
						List<ElevatorRecord> respDataList = JSON.parseObject(response, 
								new TypeReference<List<ElevatorRecord>>() {});
						if (respDataList != null && respDataList.size() > 0) {							
							if (1 == pageNumber) {
								elevatorRecordList.clear();
								for (int i=0; i<respDataList.size(); i++) {
									Log.d(tag, respDataList.get(i).getClass().getSimpleName());
									elevatorRecordList.add(respDataList.get(i));
								}
							} else if (pageNumber > 1) {
								elevatorRecordList.addAll(respDataList);
							}
							
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

}
