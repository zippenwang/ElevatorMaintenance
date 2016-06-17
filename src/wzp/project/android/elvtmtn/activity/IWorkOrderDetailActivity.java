package wzp.project.android.elvtmtn.activity;

import java.util.Date;

import wzp.project.android.elvtmtn.activity.base.IBaseContainer;

public interface IWorkOrderDetailActivity extends IBaseContainer {
	
	/**
	 * 接单成功
	 * @param receivingTime 接单时间
	 */
	void receiveSuccess(Date receivingTime);
	
	/**
	 * 成功取消接单
	 */
	void cancelReceiveSuccess();
}
