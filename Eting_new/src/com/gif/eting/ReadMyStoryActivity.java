package com.gif.eting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.etc.Const;
import com.gif.eting.etc.HttpUtil;
import com.gif.eting.etc.Util;
import com.gif.eting.obj.Story;
import com.gif.eting.svc.StoryService;
import com.gif.eting.view.EmoticonView;

/**
 * 내 이야기 목록에서 선택한 이야기를 읽는 화면
 *
 * @author lifenjoy51
 *
 */
public class ReadMyStoryActivity extends BaseActivity implements
		OnClickListener {

	private StoryService storyService;
	private String storyId;
	private Context context;
	private String replyId;
	private String replyContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO 왜 했는지 까먹음...
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		super.onCreate(savedInstanceState);

		this.context = getApplicationContext();

		// TODO 왜했지 이거...
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 레이아웃 설정
		setContentView(R.layout.activity_readmystory);

		// 시간에 따라 배경을 바꾼다.
		setBackgroundByTime();

		// 내 이야기를 출력한다.
		drawMyStory();

		// 클릭이벤트 설정
		findViewById(R.id.del_btn).setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.reply_report_btn).setOnClickListener(this);
		findViewById(R.id.reply_del_btn).setOnClickListener(this);

		findViewById(R.id.del_btn).bringToFront();

	}

	/**
	 * 클릭이벤트 설정
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.del_btn) {
			// 내 이야기 삭제
			deleteMyStory();
		} else if (v.getId() == R.id.reply_report_btn) {
			// 답글 신고
			reportReply();
		} else if (v.getId() == R.id.reply_del_btn) {
			// 답글 삭제
			deleteReply();
		} else if (v.getId() == R.id.back_btn) {
			// 화면 닫기
			finish();
		}
	}

	/**
	 * 시간에따라 배경을 바꾼다
	 */
	private void setBackgroundByTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
		String thisHourStr = sdf.format(new Date());
		int thisHour = Integer.parseInt(thisHourStr);

		if (thisHour < 12) {
			findViewById(R.id.popup_layout).setBackgroundResource(
					R.drawable.bg_purple); // 보라
		} else if (thisHour < 24) {
			findViewById(R.id.popup_layout).setBackgroundResource(
					R.drawable.bg_green); // 초록
		} else {
			findViewById(R.id.popup_layout).setBackgroundResource(
					R.drawable.bg_blue); // 파랑
		}
	}

	/**
	 * 이야기 출력
	 */
	private void drawMyStory() {
		Intent intent = getIntent();
		storyId = intent.getStringExtra("idx"); // 파라미터값으로 넘긴 이야기 고유번호
		Log.i("gcm storyId", "ReadMyStory Activity = " + storyId);

		// Service초기화
		storyService = new StoryService(this.getApplicationContext());

		try {
			// 읽음 표시
			storyService.updStoryReplyRead(storyId);

			// 해당하는 이야기 받아오기
			Story myStory = storyService.getMyStory(storyId);

			// 답글아이디
			replyId = myStory.getReply_id();

			// 작성 내용
			String content = myStory.getStory_content();

			// 작성 날짜
			String storyDate = myStory.getStory_date();
			if (!Util.isEmpty(storyDate)) {
				storyDate = storyDate.replaceAll("-", ".");
			}

			// 작성 시간
			String storyTime = myStory.getStory_time();
			String storyHH = "";
			String storyHHMM = "";
			if (Util.isEmpty(storyTime)) {
				storyHH = "0";
			} else {
				storyHHMM = storyTime.substring(0, 5);
				storyHH = storyTime.substring(0, 2);
			}

			// 작성날짜, 시간
			String storyDateTime = storyDate + "\t\t" + storyHHMM;

			// 이야기 작성 날짜,시간 그리고
			TextView storyDateView = (TextView) findViewById(R.id.mystory_content_top);
			storyDateView.setText(storyDateTime);

			// 이야기 내용을 그린다.
			TextView contentView = (TextView) findViewById(R.id.popup_content);
			contentView.setText(content);

			// 작성시간에 맞게 배경화면 변화
			int storyHour = Integer.parseInt(storyHH);
			// Log.i("currunt hour", thisHourStr);

			if (storyHour < 12) {
				findViewById(R.id.mystory_content_bg).setBackgroundResource(
						R.drawable.list_bg_p);

			} else if (storyHour < 24) {
				findViewById(R.id.mystory_content_bg).setBackgroundResource(
						R.drawable.list_bg_g);

			} else {
				findViewById(R.id.mystory_content_bg).setBackgroundResource(
						R.drawable.list_bg_b);

			}

			// 답글영역 그리기
			drawReplyArea();

		} catch (Exception e) {
			// Log.i("read my story activity error", e.toString());
			e.printStackTrace();
			Toast.makeText(context, R.string.have_problem, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 답글정보 출력
	 *
	 */
	public void drawReplyArea() {
		List<Integer> list = storyService.getEmoticons(storyId);

		// 답글여부에 따라 보여주거나 보이지 않는다.
		if (list != null) {
			if (list.size() > 0) {
				replyContent = storyService.getReplyContent(storyId);
				TextView contentView = (TextView) findViewById(R.id.mystory_reply_content);
				contentView.setText("P.S. " + replyContent);
			} else {
				// 답글영역을 숨긴다
				LinearLayout reply_area = (LinearLayout) findViewById(R.id.reply_area);
				reply_area.setVisibility(View.GONE);
			}
		}

		// 이모티콘 영역
		LinearLayout emoticonArea = (LinearLayout) findViewById(R.id.mystory_emoticon_area);

		// 왼쪽 여백
		emoticonArea.addView(getDummyView());
		// 이모티콘 하나씩 넣는다.
		for (Integer emoticonId : list) {
			// 이모티콘
			EmoticonView ev = new EmoticonView(context);
			// 이모티콘 이미지 설정
			ev.setBackgroundResource(emoticonId);
			// 레이아웃 파라미터 설정
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// 가운데 정렬
			lParams.gravity = Gravity.CENTER_HORIZONTAL;
			// 가중치 0으로해서 정렬
			lParams.weight = 0;
			// 레이아웃 설정을 적용하고
			ev.setLayoutParams(lParams);
			// 이모티콘 영역에 붙인다
			emoticonArea.addView(ev);
			// 오른쪽 여백
			emoticonArea.addView(getDummyView());
		}
	}

	/**
	 * 여백 설정용 뷰
	 *
	 * @return
	 */
	private View getDummyView() {
		View v = new View(context);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(0, 0);
		lParams.weight = 1;
		v.setLayoutParams(lParams);
		return v;
	}

	/**
	 * 내 이야기 삭제
	 */
	private void deleteMyStory() {

		// Ask the user if they want to quit
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.delete_story)
				.setMessage(R.string.really_delete_story)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// 삭제버튼을 클릭했을 때
								storyService.delStory(storyId);

								// 삭제 후 로직
								Toast.makeText(context,
										R.string.delete_completed,
										Toast.LENGTH_SHORT).show();

								// 돌아가기
								finish();
							}

						}).setNegativeButton(R.string.no, null).show();
	}

	/**
	 * 답글 신고
	 */
	private void reportReply() {

		// Ask the user if they want to quit
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.reply_report)
				.setMessage(R.string.really_reply_report)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// 답글신고 버튼을 클릭했을 때
								onReportOrDeleteComment("R");

								// 신고 후 로직
								Toast.makeText(context,
										R.string.report_completed,
										Toast.LENGTH_SHORT).show();

								// 돌아가기
								finish();
							}

						}).setNegativeButton(R.string.no, null).show();
	}

	/**
	 * 답글을 삭제한다.
	 */
	private void deleteReply() {

		// Ask the user if they want to quit
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.reply_del)
				.setMessage(R.string.really_reply_del)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// 답글삭제 버튼을 클릭했을 때
								onReportOrDeleteComment("D");

								// 삭제 후 로직
								Toast.makeText(context,
										R.string.delete_completed,
										Toast.LENGTH_SHORT).show();

								// 돌아가기
								finish();
							}

						}).setNegativeButton(R.string.no, null).show();
	}

	/**
	 * 답글 삭제 or 신고 시 로직
	 *
	 * @param flag
	 */
	private void onReportOrDeleteComment(String flag) {
		storyService.deleteComment(storyId);

		// 삭제한 이야기 서버로 전송, 신고인지 삭제인지 값에 맞게!!
		new HandleReplyTask().execute(flag);
	}

	/**
	 * 답글을 신고하거나 삭제한다.
	 *
	 * @author lifenjoy51
	 *
	 */
	class HandleReplyTask extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {

			String urlStr = "";
			String flag = String.valueOf(params[0]);

			if ("D".equals(flag)) {
				urlStr = Const.serverContext + "/deleteReply";
			} else if ("R".equals(flag)) {
				urlStr = Const.serverContext + "/reportReply";
			}

			String deviceId = Util.DeviceId(context); // 기기 고유값

			// 파라미터

			Map<String,String> param = new HashMap<String,String>();
			param.put("reply_id", replyId);
			param.put("story_id", storyId);
			param.put("reply_content", replyContent);
			param.put("device_id", deviceId);

			// Http전송
			String response = HttpUtil.doPost(urlStr, param);
			// System.out.println(this.getClass().getName() + " = " + response);
			return response;
		}

		@Override
		protected void onPostExecute(String result) {

		}

	}
}
