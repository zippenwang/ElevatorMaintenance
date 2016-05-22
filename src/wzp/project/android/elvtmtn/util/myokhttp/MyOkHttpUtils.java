package wzp.project.android.elvtmtn.util.myokhttp;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.baidu.platform.comapi.map.o;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.request.RequestCall;

	
public class MyOkHttpUtils extends OkHttpUtils {
	
	private static MyOkHttpUtils mInstance;
	private Handler mDelivery;
	

	public MyOkHttpUtils(OkHttpClient okHttpClient) {
		super(okHttpClient);
		init();
	}
	
	private void init()
    {
        mDelivery = new Handler(Looper.getMainLooper());
    }
	
	public static MyPostFormBuilder post()
    {
        return new MyPostFormBuilder();
    }
	
    public static MyGetBuilder get()
    {
        return new MyGetBuilder();
    }
	
	public static MyOkHttpUtils getInstance()
    {
        if (mInstance == null)
        {
            synchronized (MyOkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new MyOkHttpUtils(null);
                }
            }
        }
        return mInstance;
    }

	@Override
	public void execute(final RequestCall requestCall, Callback callback)
    {
        if (callback == null)
            callback = MyCallback.CALLBACK_DEFAULT;
//        final MyCallback finalCallback = (MyCallback) callback;
        final MyCallback finalCallback;
        
        if (callback instanceof MyCallback) {
        	finalCallback = (MyCallback) callback;
		} else {
			throw new IllegalArgumentException("参数不匹配（回调类应该实现MyCallback接口）");
		}

        requestCall.getCall().enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(Call call, final IOException e)
            {
                sendFailResultCallback(call, e, finalCallback);
            }

            @Override
            public void onResponse(final Call call, final Response response)
            {
            	int respCode = response.code();
            	Log.d("MyOkHttpUtils", "响应码为：" + respCode);
            	
                if (respCode >= 400 && respCode <= 599)
                {
                    try
                    {
                        sendFailResultCallback(call, new RuntimeException(response.body().string()), 
                        		respCode, finalCallback);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    return;
                }

                try
                {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } catch (Exception e)
                {
                    sendFailResultCallback(call, e, finalCallback);
                }

            }
        });
    }
	

	public void sendFailResultCallback(final Call call, final Exception e, 
			final int respCode, final MyCallback callback)
    {
        if (callback == null) return;

        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                callback.onError(call, e, respCode);
                callback.onAfter();
            }
        });
    }
}
