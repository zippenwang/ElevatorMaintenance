package wzp.project.android.elvtmtn.presenter;

import wzp.project.android.elvtmtn.activity.IWorkOrderFeedbackActivity;
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.IWorkOrderFeedbackListener;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;

public class WorkOrderFeedbackPresenter {
	
	private static IWorkOrderBiz workOrderBiz = new WorkOrderBizImpl();
	private IWorkOrderFeedbackActivity workOrderFeedbackActivity;
	
	
	public WorkOrderFeedbackPresenter(IWorkOrderFeedbackActivity workOrderFeedbackActivity) {
		this.workOrderFeedbackActivity = workOrderFeedbackActivity;
	}
	
	public void feedbackOrder(int workOrderType, Long workOrderId,
			Long employeeId, String faultReason, boolean isDone, String remark,
			String signOutAddress, String finishedItems) {
		workOrderFeedbackActivity.showProgressDialog("正在提交反馈信息，请稍后");
		workOrderBiz.feedbackOrder(workOrderType, workOrderId, 
				employeeId, faultReason, isDone, remark, 
				signOutAddress, finishedItems, new IWorkOrderFeedbackListener() {			
			@Override
			public void onFeedbackSuccess() {
				workOrderFeedbackActivity.feedbackSuccess();
			}
			
			@Override
			public void onFeedbackFailure(String tipInfo) {
				workOrderFeedbackActivity.showToast(tipInfo);
			}

			@Override
			public void onAfter() {
				workOrderFeedbackActivity.closeProgressDialog();
			}
		});
	}

}
