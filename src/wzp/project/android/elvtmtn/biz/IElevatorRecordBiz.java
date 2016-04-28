package wzp.project.android.elvtmtn.biz;

import java.util.List;

import wzp.project.android.elvtmtn.entity.ElevatorRecord;

public interface IElevatorRecordBiz {

	void getAllElevatorRecords(int pageNumber, int pageSize, 
			List<ElevatorRecord> elevatorRecordList, IElevatorRecordSearchListener listener);
}
