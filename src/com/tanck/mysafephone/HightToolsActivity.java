package com.tanck.mysafephone;

import com.tanck.mysafephone.ui.SettingItemView;
import com.tanck.mysafephone.utils.NumberQueryUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HightToolsActivity extends Activity {

	private EditText et_n;
	private TextView tv_result;
	private String phoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highttools);

		et_n = (EditText) findViewById(R.id.et_n);
		tv_result = (TextView) findViewById(R.id.tv_result);

	}

	public void NumberQuery(View view) {
		// ��ȡ�绰����
		phoneNumber = et_n.getText().toString().trim();
		if (!TextUtils.isEmpty(phoneNumber)) {
			// ��Ϊ�վͲ�ѯ
			String tempnumber = NumberQueryUtils.numberQuery(phoneNumber);
			if (!TextUtils.isEmpty(tempnumber)) {
				tv_result.setText("��ѯ���ĵ�ַΪ:" + tempnumber);
			} else {
				tv_result.setText("û�в�ѯ��...");
			}
		} else {
			Toast.makeText(HightToolsActivity.this, "��ѯ�ĵ绰���벻��Ϊ��.!", 1).show();
		}
	}
}
