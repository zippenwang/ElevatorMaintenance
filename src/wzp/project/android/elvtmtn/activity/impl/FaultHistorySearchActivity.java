package wzp.project.android.elvtmtn.activity.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import wzp.project.android.elvtmtn.R;
import wzp.project.android.elvtmtn.activity.base.BaseActivity;

public class FaultHistorySearchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fault_history);
		
	}

	public static void myStartActivivty(Context context) {
		Intent intent = new Intent(context, FaultHistorySearchActivity.class);
		context.startActivity(intent);
	}
	
	

}
