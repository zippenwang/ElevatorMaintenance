package wzp.project.android.elvtmtn.activity.impl;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;

public class EmployeeSignInDetailActivity extends BaseActivity implements OnGetGeoCoderResultListener {

	private TextView tvCurrentLocation;
	
	private GeoCoder mSearch;
//	private BaiduMap baiduMap;
	private LocationClient locationClient;
	private MyLocationListener locationListener = new MyLocationListener();
	
	private static final String tag = "EmployeeSignInDetailActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 在使用百度地图之前，一定要调用该方法，用于初始化参数！！
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_sign_in_detail);
		
		tvCurrentLocation = (TextView) findViewById(R.id.tv_currentLocation);
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		// 初始化用于定位的类LocationClient的对象
		initLocationClient();
	}
	
	@Override
	protected void onStart() {
		Log.d(tag, "开启定位功能");
		/*
		 * 开启图层定位
		 */
//		baiduMap.setMyLocationEnabled(true);
		if (!locationClient.isStarted())
		{
			locationClient.start();
		}
		
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(tag, "关闭定位功能");
		
		/*
		 * 关闭图层定位
		 */
//		baiduMap.setMyLocationEnabled(false);
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
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(EmployeeSignInDetailActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		LatLng latLng = result.getLocation();
		tvCurrentLocation.setText("经度：" + latLng.latitude + ", 纬度：" + latLng.longitude + ";\n当前地址为：" + result.getAddress());
		
	}



}
