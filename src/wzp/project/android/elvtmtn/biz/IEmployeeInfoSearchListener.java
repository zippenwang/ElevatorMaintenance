package wzp.project.android.elvtmtn.biz;

import java.util.List;

import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeInfoSearchListener {

	void onSearchSuccess(List<Employee> employeeList);
	
	void onSearchFailure(String tipInfo);
	
	void onAfter();
	
	void onBackToLoginInterface();
}
