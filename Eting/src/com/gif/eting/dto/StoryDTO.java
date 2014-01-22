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
	private String story_id;

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
	 * 스탬프여부 기본은 없고 있으면 Y를 넣는다.
	 */
	private String stamp_yn;

	/**
	 * 받아온 스탬프
	 */
	private String stamps;

	/**
	 * 피드백
	 */
	private String comment;
	
	/**
	 * 피드백 식별번호
	 */
	private String commentId;
	
	

	public String getStory_id() {
		return story_id;
	}

	public void setStory_id(String story_id) {
		this.story_id = story_id;
	}

	public String getStamps() {
		return stamps;
	}

	public void setStamps(String stamps) {
		this.stamps = stamps;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

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

	public String getCommentId() {
		if(commentId == null){
			return "";
		}else{
			return commentId;
		}
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	

}
