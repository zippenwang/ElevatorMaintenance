package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.Employee;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FaultHistoryAdapter extends ArrayAdapter<FaultOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final String tag = "FaultHistoryAdapter";
	
	public FaultHistoryAdapter(Context context, int textViewResourceId,
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
			subViewHolder.tvFaultReason = (TextView) view.findViewById(R.id.tv_faultReason);
			subViewHolder.tvFixEmployee = (TextView) view.findViewById(R.id.tv_fixEmployee);
			subViewHolder.tvFixedTime = (TextView) view.findViewById(R.id.tv_fixedTime);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}

		String no = faultOrder.getNo();
		if (!TextUtils.isEmpty(no)) {
			subViewHolder.tvWorkOrderId.setText(no);
		} else {
			subViewHolder.tvWorkOrderId.setText("无");
		}
		
		Employee employee = faultOrder.getEmployee();
		if (employee != null) {
			String employeeName = employee.getName();
			if (!TextUtils.isEmpty(employeeName)) {
				subViewHolder.tvFixEmployee.setText(employeeName);
			} else {
				subViewHolder.tvFixEmployee.setText("姓名未知");
			}
		} else {
			subViewHolder.tvFixEmployee.setText("暂无员工信息");
		}
		
		Date signOutTime = faultOrder.getSignOutTime();
		if (signOutTime != null) {
			subViewHolder.tvFixedTime.setText(sdf2.format(signOutTime));
		} else {
			subViewHolder.tvFixedTime.setText("暂无");
		}

		String reason = faultOrder.getReason();
		if (!TextUtils.isEmpty(reason)) {
			subViewHolder.tvFaultReason.setText(reason);
		} else {
			subViewHolder.tvFaultReason.setText("暂无");
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
		TextView tvFaultReason;
		TextView tvFixEmployee;
		TextView tvFixedTime;
	}
}
