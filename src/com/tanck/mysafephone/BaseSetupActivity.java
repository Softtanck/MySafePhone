package com.tanck.mysafephone;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;

/**
 * ��������
 * 
 * @author Administrator
 * 
 */
public abstract class BaseSetupActivity extends Activity {
	// 1.����ʶ����
	private GestureDetector detector;

	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 2.ʵ��������ʶ����
		detector = new GestureDetector(this, new OnGestureListener() {

			/**
			 * ������ʱ�����OnFling
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// ����б��,�������100��Ϊ��б����
				if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
					return true;// ����
				}
				// ������,x���ٶȵ�λʱ����С��200������ֱ������
				if (Math.abs(velocityX) < 200) {
					return true;// ����
				}
				if ((e2.getRawX() - e1.getRawX()) > 200) {
					// �ڶ�����ȵ�һ�����,�������һ���,������һ��ҳ��
					showPre();
					System.out.println("�ڶ�����ȵ�һ�����,�������һ���,������һ��ҳ��");
					return true;// ���ڴ��������¼�
				}
				if ((e1.getRawX() - e2.getRawX()) > 200) {
					// ��һ����ȵڶ�����,�������󻬶�,������һ��ҳ��
					showNext();
					System.out.println("��һ����ȵڶ�����,�������󻬶�,������һ��ҳ��");
					return true;// ���ڴ��������¼�
				}
				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO �Զ����ɵķ������
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO �Զ����ɵķ������

			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO �Զ����ɵķ������
				return false;
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO �Զ����ɵķ������
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO �Զ����ɵķ������

			}

		});
	}

	public abstract void showNext();

	public abstract void showPre();

	// 3.�������������¼�
	/**
	 * ���������ݸ�����ʶ��
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
