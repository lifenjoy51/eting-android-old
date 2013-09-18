package com.gif.eting.act;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
import com.gif.eting_dev.R;

public class ExportEmailActivity extends Activity implements OnClickListener {

	private Context context;
	private EditText email_address;
	private ImageView send_email;
//	private ImageView cancel_btn;

	private List<StoryDTO> contentArray = null;
	private StoryService storyService = null;
	private String mailContent;
	private String emailAddress;
	private Typeface nanum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_setting);
		
		nanum = Typeface.createFromAsset(getAssets(), "fonts/NanumGothic.ttf");

		email_address = (EditText) findViewById(R.id.email_address);
		send_email = (ImageView) findViewById(R.id.send_email);
//		cancel_btn = (ImageView) findViewById(R.id.cancel_btn);
		
		email_address.setTypeface(nanum);

		// emailAddress = email_address.getText().toString();

		send_email.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					send_email = (ImageView) findViewById(v.getId());
					send_email.setImageResource(R.drawable.send_2);
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (!v.isPressed()) {
						send_email.setImageResource(R.drawable.send_1);
						return true;
					}

				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					send_email.setImageResource(R.drawable.send_1);
				}

				return false;
			}
		});
//		cancel_btn.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					cancel_btn = (ImageView) findViewById(v.getId());
//					cancel_btn.setImageResource(R.drawable.cancel_2);
//				}
//				if (event.getAction() == MotionEvent.ACTION_MOVE) {
//					if (!v.isPressed()) {
//						cancel_btn.setImageResource(R.drawable.cancel_1);
//						return true;
//					}
//
//				}
//
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					cancel_btn.setImageResource(R.drawable.cancel_1);
//				}
//
//				return false;
//			}
//		});

		send_email.setOnClickListener(this);
//		cancel_btn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.send_email) {
			String s = email_address.getText().toString();
			if (s == null || "".equals(s)) {
				Toast.makeText(this, "이메일주소를 입력해주세요", Toast.LENGTH_SHORT).show();
				return;
			} else {
				sendEmail();
			}

		}
//		if (v.getId() == R.id.cancel_btn) {
//			clear(v);
//		}

	}

	public void sendEmail() {
		context = getApplicationContext();
		storyService = new StoryService(context);
		contentArray = storyService.getMyStoryList();

		StringBuffer sb = new StringBuffer();
		for (StoryDTO storyDTO : contentArray) {
			sb.append("==========================");
			sb.append("\n");
			sb.append(storyDTO.getIdx());
			sb.append("\n");
			sb.append(storyDTO.getStory_date());
			sb.append("\n");
			sb.append(storyDTO.getContent());
			sb.append("\n");
			sb.append("==========================");
		}

		mailContent = sb.toString();

		// 전송할 파일의 경로
		// String szSendFilePath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/myTest.txt";
		// File f = new File(szSendFilePath);
		// if (!f.exists()) {
		// Toast.makeText(this, "파일이 없습니다.", Toast.LENGTH_SHORT).show();
		// }
		// File객체로부터 Uri값 생성
		// fileUri = Uri.fromFile(f);

		Intent it = new Intent(Intent.ACTION_SEND);
		it.setType("plain/text");

		emailAddress = email_address.getText().toString();
		
		String[] tos = { emailAddress };
		it.putExtra(Intent.EXTRA_EMAIL, tos);

		it.putExtra(Intent.EXTRA_SUBJECT, "Eting backup");
		it.putExtra(Intent.EXTRA_TEXT, mailContent);

		// 파일첨부
		// it.putExtra(Intent.EXTRA_STREAM, fileUri);
		try {
			// startActivity(it);
			startActivity(Intent.createChooser(it, "메일보냄"));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "안됨", Toast.LENGTH_SHORT).show();
			//TODO 메시지 확인필요
		}

	}

	public void clear(View v) {
		email_address.setText("");

	}

}
