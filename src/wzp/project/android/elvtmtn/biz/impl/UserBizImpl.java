package wzp.project.android.elvtmtn.biz.impl;

import java.io.IOException;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import wzp.project.android.elvtmtn.biz.IUserBiz;
import wzp.project.android.elvtmtn.biz.IUserLoginListener;
import wzp.project.android.elvtmtn.entity.User;
import wzp.project.android.elvtmtn.util.OkHttpUtils;

public class UserBizImpl implements IUserBiz {
	
	private static final String tag = "UserBizImpl";

	@Override
	public void login(User user, final IUserLoginListener listener) {
		/*
		 * 用于测试的代码
		 */
		/*if (user.getId().equals("wzp") && user.getPassword().equals("1234")) {
			return true;
		}*/
		
		String strUrl = "http://localhost:8080/WebServlet";		// 服务器url
		
		// 创建请求体
		RequestBody requestBody = new FormEncodingBuilder()
			.add("userId", user.getId())
			.add("password", user.getPassword())
			.build();
		Request request = OkHttpUtils.newRequestInstance(strUrl, null, requestBody);
		OkHttpUtils.enqueue(request, new Callback() {		
			@Override
			public void onResponse(Response response) throws IOException {
				if (response.isSuccessful()) {
					// 登录成功，解析服务器的响应体（json字符串），判断是否成功登录
					String strResponse = response.body().string();
					
					
				}
			}
			
			@Override
			public void onFailure(Request request, IOException exception) {
//				exception.printStackTrace();
				Log.e(tag, Log.getStackTraceString(exception));
				listener.onServerException();
			}
		});
		
	}

}
