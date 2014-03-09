package com.gif.eting.obj;

import com.gif.eting.etc.Util;

public class Device {
	private String device_id;
	private String device_uuid;
	private String reg_date;
	private String reg_id;
	private String os;
	private String junk_yn;

	// ###############################
	// Getter, Setter
	// ###############################
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice_uuid() {
		return device_uuid;
	}

	public void setDevice_uuid(String device_uuid) {
		this.device_uuid = device_uuid;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getReg_id() {
		return reg_id;
	}

	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}

	public String getOs() {
		// 기본값을 A (안드로이드)로 셋팅
		if (Util.isEmpty(os))
			os = "A";
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getJunk_yn() {
		return junk_yn;
	}

	public void setJunk_yn(String junk_yn) {
		this.junk_yn = junk_yn;
	}

}
