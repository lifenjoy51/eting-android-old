package com.gif.eting.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 줄있는 에디터 View
 * 
 * @author lifenjoy51
 *
 */
public class LineEditText extends EditText{
	// we need this constructor for LayoutInflater
	public LineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
			mRect = new Rect();
	        mPaint = new Paint();
	        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	        mPaint.setColor(Color.GRAY);
	}

	private Rect mRect;
    private Paint mPaint;	    
    
    @Override
    protected void onDraw(Canvas canvas) {
  
        int height = getHeight();
        int line_height = getLineHeight();

        int count = height / line_height;

        if (getLineCount() > count)
            count = getLineCount();

        Rect r = mRect;
        Paint paint = mPaint;
        int baseline = getLineBounds(0, r);
        baseline = 0;

        for (int i = 0; i < count; i++) {

            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            baseline += line_height;

        super.onDraw(canvas);
    }

}
}