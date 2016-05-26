package wzp.project.android.elvtmtn.fragment;

import wzp.project.android.elvtmtn.activity.IWorkOrderSortContainer;

/**
 * 未完成、超期工单的排序接口
 * @author Zippen
 *
 */
public interface IUnfinishedOrderSortFragment extends IWorkOrderSortContainer {

	void sortMaintainOrderByFinalTimeIncrease();
	
	void sortMaintainOrderByFinalTimeDecrease();
	
	void sortFaultOrderByOccurredTimeIncrease();
	
	void sortFaultOrderByOccurredTimeDecrease();
	
	void sortMaintainOrderByReceivingTime();
	
	void sortFaultOrderByReceivingTime();
}
