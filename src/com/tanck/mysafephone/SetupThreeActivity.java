package com.tanck.mysafephone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetupThreeActivity extends BaseSetupActivity {
	private EditText et_setup_safenumber;
	private WindowManager wmManager;
	private TextView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_three);
		et_setup_safenumber = (EditText) findViewById(R.id.et_setup_safenumber);
		String pString = sp.getString("phone", "");
		et_setup_safenumber.setText(pString);
		wmManager = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	/**
	 * ��һ��
	 * 
	 * @param view
	 */
	public void next(View view) {
		if (TextUtils.isEmpty(et_setup_safenumber.getText())) {
			// Toast.makeText(this, "�����ð�ȫ����", 1).show();
			myToast("�����ð�ȫ����");
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.over);
			et_setup_safenumber.startAnimation(animation);
			return;
		}
		Intent intent = new Intent(SetupThreeActivity.this,
				SetupFourActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	private void myToast(String address) {
		view = new TextView(getApplicationContext());
		view.setText(address);
		view.setTextColor(Color.RED);
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		wmManager.addView(view, params);
	}

	/**
	 * ��һ��
	 * 
	 * @param view
	 */
	public void pre(View view) {
		Intent intent = new Intent(SetupThreeActivity.this,
				SetupTwoActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

	@Override
	public void showNext() {
		Intent intent = new Intent(SetupThreeActivity.this,
				SetupFourActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		Intent intent = new Intent(SetupThreeActivity.this,
				SetupTwoActivity.class);
		startActivity(intent);
		finish();
		// ��startActivity(intent);����finish();�������ʹ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);

	}

	/**
	 * ����ѡ����ϵ�˽���
	 * 
	 * @param view
	 */
	public void SelectContact(View view) {
		Intent intent = new Intent(SetupThreeActivity.this,
				SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String phoString = data.getStringExtra("phone").toString().trim();
		if (TextUtils.isEmpty(phoString)) {
			Toast.makeText(this, "����ʧ��", 1).show();
			return;
		}
		// ������ŵ������ļ���ȥ
		Editor editor = sp.edit();
		editor.putString("phone", phoString.replace("-", ""));
		editor.commit();
		et_setup_safenumber.setText(phoString.replace("-", ""));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// wmManager.removeView(view);
		// wmManager = null;
	}
}
