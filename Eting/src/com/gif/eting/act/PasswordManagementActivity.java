package com.gif.eting.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gif.eting.R;
import com.gif.eting.svc.PasswordService;

public class PasswordManagementActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_manage);

		// 버튼 온클릭이벤트 등록
		((Button) findViewById(R.id.setting_save_pw_btn))
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// 암호확인 버튼 클릭시
		if (view.getId() == R.id.setting_save_pw_btn) {
 			savePassword();
		}
	}


	//세팅화면에서 비밀번호 저장
	public void savePassword(){
		
		EditText et = (EditText) findViewById(R.id.setting_pw);
		String pw = et.getText().toString();	//입력한 비밀번호
		
		PasswordService svc = new PasswordService(this);			
		svc.savePassword(pw);
		

		Toast toast = Toast.makeText(this, "저장되었습니다.",
				Toast.LENGTH_SHORT);
		toast.show();
		
		// 메인화면으로 이동
		//startActivity(new Intent(context, ReadMyStoryActivity.class));
	}

}
