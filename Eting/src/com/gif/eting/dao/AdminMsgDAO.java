package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gif.eting.db.AdminMsgDBHelper;
import com.gif.eting.dto.AdminMsgDTO;

/**
 * 메세지에 대한 정보를 읽고/쓰는 객체. DataAccessObject.
 * 
 * @author lifenjoy51
 * 
 */
public class AdminMsgDAO {

	/**
	 * Database fields
	 */
	private SQLiteDatabase database;
	private AdminMsgDBHelper dbHelper;
	private String[] allColumns = { AdminMsgDBHelper.COL_MSG_ID,
			AdminMsgDBHelper.COL_MSG_CONTENT };

	/**
	 * 생성할때 dbHelper 초기화
	 * 
	 * @param context
	 */
	public AdminMsgDAO(Context context) {
		dbHelper = new AdminMsgDBHelper(context);
	}

	/**
	 * 쓰기전에 open해주고
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * 다쓰면 close한다
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * adminMsg 리스팅
	 * 
	 * @return
	 */
	public List<AdminMsgDTO> getAdminMsgList() {
		List<AdminMsgDTO> adminMsgList = new ArrayList<AdminMsgDTO>();

		Cursor cursor = database.query(AdminMsgDBHelper.TABLE_SETTING,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AdminMsgDTO adminMsg = getAdminMsgDTO(cursor);
			adminMsgList.add(adminMsg);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		return adminMsgList;
	}

	/**
	 * adminMsg 한개 가져오기
	 * 
	 * @param msgId
	 * @return
	 */
	public AdminMsgDTO getAdminMsgInfo(String msgId) {
		Cursor cur = database.query(AdminMsgDBHelper.TABLE_SETTING, allColumns,
				AdminMsgDBHelper.COL_MSG_ID + " = " + "'" + msgId + "'", null,
				null, null, null);
		cur.moveToFirst(); // 커서 처음으로

		AdminMsgDTO returnedadminMsg = getAdminMsgDTO(cur); // 반환할 객체
		cur.close();

		if (returnedadminMsg != null) {
			// Log.i("adminMsg info", returnedadminMsg.toString());
		} else {
			return null;
		}

		return returnedadminMsg;
	}

	/**
	 * adminMsg 입력
	 */
	public Long insAdminMsg(AdminMsgDTO adminMsg) {
		ContentValues values = new ContentValues();
		values.put(AdminMsgDBHelper.COL_MSG_ID, adminMsg.getMsgId());
		values.put(AdminMsgDBHelper.COL_MSG_CONTENT, adminMsg.getMsgContent());
		long insertedId = database.insert(AdminMsgDBHelper.TABLE_SETTING, null,
				values);
		// Log.i("adminMsg is inserted", String.valueOf(insertedId));

		return insertedId;
	}

	/**
	 * adminMsg 수정
	 * 
	 * @param adminMsg
	 * @return
	 */
	public Integer updAdminMsg(AdminMsgDTO adminMsg) {
		String msgId = adminMsg.getMsgId();
		ContentValues values = new ContentValues();
		values.put(AdminMsgDBHelper.COL_MSG_CONTENT, adminMsg.getMsgContent());
		int rtn = database.update(AdminMsgDBHelper.TABLE_SETTING, values,
				AdminMsgDBHelper.COL_MSG_ID + " = " + "'" + msgId + "'", null);
		// Log.i("adminMsg is updated", String.valueOf(rtn));

		return rtn;
	}

	/**
	 * adminMsg 삭제
	 * 
	 * @param msgId
	 * @return
	 */
	public Integer delAdminMsg(String msgId) {
		int rtn = database.delete(AdminMsgDBHelper.TABLE_SETTING,
				AdminMsgDBHelper.COL_MSG_ID + " = " + "'" + msgId + "'", null);
		// Log.i("adminMsg is deleted", String.valueOf(rtn));
		return rtn;
	}

	/**
	 * 커서에서 DTO받아오기
	 * 
	 * @param cursor
	 * @return
	 */
	private AdminMsgDTO getAdminMsgDTO(Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {
			AdminMsgDTO adminMsg = new AdminMsgDTO(); // 객체 초기화
			adminMsg.setMsgId(cursor.getString(0));
			adminMsg.setMsgContent(cursor.getString(1));
			return adminMsg;

		} else {
			return null;
		}
	}
}
