package me.arthurtucker.jarvisjr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.arthurtucker.jarvisjr.service.PowerService;
import me.arthurtucker.jarvisjr.utils.Global;
import me.arthurtucker.jarvisjr.utils.Prefs;

/**
 * Created by Arthur on 8/1/13.
 * Prove HER wrong.
 */
public class PlugInReceiver extends BroadcastReceiver {
    private static final String TAG = "PlugInReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Prefs.startPrefs(context);
        if (Global.batteryFullEnabled) {
            Intent powerIntent = new Intent(context, PowerService.class);
            Log.d(TAG, "powerServiceStarted = "+Global.powerServiceStarted);
            if (!Global.powerServiceStarted) {
                context.startService(powerIntent);
                Global.setPowerServiceStarted(true);
                Log.i(TAG, "Starting PowerService");
            }
        }
    }
}
