package wzp.project.android.elvtmtn.biz.listener;

import java.util.List;

import wzp.project.android.elvtmtn.biz.listener.base.IBaseListener;
import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeInfoSearchListener extends IBaseListener {

	void onSearchSuccess(List<Employee> employeeList);
	
	void onSearchFailure(String tipInfo);
	
	void onAfter();
}
