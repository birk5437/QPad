/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

/**
 *
 * @author burke
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.*;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import java.util.ArrayList;
import android.content.Context;
import android.os.AsyncTask;

public class Menu extends Activity implements OnClickListener{
    
    Context c;
    TextView txtLocalHigh;
    ProgressDialog loadingDialog;
    Runnable getScoresTask;
    Thread t;
    Handler handler, handler2;
    protected HighScoreRequest hsr;
    Toast toast;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button newGame = (Button)findViewById(R.id.bNew);
	newGame.setOnClickListener(this);

	Button highScores = (Button)findViewById(R.id.bHighScores); //uncomment in main.xml
        highScores.setOnClickListener(this);

	Button exit = (Button)findViewById(R.id.bExit);
        exit.setOnClickListener(this);
        
        QPadDataManager d = new QPadDataManager(this.getApplicationContext());
        txtLocalHigh = (TextView)findViewById(R.id.txtLocalHigh);
        txtLocalHigh.append(" " + String.valueOf(d.getHighScore()));
        
        loadingDialog = new ProgressDialog(Menu.this);
        loadingDialog.setMessage("Loading");

        toast = Toast.makeText(this, "toast", Toast.LENGTH_SHORT);

    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.bExit:
                finish();
                break;
            case R.id.bNew:
                Intent game = new Intent(Menu.this, Game.class);
                startActivity(game);
                break;
            case R.id.bHighScores:
                hsr = new HighScoreRequest();
                hsr.execute(this);
                break;
        }
    }

    @Override
    public void onResume() {

	  super.onResume();
          if (loadingDialog.isShowing())
            loadingDialog.dismiss();

          QPadDataManager d = new QPadDataManager(this.getApplicationContext());
          int gotHigh = d.getInt("new_high", 0);

          if (gotHigh == 1) {
            d.storeInt("new_high", 0);
	    Intent subHigh = new Intent(Menu.this, SubmitHighScore.class);
	    startActivity(subHigh);
          }
          
          txtLocalHigh.setText("High Score: " + String.valueOf(d.getHighScore()));

    }

    protected class HighScoreRequest extends AsyncTask<Context, Integer, String> {

        @Override
        protected void onPreExecute() {
            loadingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Context... params){

            QPadDataManager d = new QPadDataManager(params[0]);
            QPadServer s = new QPadServer("http://74.207.236.215/qpad_server", d.getUniqueId());

            try {
                ArrayList<String> high_scores = s.getHighScores();
                Bundle b = new Bundle();
                b.putStringArrayList("web_high_scores", high_scores);
                Intent highs = new Intent(Menu.this, HighScores.class);
                highs.putExtras(b);
                //loadingDialog.dismiss();
                startActivity(highs);
                return "DONE";
            } catch (Exception ex) {
                //loadingDialog.dismiss();
                //Toast.makeText(params[0], ex.getMessage(), Toast.LENGTH_SHORT).show();
                return ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadingDialog.dismiss();
            if (!result.equals("DONE")) {
                toast.setText("Connection error.");
                toast.show();
            }
        }
    }

}
