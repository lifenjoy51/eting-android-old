package com.gif.eting.dto;

/**
 * 설정값 전송용 객체
 * 
 * @author lifenjoy51
 *
 */
public class SettingDTO {
	/**
	 * 키
	 */
	private String key;
	
	/**
	 * 설정값
	 */
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
