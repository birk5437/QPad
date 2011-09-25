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
import android.content.Intent;

public class Menu extends Activity implements OnClickListener{
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button newGame = (Button)findViewById(R.id.bNew);
        Button exit = (Button)findViewById(R.id.bExit);
        newGame.setOnClickListener(this);
        exit.setOnClickListener(this);
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
    }
}

}
