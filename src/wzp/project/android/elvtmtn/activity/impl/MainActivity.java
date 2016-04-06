package wzp.project.android.elvtmtn.activity.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;

public class MainActivity extends BaseActivity {

	private Button btnWorkOrderSearch;
	private Button btnSignIn;
	private Button btnWorkOrderFeedback;
	private Button btnTakePhotos;
	private Button btnUserInfo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initWidget();
	}
	
	private void initWidget() {
		btnWorkOrderSearch = (Button) findViewById(R.id.btn_workOrderSearch);
		btnSignIn = (Button) findViewById(R.id.btn_signIn);
		btnWorkOrderFeedback = (Button) findViewById(R.id.btn_workOrderFeedback);
		btnTakePhotos = (Button) findViewById(R.id.btn_takePhotos);
		btnUserInfo = (Button) findViewById(R.id.btn_userInfo);
		
		btnWorkOrderSearch.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "工单查询被点击", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this, WorkOrderSearchActivity.class);
				startActivity(intent);
			}
		});
		
		btnSignIn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "一键签到被点击", Toast.LENGTH_SHORT).show();
			}
		});
		
		btnWorkOrderFeedback.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "工单反馈被点击", Toast.LENGTH_SHORT).show();
			}
		});
		
		btnTakePhotos.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "拍照上传被点击", Toast.LENGTH_SHORT).show();
			}
		});
		
		btnUserInfo.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "员工信息被点击", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
