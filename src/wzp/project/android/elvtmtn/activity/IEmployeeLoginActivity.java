package wzp.project.android.elvtmtn.activity;

import wzp.project.android.elvtmtn.activity.base.IBaseFragmentOrActivity;
import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeLoginActivity extends IBaseFragmentOrActivity {

	void loginSuccess(Employee employee);
	
	void loginFail();	
}
