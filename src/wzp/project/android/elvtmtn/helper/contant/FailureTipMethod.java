package wzp.project.android.elvtmtn.helper.contant;

/**
 * 访问失败后的提醒方式
 * 
 * @author Zippen
 *
 */
public interface FailureTipMethod {

	int TOAST = 0x97;				// 以toast方式提醒
	
	int VIEW = 0x98;				// 以显示控件的方式提醒
	
	int TOAST_AND_VIEW = 0x99;		// Toast、View两种方式进行提醒
}
