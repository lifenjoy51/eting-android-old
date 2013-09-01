package com.gif.eting.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 이야기를 저장할 Sqlite DB 테이블
 * 
 * @author lifenjoy51
 * 
 */
public class StoryDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_STORY_MASTER = "mystory"; // TABLE이름

	/**
	 * 컬럼정보 
	 * COL_IDX 고유번호 
	 * COL_CONTENT 이야기 내용 
	 * COL_STORY_DATE 이야기 작성일자
	 */
	public static final String COL_IDX = "idx";
	public static final String COL_CONTENT = "content";
	public static final String COL_STORY_DATE = "story_date";
	public static final String COL_STORY_TIME = "story_time";
	public static final String COL_STAMP_YN= "stamp_yn";

	private static final String DATABASE_NAME = "eting_mystory.db";
	private static final int DATABASE_VERSION = 2;

	/**
	 *  TABLE 생성문
	 */
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_STORY_MASTER + "(" 
			+ COL_IDX + " integer primary key, "
			+ COL_CONTENT + " text not null, " 
			+ COL_STORY_DATE + " text, "
			+ COL_STORY_TIME + " text, "
			+ COL_STAMP_YN + " text "
			+ ");";

	/**
	 *  Constructor
	 * @param context
	 */
	public StoryDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE); // TABLE생성
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(StoryDBHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");

		// TODO StoryDBHelper 버젼이 다를때 임시로 처리. 추후 변경 필요.
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY_MASTER);
		onCreate(db);
	}

}
