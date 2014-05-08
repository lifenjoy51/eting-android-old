package com.gif.eting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.etc.Const;
import com.gif.eting.etc.HttpUtil;
import com.gif.eting.etc.SecureUtil;
import com.gif.eting.etc.Util;
import com.gif.eting.obj.Emoticon;
import com.gif.eting.obj.Story;
import com.gif.eting.svc.InboxService;
import com.gif.eting.task.DownloadImageTask;
import com.gif.eting.view.EmoticonView;

/**
 * 받은편지함 읽기화면
 *
 * @author lifenjoy51
 *
 */
public class ReadInboxActivity extends BaseActivity implements OnClickListener {

	// 받은편지함 관련 로직을 수행하는 서비스
	private InboxService inboxService;

	// 받은이야기 고유번호
	private String inboxStoryIdx;

	// 프로그래스 동글이
	private ProgressDialog progressDialog;

	// 선택한 이모티콘 배열
	private final List<String> emoticons = new ArrayList<String>();;

	// 답글 내용
	private String replyContent;

	// 코멘트
	private EditText et;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		// 배경을 약간 어둡게한다.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.2f;
		getWindow().setAttributes(layoutParams);

		// 레이아웃 설정
		setContentView(R.layout.activity_readinbox);

		// 짧은이야기 적는 부분
		et = (EditText) findViewById(R.id.emoticon_reply);

		// Service초기화
		inboxService = new InboxService(this.getApplicationContext());

		// 내용과 날짜 시간등을 보여준다.
		setInboxContent();

		// 버튼이벤트 삽입
		findViewById(R.id.inbox_confirm_btn).setOnClickListener(this);
		findViewById(R.id.report_btn).setOnClickListener(this);
		findViewById(R.id.pass_btn).setOnClickListener(this);

		// 이모티콘 입력창 설정
		setEmoticonArea();
	}

	/**
	 * 클릭 이벤트 처리
	 */
	@Override
	public void onClick(View v) {
		// 이모티콘을 선택 했을 때
		if (v instanceof EmoticonView) {
			handleEmoticonPress((EmoticonView) v);
		} else {
			switch (v.getId()) {
			// 답장 전송버튼
			case R.id.inbox_confirm_btn:
				sendReply();
				break;
			// 신고버튼
			case R.id.report_btn:
				reportStory();
				break;
			// 패스버튼
			case R.id.pass_btn:
				passStory();
				break;
			}
		}
	}

	/**
	 * 받은이야기 내용을 보여준다.
	 */
	private void setInboxContent() {
		// 받은이야기를 가져온다.
		Story inboxStory = inboxService.getInboxStory();
		inboxStoryIdx = inboxStory.getStory_id();
		String content = inboxStory.getStory_content();
		String storyDate = inboxStory.getStory_date();

		// 날짜정보 변환
		if (!Util.isEmpty(storyDate)) {
			storyDate = storyDate.replaceAll("-", ".");
		}
		String storyTime = inboxStory.getStory_time();

		// 시간정보에서 초 자르기
		if (!Util.isEmpty(storyTime)) {
			storyTime = storyTime.substring(0, 5);
		}

		// 보여줄 형식
		String storyDateTime = storyDate + "\t\t" + storyTime;

		//이미지 넣기
		ImageView inboxImg = (ImageView) findViewById(R.id.inbox_img);
		if(content.contains(Const.imgTag)){
			String imgUrl = content.substring(
					content.indexOf(Const.imgTag) + Const.imgTag.length(), content.length());
			String realUrl = SecureUtil.decode(imgUrl);
			new DownloadImageTask(inboxImg).execute(realUrl);
			inboxImg.setVisibility(View.VISIBLE);

			//내용에선 지운다.
			content = content.substring(0, content.indexOf(Const.imgTag));
		}else{
			inboxImg.setVisibility(View.GONE);
		}

		// 내용을 보여주고
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(content);

		// 날짜와 시간도 보여준다.
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setText(storyDateTime);
	}

	/**
	 * 이모티콘 입력창을 설정한다.
	 */
	private void setEmoticonArea() {
		// 이모티콘들을 불러와서
		List<String> list = Emoticon.getEmoticon_list();
		// 이모티콘 영역
		LinearLayout emoticonArea = (LinearLayout) findViewById(R.id.inbox_emoticon_area);
		// 왼쪽 여백 설정
		emoticonArea.addView(getDummyView());
		// 이모티콘 정보가 들어있는 맵
		Map<String, Integer> emoticonMap = Emoticon.getEmoticon_map();
		// 이모티콘 하나씩 돌면서 붙인다
		for (String emoticon : list) {
			EmoticonView ev = new EmoticonView(context);
			// 이모티콘 이미지를 설정하고
			ev.setBackgroundResource(emoticonMap.get(emoticon));
			// 이모티콘 아이디를 설정하고
			ev.setEmoticonId(emoticon);
			// 레이아웃 파라미터를 설정하고
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lParams.gravity = Gravity.CENTER_HORIZONTAL;
			lParams.weight = 0;
			ev.setLayoutParams(lParams);
			// 이모티콘 영역에 붙인다.
			emoticonArea.addView(ev);
			// 오른쪽 여백
			emoticonArea.addView(getDummyView());
			// 클릭 이벤트 설정
			ev.setOnClickListener(this);
		}
	}

	/**
	 * 이모티콘 사이에 위치할 여백을 제공하는 뷰
	 *
	 * @return
	 */
	private View getDummyView() {
		View v = new View(context);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lParams.weight = 1;
		v.setLayoutParams(lParams);
		return v;
	}

	/**
	 * 이모티콘 눌렀을 때 처리
	 *
	 * @param v
	 */
	private void handleEmoticonPress(EmoticonView v) {
		String emoticonId = v.getEmoticonId();

		// 이모티콘 기본 이미지(회색)
		Map<String, Integer> emoticonMap = Emoticon.getEmoticon_map();
		// 이모티콘 눌렀을 때 이미지(채색)
		Map<String, Integer> emoticonPressMap = Emoticon
				.getEmoticon_press_map();

		// 이모티콘 추가
		if (emoticons.contains(emoticonId)) {
			// 이미 찍은거면 초기화
			v.setBackgroundResource(emoticonMap.get(emoticonId));
			emoticons.remove(emoticonId);
		} else {
			// 이모티콘찍기
			v.setBackgroundResource(emoticonPressMap.get(emoticonId));
			emoticons.add(emoticonId);
		}
	}

	// ########################################################################
	/**
	 * 답글 보내기
	 */
	private void sendReply() {
		// 이모티콘 없으면 자동이모티콘
		if (emoticons.size() == 0) {
			emoticons.add("1");
		}

		// 너무 짧으면 경고메세지
		if (et.getText().toString().length() < 2) {
			Toast.makeText(context, R.string.enter_reply_plz,
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 확인창 띄우고
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.reply_title)
				.setMessage(R.string.reply_message)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// 전송상태 나타냄
								progressDialog = ProgressDialog.show(
										ReadInboxActivity.this,
										"",
										getResources().getString(
												R.string.app_name), true, true);

								// 다른사람 이야기에 찍은 이모티콘 정보를 서버로 전송
								replyContent = et.getText().toString();

								// 서버로 이모티콘 전송
								new SendReplyTask().execute();
							}

						}).setNegativeButton(R.string.no, null).show();

	}

	/**
	 * 스탬프를 서버에 전송하는 작업 AsyncTask를 상속받았기에 Main쓰레드와 별도로 실행된다.
	 *
	 * @author lifenjoy51
	 *
	 */
	public class SendReplyTask extends AsyncTask<Object, String, String> {

		/**
		 * 실제 실행되는 부분
		 */
		@Override
		protected String doInBackground(Object... params) {

			// URL
			String urlStr = Const.serverContext + "/saveReply";

			// 파라미터
			Map<String,String> param = new HashMap<String,String>();
			param.put("story_id", inboxStoryIdx);
			param.put("device_id", Util.DeviceId(context));

			// 이모티콘 붙이기
			StringBuffer sbf = new StringBuffer();
			for (String emoticonId : emoticons) {
				sbf.append(emoticonId);
				sbf.append(",");
			}

			// 이모티콘이 있어야 보낸다.
			if (sbf.length() > 0) {
				String emoticonIdParams = sbf.substring(0, sbf.length() - 1);
				param.put("emoticon_list", emoticonIdParams);
				param.put("reply_content", replyContent);
			}

			// Http전송
			String response = HttpUtil.doPost(urlStr, param);
			return response;
		}

		/**
		 * 작업이 끝나면 자동으로 실행된다.
		 */
		@Override
		protected void onPostExecute(String result) {

			if ("HttpUtil_Error".equals(result)) {
				Toast.makeText(context, R.string.error_on_transfer,
						Toast.LENGTH_LONG).show();
			} else {
				// 성공시
				Story inboxStory = new Story();
				inboxStory.setStory_id(inboxStoryIdx);

				// 스탬프찍은 이야기 삭제
				InboxDAO inboxDao = new InboxDAO(context);
				inboxDao.delStory(inboxStory);
			}

			// 동글이 해제
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			// 정상으로 전송되었을 때.
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
			finish();
		}

	}

	// ########################################################################
	/**
	 * 이야기 신고기능
	 */
	private void reportStory() {

		// 확인창 띄우고
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.report_title)
				.setMessage(R.string.report_message)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// 전송상태 나타냄
								progressDialog = ProgressDialog.show(
										ReadInboxActivity.this,
										"",
										getResources().getString(
												R.string.app_name), true, true);
								// 신고기능
								new ReportStroyTask().execute();
							}

						}).setNegativeButton(R.string.no, null).show();

	}

	/**
	 * 이야기 신고기능
	 */
	class ReportStroyTask extends AsyncTask<Object, String, String> {

		/**
		 * 실제 실행되는 부분
		 */
		@Override
		protected String doInBackground(Object... params) {

			// url 파라미터 조립
			String urlStr = Const.serverContext + "/reportStory";
			String deviceId = Util.DeviceId(context); // 기기 고유값

			Map<String,String> param = new HashMap<String,String>();
			param.put("story_id", inboxStoryIdx);
			param.put("device_id", deviceId);

			// Http전송
			String response = HttpUtil.doPost(urlStr, param);
			return response;
		}

		/**
		 * 작업이 끝나면 자동으로 실행된다.
		 */
		@Override
		protected void onPostExecute(String result) {

			if ("HttpUtil_Error".equals(result)) {
				Toast.makeText(context, R.string.error_on_transfer,
						Toast.LENGTH_LONG).show();
			} else {
				// 성공했을때
				Story inboxStory = new Story();
				inboxStory.setStory_id(inboxStoryIdx);

				// 신고한 이야기 삭제
				InboxDAO inboxDao = new InboxDAO(context);
				inboxDao.delStory(inboxStory);
				finish();
			}

			// 동글이 해제
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	}

	// ########################################################################
	/**
	 * 이야기 패스기능
	 */
	private void passStory() {

		// Ask the user if they want to quit
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.pass_title)
				.setMessage(R.string.pass_message)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// 전송상태 나타냄
								progressDialog = ProgressDialog.show(
										ReadInboxActivity.this,
										"",
										getResources().getString(
												R.string.app_name), true, true);
								// 신고기능
								new PassStroyTask().execute();
							}

						}).setNegativeButton(R.string.no, null).show();

	}

	/**
	 * 이야기를 패스
	 *
	 */
	class PassStroyTask extends AsyncTask<Object, String, String> {

		/**
		 * 실제 실행되는 부분
		 */
		@Override
		protected String doInBackground(Object... params) {

			// URL, 파라미터.
			String urlStr = Const.serverContext + "/passStory";
			String deviceId = Util.DeviceId(context); // 기기 고유값

			Map<String,String> param = new HashMap<String,String>();
			param.put("story_id", inboxStoryIdx);
			param.put("device_id", deviceId);

			// Http전송
			String response = HttpUtil.doPost(urlStr, param);
			return response;
		}

		/**
		 * 작업이 끝나면 실행
		 */
		@Override
		protected void onPostExecute(String result) {

			if ("HttpUtil_Error".equals(result)) {
				Toast.makeText(context, R.string.error_on_transfer,
						Toast.LENGTH_LONG).show();
			} else {
				// 성공했을때
				Story inboxStory = new Story();
				inboxStory.setStory_id(inboxStoryIdx);

				// 이야기 삭제
				InboxDAO inboxDao = new InboxDAO(context);
				inboxDao.delStory(inboxStory);
				finish();
			}

			// 동글이 해제
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

		}
	}

}
