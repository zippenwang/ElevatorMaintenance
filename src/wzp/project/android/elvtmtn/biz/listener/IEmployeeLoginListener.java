package wzp.project.android.elvtmtn.biz.listener;

import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeLoginListener {
	
	void onAfter();

	void onLoginSuccess(Employee employee);
	
	void onLoginFailure(String tipInfo);
	
//	void onServerException();
}
