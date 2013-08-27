package com.gif.eting.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 스탬프를 저장할 Sqlite DB 테이블
 * 
 * @author lifenjoy51
 * 
 */
public class StampDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_STAMP_MASTER = "stamp"; // TABLE이름

	/**
	 * 컬럼정보 
	 * COL_IDX 스탬프ID 
	 * COL_NAME 스탬프이름 
	 * COL_URL 스탬프URL
	 */
	public static final String COL_IDX = "stamp_idx";
	public static final String COL_NAME= "stamp_name";
	public static final String COL_TYPE = "stamp_type";
	public static final String COL_ORDER = "stamp_order";
	public static final String COL_URL = "stamp_url";

	private static final String DATABASE_NAME = "eting_stamp.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 *  TABLE 생성문
	 */
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_STAMP_MASTER + "(" 
			+ COL_IDX + " integer primary key, "	//[0]
			+ COL_NAME + " text not null, " 	//[1]
			+ COL_TYPE + " text not null, "	//[2]
			+ COL_ORDER + " integer, "	//[3]
			+ COL_URL + " text "	//[4]
			+ ");";

	/**
	 *  Constructor
	 * @param context
	 */
	public StampDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE); // TABLE생성
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(StampDBHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");

		// TODO StampDBHelper 버젼이 다를때 임시로 처리. 추후 변경 필요.
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAMP_MASTER);
		onCreate(db);
	}

}
