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
		// 初始化
		spPreferences = getSharedPreferences("config", MODE_PRIVATE);
		dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
	}

	/**
	 * 下一步单击事件
	 * 
	 * @param view
	 */
	public void next(View view) {
		// 获取是否做过超级管理员的选项
		Boolean isadmined = spPreferences.getBoolean("isAdmined", false);
		System.out.println(isadmined);
		if (isadmined) {
			Intent intent = new Intent(SetupOneActivity.this,
					SetupTwoActivity.class);
			startActivity(intent);
			finish();
			// 在startActivity(intent);或者finish();后面才能使用
			overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		} else {
			// 没有做过超级
			openAdmin();
			// 设置已经做过了
			Editor editor = sp.edit();
			editor.putBoolean("isAdmined", true);
			// 记得提交一次
			editor.commit();
		}
	}

	/**
	 * 打开管理员权限
	 */
	private void openAdmin() {
		// 声明一个意图，作用是开启设备的超级管理员
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName cn = new ComponentName(this, MyAdmin.class);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
		// 劝说用户开启管理员
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启就可以锁屏");
		startActivity(intent);
	}

	/**
	 * 子类实现滑动调用单击事件
	 */
	@Override
	public void showNext() {
		Intent intent = new Intent(SetupOneActivity.this,
				SetupTwoActivity.class);
		startActivity(intent);
		finish();
		// 在startActivity(intent);或者finish();后面才能使用
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {

	}

}
