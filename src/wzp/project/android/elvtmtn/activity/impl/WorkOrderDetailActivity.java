package wzp.project.android.elvtmtn.activity.impl;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;

public class WorkOrderDetailActivity extends BaseActivity {

	private Button btnQueryElevatorRecord;
	private Button btnReceiveOrder;
	private Button btnCancelReceiveOrder;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker_order_detail);
		
		initWidget();
	}
	
	private void initWidget() {
		btnQueryElevatorRecord = (Button) findViewById(R.id.btn_queryElevatorRecord);
		btnReceiveOrder = (Button) findViewById(R.id.btn_receiveOrder);
		btnCancelReceiveOrder = (Button) findViewById(R.id.btn_cancelReceiveOrder);
		
		btnReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				/*
				 * 此处执行接单的相关操作，记录接单时间等。
				 */
				Log.d("WorkOrderDetailActivity", "接单按钮被按下");
				btnReceiveOrder.setVisibility(View.GONE);
				btnCancelReceiveOrder.setVisibility(View.VISIBLE);
			}
		});
		
		btnCancelReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				/*
				 * 此处执行取消接单的相关操作，记录接单时间等。
				 */
				
				btnCancelReceiveOrder.setVisibility(View.GONE);
				btnReceiveOrder.setVisibility(View.VISIBLE);
			}
		});
	}
	
	
	
	// 自定义一个startActivity()方法

}
