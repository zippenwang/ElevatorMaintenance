package wzp.project.android.elvtmtn.biz;

import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeBiz {

	void login(Employee employee, IEmployeeLoginListener listener);
}
