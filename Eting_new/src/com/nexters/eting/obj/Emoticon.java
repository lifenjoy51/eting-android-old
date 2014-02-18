package com.nexters.eting.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nexters.eting.R;

public class Emoticon {
	private static Map<String, Integer> emoticon_map;
	private static List<String> emoticon_list;

	public static Map<String, Integer> getEmoticon_map() {
		if (emoticon_map == null) {
			init();
		}
		return emoticon_map;
	}
	
	public static List<String> getEmoticon_list(){
		if (emoticon_list == null) {
			init();
		}
		return emoticon_list;
	}

	private static void init() {
		emoticon_list = new ArrayList<String>();
		emoticon_list.add("1");
		emoticon_list.add("2");
		emoticon_list.add("3");
		emoticon_list.add("4");
		emoticon_list.add("5");
		emoticon_list.add("6");
		
		emoticon_map = new HashMap<String, Integer>();
		emoticon_map.put("1", R.drawable.emotion_icon01);
		emoticon_map.put("2", R.drawable.emotion_icon02);
		emoticon_map.put("3", R.drawable.emotion_icon03);
		emoticon_map.put("4", R.drawable.emotion_icon04);
		emoticon_map.put("5", R.drawable.emotion_icon05);
		emoticon_map.put("6", R.drawable.emotion_icon06);
	}

}
