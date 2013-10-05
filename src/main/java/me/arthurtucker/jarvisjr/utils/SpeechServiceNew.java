package me.arthurtucker.jarvisjr.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import me.arthurtucker.jarvisjr.TheActivity;

/**
 * Created by Arthur on 7/24/13.
 * Prove HER wrong.
 */
public class SpeechServiceNew extends Service implements TextToSpeech.OnInitListener {
    private static final String TAG = "SpeachService";
    private static TextToSpeech myTTS;
    private TheActivity mActivity;
    private String msg;
    private HashMap<String, String> hashMap;

    @Override
    public void onCreate() {
        if (!Global.mEnabled) {
            Log.d(TAG, "SpeachService canceled");
            stopSelf();
        } else {
            Log.d(TAG, "SpeachService started");
            mActivity = new TheActivity();
            super.onCreate();
            Intent checkTTSIntent = new Intent();
            checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            myTTS = new TextToSpeech(this, this);
            hashMap = new HashMap<String, String>();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        msg = intent.getStringExtra("inmsg");

        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            hashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "speechMessage");
            if (myTTS.isLanguageAvailable(Locale.UK) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                myTTS.setLanguage(Locale.UK);
            }
            if (Build.VERSION.SDK_INT >= 15) {
                myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {

                    }

                    @Override
                    public void onDone(String s) {
                        onComplete();
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
            } else {
                /**
                 * NOT TESTED
                 */
                myTTS.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                    @Override
                    public void onUtteranceCompleted(String s) {
                        onComplete();
                    }
                });
            }
            sayString(msg);
        } else if (initStatus == TextToSpeech.ERROR) {
            mActivity.makeToast("Failed to start Text-to-Speech Service", true);
        }
    }

    private void onComplete() {
        stopSelf();
    }

    public void sayString(String string) {
        this.getBaseContext();
        ConnectivityManager connMgr = (ConnectivityManager)
            this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        assert wifi != null;
        assert mobile != null;

        if (wifi.isConnected()){
            Log.i(TAG, "Using Wifi");
        }
        else if(mobile.isConnected()){
            Log.i(TAG, "Using Mobile");
        }
        else {
            if (Global.mVoiceQuality.equals("2") || Global.mVoiceQuality.equals("1")) {
                Toast.makeText(this, "No Network " , Toast.LENGTH_LONG).show();
            }
        }

        if (myTTS != null) {

            hashMap.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_NOTIFICATION));

            if ( ( Global.mVoiceQuality.equals("2") && ( wifi.isConnected() || mobile.isConnected() ) )
                    || ( Global.mVoiceQuality.equals("1") && wifi.isConnected() ) ) {

                hashMap.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, "true");
                Log.i(TAG, "HQ voice");
            } else {
                Log.i(TAG, "SD voice");
            }
            myTTS.speak(string, 1, hashMap);
        }
    }

    @Override
    public void onDestroy() {
        if (myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
        }
        super.onDestroy();
    }

}
