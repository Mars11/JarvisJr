package me.arthurtucker.jarvisjr;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.text.format.DateFormat;
import android.util.Log;

import me.arthurtucker.jarvisjr.service.SpeechService;
import me.arthurtucker.jarvisjr.utils.Global;

import static java.lang.Math.abs;

/**
 * Created by Arthur on 7/30/13.
 * Prove HER wrong.
 */
public class TheFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private static final String TAG = "TheFragment";
    private static Preference appDonate;
    private static Preference appExample;
    private static SwitchPreference qtEnable;
    private static Preference qtStart;
    private static Preference qtEnd;
    private static DialogFragment qtStartPick;
    private static DialogFragment qtEndPick;
    private static SwitchPreference smsEnable;
    private static SwitchPreference smsAuthor;
    private static SwitchPreference smsBody;
    private static SwitchPreference batteryFull;
    private static ListPreference voiceQuality;

    public static void toggleQtSwitches() {
        qtStart.setEnabled(Global.isQTEnabled);
        qtEnd.setEnabled(Global.isQTEnabled);
    }

    public static void toggleSmsSwitches() {
        smsAuthor.setEnabled(Global.isSmsEnabled);
        smsBody.setEnabled(Global.isSmsEnabled);
    }

    public static void updateSwitches() {
        Log.d(TAG, "Enabled = " + Global.isSpeechEnabled);

        appExample.setEnabled(Global.isSpeechEnabled);

        smsEnable.setEnabled(Global.isSpeechEnabled);
        smsAuthor.setEnabled(Global.isSpeechEnabled);
        smsBody.setEnabled(Global.isSpeechEnabled);

        qtEnable.setEnabled(Global.isSpeechEnabled);
        qtStart.setEnabled(Global.isSpeechEnabled);
        qtStart.setEnabled(Global.isSpeechEnabled);

        batteryFull.setEnabled(Global.isSpeechEnabled);

        voiceQuality.setEnabled(Global.isSpeechEnabled);
    }

    public static void updateQTStartSummary(Context context) {
        if (Global.isQTStartEnabled) {
            if (DateFormat.is24HourFormat(context)) {
                qtStart.setSummary((Global.startQTHour < 10 ? "0" + Global.startQTHour : Global.startQTHour) + ":" + (Global.startQTMin < 10 ? "0" + Global.startQTMin : Global.startQTMin));
            } else {
                int hour;
                String amPM;
                if (Global.startQTHour >= 12) {
                    hour = abs(Global.startQTHour - 12);
                    amPM = "PM";
                } else {
                    hour = Global.startQTHour;
                    amPM = "AM";
                }
                qtStart.setSummary(hour + ":" + (Global.startQTMin < 10 ? "0" + Global.startQTMin : Global.startQTMin) + " " + amPM);
            }
        } else {
            qtStart.setSummary(R.string.QT_Summary_NotSet);
        }
    }

    public static void updateQTEndSummary(Context context) {
        if (Global.isQTEndEnabled) {
            if (DateFormat.is24HourFormat(context)) {
                qtEnd.setSummary((Global.endQTHour < 10 ? "0" + Global.endQTHour : Global.endQTHour) + ":" + (Global.endQTMin < 10 ? "0" + Global.endQTMin : Global.endQTMin));
            } else {
                int hour;
                String amPM;
                if (Global.endQTHour >= 12) {
                    hour = abs(Global.endQTHour - 12);
                    amPM = "PM";
                } else {
                    hour = Global.endQTHour;
                    amPM = "AM";
                }
                qtEnd.setSummary(hour + ":" + (Global.endQTMin < 10 ? "0" + Global.endQTMin : Global.endQTMin) + " " + amPM);
            }
        } else {
            qtEnd.setSummary(R.string.QT_Summary_NotSet);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the preferences
        addPreferencesFromResource(R.xml.settings);
        PreferenceScreen root = getPreferenceScreen();

        Preference abSpace = root.findPreference("space");
        assert abSpace != null;
        abSpace.setLayoutResource(R.layout.settings);

        appDonate = findPreference("app_donate");
        appExample = root.findPreference("app_example");

        qtEnable = (SwitchPreference) root.findPreference("qt_enable");
        qtStart = root.findPreference("qt_start");
        qtEnd = root.findPreference("qt_end");
        toggleQtSwitches();

        smsEnable = (SwitchPreference) root.findPreference("sms_enable");
        smsAuthor = (SwitchPreference) root.findPreference("sms_author");
        smsBody = (SwitchPreference) root.findPreference("sms_body");
        toggleSmsSwitches();

        batteryFull = (SwitchPreference) root.findPreference("battery_full");

        voiceQuality = (ListPreference) root.findPreference("voice_quality");

        if (Global.hasDonated) {
            appDonate.setSummary(R.string.App_Donate_Summary_Donated);
        }

        updateQTStartSummary(getActivity());
        updateQTEndSummary(getActivity());
        qtStartPick = new Dialog_StartQT();
        qtEndPick = new Dialog_EndQT();

        appDonate.setOnPreferenceClickListener(this);
        appExample.setOnPreferenceClickListener(this);

        qtEnable.setOnPreferenceChangeListener(this);
        qtStart.setOnPreferenceClickListener(this);
        qtEnd.setOnPreferenceClickListener(this);

        smsEnable.setOnPreferenceChangeListener(this);
        smsAuthor.setOnPreferenceChangeListener(this);
        smsBody.setOnPreferenceChangeListener(this);

        batteryFull.setOnPreferenceChangeListener(this);

        voiceQuality.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // Quiet Time Settings
        if (preference.equals(qtEnable)) {
            Global.isQTEnabled = ((Boolean) newValue);
            toggleQtSwitches();
        }
        // Text Settings
        if (preference.equals(smsEnable)) {
            Global.isSmsEnabled = ((Boolean) newValue);
            toggleSmsSwitches();
        }
        if (preference.equals(smsAuthor)) {
            Global.isSmsAuthEnabled = ((Boolean) newValue);
        }
        if (preference.equals(smsBody)) {
            Global.isSmsBodyEnabled = ((Boolean) newValue);
        }
        //  Battery Settings
        if (preference.equals(batteryFull)) {
            Global.isBatFullEnabled = ((Boolean) newValue);
        }
        // Voice settings
        if (preference.equals(voiceQuality)) {
            Global.voiceQuality = ((String) newValue);
            voiceQuality.setSummary(getResources().getStringArray(R.array.Voice_Quality_Entries)[Integer.parseInt(Global.voiceQuality)]);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.equals(appExample)) {
            String msg = getString(R.string.Example_String);
            Activity activity = getActivity();
            Intent speechIntent = new Intent(activity, SpeechService.class);
            speechIntent.putExtra("inmsg", msg);
            assert activity != null;
            activity.startService(speechIntent);
        }
        if (preference.equals(qtStart)) {
            qtStartPick.show(getFragmentManager(), "startpickdialog");
        }
        if (preference.equals(qtEnd)) {
            qtEndPick.show(getFragmentManager(), "endpickdialog");
        }
        if (preference.equals(appDonate)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=me.arthurtucker.jarvisjrdonationkey"));
            startActivity(intent);
        }
        return true;
    }

}
