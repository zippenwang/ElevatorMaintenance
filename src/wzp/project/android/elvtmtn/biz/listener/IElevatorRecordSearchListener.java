package wzp.project.android.elvtmtn.biz.listener;

import wzp.project.android.elvtmtn.biz.listener.base.IBaseListener;

public interface IElevatorRecordSearchListener extends IBaseListener {

	void onSearchSuccess(int successType);
	
	void onSearchFailure(String tipInfo, int tipMethod);
	
	void onAfter();
}
