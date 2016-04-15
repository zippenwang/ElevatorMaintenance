package wzp.project.android.elvtmtn.biz;

import java.util.List;

public interface IWorkOrderBiz {

	<T> void getWorkOrdersByCondition(int workOrderType, int workOrderState, int page, int pageSize, List<T> dataList,
			IWorkOrderSearchListener listener);
	
	
}
