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
public class GameActivity extends Activity{

    private GameView g = new GameView(this);

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(g);
        //setContentView(R.layout.main);



        // ToDo add your GUI initialization code here
    }

}
