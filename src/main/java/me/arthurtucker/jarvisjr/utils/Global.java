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
