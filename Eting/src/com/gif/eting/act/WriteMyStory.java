package com.gif.eting.act;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.gif.eting.R;
import com.gif.eting.svc.task.SendStoryTask;
import com.gif.eting.util.AsyncTaskCompleteListener;

public class WriteMyStory extends SherlockFragment implements OnClickListener{
	private ViewPager mPager;
	private ProgressDialog progressDialog;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static WriteMyStory create(int pageNumber) {
        WriteMyStory fragment = new WriteMyStory();
        return fragment;
    }

    public WriteMyStory() {
    }

    public void setViewPager(ViewPager mPager) {
    	this.mPager = mPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.write_story, container, false);
        ((ImageButton) rootView.findViewById(R.id.send_story_btn))
		.setOnClickListener(this);


        return rootView;
    }


	private void sendAndSaveStory(){
		View view = getView();
		
		EditText et = (EditText) view.findViewById(R.id.story_content);
		String content = et.getText().toString();	//이야기 내용
		
		//전송상태 나타냄
		progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.app_name), true, true);

		new SendStoryTask(new AfterSendAndSaveStory()).execute(content, getSherlockActivity());
	}
	
	private class AfterSendAndSaveStory implements AsyncTaskCompleteListener<String>{

		@Override
		public void onTaskComplete(String result) {
			Log.i("onTaskComplete", result);

			if (progressDialog != null)
				progressDialog.dismiss();

			Toast toast = Toast.makeText(getActivity(), "이야기가 전송되었습니다", Toast.LENGTH_SHORT);
			toast.show();

			// 내 이야기 읽기 화면으로 이동
			//startActivity(new Intent(context, ReadMyStoryActivity.class));
			mPager.setCurrentItem(0);
		}
	}


	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.send_story_btn:
			sendAndSaveStory();
			break;
		}

	}
}
