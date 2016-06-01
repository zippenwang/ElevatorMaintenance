package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FinishedFaultOrderAdapter extends ArrayAdapter<FaultOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH时");
	
	private static final String tag = "FinishedFaultOrderAdapter";
	
	public FinishedFaultOrderAdapter(Context context, int textViewResourceId,
			List<FaultOrder> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FaultOrder faultOrder = getItem(position);
		View view = null;
		SubViewHolder subViewHolder = null;
		
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
			subViewHolder = new SubViewHolder();
			subViewHolder.tvWorkOrderId = (TextView) view.findViewById(R.id.tv_workOrderId);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvFixEmployee = (TextView) view.findViewById(R.id.tv_fixEmployee);
			subViewHolder.tvFixedTime = (TextView) view.findViewById(R.id.tv_fixedTime);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}
		
//		subViewHolder.tvWorkOrderId.setText(faultOrder.getId() + "");
		subViewHolder.tvWorkOrderId.setText(faultOrder.getNo());
		
		if (faultOrder.getEmployee() != null) {
			if (!TextUtils.isEmpty(faultOrder.getEmployee().getName())) {
				subViewHolder.tvFixEmployee.setText(faultOrder.getEmployee().getName());
				subViewHolder.tvFixEmployee.setTextColor(Color.BLACK);
			} else {
				subViewHolder.tvFixEmployee.setText("姓名未知");
				subViewHolder.tvFixEmployee.setTextColor(Color.RED);
			}
		} else {
			subViewHolder.tvFixEmployee.setText("暂无员工信息");
			subViewHolder.tvFixEmployee.setTextColor(Color.RED);
		}
		
		if (faultOrder.getSignOutTime() != null) {
			subViewHolder.tvFixedTime.setText(sdf2.format(faultOrder.getSignOutTime()));
			subViewHolder.tvFixedTime.setTextColor(Color.BLACK);
		} else {
			subViewHolder.tvFixedTime.setText("暂无");
			subViewHolder.tvFixedTime.setTextColor(Color.RED);
		}
		
		if (faultOrder.getElevatorRecord() != null) {
			if (faultOrder.getElevatorRecord().getAddress() == null) {
				subViewHolder.tvAddress.setText("暂无地址信息");
				subViewHolder.tvAddress.setTextColor(Color.RED);
			} else {
				subViewHolder.tvAddress.setText(faultOrder.getElevatorRecord().getAddress());
				subViewHolder.tvAddress.setTextColor(Color.BLACK);
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
		TextView tvFixedTime;
	}

}
