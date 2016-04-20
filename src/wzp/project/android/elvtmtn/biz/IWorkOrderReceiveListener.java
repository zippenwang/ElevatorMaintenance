package wzp.project.android.elvtmtn.biz;

public interface IWorkOrderReceiveListener {

	void onReceiveSuccess();
	
	void onReceiveFailure(String tipInfo);
}
