package wzp.project.android.elvtmtn.biz;

public interface IEmployeeLoginListener {

	void onLoginSuccess();
	
	void onLoginFailure();
	
	void onServerException();
}
