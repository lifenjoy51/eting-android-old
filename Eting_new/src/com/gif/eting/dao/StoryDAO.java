package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gif.eting.db.StoryDBHelper;
import com.gif.eting.obj.Story;

/**
 * 이야기에 대한 정보를 읽고/쓰는 객체. DataAccessObject.
 * 
 * @author lifenjoy51
 * 
 */
public class StoryDAO {

	/**
	 * Database fields
	 */
	private SQLiteDatabase database;
	private StoryDBHelper dbHelper;
	private String[] allColumns = { StoryDBHelper.COL_STORY_ID,
			StoryDBHelper.COL_STORY_CONTENT, StoryDBHelper.COL_STORY_DATE,
			StoryDBHelper.COL_STORY_TIME, StoryDBHelper.COL_REPLY_YN,
			StoryDBHelper.COL_EMOTICON_LIST, StoryDBHelper.COL_REPLY_CONTENT,
			StoryDBHelper.COL_REPLY_ID };

	/**
	 * 생성할때 dbHelper 초기화
	 * 
	 * @param context
	 */
	public StoryDAO(Context context) {
		dbHelper = new StoryDBHelper(context);
	}

	/**
	 * 내 이야기 목록 조회
	 * 
	 * @return
	 */
	public List<Story> getStoryList() {
		database = dbHelper.getReadableDatabase();
		
		List<Story> storyList = new ArrayList<Story>();

		Cursor cursor = database.query(StoryDBHelper.TABLE_STORY_MASTER,
				allColumns, null, null, null, null,
				StoryDBHelper.COL_STORY_DATE + " DESC, "
						+ StoryDBHelper.COL_STORY_TIME + " DESC");

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
	 * @param idx
	 * @return
	 */
	public Story getStoryInfo(String idx) {
		database = dbHelper.getReadableDatabase();
		
		Cursor cur = database.query(StoryDBHelper.TABLE_STORY_MASTER,
				allColumns, StoryDBHelper.COL_STORY_ID + " = " + idx, null,
				null, null, null);
		
		cur.moveToFirst(); // 커서 처음으로
		Story returnedStory = getStory(cur); // 반환할 객체
		
		cur.close();
		database.close();
		return returnedStory;
	}

	/**
	 * 이팅 개수 가져오기
	 * 
	 * @return
	 */
	public Integer getStoryCnt() {
		database = dbHelper.getReadableDatabase();
		
		Cursor cur = database.rawQuery("SELECT COUNT(*) FROM "
				+ StoryDBHelper.TABLE_STORY_MASTER, null);
		
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
		values.put(StoryDBHelper.COL_STORY_ID, story.getStory_id());
		values.put(StoryDBHelper.COL_STORY_CONTENT, story.getStory_content());
		values.put(StoryDBHelper.COL_STORY_DATE, story.getStory_date());
		values.put(StoryDBHelper.COL_STORY_TIME, story.getStory_time());
		
		long insertedId = database.insert(StoryDBHelper.TABLE_STORY_MASTER,
				null, values);
		
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
		values.put(StoryDBHelper.COL_STORY_CONTENT, story.getStory_content());
		values.put(StoryDBHelper.COL_STORY_DATE, story.getStory_date());
		values.put(StoryDBHelper.COL_STORY_TIME, story.getStory_time());
		
		int rtn = database.update(StoryDBHelper.TABLE_STORY_MASTER, values,
				StoryDBHelper.COL_STORY_ID + " = " + idx, null);
		
		database.close();
		return rtn;
	}

	/**
	 * 답장여부 수정 (목록)
	 * 
	 * @param story
	 * @return
	 */
	public Integer updStoryReplyYn(String storyId) {
		database = dbHelper.getWritableDatabase();
		
		long idx = Long.parseLong(storyId);
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_REPLY_YN, "Y");
		
		int rtn = database.update(StoryDBHelper.TABLE_STORY_MASTER, values,
				StoryDBHelper.COL_STORY_ID + " = " + idx, null);
		
		database.close();
		return rtn;
	}

	/**
	 * 답장 저장하기
	 * 
	 * @param storyId
	 * @param stamps
	 * @param reply
	 * @return
	 */
	public Integer updStoryReply(String storyId, String stamps, String reply,
			String replyId) {
		database = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_REPLY_YN, "Y");
		values.put(StoryDBHelper.COL_EMOTICON_LIST, stamps);
		values.put(StoryDBHelper.COL_REPLY_CONTENT, reply);
		values.put(StoryDBHelper.COL_REPLY_ID, replyId);
		
		int rtn = database.update(StoryDBHelper.TABLE_STORY_MASTER, values,
				StoryDBHelper.COL_STORY_ID + " = " + storyId, null);
		
		database.close();		
		return rtn;
	}

	/**
	 * 답장 읽은 상태로
	 * 
	 * @param storyId
	 * @param stamps
	 * @param reply
	 * @return
	 */
	public Integer updStoryReplyRead(String storyId) {
		database = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_REPLY_YN, "R");
		String[] v = { "Y" };
		int rtn = database.update(StoryDBHelper.TABLE_STORY_MASTER, values,
				StoryDBHelper.COL_REPLY_YN + " = ?   AND "
						+ StoryDBHelper.COL_STORY_ID + " = " + storyId, v);
		
		database.close();
		return rtn;
	}

	/**
	 * 답글 삭제기능
	 * 
	 * @param storyId
	 * @param stamps
	 * @param reply
	 * @return
	 */
	public Integer deleteComment(String storyId) {
		database = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_REPLY_YN, "N");
		values.put(StoryDBHelper.COL_EMOTICON_LIST, "");
		values.put(StoryDBHelper.COL_REPLY_CONTENT, "");
		values.put(StoryDBHelper.COL_REPLY_ID, "");
		
		int rtn = database.update(StoryDBHelper.TABLE_STORY_MASTER, values,
				StoryDBHelper.COL_STORY_ID + " = " + storyId, null);
		
		database.close();
		return rtn;
	}

	/**
	 * Story 삭제
	 * 
	 * @param story
	 * @return
	 */
	public Integer delStory(String storyId) {
		database = dbHelper.getWritableDatabase();
		
		int rtn = database.delete(StoryDBHelper.TABLE_STORY_MASTER,
				StoryDBHelper.COL_STORY_ID + " = " + storyId, null);		
		
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
		if (cursor.getCount() == 0)
			return story;
		story.setStory_id(cursor.getString(0));
		story.setStory_content(cursor.getString(1));
		story.setStory_date(cursor.getString(2));
		story.setStory_time(cursor.getString(3));

		story.setReply_yn(cursor.getString(4));
		story.setEmoticon_list(cursor.getString(5));
		story.setReply_content(cursor.getString(6));
		story.setReply_id(cursor.getString(7));
		return story;
	}
}