package wzp.project.android.elvtmtn.biz;

import java.util.List;

import wzp.project.android.elvtmtn.entity.ElevatorRecord;

public interface IElevatorRecordBiz {

	void getAllElevatorRecords(int pageNumber, int pageSize, 
			List<ElevatorRecord> elevatorRecordList, IElevatorRecordSearchListener listener);
	
	void getAllElevatorRecords(long groupId, int pageNumber, int pageSize, 
			List<ElevatorRecord> elevatorRecordList, IElevatorRecordSearchListener listener);
	
	void getElevatorRecordsByCondition(int pageNumber, int pageSize, String searchParam, 
			List<ElevatorRecord> elevatorRecordList, IElevatorRecordSearchListener listener);
}
