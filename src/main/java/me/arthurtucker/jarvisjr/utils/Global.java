package me.arthurtucker.jarvisjr.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by Arthur on 7/28/13.
 * Prove HER wrong.
 */
public class Global {
    private static final String TAG = "GlobalVars";
    public static boolean hasDonated;

    public static boolean mEnabled;
    public static boolean isCharged;

    public static boolean quietEnabled;
    public static int quietStartH;
    public static int quietStartM;
    public static boolean startEnabled;
    public static int quietStopH;
    public static int quietStopM;
    public static boolean endEnabled;
    public static boolean usesMilTime;

    public static boolean textEnabled;
    public static boolean textSenderEnabled;
    public static boolean textBodyEnabled;

    public static boolean batteryFullEnabled;
    public static boolean saidBatteryCharged;
    public static boolean powerServiceStarted;

    public static String mVoiceQuality;

    public static void setHasDonated(boolean bool) {
        hasDonated = bool;
        Log.d(TAG, "Setting hasDonated to " + bool);
    }

    public static void setmEnabled(boolean bool) {
        mEnabled = bool;
        Log.d(TAG, "Setting mEnabled to " + bool);
    }

    public static void setQuietEnabled(boolean bool) {
        quietEnabled = bool;
        Log.d(TAG, "Setting quietEnabled to " + bool);
    }

    public static void setQuietStartH(int i) {
        quietStartH = i;
        Log.d(TAG, "Setting quietStartH to " + i);
    }

    public static void setStartEnabled(boolean bool) {
        startEnabled = bool;
        Log.d(TAG, "Setting startEnabled to " + bool);
    }

    public static void setQuietStartM(int i) {
        quietStartM = i;
        Log.d(TAG, "Setting quietStartM to " + i);
    }

    public static void setQuietStopH(int i) {
        quietStopH = i;
        Log.d(TAG, "Setting quietStopH to " + i);
    }

    public static void setQuietStopM(int i) {
        quietStopM = i;
        Log.d(TAG, "Setting quietStopM to " + i);
    }

    public static void setEndEnabled(boolean bool) {
        endEnabled = bool;
        Log.d(TAG, "Setting endEnabled to " + bool);
    }

    public static void setUsesMilTime(boolean bool) {
        usesMilTime = bool;
        Log.d(TAG, "Settigns usesMIlTime to " + bool);
    }

    public static void setisCharged(boolean bool) {
        isCharged = bool;
        Log.d(TAG, "Setting isCharged to " + bool);
    }

    public static void setTextEnabled(boolean bool) {
        textEnabled = bool;
        Log.d(TAG, "Setting textEnabled to " + bool);
    }

    public static void setTextSenderEnabled(boolean bool) {
        textSenderEnabled = bool;
        Log.d(TAG, "Setting textSenderEnabled to " + bool);
    }

    public static void setTextBodyEnabled(boolean bool) {
        textBodyEnabled = bool;
        Log.d(TAG, "Setting textBodyEnabled to " + bool);
    }

    public static void setBatteryFullEnabled(boolean bool) {
        batteryFullEnabled = bool;
        Log.d(TAG, "Setting batteryFullEnabled to " + bool);
    }

    public static void setPowerServiceStarted(boolean bool){
        powerServiceStarted = bool;
        Log.d(TAG, "Setting powerServiceStarted to " + bool);
    }

    public static void setmHQVoice(String string) {
        mVoiceQuality = string;
        Log.d(TAG, "Setting mHQVoice to " + string);
    }

    public static String getContactName(Context context, String number) {

        String name = null;

        // define the columns I want the query to return
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // query time
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                Log.v(TAG, "Contact Found @ " + number);
                Log.v(TAG, "Contact name  = " + name);
            } else {
                name = "An unknown number";
                Log.v(TAG, "Contact Not Found @ " + number);
            }
            cursor.close();
        }
        return name;
    }
}
