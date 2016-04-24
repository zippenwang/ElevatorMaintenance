package wzp.project.android.elvtmtn.helper.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.entity.ElevatorRecord;
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

public class ElevatorRecordAdapter extends ArrayAdapter<ElevatorRecord> {

	private int resourceId;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String tag = "UnfinishedFaultOrderAdapter";
	
	public ElevatorRecordAdapter(Context context, int textViewResourceId,
			List<ElevatorRecord> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ElevatorRecord elevatorRecord = getItem(position);
		View view = null;
		SubViewHolder subViewHolder = null;
		
		if (convertView == null) {
//			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
			subViewHolder = new SubViewHolder();
			subViewHolder.tvElevatorNumber = (TextView) view.findViewById(R.id.tv_elevatorNumber);
			subViewHolder.tvElevatorType = (TextView) view.findViewById(R.id.tv_elevatorType);
			subViewHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
			subViewHolder.tvPhone = (TextView) view.findViewById(R.id.tv_phone);
			view.setTag(subViewHolder);
		} else {
			view = convertView;
			subViewHolder = (SubViewHolder) view.getTag();
		}
		
		subViewHolder.tvElevatorNumber.setText(elevatorRecord.getId() + "");
		subViewHolder.tvElevatorType.setText(elevatorRecord.getType());		
		subViewHolder.tvAddress.setText(elevatorRecord.getAddress());
		subViewHolder.tvPhone.setText(elevatorRecord.getPhone());
		
		return view;
	}
	
	/**
	 * 子控件
	 * @author Zippen
	 *
	 */
	private class SubViewHolder {
		TextView tvElevatorNumber;
		TextView tvElevatorType;
		TextView tvAddress;
		TextView tvPhone;
	}

}
