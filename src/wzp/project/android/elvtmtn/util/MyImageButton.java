package wzp.project.android.elvtmtn.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton {
	
	private String text = "";
    private String color = "#000000";
    private float textsize = 0f;
    
    private static float density = 0f;
    
    
    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        density = context.getResources().getDisplayMetrics().density;
        
        String text = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
        if (!TextUtils.isEmpty(text)) {
			this.text = text;
		}

        String strTextSize = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textSize");
        if (!TextUtils.isEmpty(strTextSize)) {
			strTextSize = strTextSize.substring(0, strTextSize.length() - 2);
			textsize = Float.parseFloat(strTextSize);
		}
         
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
        
        // 文字的坐标还未搞懂什么意思
        canvas.drawText(text, canvas.getWidth()/2, canvas.getHeight() - 5, paint);
    }
}
