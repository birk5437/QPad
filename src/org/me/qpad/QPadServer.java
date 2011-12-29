/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;

/**
 *
 * @author burke
 */
public class QPadServer {

    private String serverUrl = "";
    public QPadServer(String url)
    {
	serverUrl = url;
    }

    public ArrayList<String> getHighScores() throws java.io.IOException, org.json.JSONException {

	ArrayList<String> scoreList = new ArrayList<String>();

	InputStream is;
	String result = "";
	//the year data to send
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("password","qpAdp4zz"));

	//http post
	//try{
		HttpClient httpclient = new DefaultHttpClient();
		String httpUrl = serverUrl + "/get_high_scores.php";
		//HttpPost httppost = new HttpPost("http://74.207.236.215/qpad_server/get_high_scores.php");
		HttpPost httppost = new HttpPost(httpUrl);
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();
		//convert response to string
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();

		result=sb.toString();
	//}catch(Exception e){
	//	scoreList.add(e.getMessage());
		//Log.e("log_tag", "Error converting result "+e.toString());
	//}

	//parse json data
	//try{
		JSONArray jArray = new JSONArray(result);
		for(int i=0;i<jArray.length();i++){
			JSONObject json_data = jArray.getJSONObject(i);
			String listString = json_data.getString("name") + " - " + json_data.getString("score");
			scoreList.add(listString);
			//Log.i("log_tag","id: "+json_data.getInt("id")+
			//       ", name: "+json_data.getString("name")+
			//        ", sex: "+json_data.getInt("sex")+
			//        ", birthyear: "+json_data.getInt("birthyear")
			//);
		}
	//}catch(JSONException e){
	//    scoreList.add(e.getMessage());
		//Log.e("log_tag", "Error parsing data "+e.toString());
//	}

	return scoreList;

    }

    public void addScore(String strName, int scoreToAdd) throws java.io.IOException
    {
	InputStream is;
	String result = "";
	//the year data to send
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("password","qpAdp4zz"));
	//SharedPreferences prefs = getSharedPreferences("qpad_prefs", 0);
	//int scoreToAdd = prefs.getInt("high_score", 0);
	nameValuePairs.add(new BasicNameValuePair("score",Integer.toString(scoreToAdd)));
	nameValuePairs.add(new BasicNameValuePair("name",strName));

	//http post
	//try{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(serverUrl + "/add_score.php");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();
	//convert response to string
		/*BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		 */
		is.close();

		//result=sb.toString();
	//}catch(Exception e){
		//scoreList.add(e.getMessage());
		//Log.e("log_tag", "Error converting result "+e.toString());
	//}



    }

}