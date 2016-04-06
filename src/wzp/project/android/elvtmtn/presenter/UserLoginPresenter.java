package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IUserLoginActivity;
import wzp.project.android.elvtmtn.biz.IUserBiz;
import wzp.project.android.elvtmtn.biz.IUserLoginListener;
import wzp.project.android.elvtmtn.biz.impl.UserBizImpl;
import wzp.project.android.elvtmtn.entity.User;

public class UserLoginPresenter {

	private static IUserBiz userBiz = new UserBizImpl();
	private IUserLoginActivity userLoginActivity;
	
	
	public UserLoginPresenter(IUserLoginActivity userLoginActivity) {
		this.userLoginActivity = userLoginActivity;
	}
	
	public void login(String userId, String password) {
		User user = new User(userId, password);

		userLoginActivity.showProgressDialog();
		
		userBiz.login(user, new IUserLoginListener() {
			@Override
			public void onLoginSuccess() {
				userLoginActivity.closeProgressDialog();
				userLoginActivity.loginSuccess();
			}

			@Override
			public void onLoginFailure() {
				userLoginActivity.closeProgressDialog();
				userLoginActivity.loginFail();
			}

			@Override
			public void onServerException() {
				userLoginActivity.closeProgressDialog();
				userLoginActivity.showToast("访问服务器失败\n请检查网络连接后重试");
			}
		});
	}
}
