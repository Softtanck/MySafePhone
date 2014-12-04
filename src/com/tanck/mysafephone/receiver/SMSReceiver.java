package com.tanck.mysafephone.receiver;

import com.tanck.mysafephone.R;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 消息接受类
 * 
 * @author Administrator
 * 
 */
public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";

	private SharedPreferences sp;
	// 超级管理员设备
	private DevicePolicyManager dpm;

	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		// 获取短信对象,实际上它是一个短信数组,因为短信可能发送很多条
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			// 将Object转换为消息类对象
			SmsMessage sMessage = SmsMessage.createFromPdu((byte[]) object);
			// 获取短信发送人
			String sendername = sMessage.getOriginatingAddress();
			// 获取电信内容
			String messagebody = sMessage.getMessageBody();

			// 获取安全号码
			String phone = sp.getString("phone", null);

			System.out.println(phone + "====" + sendername);
			if (!TextUtils.isEmpty(phone)) {
				// 不为空,设置了安全号码
				if (phone.equals(sendername)) {
					// 安全号码
					if (messagebody.equals("#*location*#")) {
						Log.i(TAG, "获取GPS位置");
						abortBroadcast();
					} else if (messagebody.equals("#*alarm*#")) {
						// 初始化媒体播放器
						MediaPlayer mPlayer = MediaPlayer.create(context,
								R.raw.alarm);
						// 设置循环播放
						mPlayer.setLooping(true);
						// 设置左右声道音量
						mPlayer.setVolume(1.0f, 1.0f);
						// 因为create直接初始化了所有的一切数据,所以不需要prepar,直接satrt
						mPlayer.start();
						Log.i(TAG, "播放报警音乐");
						abortBroadcast();
					} else if (messagebody.equals("#*wipedata*#")) {
						Log.i(TAG, "远程删除数据");
						abortBroadcast();
					} else if (messagebody.equals("#*lockscreen*#")) {
						Log.i(TAG, "远程锁屏");
						// 初始化超级设备管理员
						dpm = (DevicePolicyManager) context
								.getSystemService(context.DEVICE_POLICY_SERVICE);
						ComponentName cn = new ComponentName(context,
								MyAdmin.class);
						if (dpm.isAdminActive(cn)) {
							// 设备管理员的api
							dpm.resetPassword("123", 0);
							dpm.lockNow();
							// dpm.wipeData(0);
							// dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//删除sdcard数据
						} else {
							System.out.println("设备没有激活管理员");
							// 更改状态
							Editor editor = sp.edit().putBoolean("isAdmined",
									false);
							editor.commit();
						}
						abortBroadcast();
					} else if (messagebody.equals("#*removelockscreen*#")) {
						// 移除管理员
						removeDeviceAdmin();
						// 截获广播
						abortBroadcast();
					}
				}
			}
		}
	}

	/**
	 * 移除设备管理员
	 */
	private void removeDeviceAdmin() {
		ComponentName cn = new ComponentName(context, MyAdmin.class);
		// 可以移除管理员
		dpm.removeActiveAdmin(cn);
		Intent intent = new Intent();
		intent.setAction("android.intent.action.UNINSTALL_PACKAGE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + context.getPackageName()));
		context.startActivity(intent);
	}
}
