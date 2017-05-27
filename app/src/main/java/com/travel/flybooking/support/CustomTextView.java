package com.travel.flybooking.support;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {
	Context _context;

	public CustomTextView(Context context) {
		super(context);
		_context = context;
		init(null, 0);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		int textStyle = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textStyle", Typeface.NORMAL);
		init(attrs, textStyle);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		if (CommonFunctions.lang.equals("ar"))
			setTypeface(TypeFaceProvider.getTypeFace(_context, "DroidKufi-Regular"));
//		else
//			setTypeface(Typeface
//					.create("sans-serif-condensed", defStyle == Typeface.BOLD ? Typeface.BOLD : Typeface.NORMAL));
	}
}