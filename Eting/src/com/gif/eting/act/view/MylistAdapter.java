package com.gif.eting.act.view;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gif.eting.R;
import com.gif.eting.dto.StoryDTO;

public class MylistAdapter extends ArrayAdapter<StoryDTO> {

	private List<StoryDTO> items;
	private Context context;

	public MylistAdapter(Context context, int textViewResourceId,
			List<StoryDTO> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/**
		 * 내 이야기
		 */
		StoryDTO story = items.get(position);
		String storyDate = story.getStory_date();
		String storyContent = story.getContent();
		View v = convertView;
		Log.i("MylistAdapter", "list = "+position+storyDate+storyContent);
		
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			

		/**
		 * 날짜구분선 여부를 확인하고 view를 생성한다.
		 */
		if("#dateInfo".equals(storyContent)){
			v = vi.inflate(R.layout.mylist_line, null);
			
			/**
			 * 구분선 날짜
			 */
			TextView mystoryDate = (TextView) v.findViewById(R.id.mylist_item_date);
			if (mystoryDate != null) {
				mystoryDate.setText(storyDate);
			}
			
		}else{
			v = vi.inflate(R.layout.mylist_item, null);	//TODO 스티커를 받은 이야기는 별표시가 들어가야하는데 아직 기능구현이 안되어있다.
				
			/**
			 * 이야기 작성일자
			 */
			TextView mystoryDate = (TextView) v.findViewById(R.id.mylist_item_date);
			if (mystoryDate != null) {
				mystoryDate.setText(storyDate);
			}

			/**
			 * 이야기 내용
			 */
			TextView mystoryContent = (TextView) v.findViewById(R.id.mylist_item_content);
			if (mystoryContent != null) {
				String content = storyContent;
				int maxLength = 15;	//리스트에 보여줄 문자열 길이
				if(content.length()>maxLength){	//길이보다 길면
					content = content.substring(0, maxLength);		//자른다
					content += "...";
				}
				mystoryContent.setText(content);
			}
			
		}
		
		return v;	
	}
}
