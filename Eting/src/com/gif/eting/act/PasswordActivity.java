package com.gif.eting.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.dao.SettingDAO;
import com.gif.eting.dto.SettingDTO;
import com.gif.eting.svc.PasswordService;
import com.gif.eting.util.SecureUtil;
import com.gif.eting.util.Util;
import com.gif.eting_dev.R;

public class PasswordActivity extends Activity implements OnClickListener {
	private EditText origin_pwd;
	private EditText setting_pw;
	private EditText setting_pw2;
	private TextView password_textView;
	private ImageView password_check;
	private ImageView setting_save_pw_btn;
	private Button setting_save_btn;
	private boolean isValid = false;
	private String o_pw;
	private PasswordService svc;
	private Typeface nanum = Util.nanum;
	private SettingDTO setting = new SettingDTO();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);

		password_check = (ImageView) findViewById(R.id.password_check);
		setting_save_pw_btn = (ImageView) findViewById(R.id.setting_save_pw_btn);
		origin_pwd = (EditText) findViewById(R.id.origin_pwd);
		setting_pw = (EditText) findViewById(R.id.setting_pw);
		setting_pw2 = (EditText) findViewById(R.id.setting_pw2);
		password_textView = (TextView) findViewById(R.id.password_textView);
		setting_save_btn = (Button) findViewById(R.id.setting_save_btn);

		// setting_save_btn.setVisibility(View.INVISIBLE);
		// setting_save_pw_btn.setVisibility(View.VISIBLE);

		origin_pwd.setTypeface(nanum);
		setting_pw.setTypeface(nanum);
		setting_pw2.setTypeface(nanum);
		password_textView.setTypeface(nanum);
		password_textView.setPaintFlags(password_textView.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);

		setting_pw.setHintTextColor(Color.parseColor("#bbbbbb"));
		setting_pw2.setHintTextColor(Color.parseColor("#bbbbbb"));
		o_pw = origin_pwd.getText().toString(); // 예전 비밀번호

		// 암호체크
		svc = new PasswordService(this);
		// isValid = svc.checkPassword(o_pw);
		isValid = svc.isPassword();

		if (isValid == true) {
			origin_pwd.setHintTextColor(Color.parseColor("#999999"));
			origin_pwd.setHint("origin passwrod");
			origin_pwd.setVisibility(View.VISIBLE);
		} else {
			// origin_pwd.setClickable(false);
			// origin_pwd.setEnabled(false);
			// origin_pwd.setFocusable(false);
			// origin_pwd.setFocusableInTouchMode(false);
			// origin_pwd.setHintTextColor(Color.parseColor("#999999"));
			origin_pwd.setVisibility(View.GONE);
			password_check.setVisibility(View.INVISIBLE);
			// origin_pwd.setHint("Don't have password!!");
		}
		setting_save_pw_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					setting_save_pw_btn = (ImageView) findViewById(v.getId());
					setting_save_pw_btn.setImageResource(R.drawable.submit_2);
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (!v.isPressed()) {
						setting_save_pw_btn
								.setImageResource(R.drawable.submit_1);
						return true;
					}

				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					setting_save_pw_btn.setImageResource(R.drawable.submit_1);
				}

				return false;
			}
		});

		setting_save_pw_btn.setOnClickListener(this);
		password_check.setOnClickListener(this);
		setting_save_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		String s1 = origin_pwd.getText().toString();
		String s2 = setting_pw.getText().toString();
		String s3 = setting_pw2.getText().toString();

		if (view.getId() == R.id.setting_save_pw_btn) {
			if (isValid == true) {
				if ((s1 == null || "".equals(s1))
						|| (s2 == null || "".equals(s2))
						|| (s3 == null || "".equals(s3))) {
					Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT)
							.show();
					return;
				} else
					savePassword();
			} else {
				if ((s2 == null || "".equals(s2))
						|| (s3 == null || "".equals(s3))) {
					Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT)
							.show();
					return;
				} else
					savePassword();
			}
		} else if (view.getId() == R.id.password_check) {
			Drawable tempImg = password_check.getDrawable();
			Drawable tempRes = PasswordActivity.this.getResources()
					.getDrawable(R.drawable.passward_off);
			Bitmap tmpBitmap = ((BitmapDrawable) tempImg).getBitmap();
			Bitmap tmpBitmapRes = ((BitmapDrawable) tempRes).getBitmap();

			if (isValid == true) {
				if (tmpBitmap.equals(tmpBitmapRes)) {
					// setting_save_pw_btn.setVisibility(View.VISIBLE);
					// setting_save_btn.setVisibility(View.INVISIBLE);

					password_check.setImageResource(R.drawable.passward_on);
					origin_pwd.setHintTextColor(Color.parseColor("#bbbbbb"));
					setting_pw.setHintTextColor(Color.parseColor("#bbbbbb"));
					setting_pw2.setHintTextColor(Color.parseColor("#bbbbbb"));
					origin_pwd.setEnabled(true);
					setting_pw.setEnabled(true);
					setting_pw2.setEnabled(true);
					setting_save_btn.setEnabled(true);

				} else {
					savePasswordOff();
					// setting_save_pw_btn.setVisibility(View.INVISIBLE);
					// setting_save_btn.setVisibility(View.VISIBLE);

					password_check.setImageResource(R.drawable.passward_off);
					origin_pwd.setHintTextColor(Color.parseColor("#e2e2e2"));
					setting_pw.setHintTextColor(Color.parseColor("#e2e2e2"));
					setting_pw2.setHintTextColor(Color.parseColor("#e2e2e2"));
					origin_pwd.setEnabled(false);
					setting_pw.setEnabled(false);
					setting_pw2.setEnabled(false);
					setting_save_btn.setEnabled(false);

				}
			} else if (view.getId() == R.id.setting_save_btn) {
				savePasswordOff();

			}
		}
	}

	public void savePasswordOff() { // password off 하고 저장했을때
		setting.setKey(null);
		setting.setValue(null);
		svc.settingDao.open();
		svc.settingDao.delsetting("password");
		svc.settingDao.close(); // 닫고

		AlertDialog.Builder alertDlg = new AlertDialog.Builder(
				PasswordActivity.this);
		alertDlg.setTitle("비밀번호");
		alertDlg.setMessage("비밀번호를 OFF 하시겠습니까?");
		alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String msg = "Password Off 설정 되었습니다";
				Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(PasswordActivity.this,
						MainViewPagerActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish(); // 뒤로가기 안먹게
			}
		});
		alertDlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				password_check.setImageResource(R.drawable.passward_on);
				origin_pwd.setHintTextColor(Color.parseColor("#bbbbbb"));
				setting_pw.setHintTextColor(Color.parseColor("#bbbbbb"));
				setting_pw2.setHintTextColor(Color.parseColor("#bbbbbb"));
				origin_pwd.setEnabled(true);
				setting_pw.setEnabled(true);
				setting_pw2.setEnabled(true);
				setting_save_btn.setEnabled(true);
				
			}
		});
		alertDlg.show();

	}

	// 세팅화면에서 비밀번호 저장
	public void savePassword() {
		boolean isValid = false;

		String o_pw = origin_pwd.getText().toString(); // 예전 비밀번호
		String pw = setting_pw.getText().toString(); // 새로입력한 비밀번호
		String pw2 = setting_pw2.getText().toString(); // 새로입력한 비밀번호 확인

		// 암호체크
		svc = new PasswordService(this);
		isValid = svc.checkPassword(o_pw);

		// String s1 = origin_pwd.getText().toString();
		// if (isValid == false) {
		// origin_pwd.setClickable(false);
		// origin_pwd.setEnabled(false);
		// origin_pwd.setFocusable(false);
		// origin_pwd.setFocusableInTouchMode(false);
		// }
		// 암호 성공/실패 분기처리
		if (isValid) {
			if (pw.equals(pw2)) {
				svc.savePassword(pw);

				Toast toast = Toast.makeText(this, "저장되었습니다.",
						Toast.LENGTH_SHORT);
				toast.show();
				Intent intent = new Intent(PasswordActivity.this,
						MainViewPagerActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish(); // 뒤로가기 안먹게
			} else {
				Toast toast = Toast.makeText(this, "입력한 비밀번호가 다릅니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			}

		} else { // 비밀번호 틀렸을때
			Toast toast = Toast.makeText(this, "기존의 비밀번호가 맞지 않습니다.",
					Toast.LENGTH_SHORT);
			toast.show();

			origin_pwd.setText(""); // 암호입력필드 초기화
		}

		// 메인화면으로 이동
		// startActivity(new Intent(context, ReadMyStoryActivity.class));
	}

	public void clear(View view) {
		origin_pwd.setText("");
		setting_pw.setText("");
		setting_pw2.setText("");
	}
}
