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
import android.os.Bundle;
import android.widget.*;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.SharedPreferences;
import android.content.Intent;

public class Menu extends Activity implements OnClickListener{
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	//initPrefs();
        setContentView(R.layout.main);
        Button newGame = (Button)findViewById(R.id.bNew);
        //Button highScores = (Button)findViewById(R.id.bHighScores); //uncomment in main.xml
        Button exit = (Button)findViewById(R.id.bExit);
        newGame.setOnClickListener(this);
        exit.setOnClickListener(this);
        //highScores.setOnClickListener(this);
    }

    public void initPrefs() {

	SharedPreferences prefs = getSharedPreferences("qpad_prefs", 0);
	SharedPreferences.Editor editor = prefs.edit();
	editor.putInt("new_high", 0);
	editor.commit();


    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.bExit:
                //finish();
		Intent subHigh = new Intent(Menu.this, SubmitHighScore.class);
		startActivity(subHigh);
                break;
            case R.id.bNew:
                Intent game = new Intent(Menu.this, Game.class);
                startActivity(game);
                break;
            /*case R.id.bHighScores:
                Intent highs = new Intent(Menu.this, HighScores.class);
                startActivity(highs);
                break;*/
        }
    }

    @Override
    public void onResume() {

	  super.onResume();

          SharedPreferences prefs = getSharedPreferences("qpad_prefs", 0);

          int gotHigh = prefs.getInt("new_high", 0);
          if (gotHigh == 1) {
            SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt("new_high", 0);
            editor.commit();

	    Intent subHigh = new Intent(Menu.this, SubmitHighScore.class);
	    startActivity(subHigh);
          }


    }

}
