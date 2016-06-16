package wzp.project.android.elvtmtn.activity;

import java.util.Date;

import wzp.project.android.elvtmtn.activity.base.IBaseContainer;

public interface IWorkOrderDetailActivity extends IBaseContainer {
	
	void receiveSuccess(Date receivingTime);
	
	void cancelReceiveSuccess();
}
