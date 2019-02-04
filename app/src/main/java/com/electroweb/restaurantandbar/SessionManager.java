package com.electroweb.restaurantandbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

class SessionManager {

    public static final String user_name = "user_name";
    public static final String pass = "pass";
    public static final String login_id = "login_id";
    public static final String outlet_id = "outlet_id";
    public static final String outlet_name = "outlet_name";
    public static final String branch_name = "branch_name";
    public static final String ip_address = "ip_address";

    // Sharedpref file name
    private static final String PREF_NAME = "userdetail";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String username, String password, String loginId, String outletId, String OutletName, String BranchName, String IP) {

        try {

            editor.putBoolean(IS_LOGIN, true);
            // Storing name in pref
            editor.putString(user_name, username);
            editor.putString(pass, password);
            editor.putString(login_id, loginId);
            editor.putString(outlet_id, outletId);
            editor.putString(outlet_name, OutletName);
            editor.putString(branch_name, BranchName);
            editor.putString(ip_address, IP);

            // commit changes
            editor.commit();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public int checkLogin() {
        // Check login status

        int flag;

        if (!this.isLoggedIn()) {
            flag = 0;
        } else {

            flag = 1;
        }

        return flag;
    }

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(user_name, pref.getString(user_name, null));
        user.put(pass, pref.getString(pass, null));
        user.put(login_id, pref.getString(login_id, null));
        user.put(outlet_id, pref.getString(outlet_id, null));
        user.put(outlet_name, pref.getString(outlet_name, null));
        user.put(branch_name, pref.getString(branch_name, null));
        user.put(ip_address, pref.getString(ip_address, null));

        return user;
    }

    /**
     * Clear session details
     */
    public void clearUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, IPActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void RemoveUserdata() {
        editor.remove(user_name);
        editor.remove(pass);
        editor.remove(login_id);
        editor.remove(outlet_id);
        editor.remove(outlet_name);
        editor.remove(branch_name);
        editor.remove(ip_address);

        editor.commit();
    }
}
