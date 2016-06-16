package wzp.project.android.elvtmtn.activity.base;

/**
 * 从Activity或Fragment中抽象出来的接口
 * @author Zippen
 *
 */
public interface IBaseContainer {
	
	/**
	 * 显示进度对话框，对话框的提示内容固定
	 */
	void showProgressDialog();
	
	/**
	 * 显示进度对话框，对话框的提示内容由参数tipInfo决定
	 * @param tipInfo 进度对话框的提示内容
	 */
	void showProgressDialog(String tipInfo);

	/**
	 * 关闭进度对话框
	 */
	void closeProgressDialog();
	
	/**
	 * 显示Toast提示信息
	 * @param text 提示信息的内容
	 */
	void showToast(String text);
	
	/**
	 * 身份过期或身份验证不通过，跳转回登录界面
	 */
	void backToLoginInterface();
}
