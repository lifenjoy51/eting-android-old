package com.gif.eting.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SettingDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_SETTING = "setting"; // TABLE이름

	// 컬럼정보
	public static final String COL_KEY = "key"; // 받아온 이야기의 고유번호
	public static final String COL_VALUE = "value"; // 받아온 이야기 내용

	private static final String DATABASE_NAME = "eting_setting.db";
	private static final int DATABASE_VERSION = 1;

	// TABLE 생성문
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_SETTING
			+ "(" + COL_KEY + " text, " + COL_VALUE + " text " + ");";

	// Constructor
	public SettingDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE); // TABLE생성
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SettingDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		// TODO 버젼이 다를때 임시로 처리. 추후 변경 필요.
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
		onCreate(db);
	}

}
