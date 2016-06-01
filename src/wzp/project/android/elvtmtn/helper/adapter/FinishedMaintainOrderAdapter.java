package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FinishedMaintainOrderAdapter extends ArrayAdapter<MaintainOrder> {

	private int resourceId;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH时");
	
	private static final String tag = "FinishedMaintainOrderAdapter";
	
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
		subViewHolder.tvWorkOrderId.setText(maintainOrder.getNo());
		
		if (maintainOrder.getEmployee() != null) {
			if (!TextUtils.isEmpty(maintainOrder.getEmployee().getName())) {
				subViewHolder.tvFixEmployee.setText(maintainOrder.getEmployee().getName());
				subViewHolder.tvFixEmployee.setTextColor(Color.BLACK);
			} else {
				subViewHolder.tvFixEmployee.setText("姓名未知");
				subViewHolder.tvFixEmployee.setTextColor(Color.RED);
			}
		} else {
			subViewHolder.tvFixEmployee.setText("暂无员工信息");
			subViewHolder.tvFixEmployee.setTextColor(Color.RED);
		}
		
		if (maintainOrder.getSignOutTime() != null) {
			subViewHolder.tvFinishedTime.setText(sdf2.format(maintainOrder.getSignOutTime()));
			subViewHolder.tvFinishedTime.setTextColor(Color.BLACK);
		} else {
			subViewHolder.tvFinishedTime.setText("暂无");
			subViewHolder.tvFinishedTime.setTextColor(Color.RED);
		}
		
		if (maintainOrder.getElevatorRecord() != null) {
			if (maintainOrder.getElevatorRecord().getAddress() == null) {
				subViewHolder.tvAddress.setText("暂无地址信息");
				subViewHolder.tvAddress.setTextColor(Color.RED);
			} else {
				subViewHolder.tvAddress.setText(maintainOrder.getElevatorRecord().getAddress());
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
		TextView tvFinishedTime;
	}

}
