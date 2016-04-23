package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IEmployeeSignInActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeSignInListener;
import wzp.project.android.elvtmtn.biz.impl.EmployeeBizImpl;

public class EmployeeSignInPresenter {

	private static IEmployeeBiz employeeBiz = new EmployeeBizImpl();
	private IEmployeeSignInActivity employeeSignInActivity;
	
	
	public EmployeeSignInPresenter(IEmployeeSignInActivity employeeSignInActivity) {
		this.employeeSignInActivity = employeeSignInActivity;
	}
	
	public void signIn(int workOrderType, Long workOrderId, String signInAddress) {
		employeeBiz.signIn(workOrderType, workOrderId, 
				signInAddress, new IEmployeeSignInListener() {
			@Override
			public void onSignInSuccess() {
				employeeSignInActivity.signInSuccess();
			}
			
			@Override
			public void onSignInFailure(String tipInfo) {
				employeeSignInActivity.showToast(tipInfo);
			}
		});
	}

}
