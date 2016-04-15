package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MaintainOrderAdapter extends ArrayAdapter<MaintainOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public MaintainOrderAdapter(Context context, int textViewResourceId,
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
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
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
		
		subViewHolder.tvWorkOrderId.setText(maintainOrder.getId() + "");
		subViewHolder.tvAddress.setText(maintainOrder.getElevatorRecord().getAddress());
		subViewHolder.tvFinalTime.setText(sdf.format(maintainOrder.getFinalTime()));
		if (maintainOrder.getReceivingTime() != null) {
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
		TextView tvFinalTime;
	}

}
