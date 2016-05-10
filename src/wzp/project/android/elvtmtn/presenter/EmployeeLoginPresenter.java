package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IEmployeeLoginActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeLoginListener;
import wzp.project.android.elvtmtn.biz.impl.EmployeeBizImpl;
import wzp.project.android.elvtmtn.entity.Employee;

public class EmployeeLoginPresenter {

	private static IEmployeeBiz employeeBiz = new EmployeeBizImpl();
	private IEmployeeLoginActivity employeeLoginActivity;
	
	
	public EmployeeLoginPresenter(IEmployeeLoginActivity employeeLoginActivity) {
		this.employeeLoginActivity = employeeLoginActivity;
	}
	
	public void login(String username, String password) {
		Employee employee = new Employee(username, password);

		employeeLoginActivity.showProgressDialog();
		
		employeeBiz.login(employee, new IEmployeeLoginListener() {
			@Override
			public void onLoginSuccess(Employee employee) {
				employeeLoginActivity.loginSuccess(employee);
			}

			@Override
			public void onLoginFailure() {
				employeeLoginActivity.loginFail();
			}

			@Override
			public void onServerException(String tipInfo) {
				employeeLoginActivity.showToast(tipInfo);
			}

			@Override
			public void onAfter() {
				employeeLoginActivity.closeProgressDialog();
			}
		});
	}
}
