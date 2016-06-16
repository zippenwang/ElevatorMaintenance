package wzp.project.android.elvtmtn.activity;

import wzp.project.android.elvtmtn.activity.base.IBaseContainer;
import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeLoginActivity extends IBaseContainer {

	/**
	 * 登录成功
	 * 
	 * @param employee 员工实体类
	 */
	void loginSuccess(Employee employee);
	
	/**
	 * 登录失败
	 */
	void loginFail();	
}
