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
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

/**
 *
 * @author burke
 */
public class SubmitHighScore extends Activity implements OnClickListener {

    EditText edName;
    ProgressDialog submittingDialog;
    Context c;
    Toast toast;
    protected BackgroundRequest br;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        setContentView(R.layout.submit_high);
        submittingDialog = new ProgressDialog(SubmitHighScore.this);
        submittingDialog.setMessage("Submitting Score");
        toast = Toast.makeText(this, "toast", Toast.LENGTH_SHORT);
        
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
                br = new BackgroundRequest();
                br.execute(this);
                break;
        }
    }

    protected class BackgroundRequest extends AsyncTask<Context, Integer, String> {

        @Override
        protected void onPreExecute() {
            submittingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Context... params){
            QPadDataManager d = new QPadDataManager(params[0]);
            QPadServer s = new QPadServer("http://74.207.236.215/qpad_server", d.getUniqueId());

            int high = d.getInt("high_score", -1);

            if (high > -1) {
                
                String name = edName.getText().toString().trim();
                if (!name.equals("")) {
                    try{
                        s.addScore(name, high);
                        return "DONE";
                    } catch(Exception ex) {
                        return ex.getMessage();
                    }
                }
                else
                    return "EMPTY";
            }
            else
                return "DONE";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            submittingDialog.dismiss();
            if (!result.equals("DONE") && !result.equals("EMPTY")) {
                toast.setText("Error submitting score.");
                toast.show();
            } else {
                if (result.equals("EMPTY")) {
                    toast.setText("Name cannot be empty.");
                    toast.show();
                } else {
                    toast.setText("Score added.");
                    toast.show();
                    finish();
                }
            }
        }
    }
}
