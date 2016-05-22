package wzp.project.android.elvtmtn.util.myokhttp;

import java.util.List;
import java.util.Map;

import com.zhy.http.okhttp.builder.PostFormBuilder.FileInput;
import com.zhy.http.okhttp.request.PostFormRequest;

public class MyPostFormRequest extends PostFormRequest {

	public MyPostFormRequest(String url, Object tag,
			Map<String, String> params, Map<String, String> headers,
			List<FileInput> files) {
		super(url, tag, params, headers, files);
	}

	@Override
	public MyRequestCall build()
    {
        return new MyRequestCall(this);
    }

}
