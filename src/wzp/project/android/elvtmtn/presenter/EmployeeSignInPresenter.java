package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IEmployeeSignInDetailActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeSignInListener;
import wzp.project.android.elvtmtn.biz.impl.EmployeeBizImpl;

public class EmployeeSignInPresenter {

	private static IEmployeeBiz employeeBiz = new EmployeeBizImpl();
	private IEmployeeSignInDetailActivity employeeSignInDetailActivity;
	
	
	public EmployeeSignInPresenter(IEmployeeSignInDetailActivity employeeSignInActivity) {
		this.employeeSignInDetailActivity = employeeSignInActivity;
	}
	
	public void signIn(int workOrderType, Long workOrderId, String signInAddress) {
		employeeBiz.signIn(workOrderType, workOrderId, 
				signInAddress, new IEmployeeSignInListener() {
			@Override
			public void onSignInSuccess() {
				employeeSignInDetailActivity.signInSuccess();
			}
			
			@Override
			public void onSignInFailure(String tipInfo) {
				employeeSignInDetailActivity.showToast(tipInfo);
			}
		});
	}

}
