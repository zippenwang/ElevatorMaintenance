package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IEmployeeSignInDetailActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.impl.EmployeeBizImpl;
import wzp.project.android.elvtmtn.biz.listener.IEmployeeSignInListener;

public class EmployeeSignInPresenter {

	private static IEmployeeBiz employeeBiz = new EmployeeBizImpl();
	private IEmployeeSignInDetailActivity employeeSignInDetailActivity;
	
	
	public EmployeeSignInPresenter(IEmployeeSignInDetailActivity employeeSignInActivity) {
		this.employeeSignInDetailActivity = employeeSignInActivity;
	}
	
	public void signIn(int workOrderType, Long workOrderId, String signInAddress) {
		employeeSignInDetailActivity.showProgressDialog("正在签到，请稍后...");
		
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

			@Override
			public void onAfter() {
				employeeSignInDetailActivity.closeProgressDialog();
			}

			@Override
			public void onBackToLoginInterface() {
				employeeSignInDetailActivity.backToLoginInterface();
			}
		});
	}

}
