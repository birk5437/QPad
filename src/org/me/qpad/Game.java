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
import android.content.Intent;
import android.widget.*;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;

public class Game extends Activity {


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        GameView view = new GameView(this);
        setContentView(view);
    }

}
