package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;

public class WorkOrderSortPresenter {

	private static IWorkOrderBiz workOrderBiz = new  WorkOrderBizImpl();
	private IWorkOrderSearchFragment workOrderSearchFragment;
	
	
	public WorkOrderSortPresenter(IWorkOrderSearchFragment workOrderSearchFragment) {
		this.workOrderSearchFragment = workOrderSearchFragment;
	}
	
	public void sortMaintainOrderByFinalTime(List<MaintainOrder> maintainOrderList) {
		if (maintainOrderList.size() == 0) {
			workOrderSearchFragment.showToast("当前不存在工单");
			return;
		}
		
		workOrderBiz.sortMaintainOrderByFinalTime(maintainOrderList);	
		workOrderSearchFragment.updateInterface();
	}
	
	
}
