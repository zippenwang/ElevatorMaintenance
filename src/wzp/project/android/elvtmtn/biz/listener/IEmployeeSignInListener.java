package wzp.project.android.elvtmtn.biz.listener;

import wzp.project.android.elvtmtn.biz.listener.base.IBaseListener;

public interface IEmployeeSignInListener extends IBaseListener {

	void onSignInSuccess();
	
	void onSignInFailure(String tipInfo);
	
	void onAfter();
}
