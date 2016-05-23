package wzp.project.android.elvtmtn.biz.listener;

import wzp.project.android.elvtmtn.biz.listener.base.IBaseListener;

public interface ISignleOrderSearchListener extends IBaseListener {

	void onSearchFailure(String tipInfo);
	
	void onSearchSuccess(String jsonOrder);
	
}
