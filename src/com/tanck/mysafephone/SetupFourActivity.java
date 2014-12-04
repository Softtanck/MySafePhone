package com.tanck.mysafephone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SetupFourActivity extends BaseSetupActivity {

	private SharedPreferences sp;
	private CheckBox cb_setup_switch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_four);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_setup_switch = (CheckBox) findViewById(R.id.cb_setup_switch);
		// 设置初始化状态
		if (sp.getBoolean("finded", false)) {
			cb_setup_switch.setChecked(true);
			cb_setup_switch.setText("您开启了防盗保护");

		} else {
			cb_setup_switch.setChecked(false);
			cb_setup_switch.setText("您没有开启了防盗保护");
		}
		cb_setup_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sp.edit();
						if (cb_setup_switch.isChecked()) {
							// 被选中
							editor.putBoolean("finded", true);
							cb_setup_switch.setText("您开启了防盗保护");
						} else {
							editor.putBoolean("finded", false);
							cb_setup_switch.setText("您没有开启了防盗保护");
						}
						editor.commit();
					}
				});
	}

	/**
	 * 下一步
	 * 
	 * @param view
	 */
	public void next(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent = new Intent(SetupFourActivity.this,
				LostFindActivity.class);
		startActivity(intent);
		finish();
		// 在startActivity(intent);或者finish();后面才能使用
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	/**
	 * 上一步
	 * 
	 * @param view
	 */
	public void pre(View view) {
		Intent intent = new Intent(SetupFourActivity.this,
				SetupThreeActivity.class);
		startActivity(intent);
		finish();
		// 在startActivity(intent);或者finish();后面才能使用
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

	@Override
	public void showNext() {
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent = new Intent(SetupFourActivity.this,
				LostFindActivity.class);
		startActivity(intent);
		finish();
		// 在startActivity(intent);或者finish();后面才能使用
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

	}

	@Override
	public void showPre() {
		Intent intent = new Intent(SetupFourActivity.this,
				SetupThreeActivity.class);
		startActivity(intent);
		finish();
		// 在startActivity(intent);或者finish();后面才能使用
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);

	}
}
