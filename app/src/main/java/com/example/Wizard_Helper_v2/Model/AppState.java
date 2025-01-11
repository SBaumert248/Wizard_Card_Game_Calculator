package com.example.Wizard_Helper_v2.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class AppState {
    private static final String PREF_NAME = "wizardHelperProperties";
    private static final String KEY_PLAYER1 = "player1";
    private static final String KEY_PLAYER2 = "player2";
    private static final String KEY_PLAYER3 = "player3";
    private static final String KEY_PLAYER4 = "player4";
    private static final String KEY_PLAYER5 = "player5";
    private static final String KEY_PLAYER6 = "player6";
    private static final String KEY_AGE = "age";

    public static void saveState(Context context, String username, int age) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(KEY_USERNAME, username);
        editor.putInt(KEY_AGE, age);
        editor.apply(); // apply() speichert asynchron, commit() synchron
    }
}
