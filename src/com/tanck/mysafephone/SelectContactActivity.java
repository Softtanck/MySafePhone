package com.tanck.mysafephone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {

	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private ListView list_select_contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		list_select_contact = (ListView) findViewById(R.id.list_select_contact);
		data = getContactValue();
		list_select_contact.setAdapter(new SimpleAdapter(this, data,
				R.layout.contact_item, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_phone }));
		/**
		 * 列表单击获取号码
		 */
		list_select_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phoneString = data.get(position).get("phone").toString()
						.replace("-", "");
				Intent data = new Intent();
				data.putExtra("phone", phoneString);
				setResult(0, data);
				finish();// 将自己杀掉
			}
		});
	}

	/**
	 * 读取联系人
	 * 
	 * @return
	 */
	private List<Map<String, String>> getContactValue() {

		ContentResolver cResolver = getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uridata = Uri.parse("content://com.android.contacts/data");
		System.out.println("uri的地址为:" + uri);
		Cursor cursor = cResolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		while (cursor.moveToNext()) {
			String numString = cursor.getString(0);
			if (!TextUtils.isEmpty(numString)) {
				Map<String, String> map = new HashMap<String, String>();
				// 有联系人
				Cursor cdata = cResolver.query(uridata, new String[] { "data1",
						"mimetype" }, "contact_id=?",
						new String[] { numString }, null);
				while (cdata.moveToNext()) {
					String data = cdata.getString(0);
					String type = cdata.getString(1);
					if ("vnd.android.cursor.item/phone_v2".equals(type)) {
						// 电话号码
						map.put("phone", data);
					} else if ("vnd.android.cursor.item/name".equals(type)) {
						// 联系人名字
						map.put("name", data);
					}
				}
				cdata.close();
				data.add(map);
			}
		}
		cursor.close();
		return data;
	}

}
