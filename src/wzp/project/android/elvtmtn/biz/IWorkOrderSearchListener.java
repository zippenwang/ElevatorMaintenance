package wzp.project.android.elvtmtn.biz;

public interface IWorkOrderSearchListener {
	
	void onSearchSuccess(int successType);		// 查询到工单信息，认为查询成功
	
//	void onSearchFailure(int failType);			// 未查询到工单信息，则认为查询失败
	
	void onSearchFailure(String tipInfo);		// 未能成功连接上服务器、服务器响应失败等，都认为是服务器异常
}
