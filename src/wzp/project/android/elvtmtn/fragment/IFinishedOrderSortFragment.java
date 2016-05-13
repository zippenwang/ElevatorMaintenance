package wzp.project.android.elvtmtn.fragment;

/**
 * 已完成工单的排序接口
 * @author Zippen
 *
 */
public interface IFinishedOrderSortFragment {
	
	/*void sortMaintainOrderByFinalTimeIncrease();
	
	void sortMaintainOrderByFinalTimeDecrease();
	
	void sortFaultOrderByOccurredTimeIncrease();
	
	void sortFaultOrderByOccurredTimeDecrease();
	
	void sortMaintainOrderByReceivingTime();
	
	void sortFaultOrderByReceivingTime();*/
	
	void sortMaintainOrderByFinishedTimeIncrease();
	
	void sortMaintainOrderByFinishedTimeDecrease();
	
	void sortFaultOrderByFixedTimeIncrease();
	
	void sortFaultOrderByFixedTimeDecrease();
	
	void updateInterface();
	
	void showToast(String tipInfo);
}
