package wzp.project.android.elvtmtn.activity;

import wzp.project.android.elvtmtn.activity.base.IBaseFragmentOrActivity;

public interface IElevatorRecordSearchActivity extends IBaseFragmentOrActivity {
	
	void updateInterface();
	
	void closePullUpToRefresh();
	
	void openPullUpToRefresh();
	
	void hidePtrlvAndShowLinearLayout(String info);	// 隐藏PullToRefreshListView控件，显示LinearLayout控件
	
	void setIsPtrlvHidden(boolean isPtrlvHidden);
	
	void hideBtnRefresh();
}
