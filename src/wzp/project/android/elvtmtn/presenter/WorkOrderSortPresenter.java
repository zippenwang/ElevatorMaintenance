package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import wzp.project.android.elvtmtn.activity.IEmployeeSignInActivity;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IFinishedOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IUnfOvdOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;

public class WorkOrderSortPresenter {

	private static IWorkOrderBiz workOrderBiz = new  WorkOrderBizImpl();
	private IUnfOvdOrderSortFragment unfOvdOrderSortFragment;
	private IFinishedOrderSortFragment finishedOrderSortFragment;
	private IEmployeeSignInActivity employeeSignInActivity;
	
	
	public WorkOrderSortPresenter(IUnfOvdOrderSortFragment unfOvdOrderSortFragment) {
		this.unfOvdOrderSortFragment = unfOvdOrderSortFragment;
	}
	
	public WorkOrderSortPresenter(IFinishedOrderSortFragment finishedOrderSortFragment) {
		this.finishedOrderSortFragment = finishedOrderSortFragment;
	}
	
	public WorkOrderSortPresenter(IEmployeeSignInActivity employeeSignInActivity) {
		this.employeeSignInActivity = employeeSignInActivity;
	}
	
	public void sortMaintainOrderByFinalTimeIncrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			unfOvdOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinalTimeIncrease(maintainOrderList);	
		unfOvdOrderSortFragment.updateInterface();
	}
	
	public void sortMaintainOrderByFinalTimeDecrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			unfOvdOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinalTimeDecrease(maintainOrderList);	
		unfOvdOrderSortFragment.updateInterface();
	}
	
	public void sortFaultOrderByOccurredTimeIncrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			unfOvdOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByOccurredTimeIncrease(faultOrderList);	
		unfOvdOrderSortFragment.updateInterface();
	}
	
	public void sortFaultOrderByOccurredTimeDecrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			unfOvdOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByOccurredTimeDecrease(faultOrderList);	
		unfOvdOrderSortFragment.updateInterface();
	}
	
	public void sortMaintainOrderByReceivingTime(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			unfOvdOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByReceivingTime(maintainOrderList);	
		unfOvdOrderSortFragment.updateInterface();
	}
	
	public void sortFaultOrderByReceivingTime(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			unfOvdOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByReceivingTime(faultOrderList);	
		unfOvdOrderSortFragment.updateInterface();
	}
	
	public void sortMaintainOrderByFinishedTimeIncrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			finishedOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinishedTimeIncrease(maintainOrderList);	
		finishedOrderSortFragment.updateInterface();
	}
	
	public void sortMaintainOrderByFinishedTimeDecrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			finishedOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinishedTimeDecrease(maintainOrderList);	
		finishedOrderSortFragment.updateInterface();
	}
	
	public void sortFaultOrderByFinishedTimeIncrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			finishedOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByFinishedTimeIncrease(faultOrderList);	
		finishedOrderSortFragment.updateInterface();
	}
	
	public void sortFaultOrderByFinishedTimeDecrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			finishedOrderSortFragment.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByFinishedTimeDecrease(faultOrderList);	
		finishedOrderSortFragment.updateInterface();
	}
	
	public void sortMaintainOrderBySignInTime(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			employeeSignInActivity.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderBySignInTime(maintainOrderList);	
		employeeSignInActivity.updateInterface();
	}
	
	public void sortFaultOrderBySignInTime(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			employeeSignInActivity.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderBySignInTime(faultOrderList);	
		employeeSignInActivity.updateInterface();
	}
}
