package com.gif.eting.act;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gif.eting.R;

public class SettingActivity extends Activity implements OnClickListener {

	private ImageButton password_img_btn;
	private Button email_btn;
	private Uri fileUri;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		// 버튼 온클릭이벤트 등록
		password_img_btn = (ImageButton) findViewById(R.id.password_img_btn);
		email_btn = (Button) findViewById(R.id.email_btn);
		password_img_btn.setOnClickListener(this);
		email_btn.setOnClickListener(this);

		// 전송할 파일의 경로
		String szSendFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/myTest.txt";
		File f = new File(szSendFilePath);
		if (!f.exists()) {
			Toast.makeText(this, "파일이 없습니다.", Toast.LENGTH_SHORT).show();
		}
		// File객체로부터 Uri값 생성
		fileUri = Uri.fromFile(f);
	}

	@Override
	public void onClick(View view) {
		// 비밀번호관리 버튼 클릭시
		if (view.getId() == R.id.password_img_btn) {
			startActivity(new Intent(this, PasswordManagementActivity.class));
		}
		if (view.getId() == R.id.email_btn) {
			Intent it = new Intent(Intent.ACTION_SEND);
			it.setType("plain/text");

			String[] tos = { "leehyun33@naver.com" };
			it.putExtra(Intent.EXTRA_EMAIL, tos);

			it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
			it.putExtra(Intent.EXTRA_TEXT, "The email body text");

			// 파일첨부
			it.putExtra(Intent.EXTRA_STREAM, fileUri);
			try {
				// startActivity(it);
				startActivity(Intent.createChooser(it, "메일보냄"));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this, "안됨", Toast.LENGTH_SHORT).show();
			}

		}
	}

}