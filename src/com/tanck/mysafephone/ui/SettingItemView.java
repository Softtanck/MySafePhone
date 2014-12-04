package com.tanck.mysafephone.ui;

import com.tanck.mysafephone.R;

import android.R.bool;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * 自定义一个组合控件,将2个TextView和一个CheckBox组合到一起,形成一个组件
 */
public class SettingItemView extends RelativeLayout {

	private TextView tv_setting_desc, tv_setting_title;
	private CheckBox cb_setting_up;
	private String Off, On;

	private final static String NAME_SPACE = "http://schemas.android.com/apk/res/com.tanck.mysafephone";

	/**
	 * 初始化布局文件
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		// 将seting_item加载到了SetingItemView这个布局里面,也就是将整个作为一个控件使用
		View.inflate(context, R.layout.seting_item, SettingItemView.this);
		// 这里的this就是组合控件
		tv_setting_desc = (TextView) this.findViewById(R.id.tv_setting_desc);
		tv_setting_title = (TextView) this.findViewById(R.id.tv_setting_title);
		cb_setting_up = (CheckBox) this.findViewById(R.id.cb_setting_up);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 从布局文件加载就会调用2个参数的
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		// attrs以数组的方式存放着布局文件中的字段属性
		tv_setting_title.setText(attrs.getAttributeValue(NAME_SPACE, "title"));
		tv_setting_desc
				.setText(attrs.getAttributeValue(NAME_SPACE, "desc_off"));
		Off = attrs.getAttributeValue(NAME_SPACE, "desc_off");
		On = attrs.getAttributeValue(NAME_SPACE, "desc_on");
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 获取选择框是否选中
	 */
	public boolean getChecked() {
		return cb_setting_up.isChecked();
	}

	/**
	 * 设置选择框选中状态
	 * 
	 * @param checked
	 *            选中
	 */
	public void setChecked(boolean checked) {
		cb_setting_up.setChecked(checked);
		if (getChecked()) {
			tv_setting_desc.setText(On);
		} else {
			tv_setting_desc.setText(Off);
		}
	}
}
