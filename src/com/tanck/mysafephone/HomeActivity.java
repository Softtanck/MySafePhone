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

	private String[] names = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���", "����ͳ��", "�ֻ�ɱ��",
			"��������", "�߼�����", "��������" };
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
		// ��������
		gv_home.setAdapter(new MyGvAdapter());

		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:// ����
					showMyLostDialog();
					break;
				case 4:// �������
					Intent flow = new Intent(HomeActivity.this,
							FlowActivity.class);
					startActivity(flow);
					break;
				case 7:// �߼�����
					Intent intent1 = new Intent(HomeActivity.this,
							HightToolsActivity.class);
					startActivity(intent1);
					break;
				case 8:// ������������
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
	 * �����Ի���
	 */
	protected void showMyLostDialog() {
		// ���ȼ������
		if (!isPwd()) {
			// ���벻Ϊ��,��ʾ���������
			showInputPwdDialog();
		} else {
			// ����Ϊ��,��ʾ���������
			showSetPwdDialog();
		}
	}

	/**
	 * ����һ����������Ի���
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
				// ȡ������
				String password = et_dialog_setpwd.getText().toString().trim();
				String savepwd = sp.getString("password", "").toString().trim();
				// ��ʼ�ж�
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "���벻�ܿ�,����������", 1).show();
					return;
				}
				// �ж��Ƿ���ͬ
				if (MD5Util.md5Password(password).equals(savepwd)) {
					// ������ͬ,��ȡ���Ի���,�������������
					dialog.dismiss();
					System.out.println("����");
					Intent intent = new Intent(HomeActivity.this,
							LostFindActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "�������,������ȷ������", 1).show();
					et_dialog_setpwd.setText("");
					return;
				}
			}
		});
		bt_dialog_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// ȡ���Ի���
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
	 * ����һ����������Ի���
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
				// ȡ������
				String password = et_dialog_setpwd.getText().toString().trim();
				String confirmpwd = et_dialog_setconfirmpwd.getText()
						.toString().trim();
				// ��ʼ�ж�
				if (TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(confirmpwd)) {
					Toast.makeText(HomeActivity.this, "���벻�ܿ�,����������", 0).show();
					return;
				}
				// �ж��Ƿ���ͬ
				if (password.equals(confirmpwd)) {
					// ������ͬ,д�������ļ�,��ȡ���Ի���,�������������
					Editor dEditor = sp.edit();
					dEditor.putString("password", MD5Util.md5Password(password));
					dEditor.commit();
					dialog.dismiss();
					System.out.println(sp.getString("password", null));
					Intent intent = new Intent(HomeActivity.this,
							LostFindActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this, "�������벻һ��,������ȷ������", 0)
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
				dialog.dismiss();// ȡ���Ի���
			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * ��������Ƿ����.
	 * 
	 * @return �����Ϊ��,�ٴ���Ϊ��
	 */
	private boolean isPwd() {
		return TextUtils.isEmpty(sp.getString("password", null));
	}

	/**
	 * ��ҳ���ڲ�������������
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
