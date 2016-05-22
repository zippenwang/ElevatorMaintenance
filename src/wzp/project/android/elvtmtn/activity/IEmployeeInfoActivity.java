package wzp.project.android.elvtmtn.activity;

import java.util.List;

import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeInfoActivity {

	void showToast(String tipInfo);
	
	void searchSuccess(List<Employee> employeeList);
	
	void showProgressDialog();
	
	void closeProgressDialog();
	
	void backToLoginInterface();
}
