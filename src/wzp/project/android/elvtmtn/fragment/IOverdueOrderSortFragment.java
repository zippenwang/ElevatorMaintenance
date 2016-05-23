package wzp.project.android.elvtmtn.fragment;

/**
 * 未完成、超期工单的排序接口
 * @author Zippen
 *
 */
public interface IOverdueOrderSortFragment {

	void sortMaintainOrderByFinalTimeIncrease();
	
	void sortMaintainOrderByFinalTimeDecrease();
	
	void sortMaintainOrderByReceivingTime();
	
	void updateInterface();
	
	void showToast(String tipInfo);
}
