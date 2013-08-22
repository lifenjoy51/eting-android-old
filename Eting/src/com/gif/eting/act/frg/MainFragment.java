package com.gif.eting.act.frg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.gif.eting.R;
import com.gif.eting.act.ReadInboxActivity;
import com.gif.eting.act.SettingActivity;

/**
 * 메인화면
 * 
 * @author lifenjoy51
 *
 */
public class MainFragment extends SherlockFragment implements OnClickListener{
	private ViewPager mPager;
	
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static MainFragment create() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }
    
    /**
     * 생성자
     */
    public MainFragment(){
    }

    /**
     * ViewPager setter
     * @param mPager
     */
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
        
        //클릭이벤트 설정
        rootView.findViewById(R.id.inbox_btn).setOnClickListener(this);
  		rootView.findViewById(R.id.write_et_btn).setOnClickListener(this);
  		rootView.findViewById(R.id.read_et_btn).setOnClickListener(this);
  		rootView.findViewById(R.id.setting_btn).setOnClickListener(this);

        return rootView;
    }

    /**
     * 클릭이벤트 실행
     */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.inbox_btn:
			/**
			 * 받은이야기 보는 로직
			 */
			this.checkInbox();
			break;
			
		case R.id.write_et_btn:
			/**
			 * 이야기 작성화면으로 이동
			 */
			mPager.setCurrentItem(2);
			break;
			
		case R.id.read_et_btn:
			/**
			 * 내 이야기 목록 화면으로 이동
			 */
			mPager.setCurrentItem(0);
			break;
			
		case R.id.setting_btn:
			/**
			 * 설정화면으로 이동
			 */
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
			
		}

	}

	/**
	 *  받은편지함 개수 확인하고 열지 안열지 체크
	 */
	public void checkInbox() {
		//TODO 받은편지함 개수를 확인하고 있지 않고있다. 로직 추가 필요함.
		startActivity(new Intent(getActivity(), ReadInboxActivity.class)); // 팝업
	}
}