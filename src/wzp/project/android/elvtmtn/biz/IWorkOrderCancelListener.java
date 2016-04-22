package wzp.project.android.elvtmtn.biz;

public interface IWorkOrderCancelListener {

	void onCancelSuccess();
	
	void onCancelFailure(String tipInfo);
}
