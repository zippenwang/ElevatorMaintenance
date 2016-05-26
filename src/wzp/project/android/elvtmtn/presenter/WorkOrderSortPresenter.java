package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import wzp.project.android.elvtmtn.activity.IWorkOrderSortContainer;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IFinishedOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IOverdueOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IUnfOvdOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IUnfinishedOrderSortFragment;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;

public class WorkOrderSortPresenter {

	private static IWorkOrderBiz workOrderBiz = new  WorkOrderBizImpl();
	private IWorkOrderSortContainer workOrderSortContainer;
	
	
	public WorkOrderSortPresenter(IWorkOrderSortContainer workOrderSortContainer) {
		this.workOrderSortContainer = workOrderSortContainer;
	}
	
	
	public void sortMaintainOrderByFinalTimeIncrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinalTimeIncrease(maintainOrderList);	
		workOrderSortContainer.updateInterface();
	}
	
	public void sortMaintainOrderByFinalTimeDecrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinalTimeDecrease(maintainOrderList);	
		workOrderSortContainer.updateInterface();
	}
	
	public void sortFaultOrderByOccurredTimeIncrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByOccurredTimeIncrease(faultOrderList);
		workOrderSortContainer.updateInterface();
	}
	
	public void sortFaultOrderByOccurredTimeDecrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByOccurredTimeDecrease(faultOrderList);
		workOrderSortContainer.updateInterface();
	}
	
	public void sortMaintainOrderByReceivingTime(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByReceivingTime(maintainOrderList);	
		workOrderSortContainer.updateInterface();
	}
	
	public void sortFaultOrderByReceivingTime(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByReceivingTime(faultOrderList);
		workOrderSortContainer.updateInterface();
	}
	
	public void sortMaintainOrderByFinishedTimeIncrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinishedTimeIncrease(maintainOrderList);
		workOrderSortContainer.updateInterface();
	}
	
	public void sortMaintainOrderByFinishedTimeDecrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinishedTimeDecrease(maintainOrderList);
		workOrderSortContainer.updateInterface();
	}
	
	public void sortFaultOrderByFinishedTimeIncrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByFinishedTimeIncrease(faultOrderList);
		workOrderSortContainer.updateInterface();
	}
	
	public void sortFaultOrderByFinishedTimeDecrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByFinishedTimeDecrease(faultOrderList);
		workOrderSortContainer.updateInterface();
	}
	
	public void sortMaintainOrderBySignInTime(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderBySignInTime(maintainOrderList);	
		workOrderSortContainer.updateInterface();
	}
	
	public void sortFaultOrderBySignInTime(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderBySignInTime(faultOrderList);	
		workOrderSortContainer.updateInterface();
	}
	
}
