package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gif.eting.db.SettingDBHelper;
import com.gif.eting.dto.SettingDTO;

public class SettingDAO {

	// Database fields
	private SQLiteDatabase database;
	private SettingDBHelper dbHelper;
	private String[] allColumns = { SettingDBHelper.COL_KEY,
			SettingDBHelper.COL_VALUE };

	// 생성할때 dbHelper 초기화
	public SettingDAO(Context context) {
		dbHelper = new SettingDBHelper(context);
	}

	// 쓰기전에 open해주고
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	// 다쓰면 close한다
	public void close() {
		dbHelper.close();
	}

	// setting 리스팅
	public List<SettingDTO> getsettingList() {
		List<SettingDTO> settingList = new ArrayList<SettingDTO>();

		Cursor cursor = database.query(SettingDBHelper.TABLE_SETTING,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SettingDTO setting = getSettingDTO(cursor);
			settingList.add(setting);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();

		for (SettingDTO setting : settingList) {
			Log.i("setting setting list", setting.getKey() + setting.getValue());
		}

		return settingList;
	}

	// setting 한개 가져오기
	public SettingDTO getsettingInfo(String key) {
		Cursor cur = database.query(SettingDBHelper.TABLE_SETTING, allColumns,
				SettingDBHelper.COL_KEY + " = " + "'"+key+"'", null, null, null, null);
		cur.moveToFirst(); // 커서 처음으로

		SettingDTO returnedsetting = getSettingDTO(cur); // 반환할 객체
		cur.close();
		
		if(returnedsetting!=null){
			Log.i("setting info", returnedsetting.toString());
		}

		return returnedsetting;
	}

	// setting 입력
	public Long inssetting(SettingDTO setting) {
		ContentValues values = new ContentValues();
		values.put(SettingDBHelper.COL_KEY, setting.getKey());
		values.put(SettingDBHelper.COL_VALUE, setting.getValue());
		long insertedId = database.insert(SettingDBHelper.TABLE_SETTING, null,
				values);
		Log.i("setting is inserted", String.valueOf(insertedId));

		return insertedId;
	}

	// setting 수정
	public Integer updsetting(SettingDTO setting) {
		String key = setting.getKey();
		ContentValues values = new ContentValues();
		values.put(SettingDBHelper.COL_VALUE, setting.getValue());
		int rtn = database.update(SettingDBHelper.TABLE_SETTING, values,
				SettingDBHelper.COL_KEY + " = " + "'"+key+"'", null);
		Log.i("setting is updated", String.valueOf(rtn));

		return rtn;
	}

	// setting 삭제
	public Integer delsetting(String key) {
		int rtn = database.delete(SettingDBHelper.TABLE_SETTING,
				SettingDBHelper.COL_KEY + " = " + "'"+key+"'", null);
		Log.i("setting is deleted", String.valueOf(rtn));
		return rtn;
	}

	// 커서에서 DTO받아오기
	private SettingDTO getSettingDTO(Cursor cursor) {
		if (cursor!=null && cursor.getCount()>0) {
			SettingDTO setting = new SettingDTO(); // 객체 초기화
			setting.setKey(cursor.getString(0));
			setting.setValue(cursor.getString(1));
			return setting;

		} else {
			return null;
		}
	}
}
