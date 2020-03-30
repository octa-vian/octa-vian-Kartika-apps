package co.id.gmedia.coremodul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "GmediaKartikaRS";

    // All Shared Preferences Keys
    public static final String TAG_USERNAME = "username";
    public static final String TAG_ID = "id_customer";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_TOKEN = "token";
    public static final String TAG_EXP = "expired_at";

    // Constructor
    public SessionManager(Context context){
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String id, String username, String token, String expired_at, String nama){

        editor.putString(TAG_ID, id);

        editor.putString(TAG_USERNAME, username);

        editor.putString(TAG_TOKEN, token);

        editor.putString(TAG_EXP, expired_at);

        editor.putString(TAG_NAMA, nama);

        // commit changes
        editor.commit();
    }


    /**
     * Get stored session data
     * */

    public String getUserInfo(String key){
        return pref.getString(key, "");
    }

    public String getId(){
        return pref.getString(TAG_ID, "");
    }

    public String getUsername(){
        return pref.getString(TAG_USERNAME, "");
    }

    public String getToken(){
        return pref.getString(TAG_TOKEN, "");
    }

    public String getNama(){
        return pref.getString(TAG_NAMA, "");
    }

    public String getExp(){
        return pref.getString(TAG_EXP, "");
    }



    /**
     * Clear session details
     * */
    public void logoutUser(Intent logoutIntent){

        // Clearing all data from Shared Preferences
        try {
            editor.clear();
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(logoutIntent);
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        if(getUsername().isEmpty()){
            return false;
        }else{
            return true;
        }
        /*return pref.getBoolean(IS_LOGIN, false);*/
    }
}
