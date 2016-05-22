package wzp.project.android.elvtmtn.util.myokhttp;

import java.util.Map;

import com.zhy.http.okhttp.request.GetRequest;

public class MyGetRequest extends GetRequest {

	public MyGetRequest(String url, Object tag, Map<String, String> params,
			Map<String, String> headers) {
		super(url, tag, params, headers);
	}
	
	@Override
	public MyRequestCall build()
    {
        return new MyRequestCall(this);
    }

}
