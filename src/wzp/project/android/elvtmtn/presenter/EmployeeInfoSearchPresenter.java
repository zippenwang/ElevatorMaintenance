package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import android.content.Intent;

import wzp.project.android.elvtmtn.activity.IEmployeeInfoActivity;
import wzp.project.android.elvtmtn.activity.IEmployeeSignInDetailActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeBiz;
import wzp.project.android.elvtmtn.biz.IEmployeeInfoSearchListener;
import wzp.project.android.elvtmtn.biz.IEmployeeSignInListener;
import wzp.project.android.elvtmtn.biz.impl.EmployeeBizImpl;
import wzp.project.android.elvtmtn.entity.Employee;

public class EmployeeInfoSearchPresenter {

	private static IEmployeeBiz employeeBiz = new EmployeeBizImpl();
	private IEmployeeInfoActivity employeeInfoActivity;
	
	
	public EmployeeInfoSearchPresenter(IEmployeeInfoActivity employeeInfoActivity) {
		this.employeeInfoActivity = employeeInfoActivity;
	}
	
	public void searchEmployeeInfo(long groupId) {
		employeeInfoActivity.showProgressDialog();
		employeeBiz.getInfo(groupId, new IEmployeeInfoSearchListener() {
			@Override
			public void onSearchSuccess(List<Employee> employeeList) {
				employeeInfoActivity.searchSuccess(employeeList);
			}
			
			@Override
			public void onSearchFailure(String tipInfo) {
				employeeInfoActivity.showToast(tipInfo);
			}

			@Override
			public void onAfter() {
				employeeInfoActivity.closeProgressDialog();
			}

			@Override
			public void onBackToLoginInterface() {
				employeeInfoActivity.backToLoginInterface();
			}
		});
	}


}
