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

public class MaintainOrderFeedbackAdapter extends ArrayAdapter<MaintainOrder> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String tag = "MaintainOrderFeedbackAdapter";
	
	public MaintainOrderFeedbackAdapter(Context context, int textViewResourceId,
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
			subViewHolder.tvWorkOrderNo = (TextView) view.findViewById(R.id.tv_workOrderNo);
			subViewHolder.tvIsPartFinished = (TextView) view.findViewById(R.id.tv_isPartFinished);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvSignInTime = (TextView) view.findViewById(R.id.tv_signInTime);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}
		
//		subViewHolder.tvWorkOrderNo.setText(maintainOrder.getId() + "");
		String no = maintainOrder.getNo();
		if (!TextUtils.isEmpty(no)) {
			subViewHolder.tvWorkOrderNo.setText(no);
		} else {
			subViewHolder.tvWorkOrderNo.setText("无");
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
		
		Date signInTime = maintainOrder.getSignInTime();
		if (signInTime != null) {
			subViewHolder.tvSignInTime.setText(sdf.format(signInTime));
		} else {
			subViewHolder.tvSignInTime.setText("无");
		}
		
		if (maintainOrder.getSignOutTime() != null) {
			if (!maintainOrder.getFinished()) {
				subViewHolder.tvIsPartFinished.setText("部分反馈");
				subViewHolder.tvIsPartFinished.setTextColor(Color.BLACK);
			} else {
				subViewHolder.tvIsPartFinished.setText("已反馈");
				subViewHolder.tvIsPartFinished.setTextColor(Color.BLACK);
			}
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
