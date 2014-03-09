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
	 * COL_STORY_ID 고유번호
	 * COL_STORY_CONTENT 이야기 내용
	 * COL_STORY_DATE 이야기 작성일자
	 */
	public static final String COL_STORY_ID = "idx";
	public static final String COL_STORY_CONTENT = "content";
	public static final String COL_STORY_DATE = "story_date";
	public static final String COL_STORY_TIME = "story_time";
	public static final String COL_REPLY_YN= "reply_yn";
	public static final String COL_EMOTICON_LIST= "emoticon_list";
	public static final String COL_REPLY_CONTENT= "reply_content";
	public static final String COL_REPLY_ID= "reply_id";

	private static final String DATABASE_NAME = "eting_mystory.db";
	private static final int DATABASE_VERSION = 4;

	/**
	 *  TABLE 생성문
	 */
	private static final String DATABASE_CREATE =

			new StringBuilder().append("CREATE TABLE ")
			.append(TABLE_STORY_MASTER)
			.append("(")
				.append(COL_STORY_ID)
				.append( " integer primary key, ")
				.append(COL_STORY_CONTENT)
				.append( " text, ")
				.append(COL_STORY_DATE)
				.append( " text, ")
				.append(COL_STORY_TIME)
				.append( " text, ")

				.append(COL_REPLY_YN)
				.append( " text, ")
				.append(COL_EMOTICON_LIST)
				.append( " text, ")
				.append(COL_REPLY_CONTENT)
				.append( " text, ")
				.append(COL_REPLY_ID)
				.append( " integer ")
			.append(");")
			.toString();

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
		/**
		 *  TABLE 생성문
		 */
		final String TABLE_STORY_MASTER_TEMP = "mystory_temp"; // TABLE이름
		final String CREATE_TEMP_TABLE =

				new StringBuilder().append("CREATE TABLE ")
				.append(TABLE_STORY_MASTER_TEMP)
				.append("(")
					.append(COL_STORY_ID)
					.append( " integer primary key, ")
					.append(COL_STORY_CONTENT)
					.append( " text, ")
					.append(COL_STORY_DATE)
					.append( " text, ")
					.append(COL_STORY_TIME)
					.append( " text, ")

					.append(COL_REPLY_YN)
					.append( " text, ")
					.append(COL_EMOTICON_LIST)
					.append( " text, ")
					.append(COL_REPLY_CONTENT)
					.append( " text, ")
					.append(COL_REPLY_ID)
					.append( " integer ")
				.append(");")
				.toString();

		final String OLD_TO_TEMP =
				new StringBuilder().append("INSERT INTO ")
				.append(TABLE_STORY_MASTER_TEMP)
				.append("(")
					.append(COL_STORY_ID)
					.append( ", ")
					.append(COL_STORY_CONTENT)
					.append( ", ")
					.append(COL_STORY_DATE)
					.append( ", ")
					.append(COL_STORY_TIME)
					.append( ", ")
					.append(COL_REPLY_YN)
					.append( ", ")
					.append(COL_EMOTICON_LIST)
					.append( ", ")
					.append(COL_REPLY_CONTENT)
				.append(") ")
				.append(" SELECT idx, content, story_date, story_time, stamp_yn, stamps, comment ")
				.append(" FROM ")
				.append(TABLE_STORY_MASTER)
				.toString();


		final String TEMP_TO_NEW =

				new StringBuilder().append("INSERT INTO ")
				.append(TABLE_STORY_MASTER)
				.append("(")
					.append(COL_STORY_ID)
					.append( ", ")
					.append(COL_STORY_CONTENT)
					.append( ", ")
					.append(COL_STORY_DATE)
					.append( ", ")
					.append(COL_STORY_TIME)
					.append( ", ")
					.append(COL_REPLY_YN)
					.append( ", ")
					.append(COL_EMOTICON_LIST)
					.append( ", ")
					.append(COL_REPLY_CONTENT)
				.append(") ")
					.append(" SELECT ")
					.append(COL_STORY_ID)
					.append( ", ")
					.append(COL_STORY_CONTENT)
					.append( ", ")
					.append(COL_STORY_DATE)
					.append( ", ")
					.append(COL_STORY_TIME)
					.append( ", ")
					.append(COL_REPLY_YN)
					.append( ", ")
					.append(COL_EMOTICON_LIST)
					.append( ", ")
					.append(COL_REPLY_CONTENT)
				.append( " FROM ")
				.append(TABLE_STORY_MASTER_TEMP)
				.toString();

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY_MASTER_TEMP);
		db.execSQL(CREATE_TEMP_TABLE);
		db.execSQL(OLD_TO_TEMP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY_MASTER);
		db.execSQL(DATABASE_CREATE);
		db.execSQL(TEMP_TO_NEW);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY_MASTER_TEMP);

	}

}
