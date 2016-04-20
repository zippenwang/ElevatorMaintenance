package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IWorkOrderDetailActivity;
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.IWorkOrderReceiveListener;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;

public class WorkOrderReceivePresenter {

	private static IWorkOrderBiz workOrderBiz = new WorkOrderBizImpl();
	private IWorkOrderDetailActivity workOrderDetailActivity;
	
	
	public WorkOrderReceivePresenter(IWorkOrderDetailActivity workOrderDetailActivity) {
		this.workOrderDetailActivity = workOrderDetailActivity;
	}
	
	public void receiveOrder(Long workOrderId, Long employeeId) {
		workOrderBiz.receiveOrder(workOrderId, employeeId, new IWorkOrderReceiveListener() {	
			@Override
			public void onReceiveSuccess() {
				workOrderDetailActivity.receiveSuccess();
			}
			
			@Override
			public void onReceiveFailure(String tipInfo) {
				workOrderDetailActivity.showToast(tipInfo);
			}
		});
	}
	

	

}
