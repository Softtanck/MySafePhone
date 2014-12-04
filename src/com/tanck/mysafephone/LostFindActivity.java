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
		// ��ȥ�ж��Ƿ񾭹�����
		if (sp.getBoolean("configed", false)) {
			// �Ѿ���������.ֱ�ӽ����ֻ�����������
			setContentView(R.layout.activity_lost_find);
			tv_lostfind_safenumer = (TextView) findViewById(R.id.tv_lostfind_safenumer);
			iv_lostfind_lock = (ImageView) findViewById(R.id.iv_lostfind_lock);
			// ���ð�ȫ����
			String phone = sp.getString("phone", "");
			tv_lostfind_safenumer.setText(phone);
			if (sp.getBoolean("finded", false)) {
				iv_lostfind_lock.setBackgroundResource(R.drawable.lock);
			} else {
				iv_lostfind_lock.setBackgroundResource(R.drawable.unlock);
			}
		} else {
			System.out.println("��ʼ�����������");
			// û��������,������
			Intent intent = new Intent(LostFindActivity.this,
					SetupOneActivity.class);
			startActivity(intent);
			// ���Լ���finsh��
			finish();
		}
	}

	/**
	 * ���½�����ҳ��
	 * 
	 * @param view
	 */
	public void returnEnter(View view) {
		Intent intent = new Intent(LostFindActivity.this,
				SetupOneActivity.class);
		startActivity(intent);
		// ���Լ���finsh��
		finish();
	}
}
