package wzp.project.android.elvtmtn.util.myokhttp;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

import com.zhy.http.okhttp.request.OkHttpRequest;
import com.zhy.http.okhttp.request.RequestCall;

public abstract class MyOkHttpRequest extends OkHttpRequest {

	protected MyOkHttpRequest(String url, Object tag,
			Map<String, String> params, Map<String, String> headers) {
		super(url, tag, params, headers);
	}
	
}
