package wzp.project.android.elvtmtn.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class BaseActivity extends Activity {

	private static final String tag = "BaseActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.d(tag, this.getClass().getSimpleName());
	}
}
