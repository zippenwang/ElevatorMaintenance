package wzp.project.android.elvtmtn.util.myokhttp;

import okhttp3.Call;
import okhttp3.Response;

import com.zhy.http.okhttp.callback.Callback;

public abstract class MyCallback<T> extends Callback<T> {

	/**
	 * 能够连接上服务器，但服务器返回的响应码不在[200, 300)的范围内，
	 * 则回调该方法
	 * 
	 * @param call
	 * @param e
	 * @param respCode
	 */
	public abstract void onError(Call call, Exception e, int respCode);
	
	/**
	 * 服务器异常或无法连接网络，则回调该方法
	 */
	public void onError(Call call, Exception e) {}
	
	/**
	 * 默认的MyCallback接口实现
	 */
	public static MyCallback CALLBACK_DEFAULT = new MyCallback()
    {
        @Override
        public Object parseNetworkResponse(Response response) throws Exception
        {
            return null;
        }

        @Override
        public void onError(Call call, Exception e)
        {

        }

        @Override
        public void onResponse(Object response)
        {

        }

		@Override
		public void onError(Call call, Exception e, int respCode) 
		{
			
		}
    };
}
