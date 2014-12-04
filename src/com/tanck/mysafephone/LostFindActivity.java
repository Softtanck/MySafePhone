package com.tanck.mysafephone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_lostfind_safenumer;
	private ImageView iv_lostfind_lock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 先去判断是否经过了向导
		if (sp.getBoolean("configed", false)) {
			// 已经做过向导了.直接进入手机防盗主界面
			setContentView(R.layout.activity_lost_find);
			tv_lostfind_safenumer = (TextView) findViewById(R.id.tv_lostfind_safenumer);
			iv_lostfind_lock = (ImageView) findViewById(R.id.iv_lostfind_lock);
			// 设置安全号码
			String phone = sp.getString("phone", "");
			tv_lostfind_safenumer.setText(phone);
			if (sp.getBoolean("finded", false)) {
				iv_lostfind_lock.setBackgroundResource(R.drawable.lock);
			} else {
				iv_lostfind_lock.setBackgroundResource(R.drawable.unlock);
			}
		} else {
			System.out.println("开始进入防盗的向导");
			// 没有做过向导,启动向导
			Intent intent = new Intent(LostFindActivity.this,
					SetupOneActivity.class);
			startActivity(intent);
			// 把自己给finsh掉
			finish();
		}
	}

	/**
	 * 重新进入向导页面
	 * 
	 * @param view
	 */
	public void returnEnter(View view) {
		Intent intent = new Intent(LostFindActivity.this,
				SetupOneActivity.class);
		startActivity(intent);
		// 把自己给finsh掉
		finish();
	}
}
