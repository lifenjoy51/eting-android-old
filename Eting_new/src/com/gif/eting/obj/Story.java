package com.gif.eting.obj;

import com.gif.eting.etc.Util;

public class Story {
	// 이야기관련
	private String story_id;
	private String story_content;
	private String story_date;
	private String story_time;
	
	// 답장관련
	private String reply_id;
	private String reply_content;
	private String emoticon_list;
	private String reply_yn;
	
	// DB컬럼 등록 안된 변수들
	private String story_write_device_id;
	private String story_length;
	private String story_type;
	private String reply_write_device_id;
	private String reply_date;
	private String reply_time;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Story) {
			Story s = (Story) obj;
			if (s.getStory_content().equals(this.getStory_content())) {
				return true;
			}
		}

		return false;
	}

	// ###############
	// Getter, Setter
	// ###############
	public String getStory_id() {
		return story_id;
	}

	public void setStory_id(String story_id) {
		this.story_id = story_id;
	}

	public String getStory_content() {
		return story_content;
	}

	public void setStory_content(String story_content) {
		this.story_content = story_content;
	}

	public String getStory_date() {
		// 값이 있으면 리턴하고
		if (!Util.isEmpty(story_date))
			return story_date;

		// 아니면 현재날짜를 저장하고 출력한다.
		setStory_date(Util.getDate("-"));
		return story_date;
	}

	public void setStory_date(String story_date) {
		this.story_date = story_date;
	}

	public String getStory_time() {
		// 값이 있으면 리턴하고
		if (!Util.isEmpty(story_time))
			return story_time;

		// 아니면 현재시간을 저장하고 출력한다.
		setStory_time(Util.getTime());
		return story_time;
	}

	public void setStory_time(String story_time) {
		this.story_time = story_time;
	}

	public String getStory_write_device_id() {
		return story_write_device_id;
	}

	public void setStory_write_device_id(String story_write_device_id) {
		this.story_write_device_id = story_write_device_id;
	}

	public String getStory_length() {
		// 값이 있으면 리턴하고
		if (!Util.isEmpty(story_length))
			return story_length;

		// 길이값이 없으면
		// 내용이 있는지 판별한 후에
		if (Util.isEmpty(getStory_content())) {
			return "0";
		} else {
			// 내용 길이를 저장한다.
			int storyLength = getStory_content().length();
			setStory_length(String.valueOf(storyLength));
		}
		return story_length;
	}

	public void setStory_length(String story_length) {
		this.story_length = story_length;
	}

	public String getStory_type() {
		// 값이 있으면 리턴하고
		if (!Util.isEmpty(story_type))
			return story_type;
		return story_type;
	}

	public void setStory_type(String story_type) {
		this.story_type = story_type;
	}
	


	public String getReply_id() {
		return reply_id;
	}

	public void setReply_id(String reply_id) {
		this.reply_id = reply_id;
	}
	
	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	public String getReply_write_device_id() {
		return reply_write_device_id;
	}

	public void setReply_write_device_id(String reply_write_device_id) {
		this.reply_write_device_id = reply_write_device_id;
	}

	public String getEmoticon_list() {
		return emoticon_list;
	}

	public void setEmoticon_list(String emoticon_list) {
		// TODO 버그수정위해
		if(!Util.isEmpty(emoticon_list)){
			emoticon_list = emoticon_list.replace("0", "1");
		}
		this.emoticon_list = emoticon_list;
	}

	public String getReply_date() {
		return reply_date;
	}

	public void setReply_date(String reply_date) {
		this.reply_date = reply_date;
	}

	public String getReply_time() {
		return reply_time;
	}

	public void setReply_time(String reply_time) {
		this.reply_time = reply_time;
	}

	public String getReply_yn() {
		return reply_yn;
	}

	public void setReply_yn(String reply_yn) {
		this.reply_yn = reply_yn;
	}
}
