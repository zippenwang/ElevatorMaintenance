package wzp.project.android.elvtmtn.presenter;

import java.util.List;

import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.IWorkOrderSearchFragment;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;

public class WorkOrderSearchPresenter implements IWorkOrderSearchListener {

	private static IWorkOrderBiz workOrderBiz = new WorkOrderBizImpl();
	private IWorkOrderSearchFragment workOrderSearchFragment;
	

	public WorkOrderSearchPresenter(IWorkOrderSearchFragment workOrderSearchFragment) {
		this.workOrderSearchFragment = workOrderSearchFragment;
	}

	/*public <T> void searchWorkOrder(int workOrderType, int workOrderState, 
			int page, int pageSize, List<T> dataList) {
		workOrderSearchFragment.showProgressDialog();
		
		workOrderBiz.getWorkOrdersByCondition(workOrderType, workOrderState, page, 
				pageSize, dataList, new IWorkOrderSearchListener() {		
			@Override
			public void onServerException(String tipInfo) {
				workOrderSearchFragment.closeProgressDialog();
				workOrderSearchFragment.showToast(tipInfo);
				workOrderSearchFragment.hidePtrlvAndShowLinearLayout("服务器正在打盹，请检查网络后重试...");
			}
			
			@Override
			public void onSearchSuccess(int successType) {
				workOrderSearchFragment.closeProgressDialog();
				
				if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
					workOrderSearchFragment.showToast("当前已经显示出所有工单了");
					workOrderSearchFragment.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
				} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
					workOrderSearchFragment.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
				} else if (successType == ProjectContants.ORDER_IS_NULL) {
					// 该条件下的工单不存在
					workOrderSearchFragment.hidePtrlvAndShowLinearLayout("当前不存在未完成的工单");
					return;
				}
				
				workOrderSearchFragment.updateInterface();
			}
			
			@Override
			public void onSearchFailure(String tipInfo) {
				workOrderSearchFragment.closeProgressDialog();
				
				if (failType == ProjectContants.ORDER_IS_NULL) {
					// 该条件下的工单不存在
					workOrderSearchFragment.hidePtrlvAndShowLinearLayout("当前不存在未完成的工单");
				} else if (failType == ProjectContants.ORDER_SHOW_COMPLETE) {
					// 所有工单都已经查询完了
					workOrderSearchFragment.showToast("当前已经显示出所有工单了");
					
					 * 关闭上拉加载的功能，只提供下拉刷新的功能
					 
					workOrderSearchFragment.closePullUpToRefresh();
				}
				
				workOrderSearchFragment.closeProgressDialog();
				workOrderSearchFragment.showToast(tipInfo);
				workOrderSearchFragment.hidePtrlvAndShowLinearLayout("服务器正在打盹，请检查网络后重试...");
			}
		});
	}*/
	
	public void searchMaintainOrder(int workOrderState, int pageNumber, int pageSize, List<MaintainOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		workOrderBiz.getMaintainOrdersByCondition(workOrderState, pageNumber, pageSize, dataList, this);
		/*workOrderBiz.getMaintainOrdersByCondition(workOrderState, page, 
				pageSize, dataList, new IWorkOrderSearchListener() {			
			@Override
			public void onSearchSuccess(int successType) {
				workOrderSearchFragment.closeProgressDialog();
				
				if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
					workOrderSearchFragment.showToast("当前已经显示出所有工单了");
					workOrderSearchFragment.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
				} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
					workOrderSearchFragment.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
				} else if (successType == ProjectContants.ORDER_IS_NULL) {
					// 该条件下的工单不存在
					workOrderSearchFragment.hidePtrlvAndShowLinearLayout("当前不存在未完成的工单");
					return;
				}
				
				workOrderSearchFragment.updateInterface();
			}
			
			@Override
			public void onSearchFailure(String tipInfo) {				
				workOrderSearchFragment.closeProgressDialog();
				workOrderSearchFragment.showToast(tipInfo);
				workOrderSearchFragment.hidePtrlvAndShowLinearLayout("服务器正在打盹，请检查网络后重试...");
			}
		});*/
	}
	
	public void searchFaultOrder(int workOrderState, int pageNumber, int pageSize, List<FaultOrder> dataList) {
		workOrderSearchFragment.showProgressDialog();
		
		/*workOrderBiz.getFaultOrdersByCondition(workOrderState, page, 
				pageSize, dataList, new IWorkOrderSearchListener() {			
			@Override
			public void onSearchSuccess(int successType) {
				workOrderSearchFragment.closeProgressDialog();
				
				if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
					workOrderSearchFragment.showToast("当前已经显示出所有工单了");
					workOrderSearchFragment.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
				} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
					workOrderSearchFragment.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
				} else if (successType == ProjectContants.ORDER_IS_NULL) {
					// 该条件下的工单不存在
					workOrderSearchFragment.hidePtrlvAndShowLinearLayout("当前不存在未完成的工单");
					return;
				}
				
				workOrderSearchFragment.updateInterface();
			}
			
			@Override
			public void onSearchFailure(String tipInfo) {				
				workOrderSearchFragment.closeProgressDialog();
				workOrderSearchFragment.showToast(tipInfo);
				workOrderSearchFragment.hidePtrlvAndShowLinearLayout("服务器正在打盹，请检查网络后重试...");
			}
		});*/
		
		workOrderBiz.getFaultOrdersByCondition(workOrderState, pageNumber, pageSize, dataList, this);
	}

	@Override
	public void onSearchSuccess(int successType) {
		workOrderSearchFragment.closeProgressDialog();
		workOrderSearchFragment.setIsPtrlvHidden(false);
		
		if (successType == ProjectContants.ORDER_SHOW_COMPLETE) {
			workOrderSearchFragment.showToast("当前已经显示出所有工单了");
			workOrderSearchFragment.closePullUpToRefresh();		// 关闭上拉加载功能，只提供下拉刷新功能
		} else if (successType == ProjectContants.ORDER_SHOW_UNCOMPLETE) {
			workOrderSearchFragment.openPullUpToRefresh();		// 打开上拉加载功能，此时包含上拉和下拉两种功能
		} else if (successType == ProjectContants.ORDER_IS_NULL) {
			// 该条件下的工单不存在
			workOrderSearchFragment.hidePtrlvAndShowLinearLayout("符合条件的工单不存在");
			return;
		}
		
		workOrderSearchFragment.updateInterface();		
	}

	@Override
	public void onSearchFailure(String tipInfo) {
		workOrderSearchFragment.closeProgressDialog();
		workOrderSearchFragment.showToast(tipInfo);
		workOrderSearchFragment.hidePtrlvAndShowLinearLayout("服务器正在打盹，请检查网络后重试...");
	}
	
	
}
