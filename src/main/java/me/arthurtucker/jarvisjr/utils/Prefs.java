package me.arthurtucker.jarvisjr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Arthur on 7/29/13.
 * Prove HER wrong.
 */
public class Prefs {
    private static final String TAG = "Prefs";
    public static SharedPreferences mPrefs;
    public static SharedPreferences.Editor mPrefsEditor;

    public static void restorePrefs(Context context) {
        // Restoring Preferences
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        pullPrefs();
    }

    public static void pullPrefs() {
        Log.d(TAG, "Updating variables from preferences.");
        Global.isSpeechEnabled  = mPrefs.getBoolean("isSpeechEnabled", false);
        Global.voiceQuality     = mPrefs.getString("voiceQuality", "0");

        Global.isQTEnabled      = mPrefs.getBoolean("isQTEnabled", false);
        Global.startQTHour      = mPrefs.getInt("startQTHour", 0);
        Global.startQTMin       = mPrefs.getInt("startQTMin", 0);
        Global.isQTStartEnabled = mPrefs.getBoolean("isQTStartEnabled", false);
        Global.endQTHour        = mPrefs.getInt("endQTHour", 0);
        Global.endQTMin         = mPrefs.getInt("endQTMin", 0);
        Global.isQTEndEnabled   = mPrefs.getBoolean("isQTEndEnabled", false);

        Global.isSmsEnabled     = mPrefs.getBoolean("isSmsEnabled", false);
        Global.isSmsAuthEnabled = mPrefs.getBoolean("isSmsAuthEnabled", false);
        Global.isSmsBodyEnabled = mPrefs.getBoolean("isSmsBodyEnabled", false);

        Global.isBatFullEnabled = mPrefs.getBoolean("isBatFullEnabled", false);
    }

    public static void pushPrefs() {
        Log.d(TAG, "Overriding old preferences.");
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("isSpeechEnabled",  Global.isSpeechEnabled);
        mPrefsEditor.putInt(    "startQTHour",      Global.startQTHour);
        mPrefsEditor.putInt(    "startQTMin",       Global.startQTMin);
        mPrefsEditor.putBoolean("isQTStartEnabled", Global.isQTStartEnabled);
        mPrefsEditor.putInt(    "endQTHour",        Global.endQTHour);
        mPrefsEditor.putInt(    "endQTMin",         Global.endQTMin);
        mPrefsEditor.putBoolean("isQTEndEnabled",   Global.isQTEndEnabled);

        // Commit the edits!
        mPrefsEditor.apply();
    }
}
