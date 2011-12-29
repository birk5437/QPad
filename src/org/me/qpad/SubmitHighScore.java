/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.SharedPreferences;

/**
 *
 * @author burke
 */
public class SubmitHighScore extends Activity implements OnClickListener {

    EditText ed;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        setContentView(R.layout.submit_high);
	ed = (EditText)findViewById(R.id.txtEntry);

	Button btnSubmit = (Button)findViewById(R.id.bSubmit);
	btnSubmit.setOnClickListener(this);

	Button btnSkip = (Button)findViewById(R.id.bSkip);
	btnSkip.setOnClickListener(this);
    }

    /*@Override
    public void onPause() {
        finish();
        super.onPause();
    }*/

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.bSkip:
                finish();
                break;
            case R.id.bSubmit:
		QPadServer s = new QPadServer("http://74.207.236.215/qpad_server");
		SharedPreferences prefs = getSharedPreferences("qpad_prefs", 0);
		int high = prefs.getInt("high_score", 0);

		if (high > 0) {

		    try{
			s.addScore(ed.getText().toString(), high);
		    } catch(Exception ex) {

		    } finally {
			finish();
		    }
		}
		break;
                //Intent game = new Intent(Menu.this, Game.class);
                //startActivity(game);
                //break;
            //case R.id.bHighScores:
            //    Intent highs = new Intent(Menu.this, HighScores.class);
             //   startActivity(highs);
             //   break;
        }
    }

    /*public void onPause() {
        finish();
        super.onPause();
    }

    public void onFinish() {
	super.finish();
    }*/

}
