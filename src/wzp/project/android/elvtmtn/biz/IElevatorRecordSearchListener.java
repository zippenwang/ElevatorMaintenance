package wzp.project.android.elvtmtn.biz;

public interface IElevatorRecordSearchListener {

	void onSearchSuccess(int successType);
	
	void onSearchFailure(String tipInfo);
	
	void onAfter();
}
