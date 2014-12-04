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
		// 获取电话号码
		phoneNumber = et_n.getText().toString().trim();
		if (!TextUtils.isEmpty(phoneNumber)) {
			// 不为空就查询
			String tempnumber = NumberQueryUtils.numberQuery(phoneNumber);
			if (!TextUtils.isEmpty(tempnumber)) {
				tv_result.setText("查询到的地址为:" + tempnumber);
			} else {
				tv_result.setText("没有查询到...");
			}
		} else {
			Toast.makeText(HightToolsActivity.this, "查询的电话号码不能为空.!", 1).show();
		}
	}
}
