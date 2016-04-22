package wzp.project.android.elvtmtn.activity;

import java.util.Date;

public interface IWorkOrderDetailActivity {

	void showToast(String text);
	
	void receiveSuccess(Date receivingTime);
	
	void cancelReceiveSuccess();
}
