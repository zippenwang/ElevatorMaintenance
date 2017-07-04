package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import wzp.project.android.elvtmtn.activity.IWorkOrderSearchContainer;
import wzp.project.android.elvtmtn.activity.impl.EmployeeLoginActivity;
import wzp.project.android.elvtmtn.activity.impl.FaultOrderDetailActivity;
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;
import wzp.project.android.elvtmtn.biz.listener.ISignleOrderSearchListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.FailureTipMethod;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;

public class WorkOrderSearchPresenter implements IWorkOrderSearchListener {

	private static IWorkOrderBiz workOrderBiz = new WorkOrderBizImpl();
	private IWorkOrderSearchContainer workOrderSearchContainer;
    private Handler handler = new Handler();	
	
    
    public WorkOrderSearchPresenter() {}
    
	public WorkOrderSearchPresenter(IWorkOrderSearchContainer workOrderSearchContainer) {
		this.workOrderSearchContainer = workOrderSearchContainer;
	}
	
	/**
	 * 查询保养工单
	 * 
	 * @param groupId 小组id
	 * @param workOrderState 工单状态
	 * @param pageNumber 第几页数据
	 * @param pageSize 每页数据数量
	 * @param dataList 工单集合
	 */
	public void searchMaintainOrders(long groupId, int workOrderState, 
			int pageNumber, int pageSize, List<MaintainOrder> dataList) {
		workOrderSearchContainer.showProgressDialog();
		workOrderBiz.getMaintainOrdersByCondition(groupId, workOrderState, 
				pageNumber, pageSize, dataList, this);
	}
	
	/**
	 * 查询故障工单
	 * 
	 * @param groupId 小组id
	 * @param workOrderState 工单状态
	 * @param pageNumber 第几页数据
	 * @param pageSize 每页数据数量
	 * @param dataList 工单集合
	 */
	public void searchFaultOrders(long groupId, int workOrderState, 
			int pageNumber, int pageSize, List<FaultOrder> dataList) {
		workOrderSearchContainer.showProgressDialog();
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
				actIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		// 在Broadcast中被调用，一定需要添加该flag
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
	
	/**
	 * 按照电梯档案id查询故障工单
	 * 
	 * @param elevatorRecordId 电梯档案id
	 * @param pageNumber 第几页
	 * @param pageSize 分页中包含记录的数量
	 * @param faultOrderList 故障工单集合
	 */
	public void searchFaultOrdersByElevatorRecordId(long elevatorRecordId, int pageNumber, 
			int pageSize, List<FaultOrder> faultOrderList) {
		workOrderSearchContainer.showProgressDialog();
		workOrderBiz.getFaultOrdersByElevatorId(elevatorRecordId, pageNumber, 
				pageSize, faultOrderList, new IWorkOrderSearchListener() {
			@Override
			public void onSearchSuccess(int successType) {				
				if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
					workOrderSearchContainer.closePullUpToRefresh();	// 关闭上拉加载功能，只提供下拉刷新功能
				} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
					workOrderSearchContainer.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
				} else if (successType == ProjectContants.ORDER_IS_NULL) {
					// 该条件下的工单不存在
					workOrderSearchContainer.hidePtrlvAndShowLinearLayout("该电梯无故障记录");
					return;
				}
				
				workOrderSearchContainer.updateInterface();		
			}

			@Override
			public void onSearchFailure(String tipInfo, int tipMethod) {
				switch (tipMethod) {
					case FailureTipMethod.TOAST:
						workOrderSearchContainer.showToast(tipInfo);
						break;
					case FailureTipMethod.VIEW:
						workOrderSearchContainer.hidePtrlvAndShowLinearLayout(tipInfo);
						break;
					case FailureTipMethod.TOAST_AND_VIEW:
						workOrderSearchContainer.showToast(tipInfo);
						workOrderSearchContainer.hidePtrlvAndShowLinearLayout(tipInfo);
						break;
					default:
						break;
				}
			}

			@Override
			public void onAfter() {
				workOrderSearchContainer.closeProgressDialog();
			}

			@Override
			public void onBackToLoginInterface() {
				workOrderSearchContainer.backToLoginInterface();
			}
		});
	}
	
	public void searchReceivedMaintainOrders(long employeeId, int pageNumber, 
			int pageSize, List<MaintainOrder> dataList) {
		workOrderSearchContainer.showProgressDialog();
		workOrderBiz.getReceivedMaintainOrdersByCondition(employeeId, pageNumber, 
				pageSize, dataList, this);
	}
	
	public void searchReceivedFaultOrders(long employeeId, int pageNumber, 
			int pageSize, List<FaultOrder> dataList) {
		workOrderSearchContainer.showProgressDialog();
		workOrderBiz.getReceivedFaultOrdersByCondition(employeeId, pageNumber, 
				pageSize, dataList, this);	
	}
	
	public void searchSignedInFaultOrders(long employeeId, int pageNumber, 
			int pageSize, List<FaultOrder> faultOrderList) {
		workOrderSearchContainer.showProgressDialog();
		workOrderBiz.getSignedInFaultOrdersByCondition(employeeId, pageNumber, 
				pageSize, faultOrderList, this);
	}
	
	public void searchSignedInMaintainOrders(long employeeId, int pageNumber, 
			int pageSize, List<MaintainOrder> maintainOrderList) {
		workOrderSearchContainer.showProgressDialog();
		workOrderBiz.getSignedInMaintainOrdersByCondition(employeeId, pageNumber, 
				pageSize, maintainOrderList, this);
	}
	
	/*
	 * IWorkOrderSearchListener监听器的接口实现
	 */
	@Override
	public void onSearchSuccess(int successType) {
		if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
			// 关闭上拉加载功能，只提供下拉刷新功能
			workOrderSearchContainer.closePullUpToRefresh();
		} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
			// 打开上拉加载功能，此时包含上拉和下拉两种功能
			workOrderSearchContainer.openPullUpToRefresh();
		} else if (successType == ProjectContants.ORDER_IS_NULL) {
			// 该条件下的工单不存在
			workOrderSearchContainer.hidePtrlvAndShowLinearLayout("符合条件的工单不存在...");
			return;
		}
		
		workOrderSearchContainer.updateInterface();		
	}

	@Override
	public void onSearchFailure(String tipInfo, int tipMethod) {
		switch (tipMethod) {
			case FailureTipMethod.TOAST:
				workOrderSearchContainer.showToast(tipInfo);
				break;
			case FailureTipMethod.VIEW:
				workOrderSearchContainer.hidePtrlvAndShowLinearLayout(tipInfo);
				break;
			case FailureTipMethod.TOAST_AND_VIEW:
				workOrderSearchContainer.showToast(tipInfo);
				workOrderSearchContainer.hidePtrlvAndShowLinearLayout(tipInfo);
				break;
			default:
				break;
		}
	}

	@Override
	public void onAfter() {
		workOrderSearchContainer.closeProgressDialog();
	}

	@Override
	public void onBackToLoginInterface() {
		workOrderSearchContainer.backToLoginInterface();
	}
}
