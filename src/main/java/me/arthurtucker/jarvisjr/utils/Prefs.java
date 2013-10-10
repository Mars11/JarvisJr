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

    public static void startPrefs(Context context) {
        // Restoring Preferences
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        syncPrefs();
    }

    public static void syncPrefs() {
        Global.isSpeechEnabled  = mPrefs.getBoolean("controlsEnabled", false);
        Global.voiceQuality     = mPrefs.getString("voicequality", "0");

        Global.isQTEnabled      = mPrefs.getBoolean("quietenable", false);
        Global.startQTHour      = mPrefs.getInt("quietstarth", 0);
        Global.startQTMin       = mPrefs.getInt("quietstartm", 0);
        Global.isQTStartEnabled = mPrefs.getBoolean("quiettimestartenabled", false);
        Global.endQTHour        = mPrefs.getInt("quietendh", 0);
        Global.endQTMin         = mPrefs.getInt("quietendm", 0);
        Global.isQTEndEnabled   = mPrefs.getBoolean("quiettimeendenabled", false);

        Global.isSmsEnabled     = mPrefs.getBoolean("textenable", false);
        Global.isSmsAuthEnabled = mPrefs.getBoolean("textsenderenable", false);
        Global.isSmsBodyEnabled = mPrefs.getBoolean("textbodyenable", false);

        Global.isBatFullEnabled = mPrefs.getBoolean("batteryfull", false);
    }

    public static void pushPrefs() {
        Log.d(TAG, "Prefs running updatePrefs()");
        mPrefsEditor = mPrefs.edit();
        //mPrefsEditor.putBoolean("hasdonated", Global.hasDonated);
        mPrefsEditor.putBoolean("controlsEnabled",  Global.isSpeechEnabled);
        mPrefsEditor.putInt(    "quietstarth",      Global.startQTHour);
        mPrefsEditor.putInt(    "quietstartm",      Global.startQTMin);
        mPrefsEditor.putBoolean("quiettimestartenabled", Global.isQTStartEnabled);
        mPrefsEditor.putInt(    "quietendh",        Global.endQTHour);
        mPrefsEditor.putInt(    "quietendm",        Global.endQTMin);
        mPrefsEditor.putBoolean("quiettimeendenabled", Global.isQTEndEnabled);

        // Commit the edits!
        mPrefsEditor.apply();
    }

    public static void clearPrefs() {
        mPrefsEditor.clear();
    }
}
