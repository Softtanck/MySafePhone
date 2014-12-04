package com.tanck.mysafephone.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/*
 * 自定义一个有焦点的View
 */
public class FocuesTextView extends TextView {

	public FocuesTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FocuesTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocuesTextView(Context context) {
		super(context);
	}

	/**
	 * 欺骗系统获得焦点
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
}
