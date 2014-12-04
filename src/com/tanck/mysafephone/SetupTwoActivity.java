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
		// ��ȡ�ֻ�����
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		siv_setup_sim = (SettingItemView) findViewById(R.id.siv_setup_sim);
		if (!TextUtils.isEmpty(sp.getString("sim", null))) {
			// ��
			siv_setup_sim.setChecked(true);
		}
		siv_setup_sim.setOnClickListener(new OnClickListener() {

			Editor editor = sp.edit();

			@Override
			public void onClick(View v) {
				// ��ȡ�ֻ����к�
				String simnumer = telephonyManager.getSimSerialNumber();
				if (siv_setup_sim.getChecked()) {
					// ��ѡ��,����ȡ��
					editor.putString("sim", null);
					siv_setup_sim.setChecked(false);
				} else {
					// û�б�ѡ��,����ѡ��
					editor.putString("sim", simnumer);
					siv_setup_sim.setChecked(true);
				}
				editor.commit();
			}
		});
	}

	/**
	 * ��һ��
	 * 
	 * @param view
	 */
	public void next(View view) {
		String sim = sp.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			Toast.makeText(SetupTwoActivity.this, "�������ð�SIM��", 1).show();
			return;
		}
		Intent intent = new Intent(SetupTwoActivity.this,
				SetupThreeActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	/**
	 * ��һ��
	 * 
	 * @param view
	 */
	public void pre(View view) {
		Intent intent = new Intent(SetupTwoActivity.this,
				SetupOneActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

	@Override
	public void showNext() {
		Intent intent = new Intent(SetupTwoActivity.this,
				SetupThreeActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		Intent intent = new Intent(SetupTwoActivity.this,
				SetupOneActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}
