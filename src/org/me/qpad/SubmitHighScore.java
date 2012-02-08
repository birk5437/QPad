/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.app.ProgressDialog;
import android.os.Handler;

/**
 *
 * @author burke
 */
public class SubmitHighScore extends Activity implements OnClickListener {

    EditText edName;
    ProgressDialog submittingDialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        setContentView(R.layout.submit_high);
        submittingDialog = new ProgressDialog(SubmitHighScore.this);
        submittingDialog.setMessage("Submitting Score");
        
        TextView txtNewHigh = (TextView)findViewById(R.id.txtNewHigh);
        
	edName = (EditText)findViewById(R.id.txtEntry);

	Button btnSubmit = (Button)findViewById(R.id.bSubmit);
	btnSubmit.setOnClickListener(this);

	Button btnSkip = (Button)findViewById(R.id.bSkip);
	btnSkip.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.bSkip:
                finish();
                break;
            case R.id.bSubmit:
                
                submittingDialog.show();
                Handler handler = new Handler();
                long delay = 1000;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //submittingDialog = ProgressDialog.show(SubmitHighScore.this, "", "Submitting Score", true);
                        
                        QPadDataManager d = new QPadDataManager(SubmitHighScore.this.getApplicationContext());

                        QPadServer s = new QPadServer("http://74.207.236.215/qpad_server", d.getUniqueId());

                        SharedPreferences prefs = getSharedPreferences("qpad_prefs", 0);
                        int high = prefs.getInt("high_score", 0);

                        if (high > 0) {

                            try{
                                s.addScore(edName.getText().toString(), high);
                            } catch(Exception ex) {

                            }
                        }
                        submittingDialog.dismiss();
                        finish();
                    }
                }, delay);
                
            break;
        }
    }
}
