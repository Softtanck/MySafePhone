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
		// 获取每个包中的信息
		getPackgeInfo();
	}

	/**
	 * 获取包中的信息
	 */
	private void getPackgeInfo() {
		// 获取包管理者
		PackageManager pm = getPackageManager();
		// 获取包中的安装信息权限
		List<PackageInfo> list = pm
				.getInstalledPackages(PackageManager.GET_PERMISSIONS
						| PackageManager.GET_UNINSTALLED_PACKAGES);

		for (PackageInfo info : list) {
			// 获取对应的请求权限
			String[] pressions = info.requestedPermissions;
			if (pressions != null && pressions.length > 0) {
				// 获取对应的权限名
				for (String pression : pressions) {
					// 匹配到了对应的联网权限
					if (pression.equals("android.permission.INTERNET")) {
						// 获取对应程序的UID
						int uid = info.applicationInfo.uid;
						long rx = TrafficStats.getUidRxBytes(uid);
						// 获取对应的Tx
						long tx = TrafficStats.getUidTxBytes(uid);
						System.out.println("总共流量为:" + tx + rx);
					}
				}
			}
		}
	}
}
