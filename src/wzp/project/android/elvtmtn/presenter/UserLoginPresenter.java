package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IEmployeeLoginActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeLoginListener;
import wzp.project.android.elvtmtn.biz.impl.UserBizImpl;
import wzp.project.android.elvtmtn.entity.Employee;

public class UserLoginPresenter {

	private static IEmployeeBiz userBiz = new UserBizImpl();
	private IEmployeeLoginActivity userLoginActivity;
	
	
	public UserLoginPresenter(IEmployeeLoginActivity userLoginActivity) {
		this.userLoginActivity = userLoginActivity;
	}
	
	public void login(String username, String password) {
		Employee employee = new Employee(username, password);

		userLoginActivity.showProgressDialog();
		
		userBiz.login(employee, new IEmployeeLoginListener() {
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
