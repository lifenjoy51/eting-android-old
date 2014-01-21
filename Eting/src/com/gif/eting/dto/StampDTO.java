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
	 * 스탬프 보낸사람
	 */
	private String comment;

	/**
	 * 코멘트 아이디
	 */
	private String commentId;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	@Override
	public String toString() {
		return this.stamp_name;
	}

}
