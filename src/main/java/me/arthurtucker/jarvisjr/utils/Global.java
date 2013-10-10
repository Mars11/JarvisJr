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

    public static boolean   hasDonated;

    public static boolean   isSpeechEnabled;
    public static boolean   isBatCharged;

    public static boolean   isQTEnabled;
    public static int       startQTHour;
    public static int       startQTMin;
    public static boolean   isQTStartEnabled;
    public static int       endQTHour;
    public static int       endQTMin;
    public static boolean   isQTEndEnabled;

    public static boolean   isSmsEnabled;
    public static boolean   isSmsAuthEnabled;
    public static boolean   isSmsBodyEnabled;

    public static boolean   isBatFullEnabled;
    public static boolean   isPowerServiceStarted;

    public static String    voiceQuality;

    public static String getContactName(Context context, String number) {

        String name = null;

        // define the columns I want the query to return
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

        // encode the phone number and build the filter URI
        assert ContactsContract.PhoneLookup.CONTENT_FILTER_URI != null;
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // query time
        assert contactUri != null;
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
