package com.tanck.mysafephone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompletReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 初始化配置文件信息
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// 初始化电话管理
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String saveSim = sp.getString("sim", null);
		String realSim = tm.getSimSerialNumber();

		if (saveSim.equals(realSim)) {
			// SIM卡相同
		} else {
			// SIM卡已经变更
			System.out.println("哥们,您的手机SIM卡已经变更了.");
			// 判断是否开启了保护
			if (sp.getBoolean("finded", false)) {
				// 发送短信
				SmsManager smsManager = SmsManager.getDefault();
				String safenum = sp.getString("phone", null);
				smsManager.sendTextMessage(safenum, null,
						"哥们,有人在使用你的手机了---SIM is Change", null, null);
			}
		}

	}
}
