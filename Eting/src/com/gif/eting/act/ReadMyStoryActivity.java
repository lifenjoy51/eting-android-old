package com.gif.eting.act;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.R;
import com.gif.eting.dto.StampDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
import com.gif.eting.svc.task.ReceiveStampTask;
import com.gif.eting.util.AsyncTaskCompleteListener;

/**
 * 내 이야기 목록에서 선택한 이야기를 읽는 화면
 * 
 * @author lifenjoy51
 *
 */
public class ReadMyStoryActivity extends Activity implements OnClickListener{

	private StoryService storyService;
	private String storyIdx;
	private Context context;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);
		setContentView(R.layout.popup);
		
		this.context = getApplicationContext();
		
		Intent intent = getIntent();
		storyIdx = intent.getStringExtra("idx");	//파라미터값으로 넘긴 이야기 고유번호

		//Service초기화
		storyService = new StoryService(this.getApplicationContext());
		
		StoryDTO myStory = storyService.getMyStory(storyIdx);	//해당하는 이야기 받아오기
		String content = myStory.getContent();
		String storyDate = myStory.getStory_date();
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(content);
		
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setText(storyDate);
		
		/**
		 * 조회하는 이야기에 찍힌 스탬프 받아오기
		 * 
		 * ReceiveStampTask를 생성할때 파라미터는 ReceiveStampTask가 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = idx. 이야기 고유번호.
		 */
		new ReceiveStampTask(new AfterReceiveStampTask()).execute(storyIdx);
		
		//클릭이벤트 설정
		findViewById(R.id.del_btn).setOnClickListener(this);
	}
	
	/**
	 * ReceiveStampTask 수행 후 실행되는 콜백
	 */
	private class AfterReceiveStampTask implements AsyncTaskCompleteListener<List<StampDTO>>{

		@Override
		public void onTaskComplete(List<StampDTO> list) {
			//TODO 스탬프DTO들을 받아온다. 화면에 뿌려주는 로직 만들어야함.
			TextView stampInfoView = (TextView) findViewById(R.id.stamp_info);
			if(list.size()>0){
				StringBuffer sb = new StringBuffer();
				sb.append(list.get(0).getSender());
				for(StampDTO stamp : list){
					sb.append(stamp.getStamp_name());
					sb.append(" , ");					
				}
				stampInfoView.setText(sb.toString());
			}
			//stampInfoView.setText(result);	
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.del_btn){
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
									
									//삭제버튼을 클릭했을 때
									storyService.delStory(storyIdx);
									
									//삭제 후 로직
									Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show();
									
									//돌아가기
									finish();								}

							}).setNegativeButton(R.string.no, null).show();
		}
	}

}
