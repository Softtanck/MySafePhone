package com.tanck.mysafephone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.tanck.mysafephone.utils.StreamTools;

public class SplashActivity extends Activity {

	protected static final String TAG = "MyTanck";
	protected static final int JSON_ERROR = 1;
	protected static final int NET_ERROR = 2;
	protected static final int URL_ERROR = 3;
	protected static final int ERROR_SHOW_DIALOG = 4;
	protected static final int ENTER_HOME = 5;
	private TextView tv_splash_version, tv_splash_progress;

	private SharedPreferences sp;

	// �汾
	private String vsersion;
	// ����
	private String description;
	// �°汾���ص�ַ
	private String apkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_progress = (TextView) findViewById(R.id.tv_splash_progress);
		// ���ð汾
		tv_splash_version.setText(getVersionName() == null ? "��ȡ�汾ʧ��..."
				: "��ǰ�汾: V" + getVersionName());

		sp = getSharedPreferences("config", MODE_PRIVATE);
		// �������ݿ��ļ���data/data/����/files/address.db
		copyDB();
		if (!sp.getBoolean("isupdata", false)) {
			// ���汾
			checkUpdate();
		} else {
			// ����һ����ʱ�����߳�
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
		}

		// splash��ʼ������չʾ
		AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
		// ��������ʱ��
		animation.setDuration(1000);
		findViewById(R.id.rl_root_splash).setAnimation(animation);
	}

	/**
	 * �������ݿ��ļ���data/data/����/files/address.db
	 */
	private void copyDB() {

		try {
			File file = new File(getFilesDir(), "address.db");
			// �ж��Ƿ��Ѿ�copy����
			if (!file.exists() && file.length() <= 0) {
				// �����ݿ��ļ���Ϊ������
				InputStream is = getAssets().open("address.db");
				// ���������
				FileOutputStream fos = new FileOutputStream(file);
				// ��������
				byte[] buffer = new byte[1024];
				// ��ȡ��־
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					// ��Buffer����ĵ�0��λ��ȡ����д���ļ��ĺ���
					fos.write(buffer, 0, len);
				}
				// �ͷ���Դ
				is.close();
				fos.close();
			} else {
				// ������
				System.out.println("���ݴ�����,����Ҫ������.!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void enterHome() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ENTER_HOME:// ����������
				enterHome();
				break;

			case ERROR_SHOW_DIALOG:// ��ʾ����Ի���
				Log.i(TAG, description);
				showUpdateDialog();
				break;
			case JSON_ERROR:// json��������
				enterHome();
				Toast.makeText(getApplicationContext(), "Json��������", 0).show();
				break;
			case URL_ERROR:// url��ַ����
				enterHome();
				Toast.makeText(getApplicationContext(), "url��������", 0).show();
				break;
			case NET_ERROR:// �������
				enterHome();
				Toast.makeText(getApplicationContext(), "�������", 0).show();
				break;
			}
		}

		/**
		 * ��ʾ�����Ի���
		 */
		private void showUpdateDialog() {
			AlertDialog.Builder builder = new Builder(SplashActivity.this);
			// builder.setCancelable(false);// ǿ������,ȡ������
			builder.setTitle("��ʾ����");
			builder.setMessage(description);
			// ����ȡ��ǿ��
			builder.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					enterHome();
					dialog.dismiss();
				}
			});
			builder.setPositiveButton("��������", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ���ز��滻�°汾
					if (Environment.getExternalStorageState().equals(// ��SDCARD�����.
							Environment.MEDIA_MOUNTED)) {
						// ʹ��afinal��Դ���
						Log.i(TAG, apkurl);
						FinalHttp finalHttp = new FinalHttp();
						// �����·���ⲿ����·��
						finalHttp.download(apkurl, Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/Tanck2.0.apk", new AjaxCallBack<File>() {

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								// ����ʧ��
								super.onFailure(t, errorNo, strMsg);
								t.printStackTrace();
								Toast.makeText(getApplicationContext(), "����ʧ��",
										1).show();
							}

							@Override
							public void onLoading(long count, long current) {
								// ��������
								super.onLoading(count, current);
								// ��ȡ���صİٷֱ�
								int progess = (int) (current * 100 / count);
								tv_splash_progress.setText("�Ѿ�����:" + progess
										+ "%");
							}

							@Override
							public void onSuccess(File t) {
								// ���سɹ�
								super.onSuccess(t);
								Toast.makeText(getApplicationContext(), "���سɹ�",
										0).show();
								// ��װ�����غõ�apk
								installMyApk(t);
							}

							/**
							 * ��װ����apk
							 * 
							 * @param t
							 *            �ļ�·��
							 */
							private void installMyApk(File t) {
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								intent.addCategory("android.intent.category.DEFAULT");
								intent.setDataAndType(Uri.fromFile(t),
										"application/vnd.android.package-archive");
								startActivity(intent);
							}

						});
					} else {
						// û��SDCARD�����
						Toast.makeText(getApplicationContext(),
								"û���ҵ�SD��,�����SD��������", 0).show();
						return;
					}

				}
			});
			builder.setNegativeButton("�´���˵", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();// �رնԻ���
					enterHome();// ����������
				}
			});
			builder.show();
		}

		/**
		 * ����������
		 */
		private void enterHome() {
			Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		};
	};

	/**
	 * ȥ����������°�
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {
				// ֱ�Ӵ�ȫ�ֻ�ȡ,���ⴴ���µĶ���
				Message msg = Message.obtain();
				// ��ȡϵͳ��ǰʱ��
				long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(getString(R.string.url));
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					int rcode = connection.getResponseCode();
					// ���ӳɹ�
					if (rcode == 200) {
						// ��ȡ������
						InputStream isInputStream = connection.getInputStream();
						Log.i(TAG, "���ӳɹ�");
						// json����
						JSONObject jsonObject = new JSONObject(
								StreamTools.readFromStream(isInputStream));
						vsersion = jsonObject.getString("vsersion");
						description = jsonObject.getString("description");
						apkurl = jsonObject.getString("apkurl");

						// ���汾
						if (getVersionName().equals(vsersion)) {
							// ��ǰ�汾Ϊ���°汾,���ø���ֱ�ӽ�����ҳ��
							msg.what = ENTER_HOME;
						} else {
							// ���°汾,�����Ի������û��Լ�ȥ����
							msg.what = ERROR_SHOW_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					msg.what = URL_ERROR;// URL��ַ����
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = NET_ERROR;// �������
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = JSON_ERROR;// JSON��������
					e.printStackTrace();
				} finally {
					// ��ȡ��ǰʱ��
					long endTime = System.currentTimeMillis();
					// ������ʱʱ��
					long rTime = endTime - startTime;
					if (rTime < 2000)
						try {
							Thread.sleep(2000 - rTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * �õ��汾����
	 */

	private String getVersionName() {
		// �õ�������
		PackageManager pm = getPackageManager();
		try {
			// ��ȡ��������Ϣ
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
