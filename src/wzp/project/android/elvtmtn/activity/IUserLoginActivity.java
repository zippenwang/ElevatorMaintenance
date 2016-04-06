package wzp.project.android.elvtmtn.activity;

public interface IUserLoginActivity {

	void loginSuccess();
	
	void loginFail();
	
	void showProgressDialog();
	
	void closeProgressDialog();
	
	void showToast(String text);
}
