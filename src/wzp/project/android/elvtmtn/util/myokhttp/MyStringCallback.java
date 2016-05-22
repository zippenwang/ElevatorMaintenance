package wzp.project.android.elvtmtn.util.myokhttp;

import java.io.IOException;

import okhttp3.Response;

public abstract class MyStringCallback extends MyCallback<String> {

	@Override
	public String parseNetworkResponse(Response response) throws IOException
	{
	    return response.body().string();
	}
}
