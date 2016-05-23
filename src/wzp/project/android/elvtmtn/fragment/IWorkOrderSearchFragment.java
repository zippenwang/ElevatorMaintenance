package wzp.project.android.elvtmtn.fragment;

import wzp.project.android.elvtmtn.activity.base.IBaseFragmentOrActivity;

public interface IWorkOrderSearchFragment extends IBaseFragmentOrActivity {
	
	void updateInterface();
	
	void closePullUpToRefresh();
	
	void openPullUpToRefresh();
	
	void hidePtrlvAndShowLinearLayout(String info);				// 隐藏PullToRefreshListView控件，显示LinearLayout控件

	void setIsPtrlvHidden(boolean isPtrlvHidden);
}
