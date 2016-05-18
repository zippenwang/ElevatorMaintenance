package wzp.project.android.elvtmtn.activity;

public interface IElevatorRecordSearchActivity {

	void showProgressDialog();
	
	void closeProgressDialog();
	
	void showToast(String text);
	
	void updateInterface();
	
	void closePullUpToRefresh();
	
	void openPullUpToRefresh();
	
	void hidePtrlvAndShowLinearLayout(String info);	// 隐藏PullToRefreshListView控件，显示LinearLayout控件
	
	void setIsPtrlvHidden(boolean isPtrlvHidden);
	
	void hideBtnRefresh();
}
