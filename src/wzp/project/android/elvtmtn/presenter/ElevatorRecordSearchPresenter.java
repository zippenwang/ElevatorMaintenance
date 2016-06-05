package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import wzp.project.android.elvtmtn.activity.IElevatorRecordSearchActivity;
import wzp.project.android.elvtmtn.activity.impl.EmployeeLoginActivity;
import wzp.project.android.elvtmtn.biz.IElevatorRecordBiz;
import wzp.project.android.elvtmtn.biz.impl.ElevatorRecordBizImpl;
import wzp.project.android.elvtmtn.biz.listener.IElevatorRecordSearchListener;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;

public class ElevatorRecordSearchPresenter implements IElevatorRecordSearchListener {

	private static IElevatorRecordBiz elevatorRecordBiz = new ElevatorRecordBizImpl();
	private IElevatorRecordSearchActivity elevatorRecordSearchActivity;
	
	
	public ElevatorRecordSearchPresenter(IElevatorRecordSearchActivity elevatorRecordSearchActivity) {
		this.elevatorRecordSearchActivity = elevatorRecordSearchActivity;
	}
	
	public void searchAllElevatorRecords(int pageNumber, int pageSize, List<ElevatorRecord> elevatorRecordList) {
		elevatorRecordSearchActivity.showProgressDialog();
		elevatorRecordBiz.getAllElevatorRecords(pageNumber, 
				pageSize, elevatorRecordList, this);
	}
	
	public void searchElevatorRecordsByCondition(int pageNumber, int pageSize, String searchParam,
			List<ElevatorRecord> elevatorRecordList) {
		elevatorRecordSearchActivity.showProgressDialog();
		elevatorRecordBiz.getElevatorRecordsByCondition(pageNumber, pageSize, 
				searchParam, elevatorRecordList,
				new IElevatorRecordSearchListener() {
					@Override
					public void onSearchSuccess(int successType) {
						elevatorRecordSearchActivity.setIsPtrlvHidden(false);
						
						if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
//							elevatorRecordSearchActivity.showToast("当前已经显示出所有档案");
							elevatorRecordSearchActivity.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
						} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
							elevatorRecordSearchActivity.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
						} else if (successType == ProjectContants.ORDER_IS_NULL) {
							// 该条件下的工单不存在
							elevatorRecordSearchActivity.hidePtrlvAndShowLinearLayout("符合条件的档案不存在");
							elevatorRecordSearchActivity.hideBtnRefresh();
							return;
						}
						
						elevatorRecordSearchActivity.updateInterface();		
					}
					
					@Override
					public void onSearchFailure(String tipInfo) {
//						elevatorRecordSearchActivity.showToast(tipInfo);
						elevatorRecordSearchActivity.hidePtrlvAndShowLinearLayout("服务器正在打盹，请检查网络后重试...");
					}
					
					@Override
					public void onAfter() {
						elevatorRecordSearchActivity.closeProgressDialog();
					}
					
					@Override
					public void onBackToLoginInterface() {
						elevatorRecordSearchActivity.backToLoginInterface();
					}
				});
	}
	
	@Override
	public void onSearchSuccess(int successType) {
		elevatorRecordSearchActivity.setIsPtrlvHidden(false);
		
		if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
//			elevatorRecordSearchActivity.showToast("当前已经显示出所有档案");
			elevatorRecordSearchActivity.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
		} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
			elevatorRecordSearchActivity.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
		} else if (successType == ProjectContants.ORDER_IS_NULL) {
			// 该条件下的工单不存在
			elevatorRecordSearchActivity.hidePtrlvAndShowLinearLayout("符合条件的档案不存在");
			return;
		}
		
		elevatorRecordSearchActivity.updateInterface();		
	}
	
	@Override
	public void onSearchFailure(String tipInfo) {
//		elevatorRecordSearchActivity.showToast(tipInfo);
		elevatorRecordSearchActivity.hidePtrlvAndShowLinearLayout(tipInfo);
	}
	
	@Override
	public void onAfter() {
		elevatorRecordSearchActivity.closeProgressDialog();
	}

	@Override
	public void onBackToLoginInterface() {
		elevatorRecordSearchActivity.backToLoginInterface();
	}
}
