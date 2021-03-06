package me.arthurtucker.jarvisjr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.arthurtucker.jarvisjr.utils.Global;
import me.arthurtucker.jarvisjr.utils.PowerService;
import me.arthurtucker.jarvisjr.utils.Prefs;

/**
 * Created by Arthur on 8/1/13.
 * Prove HER wrong.
 */
public class UnPlugReceiver extends BroadcastReceiver {
    private static final String TAG = "UnPlugReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Prefs.startPrefs(context);
        Intent powerIntent = new Intent(context, PowerService.class);
        Log.d(TAG, "powerServiceStarted = "+Global.powerServiceStarted);
        if (Global.powerServiceStarted) {
            context.stopService(powerIntent);
            Global.setPowerServiceStarted(false);
            Log.i(TAG, "Stopping PowerService");
        }
    }
}
