package com.gif.eting.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StoryDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_STORY_MASTER = "mystory";	//TABLE이름
	
	//컬럼정보
	public static final String COL_IDX = "idx";
	public static final String COL_CONTENT = "content";
	public static final String COL_STORY_DATE = "story_date";

	private static final String DATABASE_NAME = "eting_mystory.db";
	private static final int DATABASE_VERSION = 1;

	  //TABLE 생성문
	  private static final String DATABASE_CREATE = 
			  "CREATE TABLE " + TABLE_STORY_MASTER + "(" 
					  + COL_IDX				+ " integer primary key, " 
					  + COL_CONTENT		+ " text not null, " 
					  + COL_STORY_DATE	+ " text " 
					  + ");";

	// Constructor
	public StoryDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE); //TABLE생성
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(StoryDBHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");

		// TODO 버젼이 다를때 임시로 처리. 추후 변경 필요.
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY_MASTER);
		onCreate(db);
	}

}

