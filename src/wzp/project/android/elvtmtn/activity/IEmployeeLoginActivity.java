package wzp.project.android.elvtmtn.activity;

import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeLoginActivity {

	void loginSuccess(Employee employee);
	
	void loginFail();
	
	void showProgressDialog();
	
	void closeProgressDialog();
	
	void showToast(String text);
}
