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

/**
 *
 * @author burke
 */
public class HighScoreDatabase {

    public HighScoreDatabase()
    {
        
    }

    public void addHighScore(String name, int score)
    {
        InputStream is;
        String result = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("password","qpAdp4zz"));
        nameValuePairs.add(new BasicNameValuePair("name", "burke"));
        nameValuePairs.add(new BasicNameValuePair("score", Integer.toString(score)));

        //http post
        try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://74.207.236.215/qpad_server/add_score.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                /*HttpEntity entity = response.getEntity();
                is = entity.getContent();
        //convert response to string
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                }
                is.close();

                result=sb.toString();*/
        }catch(Exception e){
                //scoreList.add(e.getMessage());
                //Log.e("log_tag", "Error converting result "+e.toString());
        }
    }

}
