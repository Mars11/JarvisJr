package me.arthurtucker.jarvisjr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.Log;

import me.arthurtucker.jarvisjr.service.SpeechService;
import me.arthurtucker.jarvisjr.utils.Global;
import me.arthurtucker.jarvisjr.utils.Prefs;

/**
 * Created by Arthur on 8/1/13.
 * Prove HER wrong.
 */
public class BatteryReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryReciever";
    private static final String PREFS = "PreferencesFile";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BatteryReceiver started");

        SharedPreferences mPrefs                = context.getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor mPrefsEditor   = mPrefs.edit();

        Boolean saidCharged = mPrefs.getBoolean("saidCharged", false);
        int status          = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        Global.isBatCharged = (status == BatteryManager.BATTERY_STATUS_FULL);
        if (Global.isBatFullEnabled && Global.isBatCharged && !saidCharged) {
            Log.i(TAG, "Battery charged");
            Prefs.startPrefs(context);
            String msg          = "Your battery is full.";
            Intent speechIntent = new Intent(context, SpeechService.class);
            speechIntent.putExtra("inmsg", msg);
            context.startService(speechIntent);
            mPrefsEditor.putBoolean("saidCharged", true);
        } else {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            if (level <= 99) {
                mPrefsEditor.putBoolean("saidCharged", false);
            }

            Log.i(TAG, "Battery at "+level+"%");
        }
        mPrefsEditor.commit();
    }
}
