/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

/**
 *
 * @author burke
 */

import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class HighScores extends ListActivity {
    ArrayList<String> scoreList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        ArrayList<String> scoresToDisplay = (ArrayList<String>)extras.get("web_high_scores");
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, scoresToDisplay));
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
    }
}
