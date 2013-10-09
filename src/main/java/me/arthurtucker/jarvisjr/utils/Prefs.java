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
        Global.mEnabled         = mPrefs.getBoolean("controlsEnabled", false);
        Global.mVoiceQuality    = mPrefs.getString("voicequality", "0");

        Global.quietEnabled     = mPrefs.getBoolean("quietenable", false);
        Global.quietStartH      = mPrefs.getInt("quietstarth", 0);
        Global.quietStartM      = mPrefs.getInt("quietstartm", 0);
        Global.startEnabled     = mPrefs.getBoolean("quiettimestartenabled", false);
        Global.quietStopH       = mPrefs.getInt("quietendh", 0);
        Global.quietStopM       = mPrefs.getInt("quietendm", 0);
        Global.endEnabled       = mPrefs.getBoolean("quiettimeendenabled", false);

        Global.textEnabled      = mPrefs.getBoolean("textenable", false);
        Global.textSenderEnabled=mPrefs.getBoolean("textsenderenable", false);
        Global.textBodyEnabled  =mPrefs.getBoolean("textbodyenable", false);

        Global.batteryFullEnabled=mPrefs.getBoolean("batteryfull", false);
    }

    public static void pushPrefs() {
        Log.d(TAG, "Prefs running updatePrefs()");
        mPrefsEditor = mPrefs.edit();
        //mPrefsEditor.putBoolean("hasdonated", Global.hasDonated);
        mPrefsEditor.putBoolean("controlsEnabled", Global.mEnabled);
        mPrefsEditor.putInt("quietstarth", Global.quietStartH);
        mPrefsEditor.putInt("quietstartm", Global.quietStartM);
        mPrefsEditor.putBoolean("quiettimestartenabled", Global.startEnabled);
        mPrefsEditor.putInt("quietendh", Global.quietStopH);
        mPrefsEditor.putInt("quietendm", Global.quietStopM);
        mPrefsEditor.putBoolean("quiettimeendenabled", Global.endEnabled);

        // Commit the edits!
        mPrefsEditor.apply();
    }

    public static void clearPrefs() {
        mPrefsEditor.clear();
    }
}
