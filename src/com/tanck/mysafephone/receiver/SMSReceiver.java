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
 * ��Ϣ������
 * 
 * @author Administrator
 * 
 */
public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";

	private SharedPreferences sp;
	// ��������Ա�豸
	private DevicePolicyManager dpm;

	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		// ��ȡ���Ŷ���,ʵ��������һ����������,��Ϊ���ſ��ܷ��ͺܶ���
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			// ��Objectת��Ϊ��Ϣ�����
			SmsMessage sMessage = SmsMessage.createFromPdu((byte[]) object);
			// ��ȡ���ŷ�����
			String sendername = sMessage.getOriginatingAddress();
			// ��ȡ��������
			String messagebody = sMessage.getMessageBody();

			// ��ȡ��ȫ����
			String phone = sp.getString("phone", null);

			System.out.println(phone + "====" + sendername);
			if (!TextUtils.isEmpty(phone)) {
				// ��Ϊ��,�����˰�ȫ����
				if (phone.equals(sendername)) {
					// ��ȫ����
					if (messagebody.equals("#*location*#")) {
						Log.i(TAG, "��ȡGPSλ��");
						abortBroadcast();
					} else if (messagebody.equals("#*alarm*#")) {
						// ��ʼ��ý�岥����
						MediaPlayer mPlayer = MediaPlayer.create(context,
								R.raw.alarm);
						// ����ѭ������
						mPlayer.setLooping(true);
						// ����������������
						mPlayer.setVolume(1.0f, 1.0f);
						// ��Ϊcreateֱ�ӳ�ʼ�������е�һ������,���Բ���Ҫprepar,ֱ��satrt
						mPlayer.start();
						Log.i(TAG, "���ű�������");
						abortBroadcast();
					} else if (messagebody.equals("#*wipedata*#")) {
						Log.i(TAG, "Զ��ɾ������");
						abortBroadcast();
					} else if (messagebody.equals("#*lockscreen*#")) {
						Log.i(TAG, "Զ������");
						// ��ʼ�������豸����Ա
						dpm = (DevicePolicyManager) context
								.getSystemService(context.DEVICE_POLICY_SERVICE);
						ComponentName cn = new ComponentName(context,
								MyAdmin.class);
						if (dpm.isAdminActive(cn)) {
							// �豸����Ա��api
							dpm.resetPassword("123", 0);
							dpm.lockNow();
							// dpm.wipeData(0);
							// dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//ɾ��sdcard����
						} else {
							System.out.println("�豸û�м������Ա");
							// ����״̬
							Editor editor = sp.edit().putBoolean("isAdmined",
									false);
							editor.commit();
						}
						abortBroadcast();
					} else if (messagebody.equals("#*removelockscreen*#")) {
						// �Ƴ�����Ա
						removeDeviceAdmin();
						// �ػ�㲥
						abortBroadcast();
					}
				}
			}
		}
	}

	/**
	 * �Ƴ��豸����Ա
	 */
	private void removeDeviceAdmin() {
		ComponentName cn = new ComponentName(context, MyAdmin.class);
		// �����Ƴ�����Ա
		dpm.removeActiveAdmin(cn);
		Intent intent = new Intent();
		intent.setAction("android.intent.action.UNINSTALL_PACKAGE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + context.getPackageName()));
		context.startActivity(intent);
	}
}
