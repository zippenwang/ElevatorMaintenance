package wzp.project.android.elvtmtn.activity;

import java.util.Date;

import wzp.project.android.elvtmtn.activity.base.IBaseFragmentOrActivity;

public interface IWorkOrderDetailActivity extends IBaseFragmentOrActivity {
	
	void receiveSuccess(Date receivingTime);
	
	void cancelReceiveSuccess();
}
