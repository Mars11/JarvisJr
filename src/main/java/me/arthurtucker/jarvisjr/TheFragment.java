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
import android.preference.PreferenceGroup;
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
    private static final String     TAG = "TheFragment";
    private static Preference       mDonate;
    private static Preference       mPlayExample;

    private static SwitchPreference mQuietEnabled;
    private static Preference       mStartQuietDialog;
    private static Preference       mEndQuietDialog;
    private static DialogFragment   mStartTimePick;
    private static DialogFragment   mEndTimePick;

    private static SwitchPreference mTextEnableSwitch;
    private static SwitchPreference mTextSenderSwitch;
    private static SwitchPreference mTextBodySwitch;

    private static SwitchPreference mBatteryFullSwitch;

    private static ListPreference   mHQVoiceList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the preferences
        addPreferencesFromResource(R.xml.settings);
        PreferenceScreen root   = getPreferenceScreen();

        Preference abSpace      = root.findPreference("space");
        assert abSpace          != null;
        abSpace.setLayoutResource(R.layout.settings);

        mDonate = findPreference("donate");
        assert mDonate != null;
        mDonate.setOnPreferenceClickListener(this);
        if (Global.hasDonated) {
            mDonate.setSummary("Thanks for donating!");
        }
        mPlayExample        = root.findPreference("exampleplay");
        assert mPlayExample != null;
        mPlayExample.setOnPreferenceClickListener(this);

        mQuietEnabled       = (SwitchPreference) root.findPreference("quietenable");
        assert mQuietEnabled != null;
        mQuietEnabled.setOnPreferenceChangeListener(this);
        mStartQuietDialog   = root.findPreference("startquiettime");
        assert mStartQuietDialog != null;
        mStartQuietDialog.setOnPreferenceClickListener(this);
        updateStartSummary(getActivity());
        mStartTimePick = new StartQuietDialog();
        mEndQuietDialog   = root.findPreference("stopquiettime");
        assert mEndQuietDialog != null;
        mEndQuietDialog.setOnPreferenceClickListener(this);
        updateEndSummary(getActivity());
        mEndTimePick = new StopQuietDialog();

        if (!Global.isQTEnabled) {
            Log.d(TAG, "onCreate(), removing prefs");
            PreferenceGroup group = (PreferenceGroup) findPreference("quiet");
            assert group != null;
            group.removePreference(mStartQuietDialog);
            group.removePreference(mEndQuietDialog);
        }

        mTextEnableSwitch   = (SwitchPreference) root.findPreference("textenable");
        assert mTextEnableSwitch != null;
        mTextEnableSwitch.setOnPreferenceChangeListener(this);
        mTextSenderSwitch   = (SwitchPreference) root.findPreference("textsenderenable");
        assert mTextSenderSwitch != null;
        mTextSenderSwitch.setOnPreferenceChangeListener(this);
        mTextBodySwitch     = (SwitchPreference) root.findPreference("textbodyenable");
        assert mTextBodySwitch != null;
        mTextBodySwitch.setOnPreferenceChangeListener(this);

        if (!Global.isSmsEnabled) {
            Log.d(TAG, "onCreate(), removing text prefs");
            PreferenceGroup group = (PreferenceGroup) findPreference("text");
            assert group != null;
            group.removePreference(mTextSenderSwitch);
            group.removePreference(mTextBodySwitch);
        }

        mBatteryFullSwitch  = (SwitchPreference) root.findPreference("batteryfull");
        assert mBatteryFullSwitch != null;
        mBatteryFullSwitch.setOnPreferenceChangeListener(this);

        mHQVoiceList        = (ListPreference) root.findPreference("voicequality");
        assert mHQVoiceList != null;
        mHQVoiceList.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // Quiet Time Settings
        if (preference.equals(mQuietEnabled)) {
            Global.isQTEnabled      = ((Boolean) newValue);
            PreferenceGroup group   = (PreferenceGroup) findPreference("quiet");
            assert group != null;
            if (Global.isQTEnabled) {
                group.addPreference(mStartQuietDialog);
                group.addPreference(mEndQuietDialog);
            } else if (!Global.isQTEnabled) {
                group.removePreference(mStartQuietDialog);
                group.removePreference(mEndQuietDialog);
            }
        }
        // Text Settings
        if (preference.equals(mTextEnableSwitch)) {
            Global.isSmsEnabled     = ((Boolean) newValue);
            PreferenceGroup group   = (PreferenceGroup) findPreference("text");
            assert group != null;
            if (Global.isSmsEnabled) {
                Log.d(TAG, "Adding preferences");
                group.addPreference(mTextSenderSwitch);
                group.addPreference(mTextBodySwitch);
            } else if (!Global.isSmsEnabled) {
                Log.d(TAG, "Removing preferences");
                group.removePreference(mTextSenderSwitch);
                group.removePreference(mTextBodySwitch);
            }
        }
        if (preference.equals(mTextSenderSwitch)) {
            Global.isSmsAuthEnabled = ((Boolean) newValue);
        }
        if (preference.equals(mTextBodySwitch)) {
            Global.isSmsBodyEnabled = ((Boolean) newValue);
        }
        //  Battery Settings
        if (preference.equals(mBatteryFullSwitch)) {
            Global.isBatFullEnabled = ((Boolean) newValue);
        }
        // Voice settings
        if (preference.equals(mHQVoiceList)) {
            Global.voiceQuality = ((String) newValue);
            mHQVoiceList.setSummary(getResources().getStringArray(R.array.app_hq_entries)[Integer.parseInt(Global.voiceQuality)]);
        }
        return true;
    }

    public static void updateSwitches() {
        Log.d(TAG, "Enabled = " +       Global.isSpeechEnabled);

        //  App Settings
        mPlayExample.setEnabled(        Global.isSpeechEnabled);
        mHQVoiceList.setEnabled(        Global.isSpeechEnabled);
        //  Text Settings
        mTextEnableSwitch.setEnabled(   Global.isSpeechEnabled);
        mTextSenderSwitch.setEnabled(   Global.isSpeechEnabled);
        mTextBodySwitch.setEnabled(     Global.isSpeechEnabled);
        //  Quiet Time Settings
        mQuietEnabled.setEnabled(       Global.isSpeechEnabled);
        mStartQuietDialog.setEnabled(   Global.isSpeechEnabled);
        mStartQuietDialog.setEnabled(   Global.isSpeechEnabled);
        //  Battery Settings
        mBatteryFullSwitch.setEnabled   (Global.isSpeechEnabled);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.equals(mPlayExample)) {
            String msg          = "This is an example of Jarvis Junior's voice.";
            Activity activity   = getActivity();
            Intent speechIntent = new Intent(activity, SpeechService.class);
            speechIntent.putExtra("inmsg", msg);
            assert activity    != null;
            activity.startService(speechIntent);
        }
        if (preference.equals(mStartQuietDialog)) {
            mStartTimePick.show(getFragmentManager(), "startpickdialog");
        }
        if (preference.equals(mEndQuietDialog)) {
            mEndTimePick.show(getFragmentManager(), "endpickdialog");
        }
        if (preference.equals(mDonate)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=me.arthurtucker.jarvisjrdonationkey"));
            startActivity(intent);
        }
        return true;
    }

    public static void updateStartSummary(Context context) {
        if (Global.isQTStartEnabled) {
            if (DateFormat.is24HourFormat(context)) {
                mStartQuietDialog.setSummary((Global.startQTHour < 10 ?  "0"+Global.startQTHour : Global.startQTHour )+":"+(Global.startQTMin < 10 ? "0"+Global.startQTMin : Global.startQTMin));
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
                mStartQuietDialog.setSummary(hour +":"+(Global.startQTMin < 10 ? "0"+Global.startQTMin : Global.startQTMin) +" "+ amPM);
            }
        } else {
            mStartQuietDialog.setSummary("Not yet set");
        }
    }

    public static void updateEndSummary(Context context) {
        if (Global.isQTEndEnabled) {
            if (DateFormat.is24HourFormat(context)) {
                mEndQuietDialog.setSummary((Global.endQTHour < 10 ? "0"+Global.endQTHour : Global.endQTHour)+":"+(Global.endQTMin < 10 ? "0"+Global.endQTMin : Global.endQTMin));
            } else {
                int hour;
                String amPM;
                if (Global.endQTHour >= 12) {
                    hour = abs(Global.endQTHour -12);
                    amPM = "PM";
                } else {
                    hour = Global.endQTHour;
                    amPM = "AM";
                }
                mEndQuietDialog.setSummary(hour +":"+ (Global.endQTMin < 10 ? "0"+Global.endQTMin : Global.endQTMin) +" "+ amPM);
            }
        } else {
            mEndQuietDialog.setSummary("Not yet set");
        }
    }

}
