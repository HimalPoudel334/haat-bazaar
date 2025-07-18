package com.example.testapp.managers;

import android.content.Context;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.testapp.basetypes.PhoneNumber;
import com.example.testapp.models.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthManager {

    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_ID = "user_id";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_LOCATION = "user_location";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_NEAREST_LANDMARK = "nearest_landmark";

    private static AuthManager instance;
    private SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

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
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();


            prefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
        } catch (GeneralSecurityException | IOException e) {
            Log.d("Encrypted Shared Prefs", "AuthManager: " + e.getMessage());
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
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
        String userLocation = prefs.getString(KEY_USER_LOCATION, "");
        String userType = prefs.getString(KEY_USER_TYPE, "GUEST");
        String nearestLandmark = prefs.getString(KEY_NEAREST_LANDMARK, "");

        return new User(id, firstName, lastName, phone, email, username, userLocation, userType, nearestLandmark);
    }

    // Save after login
    public void saveUserTokens(String accessToken, String refreshToken) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
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
        editor.putString(KEY_USER_LOCATION, user.getLocation());
        editor.putString(KEY_NEAREST_LANDMARK, user.getNearestLandmark());
        editor.apply();

        this.currentUser = user;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            currentUser = loadUserFromPrefs();
        }
        return currentUser;
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }
    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
        currentUser = getGuestUser();
    }

    public User getGuestUser() {
        return new User(null, "", "", null, "", "", "", "GUEST", "");
    }
}
