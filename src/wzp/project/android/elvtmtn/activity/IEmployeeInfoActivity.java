package wzp.project.android.elvtmtn.activity;

import java.util.List;

import wzp.project.android.elvtmtn.activity.base.IBaseFragmentOrActivity;
import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeInfoActivity extends IBaseFragmentOrActivity {
	
	void searchSuccess(List<Employee> employeeList);
}
