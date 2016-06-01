package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import wzp.project.android.elvtmtn.activity.impl.EmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.impl.FaultOrderDetailActivity;
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;
import wzp.project.android.elvtmtn.biz.listener.ISignleOrderSearchListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;

public class WorkOrderSearchPresenter implements IWorkOrderSearchListener {

	private static IWorkOrderBiz workOrderBiz = new WorkOrderBizImpl();
	private IWorkOrderSearchFragment workOrderSearchFragment;
    private Handler handler = new Handler();	
	
    
    public WorkOrderSearchPresenter() {}
    
	public WorkOrderSearchPresenter(IWorkOrderSearchFragment workOrderSearchFragment) {
		this.workOrderSearchFragment = workOrderSearchFragment;
	}
	
	public void searchMaintainOrder(long groupId, int workOrderState, 
			int pageNumber, int pageSize, List<MaintainOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getMaintainOrdersByCondition(groupId, workOrderState, 
				pageNumber, pageSize, dataList, this);
	}
	
	public void searchFaultOrder(long groupId, int workOrderState, 
			int pageNumber, int pageSize, List<FaultOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getFaultOrdersByCondition(groupId, workOrderState, 
				pageNumber, pageSize, dataList, this);
	}
	
	public void searchFaultOrderById(final Context context, String strOrderId) {
		workOrderBiz.getFaultOrderById(strOrderId, new ISignleOrderSearchListener() {
			@Override
			public void onSearchSuccess(String jsonOrder) {
				Intent actIntent = new Intent(context, FaultOrderDetailActivity.class);
				actIntent.putExtra("workOrderState", WorkOrderState.UNFINISHED);
				actIntent.putExtra("workOrder", jsonOrder);
				actIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(actIntent);
			}
			
			@Override
			public void onSearchFailure(final String tipInfo) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context, tipInfo, Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onBackToLoginInterface() {
				EmployeeLoginActivity.myStartActivity(context);
			}
		});
	}
	
	public void searchFaultOrdersByElevatorRecordId(long elevatorRecordId, int pageNumber, 
			int pageSize, List<FaultOrder> faultOrderList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getFaultOrdersByElevatorId(elevatorRecordId, pageNumber, 
				pageSize, faultOrderList, new IWorkOrderSearchListener() {
			@Override
			public void onSearchSuccess(int successType) {				
				if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
//					workOrderSearchFragment.showToast("已显示出所有记录");
					workOrderSearchFragment.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
				} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
					workOrderSearchFragment.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
				} else if (successType == ProjectContants.ORDER_IS_NULL) {
					// 该条件下的工单不存在
					workOrderSearchFragment.hidePtrlvAndShowLinearLayout("该电梯无故障记录");
					return;
				}
				
				workOrderSearchFragment.updateInterface();		
			}

			@Override
			public void onSearchFailure(String tipInfo) {
				workOrderSearchFragment.showToast(tipInfo);
				workOrderSearchFragment.hidePtrlvAndShowLinearLayout("服务器正在打盹，" +
						"请检查网络后重试...");
			}

			@Override
			public void onAfter() {
				workOrderSearchFragment.closeProgressDialog();
			}

			@Override
			public void onBackToLoginInterface() {
				workOrderSearchFragment.backToLoginInterface();
			}
		});
	}
	
	public void searchReceivedMaintainOrders(long employeeId, int pageNumber, 
			int pageSize, List<MaintainOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getReceivedMaintainOrdersByCondition(employeeId, pageNumber, 
				pageSize, dataList, this);
	}
	
	public void searchReceivedFaultOrders(long employeeId, int pageNumber, 
			int pageSize, List<FaultOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getReceivedFaultOrdersByCondition(employeeId, pageNumber, 
				pageSize, dataList, this);	
	}
	
	public void searchSignedInFaultOrders(long employeeId, int pageNumber, 
			int pageSize, List<FaultOrder> faultOrderList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getSignedInFaultOrdersByCondition(employeeId, pageNumber, 
				pageSize, faultOrderList, this);
	}
	
	public void searchSignedInMaintainOrders(long employeeId, int pageNumber, 
			int pageSize, List<MaintainOrder> maintainOrderList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getSignedInMaintainOrdersByCondition(employeeId, pageNumber, 
				pageSize, maintainOrderList, this);
	}
	
	/*
	 * IWorkOrderSearchListener监听器的接口实现
	 */
	@Override
	public void onSearchSuccess(int successType) {
//		workOrderSearchFragment.setIsPtrlvHidden(false);
		
		if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
//			workOrderSearchFragment.showToast("当前已经显示出所有工单了");
			workOrderSearchFragment.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
		} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
			workOrderSearchFragment.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
		} else if (successType == ProjectContants.ORDER_IS_NULL) {
			// 该条件下的工单不存在
			workOrderSearchFragment.hidePtrlvAndShowLinearLayout("符合条件的工单不存在...");
			return;
		}
		
		workOrderSearchFragment.updateInterface();		
	}

	@Override
	public void onSearchFailure(String tipInfo) {
		workOrderSearchFragment.showToast(tipInfo);
		workOrderSearchFragment.hidePtrlvAndShowLinearLayout("服务器正在打盹，请检查网络后重试...");
	}

	@Override
	public void onAfter() {
		workOrderSearchFragment.closeProgressDialog();
	}

	@Override
	public void onBackToLoginInterface() {
		workOrderSearchFragment.backToLoginInterface();
	}
}
