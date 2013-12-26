package com.gif.eting.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 알림 메세지값을 저장할 Sqlite DB 테이블
 * 
 * @author lifenjoy51
 * 
 */
public class AdminMsgDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_SETTING = "admin_msg"; // TABLE이름

	/**
	 * 컬럼정보 
	 * COL_MSG_ID 메세지 ID
	 * COL_MSG_CONTENT 메세지 내용
	 */
	public static final String COL_MSG_ID = "msg_id"; // 메세지 ID
	public static final String COL_MSG_CONTENT = "msg_content"; // 메세지 내용

	private static final String DATABASE_NAME = "eting_admin_msg.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 *  TABLE 생성문
	 */
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_SETTING
			+ "(" + COL_MSG_ID + " integer, " + COL_MSG_CONTENT + " text " + ");";

	/**
	 *  Constructor
	 */
	public AdminMsgDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE); // TABLE생성
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(AdminMsgDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		// TODO AdminMsgDBHelper 버젼이 다를때 임시로 처리. 추후 변경 필요.
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
		onCreate(db);
	}

}
