package wzp.project.android.elvtmtn.biz.listener;

import wzp.project.android.elvtmtn.biz.listener.base.IBaseListener;

public interface IWorkOrderFeedbackListener extends IBaseListener {

	void onFeedbackSuccess();
	
	void onFeedbackFailure(String tipInfo);
	
	void onAfter();
}
