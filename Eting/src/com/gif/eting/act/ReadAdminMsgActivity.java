package com.gif.eting.act;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
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

import com.gif.eting.R;
import com.gif.eting.dao.AdminMsgDAO;
import com.gif.eting.dto.AdminMsgDTO;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Util;

/**
 * 받은편지함 읽기화면
 * 
 * @author lifenjoy51
 * 
 */
public class ReadAdminMsgActivity extends Activity implements OnClickListener {

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

	/**
	 * AdminMsgDAO adminMsgDao
	 */
	AdminMsgDAO adminMsgDao;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);

		setContentView(R.layout.read_admin_msg_popup);

		/**
		 * 짧은이야기 적는 부분
		 */
		et = (EditText) findViewById(R.id.msg_comment);
		et.setTypeface(Util.getNanum(getApplicationContext()));

		adminMsgDao = new AdminMsgDAO(context);
		AdminMsgDTO sdto;

		adminMsgDao.open();

		sdto = adminMsgDao.getAdminMsgList().get(0);
		adminMsgId = sdto.getMsgId();
		String content = sdto.getMsgContent();

		adminMsgDao.close();

		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setTypeface(Util.getNanum(getApplicationContext()));
		contentView.setText(content);
		contentView.setMovementMethod(LinkMovementMethod.getInstance());
		
		Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/savoye.ttf");;
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setTypeface(font);
		storyDateView.setText("From. eting");
		storyDateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);

		// 버튼이벤트 삽입
		findViewById(R.id.admin_msg_confirm_btn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.admin_msg_confirm_btn) {
			Log.d("et length", String.valueOf(et.getText().toString().length()));

			// 전송상태 나타냄
			progressDialog = ProgressDialog.show(this, "", getResources()
					.getString(R.string.app_name), true, true);

			/**
			 * 다른사람 이야기에 찍은 스탬프 정보를 서버로 전송
			 */
			this.saveCommentToServer();
		}

	}

	/**
	 * 서버로 스탬프 전송
	 */
	private void saveCommentToServer() {

		/**
		 * 보낸이 설정
		 */
		et = (EditText) findViewById(R.id.msg_comment);
		comment = et.getText().toString();

		/**
		 * 서버로 전송
		 */
		new SendAdminMsgComment().execute();
	}

	public class SendAdminMsgComment extends AsyncTask<Object, String, String> {

		/**
		 * 생성자로 콜백을 받아온다.
		 * 
		 * @param callback
		 */
		public SendAdminMsgComment() {
		}

		/**
		 * 실제 실행되는 부분
		 */
		@Override
		protected String doInBackground(Object... params) {

			String urlStr = Util.serverContext + "/sendAdminMsgComment";
			String param = "msgId=" + adminMsgId;
			param += "&comment=" + comment;

			String response = HttpUtil.doPost(urlStr, param); // Http전송
			return response;
		}

		/**
		 * 작업이 끝나면 자동으로 실행된다.
		 */
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog != null)
				progressDialog.dismiss();

			/**
			 * 에러처리
			 */
			if ("HttpUtil_Error".equals(result)) {
				Toast toast = Toast.makeText(context,
						R.string.error_on_transfer, Toast.LENGTH_LONG);
				toast.show();

			} else if ("UnknownHostException".equals(result)) {
				Toast toast = Toast.makeText(context,
						R.string.cannot_connect_to_internet, Toast.LENGTH_LONG);
				toast.show();

			} else {

				adminMsgDao.open();
				adminMsgDao.delAdminMsg(adminMsgId);
				adminMsgDao.close();

				// 정상으로 전송되었을 때.
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Service.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

				finish();

			}
		}

	}

}
