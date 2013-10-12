package com.gif.eting.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.gif.eting.R;

public class PasswordManagementActivity extends Activity implements
		OnClickListener {

	private Button reset_pwd;
	private Button init_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.password_management);

		reset_pwd = (Button) findViewById(R.id.reset_pwd);
		init_pwd = (Button) findViewById(R.id.init_pwd);

		reset_pwd.setOnClickListener(this);
		init_pwd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.init_pwd) {
			AlertDialog.Builder ab = new AlertDialog.Builder(PasswordManagementActivity.this);
			ab.setMessage("비밀번호를 초기화 하시겠습니까?").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//Action for 'yes'
					System.out.println("YES");
					Intent intent = new Intent(PasswordManagementActivity.this, SettingActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish(); // 뒤로가기 안먹게

				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//Action for 'no'
					System.out.println("No");
					Intent intent = new Intent(PasswordManagementActivity.this, SettingActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish(); // 뒤로가기 안먹게
				}
			});
			AlertDialog alert = ab.create();
			alert.show();
			
		} else if (v.getId() == R.id.reset_pwd) {
			Intent intent = new Intent(PasswordManagementActivity.this, PasswordActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish(); // 뒤로가기 안먹게
		}

	}
}
