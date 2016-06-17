package wzp.project.android.elvtmtn.helper.contant;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import wzp.project.android.elvtmtn.util.DESUtil;
import wzp.project.android.elvtmtn.util.MyApplication;

@SuppressLint("SimpleDateFormat")
public interface ProjectContants {

//	String basePath = "http://192.168.1.103:8080/ElevatorMaintainSystem/api";		// Web服务器的域名
	String basePath = "http://vino007.cc/ElevatorMaintainSystem/api";		// Web服务器的域名
	
	int ORDER_IS_NULL = 0x50;				// 符合要求的工单不存在
	int ORDER_SHOW_COMPLETE = 0x51;			// 工单已经全部被显示出来了
	int ORDER_SHOW_UNCOMPLETE = 0X52;		// 工单还未全部显示出来
	
	int PAGE_SIZE = 10;
	
	DESUtil desUtil = new DESUtil("wzp@d!108Y#d2s-*yq&");	
	
	/*
	 * 日期显示格式
	 */
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
	SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日 HH时");
	SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
	
	SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	SharedPreferences.Editor editor = preferences.edit();
	
	
}
