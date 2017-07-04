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
	
	/**
	 * 接单
	 * 
	 * @param workOrderType 工单类型
	 * @param workOrderId 工单id
	 * @param employeeId 员工id
	 */
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
	
	/**
	 * 取消接单
	 * 
	 * @param workOrderType 工单类型
	 * @param workOrderId 工单id
	 * @param employeeId 员工id
	 */
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
