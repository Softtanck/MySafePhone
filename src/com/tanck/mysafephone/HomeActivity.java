package com.tanck.mysafephone;

import com.tanck.mysafephone.utils.MD5Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	protected static final String TAG = "MyTanck";

	private GridView gv_home;
	private SharedPreferences sp;

	private String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
			"缓存清理", "高级工具", "设置中心" };
	private int[] ids = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		gv_home = (GridView) findViewById(R.id.gv_home);
		// 数据适配
		gv_home.setAdapter(new MyGvAdapter());

		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:// 防盗
					showMyLostDialog();
					break;
				case 4:// 流量监控
					Intent flow = new Intent(HomeActivity.this,
							FlowActivity.class);
					startActivity(flow);
					break;
				case 7:// 高级工具
					Intent intent1 = new Intent(HomeActivity.this,
							HightToolsActivity.class);
					startActivity(intent1);
					break;
				case 8:// 进入设置中心
					Intent intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 创建对话框
	 */
	protected void showMyLostDialog() {
		// 首先检测密码
		if (!isPwd()) {
			// 密码不为空,显示输入密码框
			showInputPwdDialog();
		} else {
			// 密码为空,显示设置密码框
			showSetPwdDialog();
		}
	}

	/**
	 * 创建一个输入密码对话框
	 */
	private void showInputPwdDialog() {

		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.dialog_pwd_input,
				null);
		et_dialog_setpwd = (TextView) view.findViewById(R.id.et_dialog_setpwd);
		bt_dialog_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		bt_dialog_cancle = (Button) view.findViewById(R.id.bt_dialog_cancle);
		bt_dialog_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取出密码
				String password = et_dialog_setpwd.getText().toString().trim();
				String savepwd = sp.getString("password", "").toString().trim();
				// 开始判断
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "密码不能空,请重新输入", 1).show();
					return;
				}
				// 判断是否相同
				if (MD5Util.md5Password(password).equals(savepwd)) {
					// 密码相同,就取消对话框,并进入防盗界面
					dialog.dismiss();
					System.out.println("进入");
					Intent intent = new Intent(HomeActivity.this,
							LostFindActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "密码错误,请重新确认输入", 1).show();
					et_dialog_setpwd.setText("");
					return;
				}
			}
		});
		bt_dialog_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// 取消对话框
			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private TextView et_dialog_setpwd, et_dialog_setconfirmpwd;
	private Button bt_dialog_ok, bt_dialog_cancle;
	private AlertDialog dialog;

	/**
	 * 创建一个设置密码对话框
	 */
	private void showSetPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.dialog_pwd_seting,
				null);
		et_dialog_setpwd = (TextView) view.findViewById(R.id.et_dialog_setpwd);
		et_dialog_setconfirmpwd = (TextView) view
				.findViewById(R.id.et_dialog_setconfirmpwd);
		bt_dialog_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		bt_dialog_cancle = (Button) view.findViewById(R.id.bt_dialog_cancle);
		bt_dialog_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取出密码
				String password = et_dialog_setpwd.getText().toString().trim();
				String confirmpwd = et_dialog_setconfirmpwd.getText()
						.toString().trim();
				// 开始判断
				if (TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(confirmpwd)) {
					Toast.makeText(HomeActivity.this, "密码不能空,请重新输入", 0).show();
					return;
				}
				// 判断是否相同
				if (password.equals(confirmpwd)) {
					// 密码相同,写到配置文件,就取消对话框,并进入防盗界面
					Editor dEditor = sp.edit();
					dEditor.putString("password", MD5Util.md5Password(password));
					dEditor.commit();
					dialog.dismiss();
					System.out.println(sp.getString("password", null));
					Intent intent = new Intent(HomeActivity.this,
							LostFindActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "两次密码不一致,请重新确认输入", 0)
							.show();
					et_dialog_setpwd.setText("");
					et_dialog_setconfirmpwd.setText("");
					return;
				}
			}
		});
		bt_dialog_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// 取消对话框
			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 检测密码是否存在.
	 * 
	 * @return 真代表为空,假代表不为空
	 */
	private boolean isPwd() {
		return TextUtils.isEmpty(sp.getString("password", null));
	}

	/**
	 * 主页面内部类数据适配器
	 * 
	 * @author Administrator
	 */
	class MyGvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.gv_itme, null);
			view.findViewById(R.id.iv_home_img).setBackgroundResource(
					ids[position]);
			((TextView) view.findViewById(R.id.tv_home_text))
					.setText(names[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

}
