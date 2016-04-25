package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IElevatorRecordSearchActivity;
import wzp.project.android.elvtmtn.biz.IElevatorRecordBiz;
import wzp.project.android.elvtmtn.biz.impl.ElevatorRecordBizImpl;

public class ElevatorRecordSearchPresenter {

	private static IElevatorRecordBiz elevatorRecord = new ElevatorRecordBizImpl();
	private IElevatorRecordSearchActivity elevatorRecordSearchActivity;
	
	
	public ElevatorRecordSearchPresenter(IElevatorRecordSearchActivity elevatorRecordSearchActivity) {
		this.elevatorRecordSearchActivity = elevatorRecordSearchActivity;
	}
	
//	public void search
}
