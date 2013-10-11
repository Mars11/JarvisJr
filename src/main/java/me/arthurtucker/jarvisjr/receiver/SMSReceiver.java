package me.arthurtucker.jarvisjr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import me.arthurtucker.jarvisjr.service.SpeechService;
import me.arthurtucker.jarvisjr.utils.Global;
import me.arthurtucker.jarvisjr.utils.Prefs;

/**
 * Created by Arthur on 7/28/13.
 * Prove HER wrong.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            final SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            if (messages.length > -1) {
                Log.i(TAG, "Message received: " + messages[0].getMessageBody());

                Prefs.restorePrefs(context);

                if (Global.isSmsEnabled) {
                    String msg = "";
                    if (Global.isSmsAuthEnabled && Global.isSmsBodyEnabled) {
                        msg = Global.getContactName(context, messages[0].getOriginatingAddress()) + " sent you a text. It reads: " + messages[0].getMessageBody();
                    }
                    if (Global.isSmsAuthEnabled && !Global.isSmsBodyEnabled) {
                        msg = Global.getContactName(context, messages[0].getOriginatingAddress()) + " sent you a text. ";
                    }
                    if (Global.isSmsBodyEnabled && !Global.isSmsAuthEnabled) {
                        msg = "You received a text. It reads: " + messages[0].getMessageBody();
                    }
                    if (!Global.isSmsAuthEnabled && !Global.isSmsBodyEnabled) {
                        msg = "You received a text.";
                    }
                    Intent speechIntent = new Intent(context, SpeechService.class);
                    speechIntent.putExtra("inmsg", msg);
                    context.startService(speechIntent);
                }
            }
        }
    }
}
