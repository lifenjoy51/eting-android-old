package com.gif.eting.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gif.eting.R;

public class Emoticon {
	private static Map<String, Integer> emoticon_map;
	private static Map<String, Integer> emoticon_press_map;
	private static List<String> emoticon_list;

	/**
	 * 이모티콘 이미지(누르지 않았을 때, 회색)을 받아온다.
	 *
	 * @return
	 */
	public static Map<String, Integer> getEmoticon_map() {
		if (emoticon_map == null) {
			init();
		}
		return emoticon_map;
	}

	/**
	 * 이모티콘 이미지(눌렀을 때, 채색)을 받아온다.
	 *
	 * @return
	 */
	public static Map<String, Integer> getEmoticon_press_map() {
		if (emoticon_press_map == null) {
			init();
		}
		return emoticon_press_map;
	}

	/**
	 * 이모티콘들을 받아온다.
	 *
	 * @return
	 */
	public static List<String> getEmoticon_list() {
		if (emoticon_list == null) {
			init();
		}
		return emoticon_list;
	}

	/**
	 * 이모티콘 정보 초기화
	 */
	private static void init() {
		// 이모티콘 목록. 변경이 없을것 같아 하드코딩했다.
		emoticon_list = new ArrayList<String>();
		emoticon_list.add("1");
		emoticon_list.add("2");
		emoticon_list.add("3");
		emoticon_list.add("4");
		emoticon_list.add("5");
		emoticon_list.add("6");

		// 이모티콘 눌리지 않은 모습. 회색.
		emoticon_map = new HashMap<String, Integer>();
		emoticon_map.put("1", R.drawable.emotion_icon01);
		emoticon_map.put("2", R.drawable.emotion_icon04);
		emoticon_map.put("3", R.drawable.emotion_icon03);
		emoticon_map.put("4", R.drawable.emotion_icon06);
		emoticon_map.put("5", R.drawable.emotion_icon05);
		emoticon_map.put("6", R.drawable.emotion_icon02);

		// 이모티콘 눌렀을때 모습. 색깔이 있는 상태.
		emoticon_press_map = new HashMap<String, Integer>();
		emoticon_press_map.put("1", R.drawable.emotion_icon01_press);
		emoticon_press_map.put("2", R.drawable.emotion_icon04_press);
		emoticon_press_map.put("3", R.drawable.emotion_icon03_press);
		emoticon_press_map.put("4", R.drawable.emotion_icon06_press);
		emoticon_press_map.put("5", R.drawable.emotion_icon05_press);
		emoticon_press_map.put("6", R.drawable.emotion_icon02_press);
	}

}
