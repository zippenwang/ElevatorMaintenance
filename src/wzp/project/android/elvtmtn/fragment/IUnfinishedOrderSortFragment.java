package wzp.project.android.elvtmtn.fragment;

/**
 * 未完成、超期工单的排序接口
 * @author Zippen
 *
 */
public interface IUnfinishedOrderSortFragment {

	void sortMaintainOrderByFinalTimeIncrease();
	
	void sortMaintainOrderByFinalTimeDecrease();
	
	void sortFaultOrderByOccurredTimeIncrease();
	
	void sortFaultOrderByOccurredTimeDecrease();
	
	void sortMaintainOrderByReceivingTime();
	
	void sortFaultOrderByReceivingTime();
	
	void updateInterface();
	
	void showToast(String tipInfo);
}
