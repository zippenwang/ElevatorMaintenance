package wzp.project.android.elvtmtn.biz;

public interface IEmployeeLoginListener {
	
	void onBefore();
	
	void onAfter();

	void onLoginSuccess();
	
	void onLoginFailure();
	
	void onServerException(String tipInfo);
}
