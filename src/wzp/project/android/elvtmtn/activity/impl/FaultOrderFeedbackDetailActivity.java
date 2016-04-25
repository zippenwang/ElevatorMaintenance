package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IWorkOrderFeedbackActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderFeedbackPresenter;
import wzp.project.android.elvtmtn.util.MyApplication;

public class FaultOrderFeedbackDetailActivity extends BaseActivity 
		implements IWorkOrderFeedbackActivity, OnGetGeoCoderResultListener {

	private Button btnBack;
	private Button btnRefreshCurAddress;
	private TextView tvWorkOrderId;
	private TextView tvElevatorAddress;
	private TextView tvSignInTime;
	private TextView tvSignInAddress;
	private TextView tvFeedbackState;
	private TextView tvCurrentAddress;
	private EditText edtFaultReason;
	private EditText edtRemark;
	private LinearLayout linearIsFixed; 
	private RadioGroup rgIsFixed;
	private RadioButton rbYes;
	private RadioButton rbNo;
	private Button btnSubmit;
	private ProgressDialog progressDialog;
	private AlertDialog.Builder altDlgBuilder;
	
	private FaultOrder faultOrder;
	private SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private long employeeId;	
	private long workOrderId;
	
	private GeoCoder mSearch;
	private LocationClient locationClient;
	private MyLocationListener locationListener = new MyLocationListener();
	
	private String currentAddress = null;
	
	private volatile boolean isShowCurrentAddress = false;		// 是否显示出当前地址
	private volatile boolean isSubmitSuccess = false;			// 是否成功提交
	
	private WorkOrderFeedbackPresenter workOrderFeedbackPresenter
		= new WorkOrderFeedbackPresenter(this);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String tag = "FaultOrderFeedbackDetailActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 在使用百度地图之前，一定要调用该方法，用于初始化参数！！
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_fault_order_feedback_detail);
		
		initData();
		initWidget();
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		// 初始化用于定位的类LocationClient的对象
		initLocationClient();
	}
	
	@Override
	protected void onStart() {
		Log.d(tag, "开启定位功能");
		if (locationClient != null
				&& !locationClient.isStarted()) {
			locationClient.start();
		}
		
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(tag, "关闭定位功能");
		if (locationClient != null
				&& locationClient.isStarted()) {
			locationClient.stop();
		}
		
		super.onStop();
	}
	
	private void initData() {
		faultOrder = JSON.parseObject(getIntent().getStringExtra("workOrder"), FaultOrder.class);
		workOrderId = faultOrder.getId();
		employeeId = preferences.getLong("employeeId", -1);
		if (employeeId == -1) {
			throw new IllegalArgumentException("员工ID有误！");
		}
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		btnRefreshCurAddress = (Button) findViewById(R.id.btn_refreshCurAddress);
		tvWorkOrderId = (TextView) findViewById(R.id.tv_workOrderId);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvSignInTime = (TextView) findViewById(R.id.tv_signInTime);
		tvSignInAddress = (TextView) findViewById(R.id.tv_signInAddress);
		tvFeedbackState = (TextView) findViewById(R.id.tv_feedbackState);
		tvCurrentAddress = (TextView) findViewById(R.id.tv_currentAddress);
		edtFaultReason = (EditText) findViewById(R.id.edt_faultReason);
		edtRemark = (EditText) findViewById(R.id.edt_remark);
		linearIsFixed = (LinearLayout) findViewById(R.id.linear_isFixed);
		rgIsFixed = (RadioGroup) findViewById(R.id.rg_isFixed);
		rbYes = (RadioButton) findViewById(R.id.rb_yes);
		rbNo = (RadioButton) findViewById(R.id.rb_no);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		
		progressDialog = new ProgressDialog(this);
		altDlgBuilder = new AlertDialog.Builder(this);
				
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSubmitSuccess) {
					Intent actIntent = new Intent(FaultOrderFeedbackDetailActivity.this, 
							WorkOrderFeedbackActivity.class);
					actIntent.putExtra("isNeedRefresh", isSubmitSuccess);
					setResult(RESULT_OK, actIntent);		
				}
				finish();
			}
		});
		
		btnRefreshCurAddress.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// 刷新当前位置
				showProgressDialog();
				isShowCurrentAddress = false;
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(currentAddress)) {
					Toast.makeText(FaultOrderFeedbackDetailActivity.this, "无法定位到当前位置，请刷新界面后重试", Toast.LENGTH_SHORT).show();
					return;
				}
				
				final String faultReason = edtFaultReason.getEditableText().toString();
				final String remark = edtRemark.getEditableText().toString();				
				final boolean isDone = (rgIsFixed.getCheckedRadioButtonId() == R.id.rb_yes);
				String message = isDone ? "您当前已完成工单，是否确定提交？" : "您当前还未完成工单，是否确定提交？";
								
				if (!TextUtils.isEmpty(faultReason)) {
					// 访问网络
					altDlgBuilder.setTitle("注意 ");
					altDlgBuilder.setMessage(message);
					altDlgBuilder.setCancelable(true);
					altDlgBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							workOrderFeedbackPresenter.feedbackOrder(WorkOrderType.FAULT_ORDER, 
									workOrderId, employeeId, faultReason, isDone, 
									remark, currentAddress);
						}
					});
					
					// 取消按钮，默认不执行任何操作
					altDlgBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});

					altDlgBuilder.show();
				} else {
					Toast.makeText(FaultOrderFeedbackDetailActivity.this, "故障原因不能为空", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		/*
		 * 为控件设值
		 */
		tvWorkOrderId.setText(String.valueOf(faultOrder.getId()));
		tvElevatorAddress.setText(faultOrder.getElevatorRecord().getAddress());
		tvSignInTime.setText(sdf.format(faultOrder.getSignInTime()));
		tvSignInAddress.setText(faultOrder.getSignInAddress());
		
		if (faultOrder.getSignOutTime() != null
				&& !faultOrder.getFixed()) {
			tvFeedbackState.setText("部分反馈");
		} else {
			tvFeedbackState.setText("未反馈");
			tvFeedbackState.setTextColor(Color.RED);
		}
		
		if (!TextUtils.isEmpty(faultOrder.getReason())) {
			edtFaultReason.setText(faultOrder.getReason());
		}
		if (!TextUtils.isEmpty(faultOrder.getRemark())) {
			edtRemark.setText(faultOrder.getRemark());
		}
	}
	
	@Override
	public void onBackPressed() {
		if (isSubmitSuccess) {
			Intent actIntent = new Intent(FaultOrderFeedbackDetailActivity.this, 
					WorkOrderFeedbackActivity.class);
			actIntent.putExtra("isNeedRefresh", isSubmitSuccess);
			setResult(RESULT_OK, actIntent);		
		}
		finish();
	}

	private void initLocationClient() {
		locationClient = new LocationClient(this);
		
		// 利用 LocationClientOption类为LocationClient设定参数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);		// 打开gps导航
		option.setCoorType("bd09ll"); 	// 设置坐标类型
		option.setScanSpan(1000);		// 扫描的时间间隔为1s
		
		locationClient.setLocOption(option);
		
		showProgressDialog();
		locationClient.registerLocationListener(locationListener);
	}
	
	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.d(tag, "进入MyLocationListener");
			
			if (!isShowCurrentAddress) {
				Log.d(tag, "执行地理反解码操作");
				
				// MapView销毁后不再处理新接收的位置
				if (location == null)
					return;
								
				LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
				isShowCurrentAddress = true;
				Log.i(tag, "当前位置，纬度：" + ptCenter.latitude + ",经度：" + ptCenter.longitude);
			}
		}		
	}
	
	// 自定义一个startActivity()方法
/*	public static void myStartActivity(Context context,
			int workOrderType, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, FaultOrderDetailActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivity(actIntent);
	}*/
	
	public static void myStartActivityForResult(Activity context, 
			int requestCode, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, FaultOrderFeedbackDetailActivity.class);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivityForResult(actIntent, requestCode);
	}

	@Override
	public void showToast(final String tipInfo) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(FaultOrderFeedbackDetailActivity.this, tipInfo, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public void showProgressDialog() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				progressDialog.setTitle("正在定位，请稍后...");
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(true);
				
				progressDialog.show();
			}
		});
	}

	@Override
	public void closeProgressDialog() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if (progressDialog != null
						&& progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		});
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(FaultOrderFeedbackDetailActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		currentAddress = result.getAddress();
		tvCurrentAddress.setText(currentAddress);
		closeProgressDialog();
	}

	@Override
	public void feedbackSuccess() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Toast.makeText(FaultOrderFeedbackDetailActivity.this, "工单提交成功", 
						Toast.LENGTH_LONG).show();
				isSubmitSuccess = true;
				edtFaultReason.setEnabled(false);
				edtRemark.setEnabled(false);
				btnSubmit.setEnabled(false);
//				linearIsFixed.setEnabled(false);
				rbYes.setEnabled(false);
				rbNo.setEnabled(false);
				if (rbYes.isChecked()) {
					tvFeedbackState.setText("已反馈");
				} else {
					tvFeedbackState.setText("部分反馈");
				}
				locationClient.unRegisterLocationListener(locationListener);
				btnRefreshCurAddress.setVisibility(View.GONE);
			}
		});
	}

}
