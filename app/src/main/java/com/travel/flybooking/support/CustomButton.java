package com.travel.flybooking.support;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button{

	Context _context;
	public CustomButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		_context = context;
        init(null, 0);
	}
	
	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		_context = context;
        init(null, 0);
	}
	
	public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		_context = context;
        init(null, 0);
	}
	
	public CustomButton(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		_context = context;
        init(null, 0);
	}
	
	private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
		if (CommonFunctions.lang.equals("ar"))
			setTypeface(TypeFaceProvider.getTypeFace(_context, "DroidKufi-Regular"));
    }

}
