package com.lockscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * ������ֻ��App������ĵ�һ�����棬��ʾһ����ť�����������������
 * �����沢������ҳ��Ҳ���Ǹ����������Ƶ�Homeҳ
 * 
 * @author Andy
 *
 */
public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
	}

	/**
	 * �������� ��ť�ĵ����Ӧ
	 * @param v
	 */
	public void startLockScreen(View v) {
		Intent sintent = new Intent();
		sintent.setClass(this, LockScreenService.class);
		startService(sintent);
		Toast.makeText(this, "��������������ر���Ļ���ٵ�������", Toast.LENGTH_SHORT).show();
	}

}
