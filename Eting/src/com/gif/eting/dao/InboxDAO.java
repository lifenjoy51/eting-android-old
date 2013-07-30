package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gif.eting.db.InboxDBHelper;
import com.gif.eting.dto.StoryDTO;

public class InboxDAO {

	// Database fields
	private SQLiteDatabase database;
	private InboxDBHelper dbHelper;
	private String[] allColumns = { InboxDBHelper.COL_IDX,
			InboxDBHelper.COL_CONTENT, InboxDBHelper.COL_STORY_DATE };

	// 생성할때 dbHelper 초기화
	public InboxDAO(Context context) {
		dbHelper = new InboxDBHelper(context);
	}

	// 쓰기전에 open해주고
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	// 다쓰면 close한다
	public void close() {
		dbHelper.close();
	}

	// Story 리스팅
	public List<StoryDTO> getStoryList() {
		List<StoryDTO> storyList = new ArrayList<StoryDTO>();

		Cursor cursor = database.query(InboxDBHelper.TABLE_INBOX,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			StoryDTO story = getStoryDTO(cursor);
			storyList.add(story);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		Log.i("inbox list",storyList.toString());
		
		return storyList;
	}

	// Story 한개 가져오기
	public StoryDTO getStoryInfo(StoryDTO story) {
		long idx = story.getIdx();
		Cursor cur = database.query(InboxDBHelper.TABLE_INBOX,
				allColumns, InboxDBHelper.COL_IDX + " = " + idx, null, null,
				null, null);
		cur.moveToFirst(); // 커서 처음으로

		StoryDTO returnedStory = getStoryDTO(cur); // 반환할 객체
		cur.close();
		
		Log.i("inbox info",returnedStory.toString());

		return returnedStory;
	}

	// Story 입력
	public Long insStory(StoryDTO story) {
		ContentValues values = new ContentValues();
		values.put(InboxDBHelper.COL_CONTENT, story.getContent());
		values.put(InboxDBHelper.COL_STORY_DATE, story.getStory_date());
		long insertedId = database.insert(InboxDBHelper.TABLE_INBOX,
				null, values);
		Log.i("inbox is inserted",String.valueOf(insertedId));

		return insertedId;
	}

	// Story 수정
	public Integer updStory(StoryDTO story) {
		long idx = story.getIdx();
		ContentValues values = new ContentValues();
		values.put(InboxDBHelper.COL_CONTENT, story.getContent());
		values.put(InboxDBHelper.COL_STORY_DATE, story.getStory_date());
		int rtn = database.update(InboxDBHelper.TABLE_INBOX, values,
				InboxDBHelper.COL_IDX + " = " + idx, null);
		Log.i("inbox is updated",String.valueOf(rtn));


		return rtn;
	}

	// Story 삭제
	public Integer delStory(StoryDTO story) {
		long idx = story.getIdx();
		int rtn = database.delete(InboxDBHelper.TABLE_INBOX,
				InboxDBHelper.COL_IDX + " = " + idx, null);
		Log.i("inbox is deleted",String.valueOf(rtn));
		return rtn;
	}

	// 커서에서 DTO받아오기
	private StoryDTO getStoryDTO(Cursor cursor) {
		StoryDTO story = new StoryDTO(); // 객체 초기화
		story.setIdx(cursor.getLong(0));
		story.setContent(cursor.getString(1));
		story.setStory_date(cursor.getString(2));
		return story;
	}
}
