package wzp.project.android.elvtmtn.activity;

public interface IWorkOrderFeedbackActivity {

	void feedbackSuccess();
	
	void showToast(String tipInfo);
	
	void showProgressDialog();
	
	void closeProgressDialog();
}
