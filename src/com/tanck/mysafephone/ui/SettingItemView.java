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
 * �Զ���һ����Ͽؼ�,��2��TextView��һ��CheckBox��ϵ�һ��,�γ�һ�����
 */
public class SettingItemView extends RelativeLayout {

	private TextView tv_setting_desc, tv_setting_title;
	private CheckBox cb_setting_up;
	private String Off, On;

	private final static String NAME_SPACE = "http://schemas.android.com/apk/res/com.tanck.mysafephone";

	/**
	 * ��ʼ�������ļ�
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		// ��seting_item���ص���SetingItemView�����������,Ҳ���ǽ�������Ϊһ���ؼ�ʹ��
		View.inflate(context, R.layout.seting_item, SettingItemView.this);
		// �����this������Ͽؼ�
		tv_setting_desc = (TextView) this.findViewById(R.id.tv_setting_desc);
		tv_setting_title = (TextView) this.findViewById(R.id.tv_setting_title);
		cb_setting_up = (CheckBox) this.findViewById(R.id.cb_setting_up);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * �Ӳ����ļ����ؾͻ����2��������
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		// attrs������ķ�ʽ����Ų����ļ��е��ֶ�����
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
	 * ��ȡѡ����Ƿ�ѡ��
	 */
	public boolean getChecked() {
		return cb_setting_up.isChecked();
	}

	/**
	 * ����ѡ���ѡ��״̬
	 * 
	 * @param checked
	 *            ѡ��
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
