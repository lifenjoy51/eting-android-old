package com.gif.eting.act.view;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gif.eting.R;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.util.Util;

public class MylistAdapter extends ArrayAdapter<StoryDTO> {

	private List<StoryDTO> items = new ArrayList<StoryDTO>();
	//private Context context;
	private LayoutInflater vi;
	
	//static constants
	private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
    private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();
    
	public MylistAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		//this.context = context;
		this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void addItem(final StoryDTO item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final StoryDTO item) {
        items.add(item);
        mSeparatorsSet.add(items.size() - 1);
        notifyDataSetChanged();
    }

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}
	
	@Override
    public int getCount() {
        return items.size();
    }

    @Override
    public StoryDTO getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


	@SuppressLint("CutPasteId")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
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
		//Log.i("MylistAdapter", "list = "+position+storyDate+storyContent);			
		
        int type = getItemViewType(position);
        //System.out.println("getView " + position + " " + convertView + " type = " + type);
        

		View v = null;
		
		// View 생성
		if (convertView == null) {
			switch (type) {
			case TYPE_SEPARATOR:
				v = vi.inflate(R.layout.mylist_line, null);
				break;

			case TYPE_ITEM:
				v = vi.inflate(R.layout.mylist_item, null);
				v.setClickable(false);
				break;

			}
		} else {
			v = convertView;
		}        
        
		//View 내용 채우기
    	switch (type) {
    	case TYPE_SEPARATOR:
    		
			/**
			 * 구분선 날짜
			 */
			TextView seperatorDate = (TextView) v.findViewById(R.id.mylist_item_date);
			//seperatorDate.setTypeface(Util.getNanum(getContext()),Typeface.BOLD);
			if (seperatorDate != null) {
				seperatorDate.setText(storyDate);
			} 
			
    		break;
    		
    	case TYPE_ITEM:
			
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
			//mystoryDate.setTypeface(Util.getNanum(getContext()), Typeface.BOLD);
			if (mystoryDate != null) {
				mystoryDate.setText(storyDate);
			}

			/**
			 * 이야기 내용
			 */
			TextView mystoryContent = (TextView) v.findViewById(R.id.mylist_item_content);
			//mystoryContent.setTypeface(Util.getNanum(getContext()));
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
			if (star != null) {
				if("R".equals(stampYn)){
					//읽은상태
					star.setImageResource(R.drawable.star_2);
				}else if("Y".equals(stampYn)){
					//여기가 안읽은 상태
					star.setImageResource(R.drawable.star_3);
					//star.setAlpha(0.5f);
				}else{
					star.setImageResource(R.drawable.star_1);
				}
			}
			
    		break;
    	}  	

		/**
		 * 날짜구분선 여부를 확인하고 view를 생성한다.
		 */
		if("#dateInfo".equals(storyContent)){
			
		}else{
			
		}
		
		return v;	
	}
}
