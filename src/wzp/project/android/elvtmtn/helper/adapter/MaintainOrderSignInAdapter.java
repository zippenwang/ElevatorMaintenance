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

public class MaintainOrderSignInAdapter extends ArrayAdapter<MaintainOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String tag = "FaultOrderSignInAdapter";
	
	public MaintainOrderSignInAdapter(Context context, int textViewResourceId,
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
			subViewHolder.tvIsSignIn = (TextView) view.findViewById(R.id.tv_isSignIn);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvReceivingTime = (TextView) view.findViewById(R.id.tv_receivingTime);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}
		
//		subViewHolder.tvWorkOrderId.setText(maintainOrder.getId() + "");
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
		if (maintainOrder.getReceivingTime() != null) {
			subViewHolder.tvReceivingTime.setText(sdf.format(maintainOrder.getReceivingTime()));
		} else {
			subViewHolder.tvReceivingTime.setText("无");
		}
		
		if (maintainOrder.getSignInTime() != null) {
			subViewHolder.tvIsSignIn.setText("已签到");
			subViewHolder.tvIsSignIn.setTextColor(Color.BLACK);
		} else {
			subViewHolder.tvIsSignIn.setText("未签到");
			subViewHolder.tvIsSignIn.setTextColor(Color.RED);
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
		TextView tvIsSignIn;
		TextView tvAddress;
		TextView tvReceivingTime;
	}

}
