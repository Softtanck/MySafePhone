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

	// 版本
	private String vsersion;
	// 描述
	private String description;
	// 新版本下载地址
	private String apkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_progress = (TextView) findViewById(R.id.tv_splash_progress);
		// 设置版本
		tv_splash_version.setText(getVersionName() == null ? "获取版本失败..."
				: "当前版本: V" + getVersionName());

		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 复制数据库文件到data/data/包名/files/address.db
		copyDB();
		if (!sp.getBoolean("isupdata", false)) {
			// 检测版本
			checkUpdate();
		} else {
			// 启动一个定时任务线程
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
		}

		// splash初始化动画展示
		AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
		// 动画持续时间
		animation.setDuration(1000);
		findViewById(R.id.rl_root_splash).setAnimation(animation);
	}

	/**
	 * 复制数据库文件到data/data/包名/files/address.db
	 */
	private void copyDB() {

		try {
			File file = new File(getFilesDir(), "address.db");
			// 判断是否已经copy过了
			if (!file.exists() && file.length() <= 0) {
				// 将数据库文件作为输入流
				InputStream is = getAssets().open("address.db");
				// 创建输出流
				FileOutputStream fos = new FileOutputStream(file);
				// 创建缓存
				byte[] buffer = new byte[1024];
				// 读取标志
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					// 从Buffer数组的第0个位置取出来写到文件的后面
					fos.write(buffer, 0, len);
				}
				// 释放资源
				is.close();
				fos.close();
			} else {
				// 存在了
				System.out.println("数据存在了,不需要拷贝了.!");
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
			case ENTER_HOME:// 进入主界面
				enterHome();
				break;

			case ERROR_SHOW_DIALOG:// 显示错误对话框
				Log.i(TAG, description);
				showUpdateDialog();
				break;
			case JSON_ERROR:// json解析出错
				enterHome();
				Toast.makeText(getApplicationContext(), "Json解析出错", 0).show();
				break;
			case URL_ERROR:// url地址错误
				enterHome();
				Toast.makeText(getApplicationContext(), "url解析出错", 0).show();
				break;
			case NET_ERROR:// 网络错误
				enterHome();
				Toast.makeText(getApplicationContext(), "网络错误", 0).show();
				break;
			}
		}

		/**
		 * 显示升级对话框
		 */
		private void showUpdateDialog() {
			AlertDialog.Builder builder = new Builder(SplashActivity.this);
			// builder.setCancelable(false);// 强制升级,取消触摸
			builder.setTitle("提示升级");
			builder.setMessage(description);
			// 返回取消强制
			builder.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					enterHome();
					dialog.dismiss();
				}
			});
			builder.setPositiveButton("立刻升级", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 下载并替换新版本
					if (Environment.getExternalStorageState().equals(// 有SDCARD的情况.
							Environment.MEDIA_MOUNTED)) {
						// 使用afinal开源框架
						Log.i(TAG, apkurl);
						FinalHttp finalHttp = new FinalHttp();
						// 这儿的路径外部绝对路径
						finalHttp.download(apkurl, Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/Tanck2.0.apk", new AjaxCallBack<File>() {

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								// 下载失败
								super.onFailure(t, errorNo, strMsg);
								t.printStackTrace();
								Toast.makeText(getApplicationContext(), "下载失败",
										1).show();
							}

							@Override
							public void onLoading(long count, long current) {
								// 正在下载
								super.onLoading(count, current);
								// 获取下载的百分比
								int progess = (int) (current * 100 / count);
								tv_splash_progress.setText("已经升级:" + progess
										+ "%");
							}

							@Override
							public void onSuccess(File t) {
								// 下载成功
								super.onSuccess(t);
								Toast.makeText(getApplicationContext(), "下载成功",
										0).show();
								// 安装我下载好的apk
								installMyApk(t);
							}

							/**
							 * 安装下载apk
							 * 
							 * @param t
							 *            文件路径
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
						// 没有SDCARD的情况
						Toast.makeText(getApplicationContext(),
								"没有找到SD卡,请插入SD卡后再试", 0).show();
						return;
					}

				}
			});
			builder.setNegativeButton("下次再说", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();// 关闭对话框
					enterHome();// 进入主界面
				}
			});
			builder.show();
		}

		/**
		 * 进入主界面
		 */
		private void enterHome() {
			Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		};
	};

	/**
	 * 去服务器检测新版
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {
				// 直接从全局获取,避免创建新的对象
				Message msg = Message.obtain();
				// 获取系统当前时间
				long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(getString(R.string.url));
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					int rcode = connection.getResponseCode();
					// 连接成功
					if (rcode == 200) {
						// 获取输入流
						InputStream isInputStream = connection.getInputStream();
						Log.i(TAG, "连接成功");
						// json解析
						JSONObject jsonObject = new JSONObject(
								StreamTools.readFromStream(isInputStream));
						vsersion = jsonObject.getString("vsersion");
						description = jsonObject.getString("description");
						apkurl = jsonObject.getString("apkurl");

						// 检测版本
						if (getVersionName().equals(vsersion)) {
							// 当前版本为最新版本,不用更新直接进入主页面
							msg.what = ENTER_HOME;
						} else {
							// 有新版本,弹出对画框让用户自己去更新
							msg.what = ERROR_SHOW_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					msg.what = URL_ERROR;// URL地址错误
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = NET_ERROR;// 网络错误
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = JSON_ERROR;// JSON解析错误
					e.printStackTrace();
				} finally {
					// 获取当前时间
					long endTime = System.currentTimeMillis();
					// 联网耗时时间
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
	 * 得到版本名称
	 */

	private String getVersionName() {
		// 得到包管理
		PackageManager pm = getPackageManager();
		try {
			// 获取包管理信息
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
