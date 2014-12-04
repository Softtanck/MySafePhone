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
 * 滑动基类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseSetupActivity extends Activity {
	// 1.手势识别器
	private GestureDetector detector;

	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 2.实例化手势识别器
		detector = new GestureDetector(this, new OnGestureListener() {

			/**
			 * 滑动的时候调用OnFling
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// 屏蔽斜滑,距离大于100认为是斜滑了
				if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
					return true;// 屏蔽
				}
				// 慢慢滑,x的速度单位时间内小于200个像素直接屏蔽
				if (Math.abs(velocityX) < 200) {
					return true;// 屏蔽
				}
				if ((e2.getRawX() - e1.getRawX()) > 200) {
					// 第二个点比第一个点大,从左往右滑动,加载上一个页面
					showPre();
					System.out.println("第二个点比第一个点大,从左往右滑动,加载上一个页面");
					return true;// 不在触发其他事件
				}
				if ((e1.getRawX() - e2.getRawX()) > 200) {
					// 第一个点比第二个大,从右往左滑动,加载下一个页面
					showNext();
					System.out.println("第一个点比第二个大,从右往左滑动,加载下一个页面");
					return true;// 不在触发其他事件
				}
				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO 自动生成的方法存根
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO 自动生成的方法存根

			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO 自动生成的方法存根
				return false;
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO 自动生成的方法存根
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO 自动生成的方法存根

			}

		});
	}

	public abstract void showNext();

	public abstract void showPre();

	// 3.传递整个触摸事件
	/**
	 * 将整个传递给手势识别
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
