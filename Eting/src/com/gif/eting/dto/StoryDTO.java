package com.gif.eting.dto;

/**
 * 이야기 전송용 객체
 * 
 * @author lifenjoy51
 * 
 */
public class StoryDTO {
	/**
	 * 고유번호
	 */
	private long idx;

	/**
	 * 이야기 내용
	 */
	private String content;

	/**
	 * 이야기 작성일자
	 */
	private String story_date;

	/**
	 * 이야기 작성시간
	 */
	private String story_time;

	/**
	 * 스탬프여부
	 * 기본은 없고 있으면 Y를 넣는다.
	 */
	private String stamp_yn;

	public String getStamp_yn() {
		return stamp_yn;
	}

	public void setStamp_yn(String stamp_yn) {
		this.stamp_yn = stamp_yn;
	}

	public String getStory_time() {
		return story_time;
	}

	public void setStory_time(String story_time) {
		this.story_time = story_time;
	}

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStory_date() {
		return story_date;
	}

	public void setStory_date(String story_date) {
		this.story_date = story_date;
	}

}
