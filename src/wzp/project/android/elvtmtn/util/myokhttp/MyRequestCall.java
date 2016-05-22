package wzp.project.android.elvtmtn.util.myokhttp;

import okhttp3.Request;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.OkHttpRequest;
import com.zhy.http.okhttp.request.RequestCall;

public class MyRequestCall extends RequestCall {
	
	private Request request;
	

	public MyRequestCall(OkHttpRequest request) {
		super(request);
	}

	@Override
	public void execute(Callback callback)
    {
        buildCall(callback);

        if (callback != null)
        {
            callback.onBefore(request);
        }

        MyOkHttpUtils.getInstance().execute(this, callback);
    }
	

	/*public void execute(MyCallback callback)
    {
        buildCall(callback);

        if (callback != null)
        {
            callback.onBefore(request);
        }

        MyOkHttpUtils.getInstance().execute(this, callback);
    }*/
	
}
