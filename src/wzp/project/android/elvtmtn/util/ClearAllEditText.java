package wzp.project.android.elvtmtn.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 自带清除当前所有输入内容的编辑框
 * @author Zippen
 *
 */
public class ClearAllEditText extends EditText {

	private static final String tag = "ClearAllEditText";
	private Drawable deleteDrawable;
	
	
	public ClearAllEditText(Context context) {
		super(context);
		Log.d(tag, "DeleteEditText(Context context)");
		init();
	}

	public ClearAllEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.d(tag, "DeleteEditText(Context context, AttributeSet attrs, int defStyle)");
		init();
	}

	public ClearAllEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(tag, "DeleteEditText(Context context, AttributeSet attrs)");
		init();
	}
	
	private void init() {
		// getCompoundDrawables()方法返回控件四周的Drawable对象构成的数组，
		// 顺序为left, top, right, bottom 
		deleteDrawable = getCompoundDrawables() [2];
		setClearDrawableVisible(false);		
		
		setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					boolean isNotNull = getEditableText().toString().length() > 0;
					setClearDrawableVisible(isNotNull);
				} else {
					setClearDrawableVisible(false);
				}
			}
		});
		
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				boolean isNotNull = editable.toString().length() > 0;
				setClearDrawableVisible(isNotNull);
			}
		});
	}
	
	private void setClearDrawableVisible(boolean isNotNull) {
		Drawable rightDrawable = null;
		if (isNotNull) {
			rightDrawable = deleteDrawable;
		}
		Drawable[] drawables = getCompoundDrawables();
		setCompoundDrawables(drawables[0], drawables[1], rightDrawable, drawables[3]);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				int drawableRightX = getWidth() - getPaddingRight();
				int drawableLeftX = getWidth() - getTotalPaddingRight();
				
				// 注意：此次是event的实例方法getX()，而不是EditText控件的实例方法getX()
				float currentX = event.getX();
				if (currentX > drawableLeftX
						&& currentX < drawableRightX) {
					setText("");
				}
				break;
	
			default:
				break;
		}
		
		return super.onTouchEvent(event);
	}

	
}
