/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

/**
 *
 * @author burke
 */
public class SubmitHighScore extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle bundle) {
        //super.onCreate(savedInstanceState);
        super.onCreate(bundle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        setContentView(R.layout.submit_high);
	//EditText ed = (EditText)findViewById(R.id.txtEntry);

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
            //case R.id.bNew:
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
