package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FaultOrderAdapter extends ArrayAdapter<FaultOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public FaultOrderAdapter(Context context, int textViewResourceId,
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
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
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
		
		subViewHolder.tvWorkOrderId.setText(faultOrder.getId() + "");
		subViewHolder.tvAddress.setText(faultOrder.getElevatorRecord().getAddress());
		subViewHolder.tvOccuredTime.setText(sdf.format(faultOrder.getOccuredTime()));
		if (faultOrder.getReceivingTime() != null) {
			subViewHolder.tvIsWorkOrderReceived.setText("已接单");
		} else {
			subViewHolder.tvIsWorkOrderReceived.setText("未接单");
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
