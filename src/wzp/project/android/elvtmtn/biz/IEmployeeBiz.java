package wzp.project.android.elvtmtn.biz;

import wzp.project.android.elvtmtn.biz.listener.IEmployeeInfoSearchListener;
import wzp.project.android.elvtmtn.biz.listener.IEmployeeLoginListener;
import wzp.project.android.elvtmtn.biz.listener.IEmployeeSignInListener;
import wzp.project.android.elvtmtn.entity.Employee;

public interface IEmployeeBiz {

	/**
	 * 用户登录
	 * @param employee
	 * @param listener
	 */
	void login(Employee employee, IEmployeeLoginListener listener);
	
	void signIn(int workOrderType, Long workOrderId, 
			String signInAddress, IEmployeeSignInListener listener);
	
	void getInfo(Long groupId, IEmployeeInfoSearchListener listener);
}
