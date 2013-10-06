package me.arthurtucker.jarvisjr.receiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by arthur on 10/6/13.
 */
public class NotificationListener extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
