package wzp.project.android.elvtmtn.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * 自定义的ImageButton控件
 * @author Zippen
 *
 */
public class MyImageButton extends ImageButton {
	
	private String text = "";
    private String color = "#000000";
    private float textsize = 0f;
    
    private static float density = 0f;			// 当前屏幕的像素密度
    
    
    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        density = context.getResources().getDisplayMetrics().density;
        
        /*
         * 从配置文件中获取android:text的值
         */
        String text = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
        if (!TextUtils.isEmpty(text)) {
			this.text = text;
		}

        /*
         * 从配置文件中获取android:textSize的值
         */
        String strTextSize = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textSize");
        if (!TextUtils.isEmpty(strTextSize)) {
			strTextSize = strTextSize.substring(0, strTextSize.length() - 2);
			textsize = Float.parseFloat(strTextSize);
		}
         
        /*
         * 从配置文件中获取android:color的值
         */
        String color = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "color");
        if (!TextUtils.isEmpty(color)) {
			this.color = color; 
		}
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void setColor(String color){
        this.color = color;
    }
    
    public void setTextSize(float textsize){
        this.textsize = textsize;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        Paint paint = new Paint();
        paint.setTextAlign(Align.CENTER);
        paint.setColor(Color.parseColor(color));
        paint.setTextSize(textsize * density);        
        
        canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight() - 5, paint);
    }
}
