package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gif.eting.db.InboxDBHelper;
import com.gif.eting.obj.Story;

/**
 * 받은이야기에 대한 정보를 읽고/쓰는 객체. DataAccessObject.
 * 
 * @author lifenjoy51
 * 
 */
public class InboxDAO {

	/**
	 * Database fields
	 */
	private SQLiteDatabase database;
	private InboxDBHelper dbHelper;
	private String[] allColumns = { InboxDBHelper.COL_IDX,
			InboxDBHelper.COL_CONTENT, InboxDBHelper.COL_STORY_DATE,
			InboxDBHelper.COL_STORY_TIME };

	/**
	 * 생성할때 dbHelper 초기화
	 * 
	 * @param context
	 */
	public InboxDAO(Context context) {
		dbHelper = new InboxDBHelper(context);
	}

	/**
	 * Story 리스팅
	 * 
	 * @return
	 */
	public List<Story> getStoryList() {
		database = dbHelper.getReadableDatabase();

		List<Story> storyList = new ArrayList<Story>();
		Cursor cursor = database.query(InboxDBHelper.TABLE_INBOX, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Story story = getStory(cursor);
			storyList.add(story);
			cursor.moveToNext();
		}

		cursor.close();
		database.close();
		return storyList;
	}

	/**
	 * Story 한개 가져오기
	 * 
	 * @param story
	 * @return
	 */
	public Story getStoryInfo(String idx) {
		database = dbHelper.getReadableDatabase();

		Cursor cur = database.query(InboxDBHelper.TABLE_INBOX, allColumns,
				InboxDBHelper.COL_IDX + " = " + idx, null, null, null, null);
		cur.moveToFirst(); // 커서 처음으로
		Story returnedStory = getStory(cur); // 반환할 객체

		cur.close();
		database.close();
		return returnedStory;
	}

	/**
	 * 받은이야기 개수 가져오기
	 * 
	 * @return
	 */
	public Integer getStoryCnt() {
		database = dbHelper.getReadableDatabase();

		Cursor cur = database.rawQuery("SELECT COUNT(*) FROM "
				+ InboxDBHelper.TABLE_INBOX, null);
		cur.moveToFirst(); // 커서 처음으로
		Integer count = cur.getInt(0);

		cur.close();
		database.close();
		return count;
	}

	/**
	 * Story 입력
	 * 
	 * @param story
	 * @return
	 */
	public Long insStory(Story story) {
		database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(InboxDBHelper.COL_IDX, story.getStory_id());
		values.put(InboxDBHelper.COL_CONTENT, story.getStory_content());
		values.put(InboxDBHelper.COL_STORY_DATE, story.getStory_date());
		values.put(InboxDBHelper.COL_STORY_TIME, story.getStory_time());
		long insertedId = database.insert(InboxDBHelper.TABLE_INBOX, null,
				values);

		database.close();
		return insertedId;
	}

	/**
	 * Story 수정
	 * 
	 * @param story
	 * @return
	 */
	public Integer updStory(Story story) {
		database = dbHelper.getWritableDatabase();

		String idx = story.getStory_id();
		ContentValues values = new ContentValues();
		values.put(InboxDBHelper.COL_CONTENT, story.getStory_content());
		values.put(InboxDBHelper.COL_STORY_DATE, story.getStory_date());

		int rtn = database.update(InboxDBHelper.TABLE_INBOX, values,
				InboxDBHelper.COL_IDX + " = " + idx, null);

		database.close();
		return rtn;
	}

	/**
	 * Story 삭제
	 * 
	 * @param story
	 * @return
	 */
	public Integer delStory(Story story) {
		database = dbHelper.getWritableDatabase();

		String idx = story.getStory_id();

		int rtn = database.delete(InboxDBHelper.TABLE_INBOX,
				InboxDBHelper.COL_IDX + " = " + idx, null);

		database.close();
		return rtn;
	}

	/**
	 * 커서에서 DTO받아오기
	 * 
	 * @param cursor
	 * @return
	 */
	private Story getStory(Cursor cursor) {
		Story story = new Story(); // 객체 초기화
		story.setStory_id(cursor.getString(0));
		story.setStory_content(cursor.getString(1));
		story.setStory_date(cursor.getString(2));
		story.setStory_time(cursor.getString(3));
		return story;
	}
}
