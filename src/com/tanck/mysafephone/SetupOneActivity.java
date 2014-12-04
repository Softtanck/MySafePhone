package com.tanck.mysafephone;

import com.tanck.mysafephone.receiver.MyAdmin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View;

public class SetupOneActivity extends BaseSetupActivity {

	private SharedPreferences spPreferences;

	private DevicePolicyManager dpm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_one);
		// ��ʼ��
		spPreferences = getSharedPreferences("config", MODE_PRIVATE);
		dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
	}

	/**
	 * ��һ�������¼�
	 * 
	 * @param view
	 */
	public void next(View view) {
		// ��ȡ�Ƿ�������������Ա��ѡ��
		Boolean isadmined = spPreferences.getBoolean("isAdmined", false);
		System.out.println(isadmined);
		if (isadmined) {
			Intent intent = new Intent(SetupOneActivity.this,
					SetupTwoActivity.class);
			startActivity(intent);
			finish();
			// ��startActivity(intent);����finish();�������ʹ��
			overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		} else {
			// û����������
			openAdmin();
			// �����Ѿ�������
			Editor editor = sp.edit();
			editor.putBoolean("isAdmined", true);
			// �ǵ��ύһ��
			editor.commit();
		}
	}

	/**
	 * �򿪹���ԱȨ��
	 */
	private void openAdmin() {
		// ����һ����ͼ�������ǿ����豸�ĳ�������Ա
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName cn = new ComponentName(this, MyAdmin.class);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
		// Ȱ˵�û���������Ա
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "�����Ϳ�������");
		startActivity(intent);
	}

	/**
	 * ����ʵ�ֻ������õ����¼�
	 */
	@Override
	public void showNext() {
		Intent intent = new Intent(SetupOneActivity.this,
				SetupTwoActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {

	}

}
