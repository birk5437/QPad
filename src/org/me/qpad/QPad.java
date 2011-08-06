/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.View.OnClickListener;
import android.view.View;

/**
 *
 * @author burke
 */
public class QPad extends Activity {

    private Button btnStart;
    private Button btnExit;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        findAllViewsById();

        btnStart.setOnClickListener(btnStartListener);
        // ToDo add your GUI initialization code here        
    }

    private void startGameView()
    {
        setContentView(new GameView(this));
    }

    private void findAllViewsById() {
        btnStart = (Button) findViewById(R.id.btnStart);
        btnExit = (Button) findViewById(R.id.btnExit);
    }

    public void longToast(CharSequence message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
}

    private OnClickListener btnStartListener = new OnClickListener() {
        public void onClick(View v) {
            startGameView();
        }
    };

}
