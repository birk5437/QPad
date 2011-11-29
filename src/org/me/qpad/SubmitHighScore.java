/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;


import android.app.Activity;
import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
import android.content.Intent;

/**
 *
 * @author burke
 */
public class SubmitHighScore extends Activity {

    public void onCreate(Bundle bundle) {
        //super.onCreate(savedInstanceState);
        super.onCreate(bundle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        setContentView(R.layout.submit_high);
	EditText ed = (EditText)findViewById(R.id.txtEntry);
        //Button newGame = (Button)findViewById(R.id.bNew);
        //Button highScores = (Button)findViewById(R.id.bHighScores);
        //Button exit = (Button)findViewById(R.id.bExit);
        //newGame.setOnClickListener(this);
        //exit.setOnClickListener(this);
        //highScores.setOnClickListener(this);
    }

    public void onPause() {
        finish();
        super.onPause();
    }

    public void onFinish() {
	super.finish();
    }

}
