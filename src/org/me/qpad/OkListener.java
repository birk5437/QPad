/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

/**
 *
 * @author burke
 */

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;

public class OkListener implements OnClickListener {
    private Dialog dialog;
    private int clicked;


    public OkListener(Dialog dialog) {

		    clicked = 0;
		    this.dialog = dialog;
    }


    public Dialog getDialog() {
	return dialog;
    }

    public int getVal() {
	return clicked;
    }

    public void onClick(View v) {

		    clicked = 1;
		    dialog.dismiss();

    }
}
