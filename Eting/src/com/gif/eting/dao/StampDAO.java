package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gif.eting.db.StampDBHelper;
import com.gif.eting.dto.StampDTO;

/**
 * 스탬프에 대한 정보를 읽고/쓰는 객체. DataAccessObject. 
 *
 * @author lifenjoy51
 *
 */
public class StampDAO {

	/**
	 *  Database fields
	 */
	private SQLiteDatabase database;
	private StampDBHelper dbHelper;
	private String[] allColumns = { StampDBHelper.COL_IDX,
			StampDBHelper.COL_NAME, StampDBHelper.COL_TYPE, StampDBHelper.COL_ORDER, StampDBHelper.COL_URL };

	/**
	 * 생성할때 dbHelper 초기화
	 * @param context
	 */
	public StampDAO(Context context) {
		dbHelper = new StampDBHelper(context);
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
	 *  내 스탬프 목록 조회
	 * @return
	 */
	public List<StampDTO> getStampList() {
		List<StampDTO> stampList = new ArrayList<StampDTO>();

		Cursor cursor = database.query(StampDBHelper.TABLE_STAMP_MASTER,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			StampDTO stamp = getStampDTO(cursor);
			stampList.add(stamp);
			cursor.moveToNext();
		}
		
		// Make sure to close the cursor
		cursor.close();

		//확인용 로그
		for (StampDTO stamp : stampList) {
			Log.i("my stamp list",
					stamp.getStamp_id() + stamp.getStamp_name() + stamp.getStamp_url());
		}
		
		return stampList;
	}

	/**
	 *  Stamp 한개 가져오기
	 * @param idx
	 * @return
	 */
	public StampDTO getStampInfo(String idx) {
		Cursor cur = database.query(StampDBHelper.TABLE_STAMP_MASTER,
				allColumns, StampDBHelper.COL_IDX + " = " + idx, null, null,
				null, null);
		cur.moveToFirst(); // 커서 처음으로

		StampDTO returnedStamp = getStampDTO(cur); // 반환할 객체
		cur.close();
		
		Log.i("stamp info",returnedStamp.toString());

		return returnedStamp;
	}

	/**
	 *  Stamp 입력
	 *  
	 * @param stamp
	 * @return
	 */
	public Long insStamp(StampDTO stamp) {
		ContentValues values = new ContentValues();
		values.put(StampDBHelper.COL_IDX, stamp.getStamp_id());
		values.put(StampDBHelper.COL_NAME, stamp.getStamp_name());
		values.put(StampDBHelper.COL_TYPE, stamp.getStamp_type());
		values.put(StampDBHelper.COL_ORDER, stamp.getStamp_order());
		values.put(StampDBHelper.COL_URL, stamp.getStamp_url());
		long insertedId = database.insert(StampDBHelper.TABLE_STAMP_MASTER,
				null, values);
		Log.i("stamp is inserted",String.valueOf(insertedId));
		return insertedId;
	}

	/**
	 *  Stamp 수정
	 *  
	 * @param stamp
	 * @return
	 */
	public Integer updStamp(StampDTO stamp) {
		String idx = stamp.getStamp_id();
		ContentValues values = new ContentValues();
		values.put(StampDBHelper.COL_NAME, stamp.getStamp_name());
		values.put(StampDBHelper.COL_TYPE, stamp.getStamp_type());
		values.put(StampDBHelper.COL_ORDER, stamp.getStamp_order());
		values.put(StampDBHelper.COL_URL, stamp.getStamp_url());
		int rtn = database.update(StampDBHelper.TABLE_STAMP_MASTER, values,
				StampDBHelper.COL_IDX + " = " + idx, null);
		Log.i("stamp is updated",String.valueOf(rtn));

		return rtn;
	}

	/**
	 *  Stamp 삭제
	 *  
	 * @param stamp
	 * @return
	 */
	public Integer delStamp(StampDTO stamp) {
		String idx = stamp.getStamp_id();
		int rtn = database.delete(StampDBHelper.TABLE_STAMP_MASTER,
				StampDBHelper.COL_IDX + " = " + idx, null);
		Log.i("stamp is deleted",String.valueOf(rtn));
		return rtn;
	}
	
	/**
	 * 스탬프ID 최대값을 가져온다.
	 * @return
	 */
	public String getMaxStampId(){
		String query = "SELECT MAX("+StampDBHelper.COL_IDX+") FROM "+StampDBHelper.TABLE_STAMP_MASTER;
		Cursor cur = database.rawQuery(query, null);
		cur.moveToFirst(); // 커서 처음으로
		
		if(!cur.isNull(0)){
			String maxStampId = cur.getString(0);
			return maxStampId;
		}else{
			return null;
		}
	}

	/**
	 *  커서에서 DTO받아오기
	 *  
	 * @param cursor
	 * @return
	 */
	private StampDTO getStampDTO(Cursor cursor) {
		StampDTO stamp = new StampDTO(); // 객체 초기화
		stamp.setStamp_id(cursor.getString(0));
		stamp.setStamp_name(cursor.getString(1));
		stamp.setStamp_type(cursor.getString(2));
		stamp.setStamp_order(cursor.getString(3));
		stamp.setStamp_url(cursor.getString(4));
		return stamp;
	}	
	
}
