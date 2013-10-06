package me.arthurtucker.jarvisjr.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import me.arthurtucker.jarvisjr.receiver.BatteryReceiver;

/**
 * Created by Arthur on 7/31/13.
 * Prove HER wrong.
 */
public class PowerService extends Service {
    private static final String TAG = "PowerReceiver";
    private BroadcastReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "PowerService bound");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Battery state changed");
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        receiver = new BatteryReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Destroying PowerService");
        unregisterReceiver(receiver);
    }

}
