package com.gif.eting.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.gif.eting.R;

public class Main extends SherlockFragment implements OnClickListener{
	private ViewPager mPager;
	
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static Main create() {
        Main fragment = new Main();
        return fragment;
    }
    
    public Main(){
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main, container, false);
        
        //버튼이벤트 삽입
        ((ImageButton) rootView.findViewById(R.id.inbox_btn)).setOnClickListener(this);
  		((ImageButton) rootView.findViewById(R.id.write_et_btn)).setOnClickListener(this);
  		((ImageButton) rootView.findViewById(R.id.read_et_btn)).setOnClickListener(this);
  		((ImageButton) rootView.findViewById(R.id.setting_btn)).setOnClickListener(this);

        return rootView;
    }

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.inbox_btn:
			checkInbox();
			break;
			
		case R.id.write_et_btn:
			mPager.setCurrentItem(2);
			break;
			
		case R.id.read_et_btn:
			mPager.setCurrentItem(0);
			break;
			
		case R.id.setting_btn:
			startActivity(new Intent(getActivity(), Setting.class));
			break;
			
		}

	}

	// 받은편지함 개수 확인하고 열지 안열지 체크
	public void checkInbox() {
		startActivity(new Intent(getActivity(), ReadInbox.class)); // 팝업
		// 출력
	}
}