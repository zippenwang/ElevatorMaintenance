package wzp.project.android.elvtmtn.fragment;

import wzp.project.android.elvtmtn.activity.IWorkOrderSortContainer;

/**
 * 已完成工单的排序接口
 * @author Zippen
 *
 */
public interface IFinishedOrderSortFragment extends IWorkOrderSortContainer {
	
	void sortMaintainOrderByFinishedTimeIncrease();
	
	void sortMaintainOrderByFinishedTimeDecrease();
	
	void sortFaultOrderByFixedTimeIncrease();
	
	void sortFaultOrderByFixedTimeDecrease();
}
