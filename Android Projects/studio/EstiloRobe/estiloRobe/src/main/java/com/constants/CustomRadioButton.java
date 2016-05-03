package com.constants;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.stylist.R;

public class CustomRadioButton extends RadioButton {
	public CustomRadioButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CustomRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setCustomFont(context, attrs);
	}
	public CustomRadioButton(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs,
		R.styleable.CustomTextView);
		String customFont = a.getString(R.styleable.CustomRadioButton_customFontrbtn);
		setCustomFont(ctx, customFont);
		a.recycle();
	}

	public boolean setCustomFont(Context ctx, String asset) {
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + asset);
		} catch (Error error)
		{
			error.printStackTrace();
		}catch (Exception e) {
			//Log.e(TAG, "Could not get typeface: " + e.getMessage());
			return false;
		}

		setTypeface(tf);
		return true;
	}

}
