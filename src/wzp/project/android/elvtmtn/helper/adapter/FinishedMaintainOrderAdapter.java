package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 已完成的保养工单Adapter
 * @author Zippen
 *
 */
public class FinishedMaintainOrderAdapter extends ArrayAdapter<MaintainOrder> {

	private int resourceId;
	
	
	public FinishedMaintainOrderAdapter(Context context, int textViewResourceId,
			List<MaintainOrder> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MaintainOrder maintainOrder = getItem(position);
		View view = null;
		SubViewHolder subViewHolder = null;
		
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
			subViewHolder = new SubViewHolder();
			subViewHolder.tvWorkOrderId = (TextView) view.findViewById(R.id.tv_workOrderId);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvFixEmployee = (TextView) view.findViewById(R.id.tv_fixEmployee);
			subViewHolder.tvFinishedTime = (TextView) view.findViewById(R.id.tv_finishedTime);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}
		
//		subViewHolder.tvWorkOrderId.setText(maintainOrder.getId() + "");
		String no = maintainOrder.getNo();
		if (!TextUtils.isEmpty(no)) {
			subViewHolder.tvWorkOrderId.setText(no);
		} else {
			subViewHolder.tvWorkOrderId.setText("无");
		}
		
		Employee employee = maintainOrder.getEmployee();
		if (employee != null) {
			String employeeName = employee.getName();
			if (!TextUtils.isEmpty(employeeName)) {
				subViewHolder.tvFixEmployee.setText(employeeName);
				subViewHolder.tvFixEmployee.setTextColor(Color.BLACK);
			} else {
				subViewHolder.tvFixEmployee.setText("姓名未知");
				subViewHolder.tvFixEmployee.setTextColor(Color.RED);
			}
		} else {
			subViewHolder.tvFixEmployee.setText("暂无员工信息");
			subViewHolder.tvFixEmployee.setTextColor(Color.RED);
		}
		
		Date signOutTime = maintainOrder.getSignOutTime();
		if (signOutTime != null) {
			subViewHolder.tvFinishedTime.setText(ProjectContants.sdf3.format(signOutTime));
			subViewHolder.tvFinishedTime.setTextColor(Color.BLACK);
		} else {
			subViewHolder.tvFinishedTime.setText("暂无");
			subViewHolder.tvFinishedTime.setTextColor(Color.RED);
		}
		
		ElevatorRecord elevatorRecord = maintainOrder.getElevatorRecord();
		if (elevatorRecord != null) {
			String elevatorAddress = elevatorRecord.getAddress();
			if (!TextUtils.isEmpty(elevatorAddress)) {
				subViewHolder.tvAddress.setText(elevatorAddress);
				subViewHolder.tvAddress.setTextColor(Color.BLACK);
			} else {
				subViewHolder.tvAddress.setText("暂无地址信息");
				subViewHolder.tvAddress.setTextColor(Color.RED);
			}
		} else {
			subViewHolder.tvAddress.setText("电梯档案为空");
			subViewHolder.tvAddress.setTextColor(Color.RED);
		}
			
		return view;
	}
	
	/**
	 * 子控件
	 * @author Zippen
	 *
	 */
	private class SubViewHolder {
		TextView tvWorkOrderId;
		TextView tvAddress;
		TextView tvFixEmployee;
		TextView tvFinishedTime;
	}

}
