package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
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

public class UnfinishedFaultOrderAdapter extends ArrayAdapter<FaultOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String tag = "UnfinishedFaultOrderAdapter";
	
	public UnfinishedFaultOrderAdapter(Context context, int textViewResourceId,
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
			subViewHolder.tvIsWorkOrderReceived = (TextView) view.findViewById(R.id.tv_isWorkOrderReceived);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvOccuredTime = (TextView) view.findViewById(R.id.tv_occuredTime);
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
		
		ElevatorRecord elevatorRecord = faultOrder.getElevatorRecord();
		if (elevatorRecord != null) {
			String elevatorAddress = elevatorRecord.getAddress();
			if (!TextUtils.isEmpty(elevatorAddress)) {
				subViewHolder.tvAddress.setText(elevatorAddress);
			} else {
				subViewHolder.tvAddress.setText("暂无地址信息");
			}
		} else {
			subViewHolder.tvAddress.setText("电梯档案为空");
		}
		
		Date occurredTime = faultOrder.getOccuredTime();
		if (occurredTime != null) {
			subViewHolder.tvOccuredTime.setText(sdf.format(occurredTime));
		} else {
			subViewHolder.tvOccuredTime.setText("暂无");
		}
		
		if (faultOrder.getReceivingTime() != null) {
			subViewHolder.tvIsWorkOrderReceived.setText("已接单");
			subViewHolder.tvIsWorkOrderReceived.setTextColor(Color.BLACK);
		} else {
			subViewHolder.tvIsWorkOrderReceived.setText("未接单");
			subViewHolder.tvIsWorkOrderReceived.setTextColor(Color.RED);
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
		TextView tvIsWorkOrderReceived;
		TextView tvAddress;
		TextView tvOccuredTime;
	}

}
