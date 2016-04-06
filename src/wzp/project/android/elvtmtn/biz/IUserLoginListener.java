package wzp.project.android.elvtmtn.biz;

public interface IUserLoginListener {

	void onLoginSuccess();
	
	void onLoginFailure();
	
	void onServerException();
}
