package com.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class LockScreenActivity extends Activity {

	private static final String TAG = "LockScreenActivity";
	boolean inDragMode;
	int selectedImageViewX;
	int selectedImageViewY;
	int windowWidth;
	int windowHeight;
	ImageView keyImageView;
	ImageView lockerImageView;
	TextView timeNow;
	int locker_x;
	int locker_y;
	int[] keyPos;
	private LayoutParams layoutParams;
	// private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;// ����4.0+home��

	public static boolean isStarted = false;// �ж�����ҳ���Ƿ��״̬

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED
		// ,FLAG_HOMEKEY_DISPATCHED);// ����4.0+ home��
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		isStarted = true;

		keyImageView = (ImageView) findViewById(R.id.key);
		timeNow = (TextView) findViewById(R.id.time);
		timeNow.setText(TimeUtils.getTime());

		try {
			// initialize receiver
			startService(new Intent(this, LockScreenService.class));

			windowWidth = getWindowManager().getDefaultDisplay().getWidth();
			windowHeight = getWindowManager().getDefaultDisplay().getHeight();

			MarginLayoutParams marginParams2 = new MarginLayoutParams(
					keyImageView.getLayoutParams());
			marginParams2.setMargins((windowWidth / 24) * 10,
					((windowHeight / 32) * 8), 0, 0);
			RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(
					marginParams2);
			keyImageView.setLayoutParams(layoutdroid);

			LinearLayout homelinear = (LinearLayout) findViewById(R.id.homelinearlayout);
			homelinear.setPadding(0, 0, 0, (windowHeight / 32) * 3);
			lockerImageView = (ImageView) findViewById(R.id.locker);

			MarginLayoutParams marginParams1 = new MarginLayoutParams(
					lockerImageView.getLayoutParams());
			marginParams1.setMargins((windowWidth / 24) * 5, 0,
					(windowHeight / 32) * 8, 0);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
					marginParams1);
			lockerImageView.setLayoutParams(layout);

			// keyImageViewͼƬ��touch�¼�
			keyImageView.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					layoutParams = (LayoutParams) v.getLayoutParams();
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						int[] lockerPos = new int[2];
						keyPos = new int[2];
						lockerImageView.getLocationOnScreen(lockerPos);
						locker_x = lockerPos[0];
						locker_y = lockerPos[1];
						break;
					case MotionEvent.ACTION_MOVE:
						int x_cord = (int) event.getRawX();
						int y_cord = (int) event.getRawY();

						if (x_cord > windowWidth - (windowWidth / 24)) {
							x_cord = windowWidth - (windowWidth / 24) * 2;
						}
						if (y_cord > windowHeight - (windowHeight / 32)) {
							y_cord = windowHeight - (windowHeight / 32) * 2;
						}

						layoutParams.leftMargin = x_cord;
						layoutParams.topMargin = y_cord;

						keyImageView.getLocationOnScreen(keyPos);
						v.setLayoutParams(layoutParams);

						// �϶�Կ��ͼƬ��С����λ��
						if (((x_cord - locker_x) <= (windowWidth / 24) * 5 && (locker_x - x_cord) <= (windowWidth / 24) * 4)
								&& ((locker_y - y_cord) <= (windowHeight / 32) * 5)) {
							v.setVisibility(View.GONE);
							virbate();// ��
							isStarted = false;
							finish();
						}
						break;
					case MotionEvent.ACTION_UP:
						int x_cord1 = (int) event.getRawX();
						int y_cord2 = (int) event.getRawY();

						if (((x_cord1 - locker_x) <= (windowWidth / 24) * 5 && (locker_x - x_cord1) <= (windowWidth / 24) * 4)
								&& ((locker_y - y_cord2) <= (windowHeight / 32) * 5)) {
						} else {
							// ����ɿ���ʱδ�ص�����Կ��ͼƬ����ԭλ��
							layoutParams.leftMargin = (windowWidth / 24) * 10;
							layoutParams.topMargin = (windowHeight / 32) * 8;
							v.setLayoutParams(layoutParams);
						}
					}

					return true;
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_HOME)) {
			// Key code constant: Home key. This key is handled by the framework
			// and is never delivered to applications.
			Log.e(TAG, "��ӦHome��");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);// Android4.0һ������Home��
		super.onAttachedToWindow();
	}

	@Override
	public void onBackPressed() {
		Log.e(TAG, "��ӦBack��");
		return;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * ��
	 */
	private void virbate() {
		Vibrator vibrator = (Vibrator) this
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}

}