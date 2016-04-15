package wzp.project.android.elvtmtn.fragment;

public interface IWorkOrderSearchFragment {
	
	void showProgressDialog();
	
	void closeProgressDialog();
	
	void showToast(String text);
	
//	void remindDataIsNull();
	
	void updateInterface();
	
	void closePullUpToRefresh();
	
//	void remindExceptionInfo();
	
	void hidePtrlvAndShowLinearLayout(String info);				// 隐藏PullToRefreshListView控件，显示LinearLayout控件
	
	void hideLinearLayoutAndShowPtrlv();
}
