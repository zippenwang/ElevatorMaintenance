package wzp.project.android.elvtmtn.util.myokhttp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder.FileInput;
import com.zhy.http.okhttp.request.PostFormRequest;
import com.zhy.http.okhttp.request.RequestCall;

public class MyPostFormBuilder extends PostFormBuilder {

	private List<FileInput> files = new ArrayList<FileInput>();
	
	@Override
    public MyRequestCall build()
    {
        return new MyPostFormRequest(url, tag, params, headers, files).build();
    }
}
