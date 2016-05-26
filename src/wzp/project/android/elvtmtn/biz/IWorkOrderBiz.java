package wzp.project.android.elvtmtn.biz;

import java.util.List;

import wzp.project.android.elvtmtn.biz.listener.ISignleOrderSearchListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderCancelListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderFeedbackListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderReceiveListener;
import wzp.project.android.elvtmtn.biz.listener.IWorkOrderSearchListener;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;

public interface IWorkOrderBiz {

	<T> void getWorkOrdersByCondition(int workOrderType, int workOrderState, int pageNumber, int pageSize, List<T> dataList,
			IWorkOrderSearchListener listener);
	
	/**
	 * 按条件查询保养工单
	 * 
	 * @param workOrderState 工单状态（未完成、已完成、超期）
	 * @param pageNumber 第几页
	 * @param pageSize 每页大小
	 * @param dataList 保养工单的List集合
	 * @param listener 用于回调的监听器
	 */
	void getMaintainOrdersByCondition(long groupId, int workOrderState, int pageNumber, 
			int pageSize, List<MaintainOrder> dataList, IWorkOrderSearchListener listener);
	
	/**
	 * 按条件查询故障工单
	 * 
	 * @param workOrderState 工单状态（未完成、已完成、超期）
	 * @param pageNumber 第几页
	 * @param pageSize 每页大小
	 * @param dataList 故障工单的List集合
	 * @param listener 用于回调的监听器
	 */
	void getFaultOrdersByCondition(long groupId, int workOrderState, int pageNumber, 
			int pageSize, List<FaultOrder> dataList, IWorkOrderSearchListener listener);
	
	void getFaultOrderById(String id, ISignleOrderSearchListener listener);
	
	void getReceivedMaintainOrdersByCondition(long employeeId, int pageNumber, 
			int pageSize, List<MaintainOrder> dataList, IWorkOrderSearchListener listener);
	
	void getReceivedFaultOrdersByCondition(long employeeId, int pageNumber,
			int pageSize, List<FaultOrder> dataList, IWorkOrderSearchListener listener);
	
	void getSignedInMaintainOrdersByCondition(long employeeId, int pageNumber,
			int pageSize, List<MaintainOrder> maintainOrders, IWorkOrderSearchListener listener);
	
	void getSignedInFaultOrdersByCondition(long employeeId, int pageNumber,
			int pageSize, List<FaultOrder> faultOrderList, IWorkOrderSearchListener listener);
	
	void getFaultOrdersByElevatorId(long elevatorRecordId, int pageNumber, 
			int pageSize, List<FaultOrder> faultOrderList, IWorkOrderSearchListener listener);
	
	void receiveOrder(int workOrderType, Long workOrderId, Long employeeId, IWorkOrderReceiveListener listener);

	void cancelReceiveOrder(int workOrderType, Long workOrderId, Long employeeId, IWorkOrderCancelListener listener);

	/**
	 * 工单反馈
	 * 
	 * @param workOrderType
	 * @param workOrderId
	 * @param employeeId
	 * @param faultReason 保养工单不需要传该参数
	 * @param isDone
	 * @param remark
	 * @param signOutAddress
	 * @param finishedItems 故障工单不需要传该参数
	 * @param listener
	 */
	void feedbackOrder(int workOrderType, Long workOrderId, Long employeeId, String faultReason, 
			boolean isDone, String remark, String signOutAddress, 
			String finishedItems, IWorkOrderFeedbackListener listener);
	
	
	/*
	 * 工单排序
	 */
	void sortMaintainOrderByFinalTimeIncrease(List<MaintainOrder> maintainOrderList);
	
	void sortMaintainOrderByFinalTimeDecrease(List<MaintainOrder> maintainOrderList);
	
	void sortFaultOrderByOccurredTimeIncrease(List<FaultOrder> faultOrderList);
	
	void sortFaultOrderByOccurredTimeDecrease(List<FaultOrder> faultOrderList);
	
	void sortMaintainOrderByReceivingTime(List<MaintainOrder> maintainOrderList);
	
	void sortFaultOrderByReceivingTime(List<FaultOrder> faultOrderList);
	
	void sortMaintainOrderByFinishedTimeIncrease(List<MaintainOrder> maintainOrderList);
	
	void sortMaintainOrderByFinishedTimeDecrease(List<MaintainOrder> maintainOrderList);
	
	void sortFaultOrderByFinishedTimeIncrease(List<FaultOrder> faultOrderList);
	
	void sortFaultOrderByFinishedTimeDecrease(List<FaultOrder> faultOrderList);
	
	void sortMaintainOrderBySignInTime(List<MaintainOrder> maintainOrderList);
	
	void sortFaultOrderBySignInTime(List<FaultOrder> faultOrderList);
}
