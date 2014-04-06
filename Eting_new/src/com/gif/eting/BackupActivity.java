package com.gif.eting;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.etc.AD;
import com.gif.eting.obj.Story;
import com.gif.eting.svc.StoryService;

public class BackupActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private EditText email_address;
	private ImageView send_email;
	private TextView email_textView;

	private List<Story> contentArray = null;
	private StoryService storyService = null;
	private String mailContent;
	private String emailAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
		email_address = (EditText) findViewById(R.id.email_address);
		send_email = (ImageView) findViewById(R.id.send_email);
		email_textView = (TextView) findViewById(R.id.email_textView);
		email_textView.setPaintFlags(email_textView.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);
		email_address.setHintTextColor(Color.parseColor("#bbbbbb"));

		send_email.setOnClickListener(this);

		// ad
		AD.ad(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.send_email) {
			String s = email_address.getText().toString();
			if (s == null || "".equals(s)) {
				Toast.makeText(this, R.string.enter_email_plz,
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				sendEmail();
			}

		}

	}

	public void sendEmail() {
		context = getApplicationContext();
		storyService = new StoryService(context);
		contentArray = storyService.getMyStoryList();

		StringBuffer sb = new StringBuffer();
		for (Story storyDTO : contentArray) {
			sb.append("==========================");
			sb.append("\n");
			sb.append(storyDTO.getStory_id());
			sb.append("\n");
			sb.append(storyDTO.getStory_date());
			sb.append("\n");
			sb.append(storyDTO.getStory_content());
			sb.append("\n");
			sb.append("==========================");
		}

		mailContent = sb.toString();

		Intent it = new Intent(Intent.ACTION_SEND);
		it.setType("plain/text");

		emailAddress = email_address.getText().toString();

		String[] tos = { emailAddress };
		it.putExtra(Intent.EXTRA_EMAIL, tos);

		it.putExtra(Intent.EXTRA_SUBJECT, "Eting backup");
		it.putExtra(Intent.EXTRA_TEXT, mailContent);

		// 파일첨부
		try {
			startActivity(Intent.createChooser(it, "메일보냄"));
		} catch (android.content.ActivityNotFoundException ex) {
			ex.printStackTrace();
			Toast.makeText(this, R.string.have_problem, Toast.LENGTH_SHORT)
					.show();
			// TODO 메시지 확인필요
		}

	}

	public void clear(View v) {
		email_address.setText("");

	}

}
