package com.tanck.mysafephone;

import com.tanck.mysafephone.ui.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity {

	protected static final String TAG = "MyTanck";
	private SettingItemView siv_setting;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seting);
		siv_setting = (SettingItemView) findViewById(R.id.siv_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		final Editor editor = sp.edit();

		// 初始化Item状态
		if (sp.getBoolean("isupdata", false)) {
			// 设置为选中
			siv_setting.setChecked(true);
		} else {
			// 没有被选中
			siv_setting.setChecked(false);
		}
		// 对一个Item整体监听
		siv_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果当前被选中
				if (siv_setting.getChecked()) {
					siv_setting.setChecked(false);
					editor.putBoolean("isupdata", false);
				} else {
					siv_setting.setChecked(true);
					editor.putBoolean("isupdata", true);
				}
				editor.commit();// 最后得提交进去
			}
		});
	}
}
