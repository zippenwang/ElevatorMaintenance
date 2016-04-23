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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IEmployeeSignInActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.biz.IEmployeeSignInListener;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;

public class EmployeeSignInDetailActivity extends BaseActivity implements IEmployeeSignInActivity, OnGetGeoCoderResultListener {

	private Button btnBack;
	private Button btnRefreshCurAddress;
	private TextView tvWorkOrderId;
	private TextView tvReceivingTime;
	private TextView tvElevatorAddress;
	private TextView tvCurrentAddress;
	private Button btnSignIn;
	private ProgressDialog progressDialog;
	
	private MaintainOrder maintainOrder;
	private FaultOrder faultOrder;
	private int workOrderType;
	
	private GeoCoder mSearch;
	private LocationClient locationClient;
	private MyLocationListener locationListener = new MyLocationListener();
	
	private LatLng elvtAddressLatLng;
	private LatLng curAddressLatLng;
	private String elevatorAddress;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String tag = "EmployeeSignInDetailActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 在使用百度地图之前，一定要调用该方法，用于初始化参数！！
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_sign_in_detail);

//		initData();
		initWidget();
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		// 初始化用于定位的类LocationClient的对象
		initLocationClient();
	}
	
	private void initData() {
		Intent intent = getIntent();
		workOrderType = intent.getIntExtra("workOrderType", -1);		
		
		if (-1 == workOrderType) {
			throw new IllegalArgumentException("没有接收到工单类型的参数");
		}

		if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			maintainOrder = JSON.parseObject(intent.getStringExtra("workOrder"), MaintainOrder.class);
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			faultOrder = JSON.parseObject(intent.getStringExtra("workOrder"), FaultOrder.class);
		}
	}
	
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_back);
		btnRefreshCurAddress = (Button) findViewById(R.id.btn_refreshCurAddress);
		tvWorkOrderId = (TextView) findViewById(R.id.tv_workOrderId);
		tvReceivingTime = (TextView) findViewById(R.id.tv_receiveTime);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvCurrentAddress = (TextView) findViewById(R.id.tv_currentAddress);
		btnSignIn = (Button) findViewById(R.id.btn_signIn);
		
		progressDialog = new ProgressDialog(this);
		
		/*if (workOrderType == WorkOrderType.MAINTAIN_ORDER) {
			tvWorkOrderId.setText(String.valueOf(maintainOrder.getId()));
			tvReceivingTime.setText(sdf.format(maintainOrder.getReceivingTime()));
			elevatorAddress = maintainOrder.getElevatorRecord().getAddress();
			tvElevatorAddress.setText(elevatorAddress);
		} else if (workOrderType == WorkOrderType.FAULT_ORDER) {
			tvWorkOrderId.setText(String.valueOf(faultOrder.getId()));
			tvReceivingTime.setText(sdf.format(faultOrder.getReceivingTime()));
			elevatorAddress = faultOrder.getElevatorRecord().getAddress();
			tvElevatorAddress.setText(elevatorAddress);
		}*/
		
		btnBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		btnRefreshCurAddress.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// 刷新当前位置
			}
		});
		
		btnSignIn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(EmployeeSignInDetailActivity.this, "一键签到", Toast.LENGTH_SHORT).show();
//				mSearch.geocode(new GeoCodeOption().city("").address(elevatorAddress));
			}
		});
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
		double delta = Math.sqrt(2) / 2 * distance * 1e-6;
		Log.d(tag, String.valueOf(delta));
		BigDecimal bdDelta = BigDecimal.valueOf(delta);
		BigDecimal bdSrcLatitude = BigDecimal.valueOf(src.latitude);
		BigDecimal bdSrcLongitude = BigDecimal.valueOf(src.longitude);
		BigDecimal bdDestLatitude = BigDecimal.valueOf(dest.latitude);
		BigDecimal bdDestLongitude = BigDecimal.valueOf(dest.longitude);
		
		if ((bdDestLatitude.subtract(bdSrcLatitude).abs().compareTo(bdDelta) <= 0)
				&& (bdDestLongitude.subtract(bdSrcLongitude).abs().compareTo(bdDelta)) <= 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onStart() {
		Log.d(tag, "开启定位功能");
		if (!locationClient.isStarted())
		{
			locationClient.start();
		}
		
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(tag, "关闭定位功能");
		locationClient.stop();
		
		super.onStop();
	}
	
	private void initLocationClient() {
		locationClient = new LocationClient(this);
		
		/*
		 * 
		 */
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
			
			// MapView销毁后不再处理新接收的位置
			if (location == null)
				return;
			
			LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
		}		
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		elvtAddressLatLng = null;
		elvtAddressLatLng = result.getLocation();
		
		if (elvtAddressLatLng == null
				|| curAddressLatLng == null) {
			throw new IllegalArgumentException("电梯经纬度或当前经纬度为null");
		}
		
		// 误差范围定为50米
		if (isInDistanceScope(elvtAddressLatLng, curAddressLatLng, 200)) {
			Toast.makeText(EmployeeSignInDetailActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(EmployeeSignInDetailActivity.this, "超出误差范围，签到失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(EmployeeSignInDetailActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		curAddressLatLng = result.getLocation();
//		tvCurrentAddress.setText("经度：" + latLng.latitude + ", 纬度：" + latLng.longitude + ";\n当前地址为：" + result.getAddress());
		tvCurrentAddress.setText(result.getAddress());
		closeProgressDialog();
	}
	
	// 自定义一个startActivity()方法
	public static void myStartActivity(Context context,
			int workOrderType, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, FaultOrderDetailActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivity(actIntent);
	}

	@Override
	public void signInSuccess() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(EmployeeSignInDetailActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
				btnSignIn.setEnabled(false);
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
	
	/*public static void myStartActivityForResult(Fragment fragment, int requestCode, 
			int workOrderType, String jsonWorkOrder) {
		Intent actIntent = new Intent(fragment.getActivity(), FaultOrderDetailActivity.class);
		actIntent.putExtra("workOrderType", workOrderType);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		fragment.startActivityForResult(actIntent, requestCode);
	}*/
}
