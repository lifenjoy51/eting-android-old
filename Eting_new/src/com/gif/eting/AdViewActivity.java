package com.gif.eting;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.etc.Const;
import com.gif.eting.etc.HttpUtil;
import com.gif.eting.etc.Util;
import com.gif.eting.task.DownloadImageTask;

/**
 * 받은편지함 읽기화면
 *
 * @author lifenjoy51
 *
 */
public class AdViewActivity extends BaseActivity implements OnClickListener {

	// 광고정보
	private String ad_id;
	private String ad_title;
	private String ad_content;
	private String ad_img_url;
	private String ad_link_msg;
	private String ad_link_url;
	private String ad_desc;
	private String ad_st_dt;
	private String ad_en_dt;
	private String ad_icon_url;

	// ad 버튼
	TextView ad_link_btn;

	// 프로그래스 동글이
	private ProgressDialog progressDialog;

	//피드백
	private String comment;

	//피드백 ET
	EditText et;

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
		setContentView(R.layout.activity_adview);

		// 저장소 초기화
		pref = context.getSharedPreferences("eting", Context.MODE_PRIVATE);

		et = (EditText) findViewById(R.id.msg_comment);

		// 광고정보 불러오기
		ad_id = pref.getString("ad_id", "");
		ad_title = pref.getString("ad_title", "");
		ad_content = pref.getString("ad_content", "");
		ad_img_url = pref.getString("ad_img_url", "");
		ad_link_msg = pref.getString("ad_link_msg", "");
		ad_link_url = pref.getString("ad_link_url", "");
		ad_desc = pref.getString("ad_desc", "");
		ad_st_dt = pref.getString("ad_st_dt", "");
		ad_en_dt = pref.getString("ad_en_dt", "");
		ad_icon_url = pref.getString("ad_icon_url", "");

		//링크 버튼
		ad_link_btn = (TextView) findViewById(R.id.ad_link_btn);
		ad_link_btn.setText(ad_link_msg);

		//이미지 넣기
		ImageView adImg = (ImageView) findViewById(R.id.ad_img);
		new DownloadImageTask(adImg).execute(ad_img_url);

		// 공지 내용
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(ad_content);
		contentView.setMovementMethod(LinkMovementMethod.getInstance());

		// From Eting 부분
		Typeface font = Typeface.createFromAsset(context.getAssets(),
				"fonts/savoye.ttf");
		TextView fromEting = (TextView) findViewById(R.id.from_eting);
		fromEting.setTypeface(font);
		fromEting.setText(ad_title);
		fromEting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);

		// 버튼이벤트 삽입
		findViewById(R.id.ad_confirm_btn).setOnClickListener(this);
		findViewById(R.id.ad_link_btn).setOnClickListener(this);

		//로그 남기기
		new SendAdLog().execute("V");

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ad_link_btn) {
			//이벤트 페이지 접속
			new SendAdLog().execute("E");

			//링크 열기
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ad_link_url));
			startActivity(intent);
		}
		if (v.getId() == R.id.ad_confirm_btn) {

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
	 * 광고 확인 로그를 서버에 남긴다.
	 *
	 * @author lifenjoy51
	 *
	 */
	public class SendAdLog extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			String urlStr = Const.serverContext + "/insertAdLog";
			String deviceId = Util.DeviceId(context); // 기기 고유값
			String type = params[0];

			Map<String,String> param = new HashMap<String,String>();
			param.put("ad_id", ad_id);
			param.put("device_id", deviceId);
			param.put("ad_log_type", type);

			String response = HttpUtil.doPost(urlStr, param); // Http전송
			return response;
		}
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

			String urlStr = Const.serverContext + "/insertAdFeedback";
			String deviceId = Util.DeviceId(context); // 기기 고유값

			Map<String,String> param = new HashMap<String,String>();
			param.put("ad_id", ad_id);
			param.put("device_id", deviceId);
			param.put("feedback_content", comment);

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
			}

		}

	}

}
