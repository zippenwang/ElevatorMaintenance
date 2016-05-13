package wzp.project.android.elvtmtn.fragment;

public interface IOrderSortFragment {

	void sortMaintainOrderByFinalTimeIncrease();
	
	void sortMaintainOrderByFinalTimeDecrease();
	
	void sortFaultOrderByOccurredTimeIncrease();
	
	void sortFaultOrderByOccurredTimeDecrease();
	
	void sortMaintainOrderByReceivingTime();
	
	void sortMaintainOrderByFinishedTimeIncrease();
	
	void sortMaintainOrderByFinishedTimeDecrease();
	
	void updateInterface();
	
	void showToast(String tipInfo);
}
