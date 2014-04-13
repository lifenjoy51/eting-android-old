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
		emoticon_list.add("1");	//좋아요
		emoticon_list.add("2");	//힘내요
		emoticon_list.add("3");	//싫어요
		emoticon_list.add("4");	//나빠요
		//emoticon_list.add("5");	//기운내요 - 힘내요
		emoticon_list.add("6");	//잘될거예요

		// 이모티콘 눌리지 않은 모습. 회색.
		emoticon_map = new HashMap<String, Integer>();
		emoticon_map.put("1", R.drawable.emoticon_01_press);
		emoticon_map.put("2", R.drawable.emoticon_05_press);
		emoticon_map.put("3", R.drawable.emoticon_03_press);
		emoticon_map.put("4", R.drawable.emoticon_04_press);
		//emoticon_map.put("5", R.drawable.emoticon_05_press);
		emoticon_map.put("6", R.drawable.emoticon_02_press);

		// 이모티콘 눌렀을때 모습. 색깔이 있는 상태.
		emoticon_press_map = new HashMap<String, Integer>();
		emoticon_press_map.put("1", R.drawable.emoticon_01);
		emoticon_press_map.put("2", R.drawable.emoticon_05);
		emoticon_press_map.put("3", R.drawable.emoticon_03);
		emoticon_press_map.put("4", R.drawable.emoticon_04);
		emoticon_press_map.put("5", R.drawable.emoticon_05);
		emoticon_press_map.put("6", R.drawable.emoticon_02);
	}

}
