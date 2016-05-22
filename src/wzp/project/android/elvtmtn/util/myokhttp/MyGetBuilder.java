package wzp.project.android.elvtmtn.util.myokhttp;

import com.zhy.http.okhttp.builder.GetBuilder;

public class MyGetBuilder extends GetBuilder {

	@Override
    public MyRequestCall build()
    {
        if (params != null)
        {
            url = appendParams(url, params);
        }

        return new MyGetRequest(url, tag, params, headers).build();
    }
}
