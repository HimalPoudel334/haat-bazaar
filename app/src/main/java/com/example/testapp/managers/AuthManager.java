package com.example.testapp.managers;

import android.content.Context;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.testapp.basetypes.PhoneNumber;
import com.example.testapp.models.User;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {

    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_ID = "user_id";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_TYPE = "user_type";

    private static AuthManager instance;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private User currentUser;

    public static void init(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
    }

    public static AuthManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AuthManager not initialized. Call AuthManager.init(context) in your Application class.");
        }
        return instance;
    }

    private AuthManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
        this.currentUser = loadUserFromPrefs();
    }

    private User loadUserFromPrefs() {
        String id = prefs.getString(KEY_ID, null);
        if (id == null) return getGuestUser();

        String firstName = prefs.getString(KEY_FIRST_NAME, "");
        String lastName = prefs.getString(KEY_LAST_NAME, "");
        String email = prefs.getString(KEY_EMAIL, "");
        String phone = prefs.getString(KEY_PHONE, "");
        String username = prefs.getString(KEY_USERNAME, "");
        String userType = prefs.getString(KEY_USER_TYPE, "GUEST");

        return new User(id, firstName, lastName, phone, email, username, userType);
    }

    // Save after login
    public void saveUser(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // Save full user
    public void setFullUser(User user) {
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_LAST_NAME, user.getLastName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhoneNumber());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_USER_TYPE, user.getUserType());
        editor.apply();

        this.currentUser = user;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            currentUser = loadUserFromPrefs();
        }
        return currentUser;
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
        currentUser = getGuestUser();
    }

    public User getGuestUser() {
        return new User(null, "", "", null, "", "", "GUEST");
    }
}
