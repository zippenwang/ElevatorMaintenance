package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IWorkOrderFeedbackActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainItem;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.entity.MaintainType;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderFeedbackPresenter;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.MyProgressDialog;

public class MaintainOrderFeedbackDetailActivity extends BaseActivity 
		implements IWorkOrderFeedbackActivity, OnGetGeoCoderResultListener {

	private Button btnBack;
	private ImageButton ibtnRefreshCurAddress;
	private TextView tvWorkOrderId;
	private TextView tvElevatorAddress;
	private TextView tvSignInTime;
	private TextView tvSignInAddress;
	private TextView tvFeedbackState;
	private TextView tvCurrentAddress;
	private LinearLayout linearMaintainItem;
	private EditText edtRemark;
	private RadioGroup rgIsFinished;
	private RadioButton rbYes;
	private RadioButton rbNo;
	private Button btnSubmit;
	private MyProgressDialog myProgressDialog;
	private AlertDialog.Builder altDlgBuilder;
	
	private MaintainOrder maintainOrder;
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
	
	private int checkedNum;
	private List<MaintainItem> itemList;
	private List<String> finishedItemList;
	
	private ConnectivityManager mConnectivityManager;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String tag = "MaintainOrderFeedbackDetailActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 在使用百度地图之前，一定要调用该方法，用于初始化参数！！
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_maintain_order_feedback_detail);
		
		try {
			initData();
		} catch (IllegalArgumentException exp) {
			Log.e(tag, Log.getStackTraceString(exp));
			showToast("缺失重要数据，请重新登录");
			EmployeeLoginActivity.myForceStartActivity(this);
			return;
		} catch (Exception exp2) {
			Log.e(tag, Log.getStackTraceString(exp2));
			showToast("程序异常，请重新登录");
			EmployeeLoginActivity.myForceStartActivity(this);
			return;
		}

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
		String jsonWorkOrder = getIntent().getStringExtra("workOrder");
		if (null == jsonWorkOrder) {
			throw new IllegalArgumentException("没有接收到工单json字符串");
		}
		
		maintainOrder = JSON.parseObject(jsonWorkOrder, MaintainOrder.class);
		workOrderId = maintainOrder.getId();
		if (0 == workOrderId) {
			throw new IllegalArgumentException("缺失工单id号");
		}
		
		MaintainType maintainType = maintainOrder.getMaintainType();
		if (null == maintainType) {
			throw new IllegalArgumentException("缺失保养类型maintainType");
		}
		
		itemList = maintainType.getMaintainItems();
		
		String finishedItems = maintainOrder.getFinishedItems();
		if (!TextUtils.isEmpty(finishedItems)) {
			finishedItemList = Arrays.asList(finishedItems.split(";"));
		} else {
			finishedItemList = null;
		}
		
		employeeId = preferences.getLong("employeeId", -1);
		if (employeeId == -1) {
			throw new IllegalArgumentException("缺失employeeId");
		}
		
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		ibtnRefreshCurAddress = (ImageButton) findViewById(R.id.ibtn_refreshCurAddress);
		tvWorkOrderId = (TextView) findViewById(R.id.tv_workOrderId);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvSignInTime = (TextView) findViewById(R.id.tv_signInTime);
		tvSignInAddress = (TextView) findViewById(R.id.tv_signInAddress);
		tvFeedbackState = (TextView) findViewById(R.id.tv_feedbackState);
		tvCurrentAddress = (TextView) findViewById(R.id.tv_currentAddress);
		linearMaintainItem = (LinearLayout) findViewById(R.id.linear_maintainItem);
		edtRemark = (EditText) findViewById(R.id.edt_remark);
		rgIsFinished = (RadioGroup) findViewById(R.id.rg_isFinished);
		rbYes = (RadioButton) findViewById(R.id.rb_yes);
		rbNo = (RadioButton) findViewById(R.id.rb_no);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		
		myProgressDialog = new MyProgressDialog(this);
		altDlgBuilder = new AlertDialog.Builder(this)
			.setTitle("注意 ")
			.setCancelable(true)
			.setNegativeButton("取消", null);;
		
		rbYes.setEnabled(false);			// 初始状态下rbYes默认为不可选中
		if (itemList != null && finishedItemList != null) {
			if (itemList.size() == finishedItemList.size()) {
				rbYes.setEnabled(true);
			}
		}
				
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSubmitSuccess) {
					Intent actIntent = new Intent(MaintainOrderFeedbackDetailActivity.this, 
							WorkOrderFeedbackActivity.class);
					actIntent.putExtra("isNeedRefresh", isSubmitSuccess);
					setResult(RESULT_OK, actIntent);		
				}
				finish();
			}
		});
		
		ibtnRefreshCurAddress.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// 刷新当前位置
				showProgressDialog("正在定位，请稍后...");
				isShowCurrentAddress = false;
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(currentAddress)) {
					Toast.makeText(MaintainOrderFeedbackDetailActivity.this, "无法定位到当前位置，请刷新界面后重试", Toast.LENGTH_SHORT).show();
					return;
				}
				
				final String remark = edtRemark.getEditableText().toString().trim();				
				final boolean isDone = (rgIsFinished.getCheckedRadioButtonId() == R.id.rb_yes);
				String message = isDone ? "您已完成保养工单，是否确定提交？" : "您还未完成保养工单，是否确定提交？";
			
				if (checkedNum > 0
						|| null == itemList
						|| itemList.size() < 1) {
					altDlgBuilder.setMessage(message)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								StringBuilder finishedItems = new StringBuilder();
								CheckBox ckbxItem = null;
								for (int i=0; i<linearMaintainItem.getChildCount(); i++) {
									ckbxItem = (CheckBox) linearMaintainItem.getChildAt(i);
									if (ckbxItem.isChecked()) {
										finishedItems.append(itemList.get(i).getId());
										finishedItems.append(";");
									}
									ckbxItem = null;
								}
								workOrderFeedbackPresenter.feedbackOrder(WorkOrderType.MAINTAIN_ORDER, 
										workOrderId, employeeId, null, isDone, 
										remark, currentAddress, finishedItems.toString());
							}
						})
						.show();
				} else {
					Toast.makeText(MaintainOrderFeedbackDetailActivity.this, "至少完成一个保养项目", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		/*
		 * 为控件设值
		 */
		String no = maintainOrder.getNo();
		if (!TextUtils.isEmpty(no)) {
			tvWorkOrderId.setText(no);
		} else {
			tvWorkOrderId.setText("无");
		}
		
		ElevatorRecord elevatorRecord = maintainOrder.getElevatorRecord();
		if (elevatorRecord != null) {
			String elevatorAddrress = elevatorRecord.getAddress();
			if (!TextUtils.isEmpty(elevatorAddrress)) {
				tvElevatorAddress.setText(elevatorAddrress);
			} else {
				tvElevatorAddress.setText("暂无地址信息");
			}
		} else {
			tvElevatorAddress.setText("电梯档案为空");
		}
		
		Date signInTime = maintainOrder.getSignInTime();
		if (signInTime != null) {
			tvSignInTime.setText(sdf.format(signInTime));
		} else {
			tvSignInTime.setText("暂无该信息");
		}
		
		String signInAddress = maintainOrder.getSignInAddress();
		if (!TextUtils.isEmpty(signInAddress)) {
			tvSignInAddress.setText(signInAddress);
		} else {
			tvSignInAddress.setText("暂无该信息");
		}
		
		if (maintainOrder.getSignOutTime() != null) {
			tvFeedbackState.setTextColor(Color.BLACK);
			if (!maintainOrder.getFinished()) {
				tvFeedbackState.setText("部分反馈");
			} else {
				tvFeedbackState.setText("已反馈");
			}
		} else {
			tvFeedbackState.setText("未反馈");
			tvFeedbackState.setTextColor(Color.RED);
		}
		
		/*
		 * 动态生成保养项目复选框
		 */		
		CheckBox ckBox = null;
		if (itemList != null 
				&& itemList.size() > 0) {
			for (MaintainItem item : itemList) {
				ckBox = new CheckBox(this);
				ckBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				ckBox.setText(item.getName());
				ckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {				
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							checkedNum++;
						} else {
							checkedNum--;
						}
						
						if (checkedNum == itemList.size()) {
							rbYes.setEnabled(true);
						} else {
							rbYes.setEnabled(false);
//							rbNo.setChecked(true);
						}
					}
				});
				if (finishedItemList != null
						&& finishedItemList.contains(String.valueOf(item.getId()))) {
					ckBox.setChecked(true);
				}
				linearMaintainItem.addView(ckBox);
				ckBox = null;
			}
		} else {
			rbYes.setEnabled(true);
		}

		String remark = maintainOrder.getRemark();
		if (!TextUtils.isEmpty(remark)) {
			edtRemark.setText(remark);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (isSubmitSuccess) {
			Intent actIntent = new Intent(MaintainOrderFeedbackDetailActivity.this, 
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
		
		showProgressDialog("正在定位，请稍后...");
		locationClient.registerLocationListener(locationListener);
	}
	
	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.d(tag, "进入MyLocationListener");
			
			if (!isShowCurrentAddress) {
				Log.d(tag, "执行地理反解码操作");
				isShowCurrentAddress = true;
				
				// MapView销毁后不再处理新接收的位置
				if (location == null)
					return;
								
				LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());
				Log.i(tag, "当前位置，纬度：" + ptCenter.latitude + ",经度：" + ptCenter.longitude);
				
				NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo(); 
				if (networkInfo == null) { 
					Toast.makeText(MaintainOrderFeedbackDetailActivity.this, "网络异常，定位失败，检查网络后重试", Toast.LENGTH_SHORT).show();
					closeProgressDialog();
					return;
				} 
				
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));		
			}
		}
	}
	
	public static void myStartActivityForResult(Activity context, 
			int requestCode, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, MaintainOrderFeedbackDetailActivity.class);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivityForResult(actIntent, requestCode);
	}

	@Override
	public void showToast(final String tipInfo) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(MaintainOrderFeedbackDetailActivity.this, tipInfo, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public void showProgressDialog(final String tipInfo) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				myProgressDialog.setMessage(tipInfo);
				myProgressDialog.setCancelable(true);
				
				myProgressDialog.show();
			}
		});
	}
	
	@Override
	public void showProgressDialog() {}

	@Override
	public void closeProgressDialog() {
		// 在百度地图SDK中被调用，因此需要包含在runOnUiThread方法中
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if (myProgressDialog != null
						&& myProgressDialog.isShowing()) {
					myProgressDialog.dismiss();
				}
			}
		});
	}

	@Override
	public void backToLoginInterface() {
		EmployeeLoginActivity.myForceStartActivity(this);
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MaintainOrderFeedbackDetailActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		Log.d(tag, "成功反编码地理位置");
		
		currentAddress = result.getAddress();
		tvCurrentAddress.setText(currentAddress);
		closeProgressDialog();
	}

	@Override
	public void feedbackSuccess() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Toast.makeText(MaintainOrderFeedbackDetailActivity.this, "工单提交成功", 
						Toast.LENGTH_LONG).show();
				isSubmitSuccess = true;
				edtRemark.setEnabled(false);
				btnSubmit.setEnabled(false);
				rbYes.setEnabled(false);
				rbNo.setEnabled(false);
				tvFeedbackState.setTextColor(Color.BLACK);
				if (rbYes.isChecked()) {
					tvFeedbackState.setText("已反馈");
				} else {
					tvFeedbackState.setText("部分反馈");
				}
				 
				for (int i=0; i<linearMaintainItem.getChildCount(); i++) {
					linearMaintainItem.getChildAt(i).setEnabled(false);
				}
				
				locationClient.unRegisterLocationListener(locationListener);
				ibtnRefreshCurAddress.setVisibility(View.GONE);
			}
		});
	}

}
