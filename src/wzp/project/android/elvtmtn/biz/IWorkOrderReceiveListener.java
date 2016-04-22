package wzp.project.android.elvtmtn.biz;

import java.util.Date;

public interface IWorkOrderReceiveListener {

	void onReceiveSuccess(Date receivingTime);
	
	void onReceiveFailure(String tipInfo);
}
