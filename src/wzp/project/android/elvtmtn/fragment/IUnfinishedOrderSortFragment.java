package wzp.project.android.elvtmtn.fragment;

import wzp.project.android.elvtmtn.activity.IWorkOrderSortContainer;

/**
 * 未完成、超期工单的排序接口
 * @author Zippen
 *
 */
public interface IUnfinishedOrderSortFragment extends IWorkOrderSortContainer {

	/**
	 * 对保养工单，按截止日期的顺序排列
	 */
	void sortMaintainOrderByFinalTimeIncrease();
	
	/**
	 * 对保养工单，按截止日期的逆序排列
	 */
	void sortMaintainOrderByFinalTimeDecrease();
	
	/**
	 * 对故障工单，按故障发生日期的顺序排列
	 */
	void sortFaultOrderByOccurredTimeIncrease();
	
	/**
	 * 对故障工单，按故障发生日期的逆序排列
	 */
	void sortFaultOrderByOccurredTimeDecrease();
	
	/**
	 * 对保养工单，按接单日期的逆序排列
	 */
	void sortMaintainOrderByReceivingTime();
	
	/**
	 * 对故障工单，按接单日期的逆序排列
	 */
	void sortFaultOrderByReceivingTime();
}
