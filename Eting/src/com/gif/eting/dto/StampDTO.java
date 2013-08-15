package com.gif.eting.dto;

public class StampDTO {

	public StampDTO() {
	}

	private String story_id;
	private String stamp_id;
	private String stamp_name;
	private String stamp_url;

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
