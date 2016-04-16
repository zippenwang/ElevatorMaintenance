package wzp.project.android.elvtmtn.util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * 对OkHttpClient进行封装
 * @author Zippen
 *
 */
public class OkHttpUtils {
	/*
	 * OkHttpClient官方并不建议创建多个OkHttpClient实例，因此只定义一个对象，
	 * 如果需要多个OkHttpClient实例，可以调用clone()方法
	 */
	private static final OkHttpClient mOkHttpClient = new OkHttpClient();

	static {
		/*
		 * 设置OkHttpClient的一些初始化参数，包括连接超时、读取超时、写超时等
		 */
		mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
		mOkHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
		mOkHttpClient.setWriteTimeout(5, TimeUnit.SECONDS);
	}
	
	/**
	 * 克隆OkHttpClient实例
	 * 
	 * @param connectTimeout 连接超时
	 * @param connectUnit 连接超时单位
	 * @param readTimeout 读取超时
	 * @param readUnit 读取超时单位
	 * @param writeTimeout 写超时
	 * @param writeUnit 写超时单位
	 * @return OkHttpClient实例
	 */
	public static OkHttpClient cloneOkhttpClient(
			long connectTimeout, TimeUnit connectUnit,
			long readTimeout, TimeUnit readUnit,
			long writeTimeout, TimeUnit writeUnit) {
		OkHttpClient okHttpClient = mOkHttpClient.clone();
		
		okHttpClient.setConnectTimeout(connectTimeout, connectUnit);
		okHttpClient.setReadTimeout(readTimeout, readUnit);
		okHttpClient.setWriteTimeout(writeTimeout, writeUnit);
		
		return okHttpClient;
	}
	
	public static OkHttpClient cloneOkHttpClient(long connectTimeout,  long readTimeout, 
			long writeTimeout, TimeUnit timeUnit) {
		OkHttpClient okHttpClient = mOkHttpClient.clone();
		
		okHttpClient.setConnectTimeout(connectTimeout, timeUnit);
		okHttpClient.setReadTimeout(readTimeout, timeUnit);
		okHttpClient.setWriteTimeout(writeTimeout, timeUnit);
		
		return okHttpClient;
	}
	
	/**
	 * 封装创建Request实例的方法
	 * 
	 * @param strUrl 服务器url
	 * @param headers 请求头
	 * @param requestBody 请求体（若为null，GET请求；否则，POST请求）
	 * @return
	 */
	public static Request newRequestInstance(String strUrl, Headers headers, RequestBody requestBody) {
		Request.Builder builder = new Request.Builder().url(strUrl);
		
		if (headers != null && headers.size() > 0) {
			builder.headers(headers);
		}
		
		if (requestBody != null) {
			builder = builder.post(requestBody);
		}		
		
		return builder.build();
	}

	/**
	 * 同步访问网络，该方法会造成线程阻塞，直至服务器返回响应报文
	 * 
	 * @param request Request实例
	 * @return Response实例
	 * @throws IOException
	 * @attention 使用该方法时，一定要开辟一条新线程
	 */
	public static Response execute(Request request) throws IOException {
		return mOkHttpClient.newCall(request).execute();
	}

	/**
	 * 异步访问网络，不会造成线程阻塞，当服务器成功返回响应报文时，会自动调用回调接口中的方法
	 * 
	 * @param request Request实例
	 * @param responseCallback 回调接口
	 */
	public static void enqueue(Request request, Callback responseCallback) {
		mOkHttpClient.newCall(request).enqueue(responseCallback);
	}

	/**
	 * 异步访问网络, 但不在意返回结果（实现空callback）
	 * 
	 * @param request Request实例
	 */
	public static void enqueue(Request request) {
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Response arg0) 
					throws IOException {}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {}
		});
	}

	/**
	 * 以同步方式向服务器发送GET请求，并以字符串的格式返回服务器响应内容
	 * 
	 * @param url
	 * @return 服务器响应内容
	 * @throws IOException
	 */
	public static String getStringFromServer(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = execute(request);
		if (response.isSuccessful()) {
			String responseUrl = response.body().string();
			return responseUrl;
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}

	// 默认的字符集
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * 格式化POST请求中的参数
	 * 
	 * @param params 请求参数
	 * @return
	 */
	public static String formatParams(List<BasicNameValuePair> params) {
		return URLEncodedUtils.format(params, CHARSET_NAME);
	}

	/**
	 * 
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String attachHttpGetParams(String url,
			List<BasicNameValuePair> params) {
		return url + "?" + formatParams(params);
	}

	/**
	 * 
	 * 
	 * @param url
	 * @param name
	 * @param value
	 * @return
	 */
	public static String attachHttpGetParam(String url, String name,
			String value) {
		return url + "?" + name + "=" + value;
	}
}
