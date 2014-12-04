package com.tanck.mysafephone;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.View;

public class FlowActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flow);
	}

	public void NumberQuery(View view) {
		// ��ȡÿ�����е���Ϣ
		getPackgeInfo();
	}

	/**
	 * ��ȡ���е���Ϣ
	 */
	private void getPackgeInfo() {
		// ��ȡ��������
		PackageManager pm = getPackageManager();
		// ��ȡ���еİ�װ��ϢȨ��
		List<PackageInfo> list = pm
				.getInstalledPackages(PackageManager.GET_PERMISSIONS
						| PackageManager.GET_UNINSTALLED_PACKAGES);

		for (PackageInfo info : list) {
			// ��ȡ��Ӧ������Ȩ��
			String[] pressions = info.requestedPermissions;
			if (pressions != null && pressions.length > 0) {
				// ��ȡ��Ӧ��Ȩ����
				for (String pression : pressions) {
					// ƥ�䵽�˶�Ӧ������Ȩ��
					if (pression.equals("android.permission.INTERNET")) {
						// ��ȡ��Ӧ�����UID
						int uid = info.applicationInfo.uid;
						long rx = TrafficStats.getUidRxBytes(uid);
						// ��ȡ��Ӧ��Tx
						long tx = TrafficStats.getUidTxBytes(uid);
						System.out.println("�ܹ�����Ϊ:" + tx + rx);
					}
				}
			}
		}
	}
}
