package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UnfOvdMaintainOrderAdapter extends ArrayAdapter<MaintainOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public UnfOvdMaintainOrderAdapter(Context context, int textViewResourceId,
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
			subViewHolder.tvIsWorkOrderReceived = (TextView) view.findViewById(R.id.tv_isWorkOrderReceived);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvFinalTime = (TextView) view.findViewById(R.id.tv_finalTime);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}
		
//		subViewHolder.tvWorkOrderId.setText(String.valueOf(maintainOrder.getId()));
		subViewHolder.tvWorkOrderId.setText(maintainOrder.getNo());
		if (maintainOrder.getElevatorRecord() != null) {
			if (maintainOrder.getElevatorRecord().getAddress() == null) {
				subViewHolder.tvAddress.setText("暂无地址信息");
			} else {
				subViewHolder.tvAddress.setText(maintainOrder.getElevatorRecord().getAddress());
			}
		} else {
			subViewHolder.tvAddress.setText("电梯档案为空");
		}
		if (maintainOrder.getFinalTime() != null) {
			subViewHolder.tvFinalTime.setText(sdf.format(maintainOrder.getFinalTime()));
		} else {
			subViewHolder.tvFinalTime.setText("暂无该信息");
		}
		if (maintainOrder.getReceivingTime() != null) {
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
		TextView tvFinalTime;
	}

}
