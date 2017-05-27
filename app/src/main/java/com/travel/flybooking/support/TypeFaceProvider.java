package com.travel.flybooking.support;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

 /*To load custom font to cache for faster loading*/

public class TypeFaceProvider {
	public static final String TYPEFACE_FOLDER = "fonts";
	public static final String TYPEFACE_EXTENSION = ".ttf";

	private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(
			4);

	public static Typeface getTypeFace(Context context, String fileName) {
		Typeface tempTypeface = sTypeFaces.get(fileName);

		if (tempTypeface == null) {
			String fontPath = new StringBuilder(fileName).append(TYPEFACE_EXTENSION).toString();
			tempTypeface = Typeface.createFromAsset(context.getAssets(),
					fontPath);
			sTypeFaces.put(fileName, tempTypeface);
		}

		return tempTypeface;
	}
}
