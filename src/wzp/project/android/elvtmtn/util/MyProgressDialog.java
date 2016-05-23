package wzp.project.android.elvtmtn.util;

import wzp.project.android.elvtmtn.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProgressDialog extends Dialog {
	
	private Context context;
	private View view;
	private TextView tvTip;
	private ImageView ivProgress;
	
	private Animation animation;
	

	public MyProgressDialog(Context context) {
		super(context, R.style.loading_dialog);
		this.context = context;
		init();
	}

	public void setMessage(String msg) {
		tvTip.setText(msg);
	}	
	
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.dialog_progress, null);
		tvTip = (TextView) view.findViewById(R.id.tv_tip);
		ivProgress = (ImageView) view.findViewById(R.id.iv_progress);
		
		animation = AnimationUtils.loadAnimation(context, R.anim.anim_progress);
		setContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void show() {
		ivProgress.setAnimation(animation);
		super.show();
	}
}
