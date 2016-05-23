package wzp.project.android.elvtmtn.biz.listener;

import java.util.Date;

import wzp.project.android.elvtmtn.biz.listener.base.IBaseListener;

public interface IWorkOrderReceiveListener extends IBaseListener {

	void onReceiveSuccess(Date receivingTime);
	
	void onReceiveFailure(String tipInfo);
}
