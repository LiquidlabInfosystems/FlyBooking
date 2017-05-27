package com.travel.flybooking.support;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class StrikedTextView extends TextView {
	Context _context;
	public boolean addStrike = false;
	public Paint paint;

	public StrikedTextView(Context context) {
		super(context);
		_context = context;
		init(null, 0);
	}

	public StrikedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		int textStyle = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textStyle", Typeface.NORMAL);
		init(attrs, textStyle);
	}

	public StrikedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load custom font with attributes
		if (CommonFunctions.lang.equals("ar"))
			setTypeface(TypeFaceProvider.getTypeFace(_context, "DroidKufi-Regular"));
		
		paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(getResources().getDisplayMetrics().density * 1);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (addStrike) {
            canvas.drawLine(0, getHeight() / 2, getWidth(),
                    getHeight() / 2, paint);
        }
	}
}