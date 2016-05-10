package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;

public class WorkOrderSearchPresenter implements IWorkOrderSearchListener {

	private static IWorkOrderBiz workOrderBiz = new WorkOrderBizImpl();
	private IWorkOrderSearchFragment workOrderSearchFragment;
	

	public WorkOrderSearchPresenter(IWorkOrderSearchFragment workOrderSearchFragment) {
		this.workOrderSearchFragment = workOrderSearchFragment;
	}
	
	public void searchMaintainOrder(long groupId, int workOrderState, 
			int pageNumber, int pageSize, List<MaintainOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getMaintainOrdersByCondition(groupId, workOrderState, 
				pageNumber, pageSize, dataList, this);
	}
	
	public void searchFaultOrder(long groupId, int workOrderState, 
			int pageNumber, int pageSize, List<FaultOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getFaultOrdersByCondition(groupId, workOrderState, 
				pageNumber, pageSize, dataList, this);
	}
	
	public void searchReceivedMaintainOrders(long employeeId, int pageNumber, 
			int pageSize, List<MaintainOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getReceivedMaintainOrdersByCondition(employeeId, pageNumber, 
				pageSize, dataList, this);
	}
	
	public void searchReceivedFaultOrders(long employeeId, int pageNumber, 
			int pageSize, List<FaultOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getReceivedFaultOrdersByCondition(employeeId, pageNumber, 
				pageSize, dataList, this);	
	}
	
	public void searchSignedInFaultOrders(long employeeId, int pageNumber, 
			int pageSize, List<FaultOrder> faultOrderList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getSignedInFaultOrdersByCondition(employeeId, pageNumber, 
				pageSize, faultOrderList, this);
	}
	
	public void searchSignedInMaintainOrders(long employeeId, int pageNumber, 
			int pageSize, List<MaintainOrder> maintainOrderList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getSignedInMaintainOrdersByCondition(employeeId, pageNumber, 
				pageSize, maintainOrderList, this);
	}
	

	@Override
	public void onSearchSuccess(int successType) {
		workOrderSearchFragment.setIsPtrlvHidden(false);
		
		if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
			workOrderSearchFragment.showToast("当前已经显示出所有工单了");
			workOrderSearchFragment.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
		} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
			workOrderSearchFragment.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
		} else if (successType == ProjectContants.ORDER_IS_NULL) {
			// 该条件下的工单不存在
			workOrderSearchFragment.hidePtrlvAndShowLinearLayout("符合条件的工单不存在...");
			return;
		}
		
		workOrderSearchFragment.updateInterface();		
	}

	@Override
	public void onSearchFailure(String tipInfo) {
		workOrderSearchFragment.showToast(tipInfo);
		workOrderSearchFragment.hidePtrlvAndShowLinearLayout("服务器正在打盹，请检查网络后重试...");
	}

	@Override
	public void onAfter() {
		workOrderSearchFragment.closeProgressDialog();
	}
	
	
}
