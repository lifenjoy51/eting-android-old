package com.nexters.eting;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nexters.eting.etc.Const;
import com.nexters.eting.etc.HttpUtil;

/**
 * 받은편지함 읽기화면
 *
 * @author lifenjoy51
 *
 */
public class NotifyUfoActivity extends BaseActivity implements OnClickListener {

	/**
	 * 알림 메세지 번호
	 */
	private String adminMsgId;

	/**
	 * 프로그래스 동글이
	 */
	private ProgressDialog progressDialog;

	/**
	 * 보낸사람
	 */
	private String comment;

	/**
	 * 코멘트
	 */
	private EditText et;

	// 설정 저장소
	SharedPreferences pref;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		// 배경을 검게
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.3f;
		getWindow().setAttributes(layoutParams);

		// 레이아웃 설정
		setContentView(R.layout.activity_notifyufo);

		// 저장소 초기화
		pref = context.getSharedPreferences("eting", Context.MODE_PRIVATE);

		// 공지 번호와 메세지
		String notifyContent = pref.getString("notify_ufo", "");

		// 공지 내용
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(notifyContent);
		contentView.setMovementMethod(LinkMovementMethod.getInstance());

		// From Eting 부분
		Typeface font = Typeface.createFromAsset(context.getAssets(),
				"fonts/savoye.ttf");
		TextView fromEting = (TextView) findViewById(R.id.from_eting);
		fromEting.setTypeface(font);
		fromEting.setText("From. eting");
		fromEting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);

		// 버튼이벤트 삽입
		findViewById(R.id.notify_confirm_btn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.notify_confirm_btn) {
			Log.d("et length", String.valueOf(et.getText().toString().length()));

			// 전송상태 나타냄
			progressDialog = ProgressDialog.show(this, "", getResources()
					.getString(R.string.app_name), true, true);

			// 공지메세지 답장 전송
			saveCommentToServer();
		}

	}

	/**
	 * 서버로 스탬프 전송
	 */
	private void saveCommentToServer() {

		// 코멘트
		et = (EditText) findViewById(R.id.msg_comment);
		comment = et.getText().toString();

		// 서버로 전송
		new SendAdminMsgComment().execute();
	}

	/**
	 * 공지메세지에 대한 피드백을 서버로 전송한다.
	 *
	 * @author lifenjoy51
	 *
	 */
	public class SendAdminMsgComment extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {

			String urlStr = Const.serverContext + "/sendNotifyComment";

			Map<String,String> param = new HashMap<String,String>();
			param.put("msgId", adminMsgId);
			param.put("comment", comment);

			String response = HttpUtil.doPost(urlStr, param); // Http전송
			return response;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog != null)
				progressDialog.dismiss();

			// 에러처리
			if ("HttpUtil_Error".equals(result)) {
				Toast.makeText(context, R.string.error_on_transfer,
						Toast.LENGTH_LONG).show();
			} else {
				// 정상으로 전송되었을 때.
				// 키보드 숨기고
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Service.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				finish();

				// 공지메세지 삭제
				pref.edit().putString("notify_id", "").commit();
				pref.edit().putString("notify_ufo", "").commit();
			}

		}

	}

}
