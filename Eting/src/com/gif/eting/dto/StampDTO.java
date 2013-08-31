package com.gif.eting.dto;

/**
 * 스탬프 전송용 객체
 * 
 * @author lifenjoy51
 * 
 */
public class StampDTO {

	public StampDTO() {
	}

	/**
	 * 이야기 고유번호
	 */
	private String story_id;

	/**
	 * 스탬프 고유번호
	 */
	private String stamp_id;

	/**
	 * 스탬프 이름
	 */
	private String stamp_name;

	/**
	 * 스탬프 종류
	 */
	private String stamp_type;

	/**
	 * 스탬프 순서
	 */
	private String stamp_order;

	/**
	 * 스탬프 이미지 url (현재 사용 안함)
	 */
	private String stamp_url;

	/**
	 * 스탬프 보낸사람
	 */
	private String sender;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getStamp_type() {
		return stamp_type;
	}

	public void setStamp_type(String stamp_type) {
		this.stamp_type = stamp_type;
	}

	public String getStamp_order() {
		return stamp_order;
	}

	public void setStamp_order(String stamp_order) {
		this.stamp_order = stamp_order;
	}

	public String getStory_id() {
		return story_id;
	}

	public void setStory_id(String story_id) {
		this.story_id = story_id;
	}

	public String getStamp_id() {
		return stamp_id;
	}

	public void setStamp_id(String stamp_id) {
		this.stamp_id = stamp_id;
	}

	public String getStamp_name() {
		return stamp_name;
	}

	public void setStamp_name(String stamp_name) {
		this.stamp_name = stamp_name;
	}

	public String getStamp_url() {
		return stamp_url;
	}

	public void setStamp_url(String stamp_url) {
		this.stamp_url = stamp_url;
	}

	@Override
	public String toString() {
		return this.stamp_name;
	}

}
