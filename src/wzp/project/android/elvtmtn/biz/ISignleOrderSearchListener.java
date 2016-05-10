package wzp.project.android.elvtmtn.biz;

public interface ISignleOrderSearchListener {

	void onSearchFailure(String tipInfo);
	
	void onSearchSuccess(String jsonOrder);
	
}
