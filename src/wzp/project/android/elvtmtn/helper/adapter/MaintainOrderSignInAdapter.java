package wzp.project.android.elvtmtn.helper.adapter;

import java.util.Date;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
import wzp.project.android.elvtmtn.entity.MaintainOrder;
import wzp.project.android.elvtmtn.helper.contant.ProjectContants;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 签到操作所列出的已经接单的保养工单Adapter
 * @author Zippen
 *
 */
public class MaintainOrderSignInAdapter extends ArrayAdapter<MaintainOrder> {

	private int resourceId;
	
	
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
		
		String no = maintainOrder.getNo();
		if (!TextUtils.isEmpty(no)) {
			subViewHolder.tvWorkOrderId.setText(no);
		} else {
			subViewHolder.tvWorkOrderId.setText("无");
		}
		
		ElevatorRecord elevatorRecord = maintainOrder.getElevatorRecord();
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
		
		Date receivingTime = maintainOrder.getReceivingTime();
		if (receivingTime != null) {
			subViewHolder.tvReceivingTime.setText(ProjectContants.sdf1.format(receivingTime));
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
