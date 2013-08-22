package com.gif.eting.act.frg;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.gif.eting.R;
import com.gif.eting.svc.task.SendStoryTask;
import com.gif.eting.util.AsyncTaskCompleteListener;

/**
 * 내 이야기 쓰기
 * 
 * @author lifenjoy51
 *
 */
public class WriteMyStoryFragment extends SherlockFragment implements OnClickListener{
	private ViewPager mPager;
	private ProgressDialog progressDialog;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static WriteMyStoryFragment create(int pageNumber) {
        WriteMyStoryFragment fragment = new WriteMyStoryFragment();
        return fragment;
    }

    public WriteMyStoryFragment() {
    }

    public void setViewPager(ViewPager mPager) {
    	this.mPager = mPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	//TODO 뒤로가기 눌렀을때 메인페이지로 이동하는 로직 필요.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.write_story, container, false);
        
        //클릭이벤트 설정
		rootView.findViewById(R.id.send_story_btn).setOnClickListener(this);

        return rootView;
    }

    /**
     * 클릭이벤트
     */
    @Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.send_story_btn:
			sendAndSaveStory();
			break;
		}

	}

    /**
     * 작성한 이야기를 서버에 전송하고 자신의 폰에 저장한다.
     */
	private void sendAndSaveStory(){		
		EditText et = (EditText) getView().findViewById(R.id.story_content);
		String content = et.getText().toString();	//이야기 내용
		
		//전송상태 나타냄
		progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.app_name), true, true);	//TODO 전송메세지가 임시값으로 설정되어있다.

		/**
		 * 서버로 이야기 전송
		 * 
		 * SendStoryTask생성할때 파라미터는 SendStoryTask가 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = content. 이야기 내용
		 * parameter[1] = getSherlockActivity(). Context.
		 */
		new SendStoryTask(new AfterSendStoryTask()).execute(content, getSherlockActivity());
	}
	
	/**
	 * SendStoryTask수행 후 실행되는 콜백
	 */
	private class AfterSendStoryTask implements AsyncTaskCompleteListener<String>{

		@Override
		public void onTaskComplete(String result) {
			Log.i("onTaskComplete", result);

			if (progressDialog != null)
				progressDialog.dismiss();

			Toast toast = Toast.makeText(getActivity(), "이야기가 전송되었습니다", Toast.LENGTH_SHORT);
			toast.show();

			// 내 이야기 읽기 화면으로 이동
			mPager.setCurrentItem(0);
		}
	}
}
