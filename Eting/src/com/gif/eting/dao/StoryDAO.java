package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gif.eting.db.StoryDBHelper;
import com.gif.eting.dto.StoryDTO;

/**
 * 이야기에 대한 정보를 읽고/쓰는 객체. DataAccessObject. 
 *
 * @author lifenjoy51
 *
 */
public class StoryDAO {

	/**
	 *  Database fields
	 */
	private SQLiteDatabase database;
	private StoryDBHelper dbHelper;
	private String[] allColumns = { StoryDBHelper.COL_IDX,
			StoryDBHelper.COL_CONTENT, StoryDBHelper.COL_STORY_DATE,
			StoryDBHelper.COL_STORY_TIME, StoryDBHelper.COL_STAMP_YN };

	/**
	 * 생성할때 dbHelper 초기화
	 * @param context
	 */
	public StoryDAO(Context context) {
		dbHelper = new StoryDBHelper(context);
	}

	/**
	 * 쓰기전에 open해주고
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 *  다쓰면 close한다
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 *  내 이야기 목록 조회
	 * @return
	 */
	public List<StoryDTO> getStoryList() {
		List<StoryDTO> storyList = new ArrayList<StoryDTO>();

		Cursor cursor = database.query(StoryDBHelper.TABLE_STORY_MASTER,
				allColumns, null, null, null, null, StoryDBHelper.COL_STORY_DATE+" DESC, " + StoryDBHelper.COL_STORY_TIME+" DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			StoryDTO story = getStoryDTO(cursor);
			storyList.add(story);
			cursor.moveToNext();
		}
		
		// Make sure to close the cursor
		cursor.close();

		//확인용 로그
		for (StoryDTO story : storyList) {
			Log.i("my story list",
					story.getIdx() + story.getContent() + story.getStory_date());
		}
		
		return storyList;
	}

	/**
	 *  Story 한개 가져오기
	 * @param idx
	 * @return
	 */
	public StoryDTO getStoryInfo(String idx) {
		Cursor cur = database.query(StoryDBHelper.TABLE_STORY_MASTER,
				allColumns, StoryDBHelper.COL_IDX + " = " + idx, null, null,
				null, null);
		cur.moveToFirst(); // 커서 처음으로

		StoryDTO returnedStory = getStoryDTO(cur); // 반환할 객체
		cur.close();
		
		Log.i("story info",returnedStory.toString());

		return returnedStory;
	}

	/**
	 *  Story 입력
	 *  
	 * @param story
	 * @return
	 */
	public Long insStory(StoryDTO story) {
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_IDX, story.getIdx());
		values.put(StoryDBHelper.COL_CONTENT, story.getContent());
		values.put(StoryDBHelper.COL_STORY_DATE, story.getStory_date());
		values.put(StoryDBHelper.COL_STORY_TIME, story.getStory_time());
		long insertedId = database.insert(StoryDBHelper.TABLE_STORY_MASTER,
				null, values);
		Log.i("story is inserted",String.valueOf(insertedId));
		return insertedId;
	}

	/**
	 *  Story 수정
	 *  
	 * @param story
	 * @return
	 */
	public Integer updStory(StoryDTO story) {
		long idx = story.getIdx();
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_CONTENT, story.getContent());
		values.put(StoryDBHelper.COL_STORY_DATE, story.getStory_date());
		values.put(StoryDBHelper.COL_STORY_TIME, story.getStory_time());
		int rtn = database.update(StoryDBHelper.TABLE_STORY_MASTER, values,
				StoryDBHelper.COL_IDX + " = " + idx, null);
		Log.i("story is updated",String.valueOf(rtn));

		return rtn;
	}
	
	/**
	 *  스탬프여부 수정
	 *  
	 * @param story
	 * @return
	 */
	public Integer updStoryStampYn(String storyId) {
		long idx = Long.parseLong(storyId);
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_STAMP_YN, "Y");
		int rtn = database.update(StoryDBHelper.TABLE_STORY_MASTER, values,
				StoryDBHelper.COL_IDX + " = " + idx, null);
		Log.i("updStoryStampYn is updated",String.valueOf(rtn));

		return rtn;
	}

	/**
	 *  Story 삭제
	 *  
	 * @param story
	 * @return
	 */
	public Integer delStory(StoryDTO story) {
		long idx = story.getIdx();
		int rtn = database.delete(StoryDBHelper.TABLE_STORY_MASTER,
				StoryDBHelper.COL_IDX + " = " + idx, null);
		Log.i("story is deleted",String.valueOf(rtn));
		return rtn;
	}

	/**
	 *  커서에서 DTO받아오기
	 *  
	 * @param cursor
	 * @return
	 */
	private StoryDTO getStoryDTO(Cursor cursor) {
		StoryDTO story = new StoryDTO(); // 객체 초기화
		story.setIdx(cursor.getLong(0));
		story.setContent(cursor.getString(1));
		story.setStory_date(cursor.getString(2));
		story.setStory_time(cursor.getString(3));
		story.setStamp_yn(cursor.getString(4));
		return story;
	}
	
	/**
	 * 이팅 개수 가져오기
	 * @return
	 */
	public Integer getStoryCnt(){
		Cursor cur = database.rawQuery("SELECT COUNT(*) FROM "+StoryDBHelper.TABLE_STORY_MASTER, null);	
		cur.moveToFirst(); // 커서 처음으로
		Integer count = cur.getInt(0);
		cur.close();
		
		return count;
	}
}
