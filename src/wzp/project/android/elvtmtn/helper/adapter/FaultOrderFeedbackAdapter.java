package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.FaultOrder;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FaultOrderFeedbackAdapter extends ArrayAdapter<FaultOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String tag = "FaultOrderFeedbackAdapter";
	
	public FaultOrderFeedbackAdapter(Context context, int textViewResourceId,
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
			subViewHolder.tvWorkOrderNo = (TextView) view.findViewById(R.id.tv_workOrderNo);
			subViewHolder.tvIsPartFinished = (TextView) view.findViewById(R.id.tv_isPartFinished);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvSignInTime = (TextView) view.findViewById(R.id.tv_signInTime);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}
		
		subViewHolder.tvWorkOrderNo.setText(faultOrder.getId() + "");
		
		if (faultOrder.getElevatorRecord() != null) {
			if (faultOrder.getElevatorRecord().getAddress() == null) {
				subViewHolder.tvAddress.setText("暂无地址信息");
			} else {
				subViewHolder.tvAddress.setText(faultOrder.getElevatorRecord().getAddress());
			}
		} else {
			subViewHolder.tvAddress.setText("电梯档案为空");
		}
		
		if (faultOrder.getSignInTime() != null) {
			subViewHolder.tvSignInTime.setText(FaultOrderFeedbackAdapter.sdf.format(faultOrder.getSignInTime()));
		} else {
			subViewHolder.tvSignInTime.setText("无");
		}
		
		if (faultOrder.getSignOutTime() != null
				&& !faultOrder.getFixed()) {
			subViewHolder.tvIsPartFinished.setText("部分反馈");
			subViewHolder.tvIsPartFinished.setTextColor(Color.BLACK);
		} else {
			subViewHolder.tvIsPartFinished.setText("未反馈");
			subViewHolder.tvIsPartFinished.setTextColor(Color.RED);
		}
			
		return view;
	}
	
	/**
	 * 子控件
	 * @author Zippen
	 *
	 */
	private class SubViewHolder {
		TextView tvWorkOrderNo;
		TextView tvIsPartFinished;
		TextView tvAddress;
		TextView tvSignInTime;
	}

}
