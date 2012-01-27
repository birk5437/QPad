/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.qpad;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import java.util.UUID;

public class QPadDataManager {
    
    Context myContext;
    
    public QPadDataManager(Context c) {
        myContext = c;
    }
    
    public void storeString(String valueName, String value) {
        
	SharedPreferences prefs = myContext.getSharedPreferences("qpad_prefs", 0);
	SharedPreferences.Editor editor = prefs.edit();
	editor.putString(valueName, value);
	editor.commit();        
        
    }
    
    public String getString(String valueName, String defaultValue)  {

        SharedPreferences prefs = myContext.getSharedPreferences("qpad_prefs", 0);
        return prefs.getString(valueName, defaultValue);
        
    }
    
    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
    
    public String getUniqueId() {
        
        String id = this.getString("unique_id", "null");
       
        if (id.equals("null")) {
            id = this.generateUniqueId();
            this.storeString("unique_id", id);
        }
        return id;
    }
    
    public String getAndroidId() {
        return Settings.Secure.getString(myContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    
}
