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
import android.provider.Settings;
import java.lang.reflect.Method;
import android.content.Context;
import android.content.ContentResolver;
import android.app.Activity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author burke
 */
public class QPadServer {

    private String serverUrl = "";
    private Context localContext;    
    private String androidId;
    
    public QPadServer(String url, String id)
    {
        androidId = id;
	serverUrl = url;
    }
    
    private HttpParams setTimeout(int connection, int socket) {
        
        HttpParams httpParameters = new BasicHttpParams();
        int connectionTimeout = connection;
        int socketTimeout = socket;
        HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
        
        return httpParameters;
    
    }

    public ArrayList<String> getHighScores() throws java.io.IOException, org.json.JSONException {

	ArrayList<String> scoreList = new ArrayList<String>();

	InputStream is;
	String result = "";
	//the year data to send
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("password","qpAdp4zz"));

	//http post
        HttpClient httpclient = new DefaultHttpClient(setTimeout(10000, 10500));
        String httpUrl = serverUrl + "/get_high_scores.php";
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

	//parse json data
        JSONArray jArray = new JSONArray(result);
        for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                String listString = json_data.getString("name") + " - " + json_data.getString("score");
                scoreList.add(listString);
        }
                
	return scoreList;
    }

    public void addScore(String strName, int scoreToAdd) throws java.io.IOException {

        InputStream is;
	String result = "";

	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("password","qpAdp4zz"));
        nameValuePairs.add(new BasicNameValuePair("score",Integer.toString(scoreToAdd)));
	nameValuePairs.add(new BasicNameValuePair("name", strName));
        nameValuePairs.add(new BasicNameValuePair("install_id", androidId));

        //http post
        HttpClient httpclient = new DefaultHttpClient(setTimeout(10000, 10500));
        HttpPost httppost = new HttpPost(serverUrl + "/add_score.php");
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        is = entity.getContent();
        is.close();
    }

}
