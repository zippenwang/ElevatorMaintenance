package wzp.project.android.elvtmtn.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

public class ActivityCollector {

	private static List<Activity> activityList = new ArrayList<Activity>();
	private static String tag = "ActivityCollector";
	
	
	public static void addActivity(Activity activity) {
		if (!activityList.contains(activity)) {
			activityList.add(activity);
		}
		Log.d(tag, "activityList当前包含的元素个数为：" + activityList.size());
	}
	
	public static void removeActivity(Activity activity) {
		activityList.remove(activity);
		Log.d(tag, "activityList当前包含的元素个数为：" + activityList.size());
	}
	
	public static void finishAll() {
		Log.d(tag, "需要finish的activity个数为：" + activityList.size());
		for (Activity activity : activityList) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
