package wzp.project.android.elvtmtn.biz.listener;

import wzp.project.android.elvtmtn.biz.listener.base.IBaseListener;

public interface IWorkOrderCancelListener extends IBaseListener {

	void onCancelSuccess();
	
	void onCancelFailure(String tipInfo);
}
