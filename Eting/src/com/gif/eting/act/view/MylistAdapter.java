package com.gif.eting.act.view;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gif.eting.dto.StoryDTO;
import com.gif.eting.R;

public class MylistAdapter extends ArrayAdapter<StoryDTO> {

	private List<StoryDTO> items;
	private Context context;
	private Typeface nanum;
	private LayoutInflater vi;

	public MylistAdapter(Context context, int textViewResourceId,
			List<StoryDTO> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
		this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		nanum = Typeface.createFromAsset(getContext().getAssets(), "fonts/NanumGothic.ttf");
		
		/**
		 * 내 이야기
		 */
		StoryDTO story = items.get(position);
		String storyDate = story.getStory_date();
		if(storyDate!=null){
			storyDate = storyDate.replaceAll("-", ".");
		}
		
		String storyContent = story.getContent();
		String storyTime = story.getStory_time();
		String stampYn = story.getStamp_yn();
		View v = convertView;
		Log.i("MylistAdapter", "list = "+position+storyDate+storyContent);
		
		
			

		/**
		 * 날짜구분선 여부를 확인하고 view를 생성한다.
		 */
		if("#dateInfo".equals(storyContent)){
			v = vi.inflate(R.layout.mylist_line, null);
			v.setClickable(false);
			
			/**
			 * 구분선 날짜
			 */
			TextView mystoryDate = (TextView) v.findViewById(R.id.mylist_item_date);
			mystoryDate.setTypeface(nanum);
			if (mystoryDate != null) {
				mystoryDate.setText(storyDate);
			}
			
		}else{
			v = vi.inflate(R.layout.mylist_item, null);
			
			/**
			 * 작성시간에 맞게 배경변화 
			 */
			if (storyTime != null) {
				if(storyTime.length()>2){
					String thisHourStr = storyTime.substring(0,2);
					int thisHour = Integer.parseInt(thisHourStr);
										
					if(thisHour<6 ){
						v.setBackgroundResource(R.drawable.list_blank_b);
					}else if(thisHour<12 ){
						v.setBackgroundResource(R.drawable.list_blank_p);
					}else if(thisHour<24 ){
						v.setBackgroundResource(R.drawable.list_blank_g);
					}else {
						v.setBackgroundResource(R.drawable.list_blank_b);
					}
					
				}
			}
			
				
			/**
			 * 이야기 작성일자
			 */
			TextView mystoryDate = (TextView) v.findViewById(R.id.mylist_item_date);
			mystoryDate.setTypeface(nanum);
			if (mystoryDate != null) {
				mystoryDate.setText(storyDate);
			}

			/**
			 * 이야기 내용
			 */
			TextView mystoryContent = (TextView) v.findViewById(R.id.mylist_item_content);
			//mystoryContent.setTypeface(nanum);
			if (mystoryContent != null) {
				String content = storyContent;
				content = content.replaceAll("\n", " ");
				/*
				int maxLength = 15;	//리스트에 보여줄 문자열 길이
				if(content.length()>maxLength){	//길이보다 길면
					content = content.substring(0, maxLength);		//자른다
					content += "...";
				}
				*/
				mystoryContent.setText(content);
			}
			
			/**
			 * 스탬프여부
			 */
			ImageView star = (ImageView) v.findViewById(R.id.star);
			if (stampYn != null) {
				if("Y".equals(stampYn)){
					star.setImageResource(R.drawable.star_2);
				}
			}
			
		}
		
		return v;	
	}
}
