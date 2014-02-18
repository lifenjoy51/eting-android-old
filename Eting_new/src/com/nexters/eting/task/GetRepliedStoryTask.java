package com.nexters.eting.task;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.nexters.eting.etc.Const;
import com.nexters.eting.etc.HttpUtil;
import com.nexters.eting.etc.Installation;
import com.nexters.eting.etc.Util;
import com.nexters.eting.obj.Story;
import com.nexters.eting.svc.StoryService;

/**
 * 답변이 달린 메세지를 받아온다.
 * AsyncTask를 상속받았기에 Main쓰레드와 별도로 실행된다.
 * 
 * @author lifenjoy51
 *
 */
public class GetRepliedStoryTask extends AsyncTask<Object, String, String> {
	
	private Context context;

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {

			this.context = (Context) params[0];	//파라미터 두번째값 context
			String phoneId = Installation.id(context);	//기기 고유값
			
			StoryService storySvc = new StoryService(context);
			List<Story> storyList = storySvc.getMyStoryList();
			StringBuffer sb=  new StringBuffer(); 
			for(Story story : storyList){
				String replyId = story.getReply_id();
				if(Util.isEmpty(replyId)){
					sb.append(story.getStory_id());
					sb.append(",");
				}
			}
			
			String storyIdList = "";
			if(sb.lastIndexOf(",") > 0){
				storyIdList = sb.substring(0, sb.lastIndexOf(","));
			}
			
			System.out.println(storyIdList);
			
			String param = "phone_id=" + phoneId +"&story_list=" +storyIdList;	//서버에 전송할 파라미터 조립
			String urlStr = Const.serverContext+"/getReplyList";
			
			String response = HttpUtil.doPost(urlStr, param);	//Http전송 
			System.out.println(this.getClass().getName() + " = " + response);
			return response;
	}	
	
	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {
		//System.out.println(result);
		
		try {
			JSONObject json = new JSONObject(result);

			//답글정보를 저장한다.
			if (!json.isNull("ReplyList")) {				
				StoryService svc = new StoryService(context);
				JSONArray stampedStoryList = json.getJSONArray("ReplyList");
				for(int i=0; i<stampedStoryList.length(); i++){
					JSONObject repliedStory = stampedStoryList.getJSONObject(i);
					String story_id = repliedStory.getString("story_id");
					String emoticon_list = repliedStory.getString("emoticon_list");
					String reply_content = repliedStory.getString("reply_content");
					String reply_id = repliedStory.getString("reply_id");
					
					svc.updStoryReply(story_id, emoticon_list, reply_content, reply_id);
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			
		}
		
	}

}
