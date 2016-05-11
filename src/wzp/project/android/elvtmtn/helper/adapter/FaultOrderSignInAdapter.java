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

public class FaultOrderSignInAdapter extends ArrayAdapter<FaultOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String tag = "FaultOrderSignInAdapter";
	
	public FaultOrderSignInAdapter(Context context, int textViewResourceId,
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
			subViewHolder.tvIsSignIn = (TextView) view.findViewById(R.id.tv_isSignIn);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvReceivingTime = (TextView) view.findViewById(R.id.tv_receivingTime);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}
		
//		subViewHolder.tvWorkOrderId.setText(faultOrder.getId() + "");
		subViewHolder.tvWorkOrderId.setText(faultOrder.getNo());
		if (faultOrder.getElevatorRecord() != null) {
			if (faultOrder.getElevatorRecord().getAddress() == null) {
				subViewHolder.tvAddress.setText("暂无地址信息");
			} else {
				subViewHolder.tvAddress.setText(faultOrder.getElevatorRecord().getAddress());
			}
		} else {
			subViewHolder.tvAddress.setText("电梯档案为空");
		}
		if (faultOrder.getReceivingTime() != null) {
			subViewHolder.tvReceivingTime.setText(FaultOrderSignInAdapter.sdf.format(faultOrder.getReceivingTime()));
		} else {
			subViewHolder.tvReceivingTime.setText("无");
		}
		
		if (faultOrder.getSignInTime() != null) {
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
