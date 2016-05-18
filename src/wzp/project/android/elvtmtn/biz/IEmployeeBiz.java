package wzp.project.android.elvtmtn.biz;

import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeBiz {

	void login(Employee employee, IEmployeeLoginListener listener);
	
	void signIn(int workOrderType, Long workOrderId, 
			String signInAddress, IEmployeeSignInListener listener);
	
	void getInfo(Long groupId, IEmployeeInfoSearchListener listener);
}
