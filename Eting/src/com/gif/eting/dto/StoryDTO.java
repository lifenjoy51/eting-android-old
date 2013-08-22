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
