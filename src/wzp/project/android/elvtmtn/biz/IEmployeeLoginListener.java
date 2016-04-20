package wzp.project.android.elvtmtn.biz;

import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeLoginListener {
	
	void onAfter();

	void onLoginSuccess(Employee employee);
	
	void onLoginFailure();
	
	void onServerException(String tipInfo);
}
