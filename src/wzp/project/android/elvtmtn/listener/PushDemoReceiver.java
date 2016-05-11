package wzp.project.android.elvtmtn.listener;

import wzp.project.android.elvtmtn.activity.impl.FaultOrderDetailActivity;
import wzp.project.android.elvtmtn.activity.impl.MaintainOrderSearchActivity;
import wzp.project.android.elvtmtn.biz.ISignleOrderSearchListener;
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.biz.impl.WorkOrderBizImpl;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.util.MyApplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.igexin.sdk.PushConsts;

public class PushDemoReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息
     */
//    public static StringBuilder payloadData = new StringBuilder();

	private static IWorkOrderBiz workOrderBiz = new WorkOrderBizImpl();
	
    private static final String tag = "PushDemoReceiver";
    
    private Handler handler = new Handler();
    
    
    @Override
    public void onReceive(final Context context, Intent intent) {    	
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
            	Log.i(tag, "GET_MSG_DATA");
            	
            	byte[] payload = bundle.getByteArray("payload");

                if (payload != null) {
                    String data = new String(payload);

                    Log.d(tag, "receiver payload : " + data);
                    
                    JSONObject js = JSON.parseObject(data);
                    String strOrderType = js.getString("orderType");
                    String strOrderId = js.getString("id");
                    
                    if (strOrderType.equals("faultOrder")) {
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
//								showToast(tipInfo);
								handler.post(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(context, tipInfo, Toast.LENGTH_SHORT).show();
									}
								});
							}
						});
					} else if (strOrderType.equals("maintainOrder")) {
//						MaintainOrderSearchActivity.myStartActivity(context);
						Intent actIntent = new Intent(context, MaintainOrderSearchActivity.class);
						actIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(actIntent);
					}
                }    
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                MyApplication.setCid(cid);
                Log.d(tag, "cid=" + cid);
                break;

            case PushConsts.THIRDPART_FEEDBACK:
            	Log.d(tag, "THIRDPART_FEEDBACK");
            	
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 * 
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }
    
    /*public void showToast(final String text) {
		((Activity) context).runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}*/
}
