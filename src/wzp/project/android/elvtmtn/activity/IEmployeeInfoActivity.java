package wzp.project.android.elvtmtn.activity;

import java.util.List;

import wzp.project.android.elvtmtn.activity.base.IBaseContainer;
import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeInfoActivity extends IBaseContainer {
	
	void searchSuccess(List<Employee> employeeList);
}
