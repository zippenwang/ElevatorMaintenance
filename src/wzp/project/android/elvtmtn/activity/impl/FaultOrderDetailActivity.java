package wzp.project.android.elvtmtn.activity.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.IWorkOrderDetailActivity;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;
import wzp.project.android.elvtmtn.biz.IWorkOrderBiz;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.Group;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.fragment.impl.UnfinishedWorkOrderFragment;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderState;
import wzp.project.android.elvtmtn.helper.contant.WorkOrderType;
import wzp.project.android.elvtmtn.presenter.WorkOrderReceivePresenter;
import wzp.project.android.elvtmtn.util.MyApplication;
import wzp.project.android.elvtmtn.util.MyProgressDialog;

public class FaultOrderDetailActivity extends BaseActivity 
	implements IWorkOrderDetailActivity, OnGetGeoCoderResultListener {

	private Button btnBack;
	private Button btnQueryElevatorRecord;
	private Button btnDestNavi;								// 目的地导航按钮
	private Button btnReceiveOrder;
	private Button btnCancelReceiveOrder;
	private TextView tvWorkOrderId;
	private TextView tvElevatorAddress;
	private TextView tvFaultOccuredTime;
	private TextView tvFaultDescription;
	private TextView tvReceiveState;
	private LinearLayout linearReceiveTime;
	private TextView tvReceiveTime;
	
	private LinearLayout linearFinished;
	private TextView tvFixEmployee;
	private TextView tvFixGroup;
	private TextView tvSignInTime;
	private TextView tvSignInAddress;
	private TextView tvSignOutTime;
	private TextView tvSignOutAddress;
	private TextView tvFaultReason;
	private TextView tvIsFixed;
	private TextView tvRemark;
	
	private ProgressDialog progressDialog;	
	private MyProgressDialog myProgressDialog;	
	
	private WorkOrderReceivePresenter workOrderReceivePresenter 
		= new WorkOrderReceivePresenter(this);
	
	private SharedPreferences preferences 
		= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
	private Long employeeId;
	
	private int workOrderState;
	private FaultOrder faultOrder;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private boolean isReceiveOrCancelOrder = false;
	
	private GeoCoder mSearch;
	private LocationClient locationClient;
	private MyLocationListener locationListener = new MyLocationListener();
	
	private LatLng elvtAddressLatLng;
	private LatLng curAddressLatLng;
	
	private ConnectivityManager mConnectivityManager;
	private boolean isNetworkActive = true;
	
	private static final String tag = "WorkOrderDetailActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 在使用百度地图之前，一定要调用该方法，用于初始化参数！！
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_fault_order_detail);
		
		initData();
		initWidget();
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
	
	private void initData() {
		Intent intent = getIntent();
		workOrderState = intent.getIntExtra("workOrderState", -1);		
		
		if (-1 == workOrderState) {
			throw new IllegalArgumentException("没有接收到工单类型或工单状态的参数");
		} 

		faultOrder = JSON.parseObject(intent.getStringExtra("workOrder"), FaultOrder.class);
		
		employeeId = preferences.getLong("employeeId", -1);
		if (-1 == employeeId) {
			throw new IllegalArgumentException("员工id有误");
		}
		
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (networkInfo == null) {
			isNetworkActive = false;
			Toast.makeText(FaultOrderDetailActivity.this, 
					"网络异常，检查网络后重试", Toast.LENGTH_SHORT).show();
			return;
		} 
		if (workOrderState != WorkOrderState.FINISHED) {
			mSearch = GeoCoder.newInstance();
			mSearch.setOnGetGeoCodeResultListener(this);
			initLocationClient();
		}
	}
	
	private void initWidget() {
		tvWorkOrderId = (TextView) findViewById(R.id.tv_workOrderId);
		tvElevatorAddress = (TextView) findViewById(R.id.tv_elevatorAddress);
		tvFaultOccuredTime = (TextView) findViewById(R.id.tv_faultOccuredTime);
		tvFaultDescription = (TextView) findViewById(R.id.tv_faultDescription);
		tvReceiveState = (TextView) findViewById(R.id.tv_receiveState);
		linearReceiveTime = (LinearLayout) findViewById(R.id.linear_receiveTime);
		tvReceiveTime = (TextView) findViewById(R.id.tv_receiveTime);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnQueryElevatorRecord = (Button) findViewById(R.id.btn_queryElevatorRecord);
		btnReceiveOrder = (Button) findViewById(R.id.btn_receiveOrder);
		btnCancelReceiveOrder = (Button) findViewById(R.id.btn_cancelReceiveOrder);
		btnDestNavi = (Button) findViewById(R.id.btn_destNavi);
		
		progressDialog = new ProgressDialog(this);
		myProgressDialog = new MyProgressDialog(this);
		
		// btnDestNavi按钮需要等待获取了电梯对应的经纬度之后，才能起作用
		btnDestNavi.setEnabled(false);		
		
		/*
		 * 为未完成、已完成的故障工单均需要显示的控件设置数值
		 */
//		tvWorkOrderId.setText(String.valueOf(faultOrder.getId()));
		String no = faultOrder.getNo();
		if (!TextUtils.isEmpty(no)) {
			tvWorkOrderId.setText(no);
		} else {
			tvWorkOrderId.setText("暂无工单号");
			tvWorkOrderId.setTextSize(18);
		}
		
		ElevatorRecord elevatorRecord = faultOrder.getElevatorRecord();
		String elevatorAddress = "";
		if (elevatorRecord != null) {
			elevatorAddress = elevatorRecord.getAddress();
			if (!TextUtils.isEmpty(elevatorAddress)) {
				elevatorAddress = elevatorAddress.trim();
				tvElevatorAddress.setText(elevatorAddress);
			} else {
				tvElevatorAddress.setText("暂无该信息");
				showToast("电梯地址未知，无法进行导航");
			}
		} else {
			tvElevatorAddress.setText("暂无该信息");
			showToast("电梯地址未知，无法进行导航");
		}
		
		Date occuredTime = faultOrder.getOccuredTime();
		if (occuredTime != null) {
			tvFaultOccuredTime.setText(sdf.format(occuredTime));
		} else {
			tvFaultOccuredTime.setText("暂无该信息");
		}
		
		String faultDescription = faultOrder.getDescription();
		if (!TextUtils.isEmpty(faultDescription)) {
			tvFaultDescription.setText(faultDescription.trim());
		} else {
			tvFaultDescription.setText("无");
		}	
		
		Date receivingTime = faultOrder.getReceivingTime();
		Employee employee = null;
		if (receivingTime != null) {
			tvReceiveState.setText("已接单");
			tvReceiveState.setTextColor(Color.BLACK);
			linearReceiveTime.setVisibility(View.VISIBLE);
			tvReceiveTime.setText(sdf2.format(receivingTime));
			// 正常情况下，receivingTime为null，employee也一定为空
			employee = faultOrder.getEmployee();
			if (employee != null) {
				if (employeeId == employee.getId()) {
					btnReceiveOrder.setVisibility(View.GONE);
					btnCancelReceiveOrder.setVisibility(View.VISIBLE);
				} else {
					btnReceiveOrder.setEnabled(false);
				}
			}
		} else {
			tvReceiveState.setText("未接单");
			tvReceiveState.setTextColor(Color.RED);
		}
			
		/*
		 * 已完成的故障工单需要显示的控件
		 */
		if (workOrderState == WorkOrderState.FINISHED) {
			linearFinished = (LinearLayout) findViewById(R.id.linear_finished);
			tvFixEmployee = (TextView) findViewById(R.id.tv_fixEmployee);
			tvFixGroup = (TextView) findViewById(R.id.tv_fixGroup);
			tvSignInTime = (TextView) findViewById(R.id.tv_signInTime);
			tvSignInAddress = (TextView) findViewById(R.id.tv_signInAddress);
			tvSignOutTime = (TextView) findViewById(R.id.tv_signOutTime);
			tvSignOutAddress = (TextView) findViewById(R.id.tv_signOutAddress);
			tvFaultReason = (TextView) findViewById(R.id.tv_faultReason);
			tvIsFixed = (TextView) findViewById(R.id.tv_isFixed);
			tvRemark = (TextView) findViewById(R.id.tv_remark);
			
			/*
			 * 为已完成的故障工单需要显示的控件设置数值
			 */
			linearFinished.setVisibility(View.VISIBLE);
			btnReceiveOrder.setVisibility(View.GONE);
			btnCancelReceiveOrder.setVisibility(View.GONE);
			btnDestNavi.setVisibility(View.GONE);
			btnQueryElevatorRecord.setTextSize(18);
			
			if (employee != null) {
				String employeeName = employee.getName();
				if (!TextUtils.isEmpty(employeeName)) {
					tvFixEmployee.setText(employeeName.trim());
				} else {
					tvFixEmployee.setText("姓名未知");
				}
				
				Group group = employee.getGroup();
				if (group != null) {
					String groupName = group.getName();
					if (!TextUtils.isEmpty(groupName)) {
						tvFixGroup.setText(groupName.trim());
					} else {
						tvFixGroup.setText("暂无小组名称");
					}
					tvFixGroup.setText(group.getName().trim());
				} else {
					tvFixGroup.setText("暂无组信息");
				}
			} else {
				tvFixEmployee.setText("暂无员工信息");
				tvFixGroup.setText("暂无组信息");
			}
			
			Date signInTime = faultOrder.getSignInTime();
			if (signInTime != null) {
				tvSignInTime.setText(sdf2.format(signInTime));
			} else {
				tvSignInTime.setText("暂无该信息");
			}
			
			String signInAddress = faultOrder.getSignInAddress();
			if (!TextUtils.isEmpty(signInAddress)) {
				tvSignInAddress.setText(signInAddress.trim());
			} else {
				tvSignInAddress.setText("暂无该信息");
			}
			
			Date signOutTime = faultOrder.getSignOutTime();
			if (signOutTime != null) {
				tvSignOutTime.setText(sdf2.format(signOutTime));
			} else {
				tvSignOutTime.setText("暂无该信息");
			}

			String signOutAddress = faultOrder.getSignOutAddress();
			if (!TextUtils.isEmpty(signOutAddress)) {
				tvSignOutAddress.setText(signOutAddress.trim());
			} else {
				tvSignOutAddress.setText("暂无该信息");
			}
			
			String faultReason = faultOrder.getReason();
			if (!TextUtils.isEmpty(faultReason)) {
				tvFaultReason.setText(faultReason.trim());
			} else {
				tvFaultReason.setText("无");
			}
			
			tvIsFixed.setText(faultOrder.getFixed() ? "已修好" : "未修好");
			
			String remark = faultOrder.getRemark();
			if (!TextUtils.isEmpty(remark)) {
				tvRemark.setText(remark.trim());
			} else {
				tvRemark.setText("无");
			}
			
		} else if (isNetworkActive
				&& !TextUtils.isEmpty(elevatorAddress)) {
			mSearch.geocode(new GeoCodeOption().city("").address(elevatorAddress));
		}
		
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (workOrderState != WorkOrderState.FINISHED) {
					Log.i(tag, "" + isReceiveOrCancelOrder);
					Intent fragIntent = new Intent(FaultOrderDetailActivity.this, MaintainOrderSearchActivity.class);
					fragIntent.putExtra("isNeedRefresh", isReceiveOrCancelOrder);
					/*if (isReceiveOrCancelOrder) {
						fragIntent.putExtra("receivingTime", faultOrder.getReceivingTime());
					}*/
					
					setResult(RESULT_OK, fragIntent);
				}
				finish();
			}
		});
		
		btnQueryElevatorRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ElevatorRecordDetailActivity.myStartActivity(FaultOrderDetailActivity.this, 
						JSON.toJSONString(faultOrder.getElevatorRecord()));
			}
		});
		
		btnReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				isReceiveOrCancelOrder = true;
				/*
				 * 此处执行接单的相关操作，记录接单时间等。
				 */
				workOrderReceivePresenter.receiveOrder(WorkOrderType.FAULT_ORDER, faultOrder.getId(), employeeId);
			}
		});
		
		btnCancelReceiveOrder.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				isReceiveOrCancelOrder = true;
				/*
				 * 此处执行取消接单的相关操作，记录接单时间等。
				 */
				workOrderReceivePresenter.cancelReceiveOrder(WorkOrderType.FAULT_ORDER, faultOrder.getId(), employeeId);

			}
		});
		
		btnDestNavi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo(); 
				if (networkInfo == null) { 
					Toast.makeText(FaultOrderDetailActivity.this, 
							"网络异常，检查网络后重试", Toast.LENGTH_SHORT).show();
					return;
				} 
				
				if (curAddressLatLng == null) {
					Toast.makeText(FaultOrderDetailActivity.this, 
							"百度地图定位失败，无法进行\n" +
							"签到操作，检查网络后重试", 
							Toast.LENGTH_SHORT).show();
					Log.e(tag, "curAddressLatLng is null");
					return;
				}
				
				if (elvtAddressLatLng == null) {
					Toast.makeText(FaultOrderDetailActivity.this, 
							"解析电梯地址失败，刷新网络后重试", Toast.LENGTH_SHORT).show();
					Log.e(tag, "elvtAddressLatLng is null");
					return;
				}
				
				showProgressDialog();
				
				NaviPara naviPara = new NaviPara();
				naviPara.startPoint = curAddressLatLng;
				naviPara.startName = "从这里开始";
				naviPara.endPoint = elvtAddressLatLng;
				naviPara.endName = "到这里结束";				
				
				try {
					BaiduMapNavigation.openBaiduMapNavi(naviPara, 
							FaultOrderDetailActivity.this);
				} catch (BaiduMapAppNotSupportNaviException e) {
					closeProgressDialog();
					Log.e(tag, Log.getStackTraceString(e));
					AlertDialog.Builder builder = new AlertDialog.Builder(FaultOrderDetailActivity.this);
					builder.setMessage("您尚未安装百度地图app或app版本过低，请安装或更新app后重试 ！");
					builder.setTitle("提示消息");
					builder.setCancelable(true);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					builder.create().show();
					return;
				}

				closeProgressDialog();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (workOrderState != WorkOrderState.FINISHED) {
			Log.i(tag, "" + isReceiveOrCancelOrder);
			Intent fragIntent = new Intent(FaultOrderDetailActivity.this, MaintainOrderSearchActivity.class);
			fragIntent.putExtra("isNeedRefresh", isReceiveOrCancelOrder);
			if (isReceiveOrCancelOrder) {
				fragIntent.putExtra("receivingTime", faultOrder.getReceivingTime());
			}
			
			setResult(RESULT_OK, fragIntent);
		}
		finish();
	}

	@Override
	public void showToast(final String text) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(FaultOrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});		
	}
	
	@Override
	public void receiveSuccess(final Date receivingTime) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(FaultOrderDetailActivity.this, "接单成功", Toast.LENGTH_SHORT).show();
				btnReceiveOrder.setVisibility(View.GONE);
				btnCancelReceiveOrder.setVisibility(View.VISIBLE);
				tvReceiveState.setText("已接单");
				tvReceiveState.setTextColor(Color.BLACK);
				linearReceiveTime.setVisibility(View.VISIBLE);
//				faultOrder.setReceivingTime(receivingTime);
				tvReceiveTime.setText(sdf2.format(receivingTime));
			}
		});
	}

	@Override
	public void cancelReceiveSuccess() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				Toast.makeText(FaultOrderDetailActivity.this, "成功取消接单", Toast.LENGTH_SHORT).show();
				btnCancelReceiveOrder.setVisibility(View.GONE);
				btnReceiveOrder.setVisibility(View.VISIBLE);
				tvReceiveState.setText("未接单");
				tvReceiveState.setTextColor(Color.RED);
				linearReceiveTime.setVisibility(View.GONE);
//				faultOrder.setReceivingTime(null);
			}
		});
	}
	
	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.d(tag, "进入MyLocationListener");
			
			// MapView销毁后不再处理新接收的位置
			if (location == null)
				return;
			
			curAddressLatLng = new LatLng(location.getLatitude(), location.getLongitude());
			Log.i(tag, "当前位置，纬度：" + curAddressLatLng.latitude 
					+ ",经度：" + curAddressLatLng.longitude);
		}
	}
	
	private void initLocationClient() {		
		locationClient = new LocationClient(this);
		
		// 利用 LocationClientOption类为LocationClient设定参数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);		// 打开gps导航
		option.setCoorType("bd09ll"); 	// 设置坐标类型
		option.setScanSpan(2000);		// 扫描的时间间隔为2s
		
		locationClient.setLocOption(option);
		
		locationClient.registerLocationListener(locationListener);
	}

	// 自定义一个startActivity()方法
	public static void myStartActivity(Context context,
			int workOrderState, String jsonWorkOrder) {
		Intent actIntent = new Intent(context, FaultOrderDetailActivity.class);
		actIntent.putExtra("workOrderState", workOrderState);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		context.startActivity(actIntent);
	}
	
	public static void myStartActivityForResult(Fragment fragment, int requestCode, 
			int workOrderState, String jsonWorkOrder) {
		Intent actIntent = new Intent(fragment.getActivity(), FaultOrderDetailActivity.class);
		actIntent.putExtra("workOrderState", workOrderState);
		actIntent.putExtra("workOrder", jsonWorkOrder);
		fragment.startActivityForResult(actIntent, requestCode);
	}
	
	@Override
	public void showProgressDialog() {}
	
	@Override
	public void showProgressDialog(String tipInfo) {
		myProgressDialog.setMessage(tipInfo);
		myProgressDialog.setCancelable(true);
		
		myProgressDialog.show();
	}
	
	public void closeProgressDialog() {
		if (progressDialog != null
				&& progressDialog.isShowing()) {
			progressDialog.dismiss();
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
		
		
		btnDestNavi.setEnabled(true);
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void backToLoginInterface() {
		EmployeeLoginActivity.myForceStartActivity(this);
	}
}
