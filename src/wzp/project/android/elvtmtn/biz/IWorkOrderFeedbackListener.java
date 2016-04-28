package wzp.project.android.elvtmtn.biz;

public interface IWorkOrderFeedbackListener {

	void onFeedbackSuccess();
	
	void onFeedbackFailure(String tipInfo);
	
	void onAfter();
}
