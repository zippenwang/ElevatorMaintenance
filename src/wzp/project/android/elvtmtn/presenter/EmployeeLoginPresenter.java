package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IEmployeeLoginActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeLoginListener;
import wzp.project.android.elvtmtn.biz.impl.EmployeeBizImpl;
import wzp.project.android.elvtmtn.entity.Employee;

public class EmployeeLoginPresenter {

	private static IEmployeeBiz userBiz = new EmployeeBizImpl();
	private IEmployeeLoginActivity userLoginActivity;
	
	
	public EmployeeLoginPresenter(IEmployeeLoginActivity userLoginActivity) {
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
			public void onServerException(String tipInfo) {
				userLoginActivity.closeProgressDialog();
				userLoginActivity.showToast(tipInfo);
			}
		});
	}
}
