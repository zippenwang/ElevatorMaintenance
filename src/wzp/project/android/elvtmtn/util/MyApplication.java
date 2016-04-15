package wzp.project.android.elvtmtn.util;

import android.app.Application;
import android.content.Context;

/**
 * 提供一些全局的Application属性
 * @author Zippen
 *
 */
public class MyApplication extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		context = getApplicationContext();
	}
	
	public static Context getContext() {
		return context;
	}	
}
