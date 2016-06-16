package wzp.project.android.elvtmtn.activity;

import wzp.project.android.elvtmtn.activity.base.IBaseContainer;

public interface IWorkOrderSearchContainer extends IBaseContainer {
	
	/**
	 * 更新界面
	 */
	void updateInterface();
	
	/**
	 * 关闭上拉加载的功能
	 */
	void closePullUpToRefresh();
	
	/**
	 * 打开上拉加载更多的功能
	 */
	void openPullUpToRefresh();
	
	/**
	 * 隐藏PullToRefreshListView控件，显示提示信息
	 */
	void hidePtrlvAndShowLinearLayout(String info);				// 隐藏PullToRefreshListView控件，显示LinearLayout控件

}
