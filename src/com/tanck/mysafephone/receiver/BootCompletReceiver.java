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
		// ��ʼ�������ļ���Ϣ
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// ��ʼ���绰����
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String saveSim = sp.getString("sim", null);
		String realSim = tm.getSimSerialNumber();

		if (saveSim.equals(realSim)) {
			// SIM����ͬ
		} else {
			// SIM���Ѿ����
			System.out.println("����,�����ֻ�SIM���Ѿ������.");
			// �ж��Ƿ����˱���
			if (sp.getBoolean("finded", false)) {
				// ���Ͷ���
				SmsManager smsManager = SmsManager.getDefault();
				String safenum = sp.getString("phone", null);
				smsManager.sendTextMessage(safenum, null,
						"����,������ʹ������ֻ���---SIM is Change", null, null);
			}
		}

	}
}
