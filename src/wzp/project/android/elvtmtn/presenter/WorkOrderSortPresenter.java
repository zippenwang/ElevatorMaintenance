package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import wzp.project.android.elvtmtn.activity.IWorkOrderSortContainer;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;

public class WorkOrderSortPresenter {

	private static IWorkOrderBiz workOrderBiz = new  WorkOrderBizImpl();
	private IWorkOrderSortContainer workOrderSortContainer;
	
	
	public WorkOrderSortPresenter(IWorkOrderSortContainer workOrderSortContainer) {
		this.workOrderSortContainer = workOrderSortContainer;
	}
	
	/**
	 * 对保养工单，按截止日期的顺序排列
	 * 
	 * @param maintainOrderList 保养工单集合
	 */
	public void sortMaintainOrderByFinalTimeIncrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinalTimeIncrease(maintainOrderList);	
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对保养工单，按截止日期的逆序排列
	 * 
	 * @param maintainOrderList 保养工单集合
	 */
	public void sortMaintainOrderByFinalTimeDecrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinalTimeDecrease(maintainOrderList);	
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对故障工单，按故障发生日期的顺序排列
	 * 
	 * @param faultOrderList 故障工单集合
	 */
	public void sortFaultOrderByOccurredTimeIncrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByOccurredTimeIncrease(faultOrderList);
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对故障工单，按故障发生日期的逆序排列
	 * 
	 * @param faultOrderList 故障工单集合
	 */
	public void sortFaultOrderByOccurredTimeDecrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByOccurredTimeDecrease(faultOrderList);
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对保养工单，按接单日期的逆序排列
	 * 
	 * @param maintainOrderList 保养工单集合
	 */
	public void sortMaintainOrderByReceivingTime(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByReceivingTime(maintainOrderList);	
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对故障工单，按接单日期的逆序排列
	 * 
	 * @param faultOrderList 保养工单集合
	 */
	public void sortFaultOrderByReceivingTime(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByReceivingTime(faultOrderList);
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对保养工单，按完成日期的顺序排列
	 * 
	 * @param maintainOrderList 保养工单集合
	 */
	public void sortMaintainOrderByFinishedTimeIncrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinishedTimeIncrease(maintainOrderList);
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对保养工单，按完成日期的逆序排列
	 * 
	 * @param maintainOrderList 保养工单集合
	 */
	public void sortMaintainOrderByFinishedTimeDecrease(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinishedTimeDecrease(maintainOrderList);
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对故障工单，按完成日期的顺序排列
	 * 
	 * @param faultOrderList 故障工单集合
	 */
	public void sortFaultOrderByFinishedTimeIncrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByFinishedTimeIncrease(faultOrderList);
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对故障工单，按完成日期的逆序排列
	 * 
	 * @param faultOrderList 故障工单集合
	 */
	public void sortFaultOrderByFinishedTimeDecrease(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderByFinishedTimeDecrease(faultOrderList);
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对保养工单，按签到日期的逆序排列
	 * 
	 * @param maintainOrderList 保养工单集合
	 */
	public void sortMaintainOrderBySignInTime(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortMaintainOrderBySignInTime(maintainOrderList);	
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
	/**
	 * 对故障工单，按签到日期的逆序排列
	 * 
	 * @param faultOrderList 故障工单集合
	 */
	public void sortFaultOrderBySignInTime(List<FaultOrder> faultOrderList) {
		if (faultOrderList.size() == 0) {
			workOrderSortContainer.showToast("当前不存在工单，无法进行排序");
			return;
		}
		
		workOrderBiz.sortFaultOrderBySignInTime(faultOrderList);	
		workOrderSortContainer.updateInterface();
		workOrderSortContainer.locateToFirstItem();
	}
	
}
