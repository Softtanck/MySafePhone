package com.tanck.mysafephone;

import com.tanck.mysafephone.ui.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SetupTwoActivity extends BaseSetupActivity {
	private SettingItemView siv_setup_sim;
	private TelephonyManager telephonyManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_two);
		// 获取手机管理
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		siv_setup_sim = (SettingItemView) findViewById(R.id.siv_setup_sim);
		if (!TextUtils.isEmpty(sp.getString("sim", null))) {
			// 绑定
			siv_setup_sim.setChecked(true);
		}
		siv_setup_sim.setOnClickListener(new OnClickListener() {

			Editor editor = sp.edit();

			@Override
			public void onClick(View v) {
				// 获取手机序列号
				String simnumer = telephonyManager.getSimSerialNumber();
				if (siv_setup_sim.getChecked()) {
					// 被选中,让它取消
					editor.putString("sim", null);
					siv_setup_sim.setChecked(false);
				} else {
					// 没有被选中,让它选中
					editor.putString("sim", simnumer);
					siv_setup_sim.setChecked(true);
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
		String sim = sp.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			Toast.makeText(SetupTwoActivity.this, "必须设置绑定SIM卡", 1).show();
			return;
		}
		Intent intent = new Intent(SetupTwoActivity.this,
				SetupThreeActivity.class);
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
		Intent intent = new Intent(SetupTwoActivity.this,
				SetupOneActivity.class);
		startActivity(intent);
		finish();
		// 在startActivity(intent);或者finish();后面才能使用
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

	@Override
	public void showNext() {
		Intent intent = new Intent(SetupTwoActivity.this,
				SetupThreeActivity.class);
		startActivity(intent);
		finish();
		// 在startActivity(intent);或者finish();后面才能使用
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		Intent intent = new Intent(SetupTwoActivity.this,
				SetupOneActivity.class);
		startActivity(intent);
		finish();
		// 在startActivity(intent);或者finish();后面才能使用
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}
