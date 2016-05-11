package wzp.project.android.elvtmtn.activity.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IEmployeeSignInDetailActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeSignInListener;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.EmployeeSignInPresenter;

public class EmployeeSignInDetailActivity extends BaseActivity implements IEmployeeSignInDetailActivity, OnGetGeoCoderResultListener {

	private Button btnBack;
//	private Button btnRefreshCurAddress;
	private ImageButton ibtnRefreshCurAddress;
	private TextView tvWorkOrderType;
	private TextView tvWorkOrderId;
	private TextView tvReceivingTime;
	private TextView tvElevatorAddress;
	private LinearLayout linearCurrentAddress;
	private TextView tvCurrentAddress;
	private TextView tvSignInState;
	private LinearLayout linearSignInTime;
	private TextView tvSignInTime;
	private LinearLayout linearSignInAddress;
	private TextView tvSignInAddress;
	private Button btnSignIn;
	private ProgressDialog progressDialog;
	
	private EmployeeSignInPresenter employeeSignInPresenter = new EmployeeSignInPresenter(this); 
	
	private MaintainOrder maintainOrder;
	private FaultOrder faultOrder;
	private int workOrderType;
	private long workOrderId;
	
	private GeoCoder mSearch;
	private LocationClient locationClient;
	private MyLocationListener locationListener = new MyLocationListener();
	
	private LatLng elvtAddressLatLng;
	private LatLng curAddressLatLng;
	private String elevatorAddress;
	private String currentAddress;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private volatile boolean isShowCurrentAddress = false;
	private volatile boolean isSignInSuccess = false;
	
	private ConnectivityManager mConnectivityManager;
	
	private static final String tag = "EmployeeSignInDetailActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 在使用百度地图之前，一定要调用该方法，用于初始化参数！！
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_sign_in_detail);

		initData();
		initWidget();
	}
	
	private void initData() {
		Intent intent = getIntent();
		workOrderType = intent.getIntExtra("workOrderType", -1);		
		
		if (-1 == workOrderType) {
			throw new IllegalArgumentException("没有接收到工单类型的参数");
		}

		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			maintainOrder = JSON.parseObject(intent.getStringExtra("workOrder"), MaintainOrder.class);
			workOrderId = maintainOrder.getId();
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			faultOrder = JSON.parseObject(intent.getStringExtra("workOrder"), FaultOrder.class);
			workOrderId = faultOrder.getId();
		}
		
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);				
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
//		btnRefreshCurAddress = (Button) findViewById(R.id.btn_refreshCurAddress);
		ibtnRefreshCurAddress = (ImageButton) findViewById(R.id.ibtn_refreshCurAddress);
		tvWorkOrderType = (TextView) findViewById(R.id.tv_workOrderType);
		tvWorkOrderId = (TextView) findViewById(R.id.tv_workOrderId);
		tvReceivingTime = (TextView) findViewById(R.id.tv_receiveTime);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvSignInState = (TextView) findViewById(R.id.tv_signInState);
		linearSignInTime = (LinearLayout) findViewById(R.id.linear_signInTime);
		tvSignInTime = (TextView) findViewById(R.id.tv_signInTime);
		linearSignInAddress = (LinearLayout) findViewById(R.id.linear_signInAddress);
		tvSignInAddress = (TextView) findViewById(R.id.tv_signInAddress);
		linearCurrentAddress = (LinearLayout) findViewById(R.id.linear_currentAddress);
		tvCurrentAddress = (TextView) findViewById(R.id.tv_currentAddress);
		btnSignIn = (Button) findViewById(R.id.btn_signIn);
		
		progressDialog = new ProgressDialog(this);
		
		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			tvWorkOrderType.setText("保养工单 ");
//			tvWorkOrderId.setText(String.valueOf(maintainOrder.getId()));
			tvWorkOrderId.setText((maintainOrder.getNo()));
			tvReceivingTime.setText(sdf.format(maintainOrder.getReceivingTime()));
			elevatorAddress = maintainOrder.getElevatorRecord().getAddress();
			tvElevatorAddress.setText(elevatorAddress);
			
			if (maintainOrder.getSignInTime() != null) {
				btnSignIn.setEnabled(false);
				tvSignInState.setText("已签到");
				tvSignInTime.setText(sdf.format(maintainOrder.getSignInTime()));
				tvSignInAddress.setText(maintainOrder.getSignInAddress());
				linearCurrentAddress.setVisibility(View.GONE);
//				btnRefreshCurAddress.setVisibility(View.GONE);
				ibtnRefreshCurAddress.setVisibility(View.GONE);
			} else {
				linearSignInAddress.setVisibility(View.GONE);
				linearSignInTime.setVisibility(View.GONE);
				tvSignInState.setText("未签到");
				
				mSearch = GeoCoder.newInstance();
				mSearch.setOnGetGeoCodeResultListener(this);
				
				// 初始化用于定位的类LocationClient的对象
				initLocationClient();
			}
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			tvWorkOrderType.setText("故障工单 ");
//			tvWorkOrderId.setText(String.valueOf(faultOrder.getId()));
			tvWorkOrderId.setText((faultOrder.getNo()));
			tvReceivingTime.setText(sdf.format(faultOrder.getReceivingTime()));
			elevatorAddress = faultOrder.getElevatorRecord().getAddress();
			tvElevatorAddress.setText(elevatorAddress);
			
			if (faultOrder.getSignInTime() != null) {
				btnSignIn.setEnabled(false);
				tvSignInState.setText("已签到");
				tvSignInTime.setText(sdf.format(faultOrder.getSignInTime()));
				tvSignInAddress.setText(faultOrder.getSignInAddress());
				linearCurrentAddress.setVisibility(View.GONE);
//				btnRefreshCurAddress.setVisibility(View.GONE);
				ibtnRefreshCurAddress.setVisibility(View.GONE);
			} else {
				linearSignInAddress.setVisibility(View.GONE);
				linearSignInTime.setVisibility(View.GONE);
				tvSignInState.setText("未签到");
				
				mSearch = GeoCoder.newInstance();
				mSearch.setOnGetGeoCodeResultListener(this);
				
				// 初始化用于定位的类LocationClient的对象
				initLocationClient();
			}
		}
		
		btnBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent actIntent = new Intent(EmployeeSignInDetailActivity.this, 
						EmployeeSignInActivity.class);
				actIntent.putExtra("isNeedRefresh", isSignInSuccess);
				setResult(RESULT_OK, actIntent);				
				finish();				
			}
		});
		
		ibtnRefreshCurAddress.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// 刷新当前位置
				showProgressDialog();
				isShowCurrentAddress = false;
			}
		});
		
		btnSignIn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
//				Toast.makeText(EmployeeSignInDetailActivity.this, "一键签到", Toast.LENGTH_SHORT).show();
				if (mSearch == null) {
					throw new IllegalArgumentException("mSearch为null");
				}
				
				if (TextUtils.isEmpty(currentAddress)) {
					Toast.makeText(EmployeeSignInDetailActivity.this, "百度地图定位失败，无法进行签到操作，\n检查网络后重试", Toast.LENGTH_SHORT).show();
					Log.e(tag, "currentAddress is null");
					return;
				}
								
				NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo(); 
				if (networkInfo == null) { 
					Toast.makeText(EmployeeSignInDetailActivity.this, "网络异常，检查网络后重试", Toast.LENGTH_SHORT).show();
					return;
				} 
				
				// mSearch在编码过程中，不允许同时执行解码的操作，很容易造成空指针异常，因此
				// 在这个过程中，先将解码的操作关闭，编码结束后，再打开允许解码的操作。
//				btnRefreshCurAddress.setEnabled(false);
				ibtnRefreshCurAddress.setEnabled(false);
				showProgressDialog();
				mSearch.geocode(new GeoCodeOption().city("").address(elevatorAddress));
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		Intent actIntent = new Intent(EmployeeSignInDetailActivity.this, 
				EmployeeSignInActivity.class);
		actIntent.putExtra("isNeedRefresh", isSignInSuccess);
		setResult(RESULT_OK, actIntent);				
		finish();
	}

	/**
	 * 判断目标地址和参考地址的距离是否在误差范围内
	 * 
	 * @param src 参考（源）地址
	 * @param dest 目标地址
	 * @param distance 误差范围，单位为米
	 * @return true：在误差范围内；false：不在误差范围内
	 */
	private boolean isInDistanceScope(LatLng src, LatLng dest, double distance) {
		double srcLatitude = src.latitude;
		double srcLongitude = src.longitude;
		double destLatitude = dest.latitude;
		double destLongitude = dest.longitude;
		
		double d1 = Math.abs(srcLatitude - destLatitude) * 111000.0;
		double d2 = Math.abs(srcLongitude - destLongitude) * 111000.0 
				* Math.cos((srcLatitude + destLatitude) / 2.0 / 180 * Math.PI);
		
		double roundDistance = Math.sqrt(Math.pow(d1, 2) + Math.pow(d2, 2));
		
		Log.d(tag, "" + roundDistance);
		
		if (roundDistance <= distance) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onStart() {
		Log.d(tag, "开启定位功能");
		if (locationClient != null
				&& !locationClient.isStarted())
		{
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
				isShowCurrentAddress = true;
				
				// MapView销毁后不再处理新接收的位置
				if (location == null)
					return;
				
				LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());
				Log.i(tag, "当前位置，纬度：" + ptCenter.latitude + ",经度：" + ptCenter.longitude);

				NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo(); 
				if (networkInfo == null) { 
					Toast.makeText(EmployeeSignInDetailActivity.this, "网络异常，定位失败，检查网络后重试", Toast.LENGTH_SHORT).show();
					closeProgressDialog();
					return;
				} 
				
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
			}
		}		
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		Log.d(tag, "成功编码地理位置");
		
		elvtAddressLatLng = null;
		elvtAddressLatLng = result.getLocation();
		
		Log.i(tag, "电梯地址,纬度：" + elvtAddressLatLng.latitude + ",经度：" + elvtAddressLatLng.longitude);
		
		if (elvtAddressLatLng == null
				|| curAddressLatLng == null) {
//			throw new IllegalArgumentException("电梯经纬度或当前经纬度为null");
			Toast.makeText(EmployeeSignInDetailActivity.this, "百度地图定位失败，检查网络后重试", Toast.LENGTH_SHORT).show();
			Log.e(tag, "电梯经纬度或当前经纬度为null");
			return;
		}
		
		// 误差范围定为200米
		if (isInDistanceScope(elvtAddressLatLng, curAddressLatLng, 200)) {
			employeeSignInPresenter.signIn(workOrderType, workOrderId, currentAddress);			
		} else {
			Toast.makeText(EmployeeSignInDetailActivity.this, "超出误差范围，签到失败", Toast.LENGTH_SHORT).show();
		}
		
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
//				btnRefreshCurAddress.setEnabled(true);
				ibtnRefreshCurAddress.setEnabled(true);
			}
		});
		closeProgressDialog();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(EmployeeSignInDetailActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		Log.d(tag, "成功反编码地理位置");
		
		curAddressLatLng = result.getLocation();
		currentAddress = result.getAddress();
		tvCurrentAddress.setText(currentAddress);
		closeProgressDialog();
	}

	@Override
	public void signInSuccess() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				isSignInSuccess = true;
				Toast.makeText(EmployeeSignInDetailActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
				btnSignIn.setEnabled(false);
				tvSignInState.setText("已签到");
//				btnRefreshCurAddress.setVisibility(View.GONE);
				ibtnRefreshCurAddress.setVisibility(View.GONE);
				locationClient.unRegisterLocationListener(locationListener);
			}
		});
	}

	@Override
	public void showToast(final String tipInfo) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(EmployeeSignInDetailActivity.this, tipInfo, Toast.LENGTH_SHORT).show();
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
	
	// 自定义一个startActivity()方法
	public static void myStartActivity(Context context,
			int workOrderType, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, FaultOrderDetailActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivity(actIntent);
	}
	
	public static void myStartActivityForResult(Activity context, int requestCode, 
			int workOrderType, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, EmployeeSignInDetailActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivityForResult(actIntent, requestCode);
	}
}
