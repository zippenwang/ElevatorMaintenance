package wzp.project.android.elvtmtn.activity;

public interface IWorkOrderFeedbackActivity {

	void feedbackSuccess();
	
	void showToast(String tipInfo);
	
	void showProgressDialog(String tipInfo);
	
	void closeProgressDialog();
}
