package com.tanck.mysafephone.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyAdmin extends DeviceAdminReceiver {
	private static int times = 0;

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		super.onPasswordFailed(context, intent);
		Toast.makeText(context, "ÃÜÂëÊäÈë´íÎó:" + (++times), 1).show();
	}
}
