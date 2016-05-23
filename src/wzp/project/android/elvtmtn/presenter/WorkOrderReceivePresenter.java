package wzp.project.android.elvtmtn.presenter;

import java.util.Date;

import wzp.project.android.elvtmtn.activity.IWorkOrderDetailActivity;
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderCancelListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderReceiveListener;

public class WorkOrderReceivePresenter {

	private static IWorkOrderBiz workOrderBiz = new WorkOrderBizImpl();
	private IWorkOrderDetailActivity workOrderDetailActivity;
	
	
	public WorkOrderReceivePresenter(IWorkOrderDetailActivity workOrderDetailActivity) {
		this.workOrderDetailActivity = workOrderDetailActivity;
	}
	
	public void receiveOrder(int workOrderType, Long workOrderId, Long employeeId) {
		workOrderBiz.receiveOrder(workOrderType, workOrderId, employeeId, 
				new IWorkOrderReceiveListener() {	
			@Override
			public void onReceiveSuccess(Date receiveTime) {
				workOrderDetailActivity.receiveSuccess(receiveTime);
			}
			
			@Override
			public void onReceiveFailure(String tipInfo) {
				workOrderDetailActivity.showToast(tipInfo);
			}

			@Override
			public void onBackToLoginInterface() {
				workOrderDetailActivity.backToLoginInterface();
			}
		});
	}
	
	public void cancelReceiveOrder(int workOrderType, Long workOrderId, Long employeeId) {
		workOrderBiz.cancelReceiveOrder(workOrderType, workOrderId, 
				employeeId, new IWorkOrderCancelListener() {
			@Override
			public void onCancelSuccess() {
				workOrderDetailActivity.cancelReceiveSuccess();
			}
			
			@Override
			public void onCancelFailure(String tipInfo) {
				workOrderDetailActivity.showToast(tipInfo);
			}
			
			@Override
			public void onBackToLoginInterface() {
				workOrderDetailActivity.backToLoginInterface();
			}
		});
	}

}
