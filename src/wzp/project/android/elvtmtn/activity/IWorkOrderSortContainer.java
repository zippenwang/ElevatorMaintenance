package wzp.project.android.elvtmtn.activity;

public interface IWorkOrderSortContainer {
	
	/**
	 * 更新界面
	 */
	void updateInterface();
	
	/**
	 * 显示Toast
	 * 
	 * @param tipInfo
	 */
	void showToast(String tipInfo);
	
	/**
	 * 定位至第一个item
	 */
	void locateToFirstItem();
}
